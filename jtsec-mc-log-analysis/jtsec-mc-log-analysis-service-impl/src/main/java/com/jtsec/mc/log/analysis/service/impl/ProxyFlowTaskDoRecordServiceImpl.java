package com.jtsec.mc.log.analysis.service.impl;

import com.jtsec.common.exception.JtsecException;
import com.jtsec.common.util.datahandle.DataConvert;
import com.jtsec.mc.log.analysis.api.service.ProxyFlowTaskDoRecordService;
import com.jtsec.mc.log.analysis.pojo.model.ProxyFlowTaskDoRecord;
import com.jtsec.mc.log.jtsec.mapper.ProxyFlowTaskDoRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/1713:32
 */
@Slf4j
@Service ("ProxyFlowTaskDoRecordService")
public class ProxyFlowTaskDoRecordServiceImpl implements ProxyFlowTaskDoRecordService {

	@Autowired
	private ProxyFlowTaskDoRecordMapper proxyFlowTaskDoRecordMapper;

	@Override
	public Integer count (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord) {
		Map<String, Object> map = DataConvert
				.ConvertDataToMapWithoutException (proxyFlowTaskDoRecord);
		Integer count = proxyFlowTaskDoRecordMapper.count (map);
		return count;
	}

	@Override
	public ProxyFlowTaskDoRecord queryRecordByVarible (ProxyFlowTaskDoRecord condition) {
		Map<String, Object> map = DataConvert
				.ConvertDataToMapWithoutException (condition);
		return proxyFlowTaskDoRecordMapper.queryRecordByVarible (map);
	}

	@Override
	public Integer updataRecoed (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord) {
		Integer count = proxyFlowTaskDoRecordMapper
				.updateByPrimaryKeySelective (proxyFlowTaskDoRecord);
		if (count == null) throw new JtsecException ();
		return count;
	}

	@Override
	public List<ProxyFlowTaskDoRecord> queryRecordsByVarible (ProxyFlowTaskDoRecord condition) {
		Map<String, Object> map = DataConvert
				.ConvertDataToMapWithoutException (condition);
		return  proxyFlowTaskDoRecordMapper.queryRecordsByVarible (map);
	}

	@Override
	public ProxyFlowTaskDoRecord insertRecord (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord) {
		proxyFlowTaskDoRecordMapper.insertRecord(proxyFlowTaskDoRecord);
		return proxyFlowTaskDoRecord;
	}

	@Override
	public ProxyFlowTaskDoRecord queryLastRecordByVarible (ProxyFlowTaskDoRecord condition) {
		Map<String, Object> map = DataConvert
				.ConvertDataToMapWithoutException (condition);
		return proxyFlowTaskDoRecordMapper.queryLastRecordByVarible (map);
	}

	@Override
	public Integer insert (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord) {
		Integer insert = proxyFlowTaskDoRecordMapper.insertSelective (proxyFlowTaskDoRecord);
		if (insert == 0) throw new JtsecException ();
		return insert;
	}
}
