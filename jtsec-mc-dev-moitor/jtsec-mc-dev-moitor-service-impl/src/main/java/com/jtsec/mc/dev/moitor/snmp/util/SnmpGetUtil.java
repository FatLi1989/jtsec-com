package com.jtsec.mc.dev.moitor.snmp.util;

import com.jtsec.mc.dev.moitor.pojo.model.HandType;
import com.jtsec.mc.dev.moitor.pojo.model.NemDeviceInfo;
import com.jtsec.mc.dev.moitor.pojo.model.OidPojo;
import com.jtsec.mc.dev.moitor.snmp.parse.*;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * snmp主动取值工具类
 * @author surpassE
 * @see 2015-09-16
 */
@Slf4j
public class SnmpGetUtil {

	private static Snmp snmp = null;
	private static int MAX_REP = 1000;

	/**
	 * 同步获得snmp信息
	 * @param obj
	 * @param userHandler
	 * @param handType
	 * @return
	 */
	public static void snmpGetSync(NemDeviceInfo obj, Object userHandler, HandType handType){
		syncGet(obj, userHandler, handType, "0");
	}

	/**
	 * 异步获得snmp信息
	 * @param obj
	 * @param userHandler
	 * @param handType
	 * @return
	 */
	public static void snmpGetAsync(NemDeviceInfo obj, Object userHandler, HandType handType){
		asyncGet(obj, userHandler, handType, "0");
	}
	
	/**
	 * 同步获得snmp信息
	 * @param obj
	 * @param userHandler
	 * @param handType
	 * @return
	 */
	public static void snmpGetBulkSync(NemDeviceInfo obj, Object userHandler, HandType handType){
		syncGet(obj, userHandler, handType, "1");
	}

	/**
	 * 
	 * @param obj
	 * @param userHandler
	 * @param handType
	 */
	public static void snmpGetBulkAsync(NemDeviceInfo obj, Object userHandler, HandType handType){
		asyncGet(obj, userHandler, handType, "1");
	}
	
	/**
	 * 
	 * @param obj
	 * @param userHandler
	 * @param handType
	 * @param pduType
	 */
	private static void asyncGet(NemDeviceInfo obj, Object userHandler, HandType handType, String pduType){
		List<OidPojo> list = OidUtil.listOidPojoByHandType(obj.getDevOsType(), handType);
		PDU pdu = getPDU(list, pduType);
		try {
			initSnmp();
			Target ct = initCommunityTarget(obj);
			ResponseListener rl = new ResponseListener () {
				@Override
				public void onResponse(ResponseEvent event) {
					if(event != null && event.getResponse() != null){
						Vector<?> vector = event.getResponse().getVariableBindings();  
						Object obj = event.getUserObject();
						if(obj != null && vector != null && vector.size() > 0){
							String methodName = "parseVb";
							Class<?> clazz = obj.getClass();
							try {
								Method method = clazz.getMethod(methodName, Vector.class);
								method.invoke(obj, vector);
							} catch (Exception e) {
								e.printStackTrace();
							}
							for(Object vbObj : vector){
								VariableBinding vb = (VariableBinding)vbObj;
								log.debug(vb.getOid() + "==>" + vb.getVariable().toString());
							}
							log.debug("响应集合的长度：" + vector.size());
						}
					}
				}
			};
			saveRequestPud(pdu, userHandler);
			snmp.send(pdu, ct, userHandler, rl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 同步执行walk操作
	 * @param obj
	 * @param userHandler
	 * @param handType
	 */
	public static void snmpWalkSync(NemDeviceInfo obj, Object userHandler, HandType handType){
		List<OidPojo> list = OidUtil.listOidPojoByHandType(obj.getDevOsType(), handType);
		Vector<VariableBinding> vector = new Vector<VariableBinding>();
		try {
			if(list != null && list.size() > 0){
				initSnmp();
				Target target = initCommunityTarget(obj);
				PDU requestPDU = new PDU ();
				for(OidPojo oidPojo : list){
					PDU pdu = new PDU ();
					String oidValue = oidPojo.getValue();
					//logger.debug("请求的OID值：" + oidValue);
					OID oid = new OID (oidValue);
					pdu.add(new VariableBinding (oid));
					requestPDU.add(new VariableBinding (oid));
					pdu.setType(PDU.GET);
					boolean finished = false;
					while (!finished) {
						VariableBinding vb = null;
						ResponseEvent respEvent = snmp.getNext(pdu, target);
						PDU response = respEvent.getResponse();
						if (null == response) {
							finished = true;
							break;
						} else {
							vb = response.get(0);
						}
						// check finish
						finished = checkWalkFinished(oid, pdu, vb);
						if (!finished) {
							vector.add(vb);
							pdu.setRequestID(new Integer32 (0));
							pdu.set(0, vb);
						}
					}
				}
				execParseVb(obj,userHandler, vector, requestPDU);
				//logger.debug("接收的总的数据量:" + vector.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(obj.getDevIdentify());
		}
	}
	
	/**
	 * 此方法被弃用，重载的snmpWalkSync的方法已经包含了此方式的处理流程
	 * @param obj
	 * @param userHandler
	 * @param vector
	 * @param handType
	 * @return
	 */
	@Deprecated
	public static Vector<VariableBinding> snmpWalkSync(NemDeviceInfo obj, Object userHandler, Vector<VariableBinding> vector, HandType handType){
		List<OidPojo> list = OidUtil.listOidPojoByHandType(obj.getDevOsType(), handType);
		PDU pdu = new PDU ();
		String oidValue = "";
		if(list != null && list.size() > 0){
			oidValue = list.get(0).getValue();
		}
		log.debug("请求的OID值：" + oidValue);
		OID oid = new OID (oidValue);
		pdu.add(new VariableBinding (oid));
		pdu.setType(PDU.GET);
		try {
			initSnmp();
			Target target = initCommunityTarget(obj);
			boolean finished = false;
			while (!finished) {
				VariableBinding vb = null;
				ResponseEvent respEvent = snmp.getNext(pdu, target);
				PDU response = respEvent.getResponse();
				if (null == response) {
					finished = true;
					break;
				} else {
					vb = response.get(0);
				}
				// check finish
				finished = checkWalkFinished(oid, pdu, vb);
				if (!finished) {
					if(vector == null){
						vector = new Vector<VariableBinding>();
					}
					vector.add(vb);
					pdu.setRequestID(new Integer32 (0));
					pdu.set(0, vb);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vector;
	}

	/**
	 * 异步执行snmpwalk命令 只适合采集一个OID的数据信息
	 * @param obj
	 * @param userHandler
	 * @param handType
	 */
	public static void snmpWalkAsync(NemDeviceInfo obj, final Object userHandler, HandType handType){
		List<OidPojo> list = OidUtil.listOidPojoByHandType(obj.getDevOsType(), handType);
		final PDU pdu = new PDU ();
		String oidValue = "";
		if(list != null && list.size() > 0){
			oidValue = list.get(0).getValue();
		}
		log.debug("请求的OID值：" + oidValue);
		final OID oid = new OID (oidValue);
		pdu.add(new VariableBinding (oid));
		pdu.setType(PDU.GET);
		try {
			initSnmp();
			final Target target = initCommunityTarget(obj);
			final Vector<VariableBinding> vector = new Vector<VariableBinding>();
			final CountDownLatch latch = new CountDownLatch(1);
			final NemDeviceInfo finalObj = obj;
			saveRequestPud(pdu, userHandler);
			ResponseListener rl = new ResponseListener (){
				@Override
				public void onResponse(ResponseEvent event) {
					((Snmp) event.getSource()).cancel(event.getRequest(), this);
					try {
						PDU response = event.getResponse();
						if (response != null && response.size() > 0) {
							VariableBinding vb = response.get(0);
							boolean finished = checkWalkFinished(oid, pdu, vb);
							if (!finished) {
								vector.add(vb);
								pdu.setRequestID(new Integer32 (0));
								pdu.set(0, vb);
								((Snmp) event.getSource()).getNext(pdu, target, null, this);
							} else {
								execParseVb(finalObj,  userHandler, vector, pdu);
								log.debug("接收的总的数据量:" + vector.size());
								latch.countDown();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						latch.countDown();
					}
				}
			};
			snmp.getNext(pdu, target, userHandler, rl);
			boolean wait = latch.await(5, TimeUnit.SECONDS);
			log.debug("wait:" + wait);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param targetOID
	 * @param pdu
	 * @param vb
	 * @return
	 */
	private static boolean checkWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
		boolean finished = false;
		if (pdu.getErrorStatus() != 0) {
			log.debug("[true] responsePDU.getErrorStatus() != 0 ");
			log.debug(pdu.getErrorStatusText());
			finished = true;
		} else if (vb.getOid() == null) {
			log.debug("[true] vb.getOid() == null");
			finished = true;
		} else if (vb.getOid().size() < targetOID.size()) {
			log.debug("[true] vb.getOid().size() < targetOID.size()");
			finished = true;
		} else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
			log.debug("[true] targetOID.leftMostCompare() != 0");
			finished = true;
		} else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
			log.debug("[true] Null.isExceptionSyntax(vb.getVariable().getSyntax())");
			finished = true;
		} else if (vb.getOid().compareTo(targetOID) <= 0) {
			log.debug("[true] Variable received is not " + "lexicographic successor of requested " + "one:");
			log.debug(vb.toString() + " <= " + targetOID);
			finished = true;
		}
		return finished;
	}

	/**
	 * 
	 * @param obj
	 * @param userHandler
	 * @param handType
	 * @param pduType snmp4j取值的方式GET、TRAP
	 */
	private static void syncGet(NemDeviceInfo obj, Object userHandler, HandType handType, String pduType){
		List<OidPojo> list = OidUtil.listOidPojoByHandType(obj.getDevOsType(), handType);
		PDU pdu = getPDU(list, pduType);
		try {
			initSnmp();
			Target ct = initCommunityTarget(obj);
			ResponseEvent event = snmp.send(pdu, ct);
			//读取得到的绑定变量   
			if(event != null && event.getResponse() != null) { 
				Vector<?> vector = event.getResponse().getVariableBindings();   
				execParseVb(obj, userHandler, vector, pdu);
//				if(userHandler != null && vector != null && vector.size() > 0){
//					saveRequestPud(pdu, userHandler);
//					String methodName = "parseVb";
//					Class<?> clazz = userHandler.getClass();
//					Method method = clazz.getMethod(methodName, Vector.class);
//					method.invoke(userHandler, vector);
//				}
			}   
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 反射执行userHandler的parseVb方法
	 * @param userHandler
	 * @param vector
	 * @param pdu
	 * @throws Exception
	 */
	private static void execParseVb(NemDeviceInfo obj, Object userHandler, Vector<?> vector, PDU pdu) throws Exception{
		if(userHandler != null && vector != null && vector.size() > 0){
			saveRequestPud(pdu, userHandler);
			setBaseParam(obj, userHandler);
			String methodName = "parseVb";
			Class<?> clazz = userHandler.getClass();
			Method method = clazz.getMethod(methodName, Vector.class);
			method.invoke(userHandler, vector);
		}
	}

	/**
	 * 将请求的PDU传递到userHandler中
	 * @param pdu
	 */
	private static void saveRequestPud(PDU pdu, Object obj){
		if(obj == null){
			return;
		}
		try {
			String methodName = "setRequestPdu";
			Class<?> clazz = obj.getClass();
			Method[] methods = clazz.getMethods();
			if(methods != null){
				for(Method method : methods){
					if(methodName.equals(method.getName())){	//判断方法名称是不是setRequestPdu
						Class<?>[] arr = method.getParameterTypes();
						if(arr != null && arr[0].getName().equals(PDU.class.getName())){	//判断参数类型是不是PUD
							method.invoke(obj, pdu);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将请求的PDU传递到userHandler中
	 */
	private static void setBaseParam(NemDeviceInfo dev, Object obj){
		if(obj == null){
			return;
		}
		try {
			String methodName = "setBaseParams";
			Class<?> clazz = obj.getClass();
			Method[] methods = clazz.getMethods();
			if(methods != null){
				for(Method method : methods){
					if(methodName.equals(method.getName())){	//判断方法名称是不是setRequestPdu
						Class<?>[] arr = method.getParameterTypes();
						if(arr != null && arr.length == 2){	//判断参数类型是不是PUD
							method.invoke(obj, dev.getDevIdentify(), dev.getDevOsType());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化Target
	 * @param obj
	 * @return
	 */
	public static Target initCommunityTarget(NemDeviceInfo obj){
		Target target = null;
		try {
			String address = "udp:" + obj.getDevIp() + "/" + obj.getSnmpPort();
			//设定CommunityTarget  
			target = new CommunityTarget ();
			Address add = GenericAddress.parse(address);
			//设定远程主机的地址  
			target.setAddress(add);

			//设置超时重试次数 
			target.setRetries(obj.getSnmpReconnect()); 
			//设置超时的时间  
			target.setTimeout(obj.getSnmpDelay());
			//设置snmp版本
			if("1".equals(obj.getSnmpType())){
				target.setVersion(SnmpConstants.version1);
				//设置snmp共同体
				((CommunityTarget)target).setCommunity(new OctetString (obj.getSnmpRcommunity()));
			}else if("3".equals(obj.getSnmpType())){
				UsmUser usmUser = new UsmUser (new OctetString (obj.getSnmpUserName()), AuthMD5.ID, new OctetString (obj.getSnmpAuthPwd()), PrivDES.ID, new OctetString (obj.getSnmpEncryptPwd()));
				//添加用户
				snmp.getUSM().addUser(new OctetString (obj.getSnmpUserName()),usmUser);
				target = new UserTarget ();
				// 设置安全级别
				((UserTarget) target).setSecurityLevel(SecurityLevel.AUTH_PRIV);
				((UserTarget) target).setSecurityName(new OctetString ("MD5DES"));
				target.setVersion(SnmpConstants.version3);
			}else{
				target.setVersion(SnmpConstants.version2c);
				//设置snmp共同体
				((CommunityTarget)target).setCommunity(new OctetString (obj.getSnmpRcommunity() + obj.getSnmpRcommunityParam()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	/**
	 * 初始化Snmp服务
	 * @return
	 * @throws Exception
	 */
	private static Snmp initSnmp() throws Exception{
		if(snmp == null){
			//设定采取的协议  
			TransportMapping transport = new DefaultUdpTransportMapping ();
			transport.listen();
			snmp = new Snmp (transport);
		}
		return snmp;
	}

	/**
	 * 封装请求OID的PDU数据包
	 * @param list
	 * @pduType 取值方式 
	 * @return
	 */
	private static PDU getPDU(List<OidPojo> list, String pduType){
		PDU pdu = new PDU ();
		for(OidPojo oid : list){
			String value = oid.getValue();
			if(oid.getMap().size() > 0){
				recursionPdu(pdu, list, oid.getMap());
			}else{
				pdu.add(new VariableBinding (new OID (value)));
			}
		}
		if("0".equals(pduType)){
			pdu.setType(PDU.GET);
		}else{
			pdu.setType(PDU.GETBULK);
			pdu.setMaxRepetitions(MAX_REP);
		}
		for(int i = 0; i < pdu.size(); i++){
			VariableBinding vb = pdu.get(i);
			log.debug("请求的OID值==>" + vb.getOid());
		}
		return pdu;
	}

	/**
	 * 递归遍历，获得子叶节点
	 * @param pdu
	 * @param list
	 * @param map
	 */
	private static void recursionPdu(PDU pdu, List<OidPojo> list, Map<String, OidPojo> map){
		for(String key : map.keySet()){
			OidPojo op = map.get(key);
			if(op.getMap().size() > 0){
				recursionPdu(pdu, list, op.getMap());
			}else{
				pdu.add(new VariableBinding (new OID (key)));
			}
		}
	}


	public static void main(String[] args) throws InterruptedException {
		long t1 = System.currentTimeMillis();
		NemDeviceInfo ndi = new NemDeviceInfo();
		ndi.setSnmpPort("161");
		ndi.setSnmpType("2");
		ndi.setSnmpRcommunity("public");
//		ndi.setSnmpDelay(5000);
//		三层交换机
		ndi.setDevIp("192.168.8.1");
		ndi.setDevIdentify("sw");
		ndi.setDevOsType("100003");
		
//		window
//		ndi.setDevIp("192.168.8.159");
//		ndi.setDevIdentify("win_x86");
//		ndi.setDevOsType("100004");

//		linux
//		ndi.setDevIp("192.168.8.96");
//		ndi.setDevIdentify("dx_out");
//		ndi.setDevOsType("100005");
		
//		linux
//		ndi.setDevIp("115.231.122.27");
//		ndi.setDevIdentify("pt");
//		ndi.setDevOsType("100004");

//		int i = 20;
//		while(i >= 0){
//		test1(ndi);
//		test1_1(ndi);
//		test2(ndi);
//		test3(ndi);
//		test4(ndi);
//		test5(ndi);
//		test6(ndi);
		test7(ndi);
//		Thread.sleep(500);
//		i--;
//		}
//		AnalyzeExecTask.createRecvCpuMemTable(DateUtil.format(System.currentTimeMillis()));
		
//		float a = 13841560f, b=3587708, c=40f;
//		float x = 83377152, y = 4792708, z = 1048536;
//		logger.debug((a * 100/x));
//		logger.debug((b * 100/y));
//		logger.debug((c * 100/z));
//		System.out.println((a/x + b/y + c/z)/3);
		
		log.debug("测试完成...共使用时间【" + (System.currentTimeMillis() - t1) + "】ms");
	}
	
	public static void test1(NemDeviceInfo ndi){
		//采集MEM信息
//		snmpWalkSync(ndi, new MemParse("dx_out"), HandType.MEM);
		snmpWalkSync(ndi, new MemParse(ndi.getDevIdentify()), HandType.MEM);
	}
	
	public static void test1_1(NemDeviceInfo ndi){
		//采集MEM信息
//		snmpWalkSync(ndi, new MemParse("dx_out"), HandType.MEM);
		snmpWalkSync(ndi, new MemDiskParse (ndi.getDevIdentify()), HandType.MEM_DISK);
	}
	
	public static void test2(NemDeviceInfo ndi){
		//采集CPU信息
//		snmpWalkSync(ndi, new CpuParse("dx_out"), HandType.CPU);
		snmpWalkSync(ndi, new CpuParse (ndi.getDevIdentify()), HandType.CPU);
	}
	
	public static void test3(NemDeviceInfo ndi){
		//采集网卡信息
		snmpWalkSync(ndi, new InterfaceParse (ndi.getDevIdentify()), HandType.INTERFACE);
//		String[] arr = {"dx_out", "dx_in", "dx_i", "dx_o", "dx_qz", "dx_hz"};
//		String[] arr = {"dx_hz"};
//		for(String dx : arr){
			snmpWalkSync(ndi, new InterfaceEditParse (ndi.getDevIdentify()), HandType.INTERFACE_EDIT);
//		}
	}
	
	public static void test4(NemDeviceInfo ndi){
		//采集硬盘信息
		snmpWalkSync(ndi, new DiskParse (ndi.getDevIdentify()), HandType.DISK);
	}
	
	public static void test5(NemDeviceInfo ndi){
		//采集设备进程信息
		snmpWalkSync(ndi, new ProgressParse(ndi.getDevIdentify()), HandType.PROGRESS);
	}
	
	public static void test6(NemDeviceInfo ndi){
		//获取软件信息
//		Vector<VariableBinding> vector = new Vector<VariableBinding>();
//		SoftwareParse sp = new SoftwareParse("dx_out");
//		snmpWalkSync(ndi, sp, vector, HandType.SOFTWARE_NAME);
//		snmpWalkSync(ndi, sp, vector, HandType.SOFTWARE_TYPE);
//		snmpWalkSync(ndi, sp, vector, HandType.SOFTWARE_TIME);
//		sp.parseVb(vector);
		snmpWalkSync(ndi, new SoftwareParse(ndi.getDevIdentify()), HandType.SOFTWARE);
	}
	
	public static void test7(NemDeviceInfo ndi){
		snmpWalkSync(ndi, new InterfaceParse(ndi), HandType.INTERFACE);
//		if("100003".equals(ndi.getDevOsType())){
//			//获得三层交换机的vlan值
//			List<String> vlanList = new LoadInterfaceImpl().loadVlanList(ndi.getDevIdentify());
//			for(String vlan : vlanList){
//				ndi.setSnmpRcommunityParam("@" + vlan);
//				SnmpGetUtil.snmpWalkSync(ndi, new InterfaceItemParse(ndi.getDevIdentify()), HandType.SW_ITEM);
//				SnmpGetUtil.snmpWalkSync(ndi, new InterfaceItemEditParse(ndi.getDevIdentify()), HandType.SW_ITEM_EDIT);
//			}
//		}
	}

}
