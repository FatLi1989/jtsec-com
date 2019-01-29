package com.jtsec.mc.log.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jtsec.common.Constants.Constants;
import com.jtsec.common.util.date.CommonTools;
import com.jtsec.common.util.json.JsonConvert;
import com.jtsec.common.util.surpass.DateUtil;
import com.jtsec.common.util.surpass.NotNullUtil;
import com.jtsec.mc.log.analysis.api.service.*;
import com.jtsec.mc.log.analysis.api.sfts.service.CommonSftsFlowService;
import com.jtsec.mc.log.analysis.pojo.model.ProxyFlowTaskDoRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/10/11 14:21
 */
@Slf4j
@Service
public class FlowStatisticsServiceImpl implements FlowStatisticsService {

	@Autowired
	private ProxyFlowTaskDoRecordService proxyFlowTaskDoRecordService;
	@Autowired
	private CommonSftsFlowService commonSftsFlowService;

	@Override
	public void execute (String param) {
		JSONObject jsonObject = JsonConvert.ConvertToObject (param);
		//时间类型
		String timeType = (String) jsonObject.get (Constants.TIME_TYPE);
		//流类型
		String flowType = (String) jsonObject.get (Constants.FLOW_TYPE);
		//开始时间 前一个时间段
		String startTime = CommonTools.getNowTime (timeType);
		log.info ("开始执行" + flowType + "的" + timeType + "统计");
		//查询记录表中是否存在该条记录 以及记录状态 不存的话执行记录操作 并添加记录到对应统计表中
		queryBothDirectionFlow (timeType, flowType, startTime);
	}

	/**
	 * @Description: 查询两个方向的流量
	 * @author NovLi
	 * @date 2018/10/12 10:19
	 */
	public void queryBothDirectionFlow (String timeType, String flowType, String startTime) {
		log.info ("开始执行开始时间为" + timeType + "传输类型为" + flowType + "的" + Constants.IN_FLOW + "方向流量统计");
		queryRecordByVarable (Constants.IN_FLOW, timeType, flowType, startTime);
		log.info ("开始执行开始时间为" + timeType + "传输类型为" + flowType + "的" + Constants.OUT_FLOW + "方向流量统计");
		queryRecordByVarable (Constants.OUT_FLOW, timeType, flowType, startTime);
	}

	/**
	 * @Description: 查询记录表中的数据是否存在
	 * @author NovLi
	 * @date 2018/10/12 10:20
	 */
	@Transactional
	public void queryRecordByVarable (String direction, String timeType,
									  String flowType, String startTime) {
		//流量方向 时间类型 传输类型 开始时间 查询记录
		log.info ("【执行proxyFlowTaskDoRecord】中【queryRecordByVarible方法】");
		ProxyFlowTaskDoRecord proxyFlowTaskDoRecord = proxyFlowTaskDoRecordService
				.queryRecordByVarible (
						new ProxyFlowTaskDoRecord (flowType, timeType, direction, startTime, null));

		log.info ("【执行proxyFlowTaskDoRecord】中【queryRecordByVarible方法结果为】= {}", proxyFlowTaskDoRecord);
		if (proxyFlowTaskDoRecord != null) { //判断状态
			if ("0".equals (proxyFlowTaskDoRecord.getExistData ())) { //记录了 但是没有执行完毕
				//执行记录操作 并且修改记录表状态
				checkFlowType (proxyFlowTaskDoRecord);
			} else if ("1".equals (proxyFlowTaskDoRecord.getExistData ())) { //记录了 并且已经执行完毕
				log.info ("记录表数据已记录过，记录时间:" + startTime);
			}
		} else { //没有该条记录
			//先查询最后一条记录 计算出相差时间 进行补全操作 在执行本时段的记录添加 统计添加 记录修改
			log.info ("【执行查询ProxyFlowTaskDoRecord中最后一条记录】");
			ProxyFlowTaskDoRecord lastRecord = proxyFlowTaskDoRecordService
					.queryLastRecordByVarible (
							new ProxyFlowTaskDoRecord (flowType, timeType, direction));

			log.info ("【查询ProxyFlowTaskDoRecord中最后一条记录】 = {}", lastRecord);
			//补全现在执行时间和最后记录操作时间差
			if (NotNullUtil.objectNotNull (lastRecord)) {
				log.info ("【查询ProxyFlowTaskDoRecord中最存在最后一条记录并执行补全操作】");
				completData (lastRecord, startTime, flowType, direction);
			}
			//没有最后一条记录 执行本条操作
			log.info ("【ProxyFlowTaskDoRecord中没有最后一条记录执行添加操作】");
			ProxyFlowTaskDoRecord record = proxyFlowTaskDoRecordService
					.insertRecord (new ProxyFlowTaskDoRecord (flowType, timeType, direction, startTime, "0"));

			log.info ("【ProxyFlowTaskDoRecord中添加操作执行成功返回数据】 ={}", record.toString ());
			checkFlowType (record);
		}
	}

	/**
	 * @Description: 补全数据 -> 获取时间差
	 * @author NovLi
	 * @date 2018/10/17 15:14
	 */
	public void completData (ProxyFlowTaskDoRecord lastRecord,
							 String startTime,
							 String flowType,
							 String direction) {

		Long recordTtimeDiff = null;
		if ("min".equals (lastRecord.getRecordType ())) {
			recordTtimeDiff = DateUtil.minOfTimeDiff (startTime, lastRecord.getCountTime ());
		} else if ("hour".equals (lastRecord.getRecordType ())) {
			recordTtimeDiff = DateUtil.hourOfTimeDiff (startTime, lastRecord.getCountTime ());
		} else if ("day".equals (lastRecord.getRecordType ())) {
			recordTtimeDiff = DateUtil.dayOfTimeDiff (startTime, lastRecord.getCountTime ());
		}
		executeCompletionData (recordTtimeDiff, lastRecord, flowType, direction);
	}

	/**
	 * @Description: 执行数据补全操作
	 * @author NovLi
	 * @date 2018/10/17 15:40
	 */
	public void executeCompletionData (Long recordTtimeDiff,
									   ProxyFlowTaskDoRecord lastRecord,
									   String flowType,
									   String direction) {
		String time = null;
		for (int j = 0; j < recordTtimeDiff; j++) {
			if ("min".equals (lastRecord.getRecordType ())) {
				time = CommonTools.offsetMin (time, 1);
				saveRecordCompletionData (time, flowType, direction, "min");
			} else if ("hour".equals (lastRecord.getRecordType ())) {
				time = CommonTools.offsetHour (time, 1);
				saveRecordCompletionData (time, flowType, direction, "hour");
			} else if ("day".equals (lastRecord.getRecordType ())) {
				time = CommonTools.offsetDay (time, 1);
				saveRecordCompletionData (time, flowType, direction, "day");
			}
		}
	}

	/**
	 * @Description: 保存记录信息 并执行补全操作
	 * @author NovLi
	 * @date 2018/10/17 15:30
	 */
	public void saveRecordCompletionData (String time,
										  String flowType,
										  String direction,
										  String timeType) {
		ProxyFlowTaskDoRecord proxyFlowTaskDoRecord = proxyFlowTaskDoRecordService
				.insertRecord (
						new ProxyFlowTaskDoRecord (flowType, timeType, direction, time, "0"));

		checkFlowType (proxyFlowTaskDoRecord);
	}

	/**
	 * @Description: 判断文件传输类型
	 * @author NovLi
	 * @date 2018/10/12 10:32
	 */
	public void checkFlowType (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord) { //执行记录操作
		//执行分钟记录直接去原始表查询 执行小时 查询分钟表的记录数据 查询天的时候查询小时表记录的时间
		log.info ("【checkFlowType检查数据传输类型为】={}", proxyFlowTaskDoRecord.getServProtocol ());
		if (Constants.SFTS.equals (proxyFlowTaskDoRecord.getServProtocol ())) { //类型是sfts
			log.info ("【执行commonSftsFlowService】统计【execStat】方法={}", proxyFlowTaskDoRecord.toString ());
			commonSftsFlowService.execStat (proxyFlowTaskDoRecord);
		}
	}
}
