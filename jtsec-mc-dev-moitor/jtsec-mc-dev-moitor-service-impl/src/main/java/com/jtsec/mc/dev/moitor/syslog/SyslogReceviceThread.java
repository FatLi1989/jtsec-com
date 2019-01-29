package com.jtsec.mc.dev.moitor.syslog;

import com.jtsec.mc.dev.moitor.snmp.JdbcDeviceUtil;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import com.surpass.util.requestproxy.DatagramSocketUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * cadata(collection analy data)采集分析数据
 *
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 */
@SuppressWarnings ("restriction")
public class SyslogReceviceThread implements Runnable {
	/**
	 * Logger for this class
	 */
	protected static final Logger logger = Logger.getLogger (SyslogReceviceThread.class);

	private static final int THREAD_POOL_SIZE = 100;
	private static boolean RUNFALG = false;
	private static DatagramSocket socket = null;
	private ExecutorService systemLogExecutor = Executors.newFixedThreadPool (THREAD_POOL_SIZE);
//	private ExecutorService monitorLogExecutor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);


	public SyslogReceviceThread () {
	}

	public SyslogReceviceThread (boolean runFlag) {
		SyslogReceviceThread.RUNFALG = runFlag;
	}

	public static synchronized void setRunFlag (boolean runFlag) {
		RUNFALG = runFlag;
		if (runFlag) {
			try {
				if (socket == null || socket.isClosed ()) {
					socket = DatagramSocketUtil.startDatagramSocketListen ();
					SyslogReceviceThread cadt = new SyslogReceviceThread (true);
					Thread thread = new Thread (cadt);
					thread.start ();
				}
			} catch (SocketException e) {
				RUNFALG = false;
				socket = null;
			}
		} else {
			DatagramSocketUtil.closeDatagramSocket (socket);
		}
	}

	public static boolean getRunFlag () {
		return RUNFALG;
	}

	public void run () {

		while (RUNFALG) {
			try {
				DatagramPacket packet = DatagramSocketUtil.receiveData (socket);
				//判断ip是否为探针服务其的地址、或ip是否为设备列表中的设备地址
				String ip = packet.getAddress ().getHostAddress ();
				boolean flag = JdbcDeviceUtil.isMonitorDev (ip);
				logger.debug (ip + "是否是监控的日志信息：" + flag);
				if (!JdbcDeviceUtil.isMonitorDev (ip)) {
					continue;
				}

				//数据通过探针程序时，在探针服务器上添加设备的地址和设备所在的区域
//				byte[] buf = new byte[1048576];
//				ByteArrayInputStream bis = new  ByteArrayInputStream(packet.getData());
//				DataInputStream dis = new DataInputStream(bis);
//				String ip = dis.readUTF();
//				String region = dis.readUTF();
//				//如果不是监控的设备不解析数据信息
//				if(!JdbcDeviceUtil.isMonitorDev(ip, region)){
//					continue;
//				}
//				int size = dis.read(buf);

				//处理双向网闸时需要对数据进行解密的操作
//				String encode = PropertiesUtil.getValue("datagram_encrypt_encoding");
//				String data = new String(buf, 0, size, encode);
//				//对数据进行BASE64解密操作
//				data = this.getFromBASE64(this.encrypt(data, encode), encode);
//				logger.debug(data);
//				systemLogExecutor.execute(new SyslogParseThread(ip, region, data));

//				对双向网闸进行数据揭秘操作
//				String encode = PropertiesUtil.getValue("datagram_encrypt_encoding");
//				String data = new String(packet.getData(), 0, packet.getLength(), encode);
//				logger.info(data);

				//处理单道或是前后置程序
//				systemLogExecutor.execute(new SyslogParseThread(packet.getAddress(), region, buf, size));
				systemLogExecutor.execute (new SyslogParseThread (packet.getAddress (), "1", packet.getData (), packet.getLength ()));
//				Thread.sleep(5);
			} catch (Exception e) {
				e.printStackTrace ();
			}
		}
		if (!RUNFALG) {
			DatagramSocketUtil.closeDatagramSocket (socket);
		}
	}

	/**
	 * 判断 dataIp = probeIp或是deviceIpList中包含dataIp则返回true、否则返回false
	 *
	 * @param dataIp
	 * @param probeIp
	 * @param deviceIpList
	 * @return
	 */
	protected boolean isHopeFulIp (String dataIp, String probeIp, List<String> deviceIpList) {
		if (dataIp.equals (probeIp)) {
			return true;
		}
		for (String ip : deviceIpList) {
			if (ip != null && ip.equals (dataIp)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 异或运算，将每一位置反
	 *
	 * @param srcStr 所要进行运算的字符串
	 * @param encode 字符串原编码
	 * @return
	 */
	protected String encrypt (String srcStr, String encode) {
		String s = "";
		try {
			byte[] ret = srcStr.getBytes (encode);
			for (int j = 0; j < ret.length; j++) {
				ret[j] ^= 0xfe; // 异或
			}
			s = new String (ret, encode);
		} catch (Exception ex) {
			ex.printStackTrace ();
		}
		return s;
	}


	/**
	 * 将以BASE64加的密的字符串解密，并返回解密后的字符串
	 *
	 * @param s      所要进行运算的字符串
	 * @param encode 字符串原编码
	 * @return
	 */
	protected String getFromBASE64 (String s, String encode) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder ();
		try {
			byte[] b = decoder.decodeBuffer (s);
			return new String (b, encode);
		} catch (Exception e) {
			return null;
		}
	}

}
