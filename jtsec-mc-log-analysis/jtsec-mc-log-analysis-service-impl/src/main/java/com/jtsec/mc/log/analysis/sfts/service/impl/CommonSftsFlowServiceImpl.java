package com.jtsec.mc.log.analysis.sfts.service.impl;

import com.jtsec.common.Constants.Constants;
import com.jtsec.common.util.datahandle.DataConvert;
import com.jtsec.common.util.date.CommonTools;
import com.jtsec.mc.log.analysis.api.service.ProxyFlowTaskDoRecordService;
import com.jtsec.mc.log.analysis.api.sfts.service.*;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlow;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowHour;
import com.jtsec.mc.log.analysis.pojo.model.JtSftsFlowMin;
import com.jtsec.mc.log.analysis.pojo.model.ProxyFlowTaskDoRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/10/12 10:55
 */
@Slf4j
@Service
public class CommonSftsFlowServiceImpl implements CommonSftsFlowService {

	@Autowired
	private JtSftsFlowMinService jtSftsFlowMinService;
	@Autowired
	private JtSftsFlowHourService jtSftsFlowHourService;
	@Autowired
	private JtSftsFlowDayService jtSftsFlowDayService;
	@Autowired
	private JtSftsFlowService jtSftsFlowService;
	@Autowired
	private ProxyFlowTaskDoRecordService proxyFlowTaskDoRecordService;

	/**
	 * @Description: 按时间类型执行统计
	 * @author NovLi
	 * @date 2018/10/12 11:13
	 */
	@Override
	public void execStat (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord) {
		if (Constants.TIME_TYPE_MIN.equals (proxyFlowTaskDoRecord.getRecordType ())) { //分钟

			log.info ("【执行execMinStart 分钟统计方法】");

			//先去原始表中查询各server的流量 添加到对应的表中 然后修改记录表状态
			if (execMinStat (proxyFlowTaskDoRecord))
				log.info ("执行完毕");
		} else if (Constants.TIME_TYPE_HOUY.equals (proxyFlowTaskDoRecord.getRecordType ())) { //小时

			log.info ("【执行execHourStat 小时统计方法】");

			//先去查询记录表中的分钟数是否够60 够60的话 进行流量统计 添加到对应的表中 修改记录表状态
			if (execHourStat (proxyFlowTaskDoRecord))
				log.info ("执行完毕");
		} else if (Constants.TIME_TYPE_DAY.equals (proxyFlowTaskDoRecord.getRecordType ())) { //天

			log.info ("【执行execDayStat 天统计方法】");

			//先查询小时记录表中的小时是否够24小时 够24小时的话 进行统计 添加到对应的表中 修改记录表状态
			//不够的话查询出缺失数据时间段 然后补全 在进行统计 修改状态
			if (execDayStat (proxyFlowTaskDoRecord))
				log.info ("执行完毕");
		}
	}

	/**
	 * @Description: 执行时间类型为分钟的统计操作
	 * @author NovLi
	 * @date 2018/10/15 13:39
	 */
	@Transactional
	public Boolean execMinStat (ProxyFlowTaskDoRecord minRecord) {
		Boolean flag = true;

		minRecord = getStatEndTime (minRecord);
		log.info ("【getStatEndTime获取结束时间为】 = {}", minRecord);
		log.info ("【执行jtSftsFlowService中queryByVaribleGroupByServerName方法】");
		List<JtSftsFlow> jtSftsFlowList = jtSftsFlowService    //以服务器名称分组查询数据
				.queryByVaribleGroupByServerName (
						new JtSftsFlow (
								minRecord.getDirection (),
								minRecord.getStartTime (),
								minRecord.getEndTime ()));
		log.info ("【查询原始表中没有对应数据】");
		if (jtSftsFlowList.isEmpty ()) {
			JtSftsFlow jtSftsFlow = new JtSftsFlow ();
			jtSftsFlow.setServName ("");
			jtSftsFlow.setFileSize (0);
			jtSftsFlow.setDirection (minRecord.getDirection ());
			jtSftsFlow.setFileCount (0);
			jtSftsFlow.setFromIp ("");
			jtSftsFlow.setTime (minRecord.getStartTime ());
			jtSftsFlowMinService.addJtFlowMin (jtSftsFlow);
		} else {
			log.info ("【执行jtSftsFlowService中queryByVaribleGroupByServerName方法的返回结果为】 = {}", jtSftsFlowList.toString ());
			for (JtSftsFlow jtSftsFlow : jtSftsFlowList) {//添加到对应的分钟记录表中
				log.info ("【执行jtSftsFlowMinService中addJtFlowMin方法,参数为】 = {}", jtSftsFlow.toString ());
				jtSftsFlowMinService.addJtFlowMin (jtSftsFlow);
			}
		}
		minRecord.setExistData ("1");
		proxyFlowTaskDoRecordService.updataRecoed (minRecord);
		return flag;
	}

	/**
	 * @Description: 执行小时统计
	 * @author NovLi
	 * @date 2018/10/15 15:33
	 */
	@Transactional
	public Boolean execHourStat (ProxyFlowTaskDoRecord hourRecord) {
		Boolean flag = true;

		hourRecord = getStatEndTime (hourRecord);
		log.info ("【执行CommonSftsFlowServiceImpl】中【execHourStat】参数为 = {}", hourRecord);
		hourRecord.setRecordType ("min");
		log.info ("【执行proxyFlowTaskDoRecordService】中【queryRecordsByVarible】参数为 = {}", hourRecord);
		List<ProxyFlowTaskDoRecord> minRecordList = proxyFlowTaskDoRecordService
				.queryRecordsByVarible (hourRecord); //查询该时段内一小时是否有60条已完成记录
		log.info ("【执行proxyFlowTaskDoRecordService】返回结果为 = {}", minRecordList.size ());
		if (minRecordList.size () == 60) { //够60条的话 分钟表中查询 计算出流量
			log.info ("【记录条数够60条】");
			//执行状态为0的记录
			if (!execMinNotExist (minRecordList))
				return false;
			//执行小时内分钟统计并把数据保存到对应的小时表中 同时更新记录状态
			execMinToHourSta (hourRecord);
		} else if (minRecordList.size () < 60) { //不够60条的话 查询出缺失时间段数据
			log.info ("【记录条数不够60条进行数据补全】");
			//获取缺失数据时间集合
			List<String> lossTimesList = getLossTimes (minRecordList, "hour");
			//解析出没有统计到的分钟 并执行分钟统计操作
			if (lossTimesList.size () > 0) {
				doHourStat (hourRecord, lossTimesList);
			} else if (lossTimesList.size () == 0) {
				doHourStat (hourRecord, CommonTools.minList ());
			}
			execMinToHourSta (hourRecord);
		}
		return flag;
	}

	@Transactional
	public void doHourStat (ProxyFlowTaskDoRecord hourRecord, List<String> lossTimesList) {
		String time = hourRecord.getStartTime ().substring (0, 14);//获取2015-11-16 19:
		//循环统计noHavaMinList中的每分钟的日志
		for (String loseTime : lossTimesList) {
			StringBuilder startTime = new StringBuilder ();
			startTime.append (time + loseTime + ":00");
			//补全缺失的分钟记录
			ProxyFlowTaskDoRecord proxyFlowTaskDoRecord = proxyFlowTaskDoRecordService
					.insertRecord (
							new ProxyFlowTaskDoRecord (
									hourRecord.getServProtocol (),
									"min",
									hourRecord.getDirection (),
									startTime.toString (),
									"0"));
			execMinStat (proxyFlowTaskDoRecord);
		}
	}

	/**
	 * @Description: 执行天统计
	 * @author NovLi
	 * @date 2018/10/15 16:38
	 */
	@Transactional
	public boolean execDayStat (ProxyFlowTaskDoRecord dayRecord) {
		Boolean flag = true;

		dayRecord = getStatEndTime (dayRecord);
		//查询记录表中当天24小时记录是否完整
		dayRecord.setRecordType ("hour");

		List<ProxyFlowTaskDoRecord> hourRecordList = proxyFlowTaskDoRecordService
				.queryRecordsByVarible (dayRecord); //查询该时段内一小时是否有24条已完成记录

		if (hourRecordList.size () == 24) { //够24条记录的话 查询24条记录状态 是否都执行完毕
			//执行状态为0的小时记录
			if (!executeHourExistIs0 (hourRecordList))
				return false;
			//查询小时表中的数据并保存到对应的记录表中 修改数据状态
			execHourToDayStat (dayRecord);
		} else if (hourRecordList.size () < 24) { //不够24条记录的话 查询缺失的小时
			//获取缺失数据时间集合
			List<String> lossTimesList = getLossTimes (hourRecordList, "day");

			if (lossTimesList.size () > 0) {
				doDayStat (dayRecord, lossTimesList);
			} else if (lossTimesList.size () == 0) {
				doDayStat (dayRecord, CommonTools.hourList ());
			}
		}
		execHourToDayStat (dayRecord);
		return flag;
	}

	@Transactional
	public void doDayStat (ProxyFlowTaskDoRecord dayRecord, List<String> lossTimesList) {
		String time = dayRecord.getStartTime ().substring (0, 11);//获取2015-11-16
		for (String loseTime : lossTimesList) {
			StringBuilder startTime = new StringBuilder ();
			if ("24".equals (loseTime)) {
				startTime.append (new StringBuilder (CommonTools.genDelayOneDay (new StringBuilder (time).append ("00:00:00").toString ())));
			} else {
				startTime.append (time).append (loseTime).append (":00:00");
			}
			//补全缺失的小时记录
			ProxyFlowTaskDoRecord proxyFlowTaskDoRecord = proxyFlowTaskDoRecordService
					.insertRecord (
							new ProxyFlowTaskDoRecord (
									dayRecord.getServProtocol (),
									"hour",
									dayRecord.getDirection (),
									startTime.toString (),
									"0"));
			//在执行记录状态为0的小时记录补全以及记录表补全
			execHourStat (proxyFlowTaskDoRecord);
		}
	}

	/**
	 * @Description: 执行一天内小时统计并修改记录表中的状态
	 * @author NovLi
	 * @date 2018/10/15 15:50
	 */
	@Transactional
	public void execHourToDayStat (ProxyFlowTaskDoRecord dayRecord) {
		//查询小时表中每个服务器的流量
		List<JtSftsFlowHour> jtSftsFlowHourList = jtSftsFlowHourService
				.statisticsFlowHourToDayGroupByServName (
						new JtSftsFlowHour (
								dayRecord.getDirection (),
								dayRecord.getStartTime (),
								dayRecord.getEndTime ()
						));
		//计算后添加到对应的天统计表中
		for (JtSftsFlowHour jtSftsFlowHour : jtSftsFlowHourList) {
			if (jtSftsFlowHourList.size () == 1) {
				jtSftsFlowHour.setStartTime (dayRecord.getStartTime ());
				jtSftsFlowDayService.addFlowDay (jtSftsFlowHour);
			} else if (jtSftsFlowHourList.size () >= 1) {
				if (!"".equals (jtSftsFlowHour.getServName ())) {
					jtSftsFlowHour.setStartTime (dayRecord.getStartTime ());
					jtSftsFlowDayService.addFlowDay (jtSftsFlowHour);
				}
			}
		}
		//修改记录表该条记录的状态
		dayRecord.setExistData ("1");
		dayRecord.setRecordType ("day");
		proxyFlowTaskDoRecordService.updataRecoed (dayRecord);
	}

	/**
	 * @Description: 执行状态为0的小时记录
	 * @author NovLi
	 * @date 2018/10/16 9:48
	 */
	private boolean executeHourExistIs0 (List<ProxyFlowTaskDoRecord> hourRecordList) {
		Boolean flag = true;

		for (ProxyFlowTaskDoRecord hourRecord : hourRecordList) {
			if ("0".equals (hourRecord.getExistData ())) {
				if (!execHourStat (hourRecord))
					return false;
			}
		}
		return flag;
	}

	/**
	 * @Description: 执行小时内分钟统计并修改记录表中的状态
	 * @author NovLi
	 * @date 2018/10/15 15:50
	 */
	@Transactional
	public void execMinToHourSta (ProxyFlowTaskDoRecord hourRecord) {
		//查询分钟表中各服务器的一小时流量
		List<JtSftsFlowMin> jtSftsFlowMinList = jtSftsFlowMinService
				.staFlowMinToHourGroupByServName (
						new JtSftsFlowMin (
								hourRecord.getStartTime (),
								hourRecord.getDirection (),
								hourRecord.getEndTime ()));
		//根据服务器名称保存到对应统计表中
		for (JtSftsFlowMin jtSftsFlowMin : jtSftsFlowMinList) {
			if (jtSftsFlowMinList.size () == 1) {
				jtSftsFlowMin.setStartTime (hourRecord.getStartTime ());
				jtSftsFlowHourService.addFlowHour (jtSftsFlowMin);
			} else if (jtSftsFlowMinList.size () >= 1) {
				if (!"".equals (jtSftsFlowMin.getServName ())) {
					jtSftsFlowMin.setStartTime (hourRecord.getStartTime ());
					jtSftsFlowHourService.addFlowHour (jtSftsFlowMin);
				}
			}
		}
		hourRecord.setExistData ("1");
		hourRecord.setRecordType ("hour");
		proxyFlowTaskDoRecordService.updataRecoed (hourRecord);
	}

	/**
	 * 比较两个数组中的元素是否相等
	 * 将baseList中有，而currList中没有的的元素
	 * 以数组形式返回，若返回数组为空，则两数组相等
	 *
	 * @param baseList 参照数组
	 * @param currList 要对比的数组
	 * @return
	 */
	private List<String> compareList (List<String> baseList, List<String> currList) {
		List<String> list = new ArrayList<> ();
		if (baseList.size () == currList.size ()) {
			return list;
		}
		Iterator<String> it = baseList.iterator ();
		while (it.hasNext ()) {
			String currMin = it.next ();
			if (!currList.contains (currMin)) {
				list.add (currMin);
			}
		}
		return list;
	}

	public static void main (String[] args) {
		System.out.println (new String ("1989-11-17 14:00:00").substring (0, 14));
	}

	/**
	 * @Description: 获取统计结束时间
	 * @author NovLi
	 * @date 2018/10/15 15:39
	 */
	private ProxyFlowTaskDoRecord getStatEndTime (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord) {

		if ("min".equals (proxyFlowTaskDoRecord.getRecordType ())) {
			proxyFlowTaskDoRecord
					.setEndTime (DataConvert
							.genEndTime (
									proxyFlowTaskDoRecord.getRecordType (),
									proxyFlowTaskDoRecord.getCountTime ()));
			proxyFlowTaskDoRecord
					.setStartTime (proxyFlowTaskDoRecord.getCountTime ());
		} else if ("hour".equals (proxyFlowTaskDoRecord.getRecordType ())
				|| "day".equals (proxyFlowTaskDoRecord.getRecordType ())) {
			proxyFlowTaskDoRecord
					.setEndTime (proxyFlowTaskDoRecord.getCountTime ());
			proxyFlowTaskDoRecord
					.setStartTime (DataConvert
							.genEndTime (
									proxyFlowTaskDoRecord.getRecordType (),
									proxyFlowTaskDoRecord.getCountTime ()));
		}
		proxyFlowTaskDoRecord.setCountTime ("");
		proxyFlowTaskDoRecord.setExistData ("");
		return proxyFlowTaskDoRecord;
	}

	/**
	 * @Description: 执行状态为0的分钟记录
	 * @author NovLi
	 * @date 2018/10/15 17:36
	 */
	private Boolean execMinNotExist (List<ProxyFlowTaskDoRecord> proxyFlowTaskDoRecordList) {
		Boolean flag = true;

		for (ProxyFlowTaskDoRecord minRecord : proxyFlowTaskDoRecordList) { //遍历数据60条
			if ("0".equals (minRecord.getExistData ())) {    //获取没有执行的
				if (!execMinStat (minRecord)) {
					log.info ("执行" + CommonSftsFlowServiceImpl.class + "中executeStatistics方法中execMinStatistics时" +
							minRecord.toString () + "时出现错误");
					return false;
				}
			}
		}
		return flag;
	}

	/**
	 * @Description: 获取丢失时间
	 * @author NovLi
	 * @date 2018/10/16 13:37
	 */
	private List<String> getLossTimes (List<ProxyFlowTaskDoRecord> minRecordList, String timeType) {
		List<String> currTimeList = new ArrayList<> ();
		List<String> lossTimeList = new ArrayList<> ();
		//循环一小时内的每分钟
		for (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord : minRecordList) {
			if ("hour".equals (timeType)) {
				currTimeList.add (proxyFlowTaskDoRecord.getCountTime ().substring (14, 16));
				lossTimeList = compareList (CommonTools.minList (), currTimeList);
			} else if ("day".equals (timeType)) {
				currTimeList.add (proxyFlowTaskDoRecord.getCountTime ().substring (11, 13));
				lossTimeList = this.compareList (CommonTools.hourList (), currTimeList);
			}
		}
		return lossTimeList;
	}
}
