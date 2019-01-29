package com.jtsec.mc.dev.moitor.snmp;

import com.jtsec.common.util.surpass.DateUtil;
import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.common.util.surpass.NotNullUtil;
import com.jtsec.mc.dev.moitor.snmp.util.OidUtil;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.VariableBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * SNMP数据包解析接口
 * 
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 *
 */
@Slf4j
public class DefaultCommandResponder implements CommandResponder {
	/**
	 * Logger for this class
	 */
	private String ip = "";
	private String port = "";
	private String region = "1";
	private String monitorTime = "";
	
	@SuppressWarnings("unchecked")
	public synchronized void processPdu(CommandResponderEvent event) {
		monitorTime = DateUtil.getNowTime();
		Address address = event.getPeerAddress();
		ip = address.toString().split("/")[0];
		port = address.toString().split("/")[1];
		
		PDU pdu = event.getPDU();
		List<VariableBinding> list = pdu.getVariableBindings();
		log.info (list.get(0).getOid().toString());
		if(NotNullUtil.collectionNotNull(list) && list.size() >= 2){
			String firstOid = list.get(0).getOid().toString();
			String secondOid = list.get(1).getOid().toString();
			if(OidUtil.isLegal(firstOid, secondOid)){
				this.analysisVarbinds(list);
			}
		}
	}
	
	/**
	 * 分析绑定的变量值
	 * 
	 * @param vbList
	 * @param
	 */
	private void analysisVarbinds(List<VariableBinding> vbList){
		//OID:1.3.6.1.6.3.1.1.4.1.0 variable : 1.3.6.1.4.1.39056.*.*
		String snmpTropOid = vbList.get(1).getVariable().toString();
		List<String> sqlList = new ArrayList<String>();
		
		List<VariableBinding> indexList = new ArrayList<VariableBinding>();
		for(VariableBinding vb : vbList){
			String oid = vb.getOid().toString();
			String typeTmp = OidUtil.getHandType(oid);
			if("ip".equals(typeTmp)){
				this.ip = vb.getVariable().toString();
				indexList.add(vb);
			}
			if("port".equals(typeTmp)){
				this.port = vb.getVariable().toString();
				indexList.add(vb);
			}
			if("region".equals(typeTmp)){
				this.region = vb.getVariable().toString();
				indexList.add(vb);
			}
		}
		
		if(ip == null || ip == "" || !JdbcDeviceUtil.isMonitorDev(this.ip, this.region)){
			return;
		}
		
		for(Object obj : indexList){
			vbList.remove(obj);
		}
		String dataType = OidUtil.getHandType(snmpTropOid);
		if(dataType != null){
			//通过职责链解析VariableBinding集合数据信息
			AbstractDataHandler dataHandler = DataHandlerFactory.createDataHandlerFactory().getDataHandler(this.ip, this.port, this.region, this.monitorTime);
			dataHandler.handleRequest(dataType, sqlList, vbList);
			DataHandlerFactory.releaseSources(dataHandler);
		}
		log.info ("sqlList = {}", sqlList);
		//批量执行sql将解析获得数据信息持久化到数据库中
		DbUtil.executeBetchSql(null, sqlList);
		//如果接收的设备配置信息，那么重新加载监控设备列表
		if("devinfo".equals(dataType)){
			JdbcDeviceUtil.reloadDevice();
		}
	}
}
