package com.jtsec.mc.log.analysis.api.service;

import com.jtsec.mc.log.analysis.pojo.model.ProxyFlowTaskDoRecord;

import java.util.List;

/**
 * @author NovLi
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/9/17 13:30
 */
public interface ProxyFlowTaskDoRecordService {

	ProxyFlowTaskDoRecord queryLastRecordByVarible (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord);

	Integer insert (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord);

	Integer count (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord);

	ProxyFlowTaskDoRecord queryRecordByVarible (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord);

	Integer updataRecoed  (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord);

	List<ProxyFlowTaskDoRecord> queryRecordsByVarible (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord);

	ProxyFlowTaskDoRecord insertRecord (ProxyFlowTaskDoRecord proxyFlowTaskDoRecord);
}
