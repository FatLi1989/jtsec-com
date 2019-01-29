package com.jtsec.mc.dev.moitor.snmp.util;

import com.jtsec.common.util.surpass.PropertiesUtil;
import com.jtsec.mc.dev.moitor.pojo.model.PropPojo;
import com.surpass.aisr.ai.AiReadAndSaveI;
import com.surpass.aisr.ai.AiReadAndSaveImpl;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * SNMP4J工具类
 * 初始化SNMP服务
 * 加载oid的properties数据
 * 
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 *
 */
@Slf4j
public class SnmpUtil {
	/**
	 * Logger for this class
	 */

	private static AiReadAndSaveI airs = AiReadAndSaveImpl.createAiReadAndSaveSaveImpl();

	private static Snmp snmp = null;
	private static ThreadPool threadPool = null;
	
	private static Snmp sendSnmp = null;
	private static CommunityTarget communityTarget = null;

	/**
	 * 启动SNMP侦听
	 * 
	 * @param param default "udp:127.0.0.1/162"
	 * @param param
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static synchronized void startSnmp(String param, CommandResponder commandResponder) throws Exception {
		if(threadPool == null && snmp == null){
			threadPool = ThreadPool.create("Trap", 2);
			MultiThreadedMessageDispatcher dispatcher = new MultiThreadedMessageDispatcher(threadPool,new MessageDispatcherImpl());
			Address listenAddress = GenericAddress.parse(param); // 本地IP与监听端口
			TransportMapping transport;
			// 对TCP与UDP协议进行处理
			if (listenAddress instanceof UdpAddress) {
				transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
			} else {
				transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
			}
			snmp = new Snmp(dispatcher, transport);
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
			USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
			SecurityModels.getInstance().addSecurityModel(usm);
			snmp.listen();
			//更改为自定义的udp包处理接口
			snmp.addCommandResponder(commandResponder);
			
			log.info("准备接收udp数据包....");
		}
	}
	
	/**
	 * 关闭SNMP服务
	 */
	public static synchronized void stopSnmp(){
		try {
			if(snmp != null){
				snmp.close();
			}
			if(threadPool != null){
				threadPool.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			snmp = null;
			threadPool = null;
		}
	}
	
	/**
	 * 判断SNMP服务运行状态
	 * 
	 * @return
	 */
	public static boolean getStatus(){
		if(snmp != null && threadPool != null){
			return true;
		}
		return false;
	}
	
	/**
	 * 从UNIT_TYPE_MIB_AND_CONF_MAP.properties中查找oid相匹配的key值，如果完全匹配则获得key对应的value值
	 * 如果没有完全匹配的那么取以oid开头的且长度最长的key作为返回值
	 * eg： oid = 1.2.3.4.1.1110
	 * properties中key=value
	 * [1]1.2.3.4.1.1110 = a
	 * [2]1.2.3.4.1.1110.2 = b
	 * [3]1.2.3.4.1.1110.2.3 = c;
	 * 那么OidPojo为key=1.2.3.4.1.1110 value=a
	 * 如果没有[1]属性值那么
	 * OidPojo为key=1.2.3.4.1.1110.2.3 value=c
	 * 
	 * @param oid 要检索的OID
	 * @return
	 */
	public static PropPojo findOidPojoByOid(String oid){
		PropPojo oidPojo = null;
		Map<String, String> map = getUnitTypeMibAndConfMap();
		String oidTmp = "";
		for(String key : map.keySet()){
			if(oid.equals(key)){
				oidPojo = new PropPojo();
				oidPojo.setKey(key);
				oidPojo.setValue(map.get(key));
				return oidPojo;
			}
			if(oid.startsWith(key) && oidTmp.length() < key.length()){
				oidTmp = key;
			}
		}
		if(oidTmp != ""){
			oidPojo = new PropPojo();
			oidPojo.setKey(oidTmp);
			oidPojo.setValue(map.get(oidTmp));
		}
		return oidPojo;
	}
	
	/**
	 * 获得properties中values
	 * 
	 * @param filePath
	 * @return
	 */
	public static Map<String, String> getMibMappingMap(String filePath){
		filePath = SnmpUtil.class.getResource("/") + "snmp/" + filePath + "_MIB.properties";
		return getMap(filePath);
	}
	
	/**
	 * 将tab_monitor_map.properties转为Map
	 * @return
	 */
	public static Map<String, String> getTabMonitorMap(){
		String filePath = SnmpUtil.class.getResource("/") + "/snmp/tab_monitor_map.properties";
		return getMap(filePath);
	}
	
	/**
	 * 将UNIT_TYPE_MIB_AND_CONF_MAP.properties转为Map
	 * @return
	 */
	public static Map<String, String> getUnitTypeMibAndConfMap(){
		String filePath = SnmpUtil.class.getResource("/") + "snmp/UNIT_TYPE_MIB_AND_CONF_MAP.properties";
		return getMap(filePath);
	}
	
	/**
	 * 将filePath对应的properties转为Map
	 * @param filePath
	 * @return
	 */
	private static Map<String, String> getMap(String filePath){
		Map<String, String> map = airs.readPropertiesToMap(filePath);
		if(map == null){
			return new HashMap<String, String>();
		}
		return map;
	}
	
	/**
	 * 将接收的udp包发送出去
	 * @param pdu
	 * @param param eg:udp:192.168.8.8/161
	 */
	public static void sendPDU(PDU pdu, String param){
		try{
			if(param == null){
			String servIp = ServIpUtil.loadValue("serv_ip");
			String snmpServParam = PropertiesUtil.getValue("snmp_server_param");
			 param = snmpServParam.replaceAll("serv_ip", servIp);
			}
			if(communityTarget == null){
				communityTarget = new CommunityTarget();
				Address address = GenericAddress.parse(param);
				communityTarget.setAddress(address);
				communityTarget.setCommunity(new OctetString("public"));
				communityTarget.setTimeout(5 * 60);
				communityTarget.setVersion(SnmpConstants.version2c);
				TransportMapping transport = new DefaultUdpTransportMapping();
				transport.listen();
				if(sendSnmp == null){
					sendSnmp = new Snmp(transport);
				}
			}
			sendSnmp.send(pdu, communityTarget);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
