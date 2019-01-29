package com.jtsec.mc.dev.moitor.syslog;

import com.jtsec.mc.dev.moitor.snmp.JdbcDeviceUtil;
import com.jtsec.mc.dev.moitor.snmp.util.ServIpUtil;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.surpass.util.requestproxy.DatagramSocketUtil;

/**
 * syslog数据包转发线程
 * 
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 *
 */

@SuppressWarnings("restriction")
public class SyslogForwardThread implements Runnable{

	/**
	 * Logger for this class
	 */
	protected static final Logger logger = Logger.getLogger(SyslogForwardThread.class);

	private static boolean RUNFALG = false;
	private static DatagramSocket socket = null;
	private static final int THREAD_POOL_SIZE = 8;
	private ExecutorService systemLogExecutor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	
	public SyslogForwardThread(){	}

	public SyslogForwardThread(boolean runFlag){
		SyslogForwardThread.RUNFALG = runFlag;
	}
	
	public static synchronized void setRunFlag(boolean runFlag){
		RUNFALG = runFlag;
		if(runFlag){
			if(runFlag){
				try {
					if(socket == null || socket.isClosed()){
						socket = DatagramSocketUtil.startDatagramSocketListen();
						SyslogForwardThread cadt = new SyslogForwardThread(true);
						Thread thread = new Thread(cadt);
						thread.start();
					}
				} catch (SocketException e) {
					RUNFALG = false;
					socket = null;
				}
			}else{
				DatagramSocketUtil.closeDatagramSocket(socket);
			}
		}
	}
	
	public static synchronized boolean getRunFlag(){
		return RUNFALG;
	}
	
	public void run(){
		try {
			while(RUNFALG){
				DatagramPacket packet = DatagramSocketUtil.receiveData(socket);
				
				//判断ip是否为探针服务其的地址、或ip是否为设备列表中的设备地址
				String ip = packet.getAddress().getHostAddress();
				String region = ServIpUtil.loadValue("region");
				if(!JdbcDeviceUtil.isMonitorDev(ip, region)){
					continue;
				}
				//获得数据包的编码方式
//				String encode = PropertiesUtil.getValue("datagram_encrypt_encoding");
//				String data = new String(packet.getData(), 0, packet.getLength(), encode);
//				data = this.getFromBASE64(this.encrypt(data, encode), encode);
//				systemLogExecutor.execute(new SystemLogThread(data));
				
				//将源ip、端口、及信息设备所处在位置添加到数据包中
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(bos);
				dos.writeUTF(ip);
				dos.writeUTF(region);
				dos.write(packet.getData(), 0, packet.getLength());
				
				systemLogExecutor.execute(new SyslogParseThread(bos.toByteArray()));
//				Thread.sleep(3000);
			}
			if(!RUNFALG){
				DatagramSocketUtil.closeDatagramSocket(socket);
				socket = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 异或运算，将每一位置反
	 * @param srcStr 所要进行运算的字符串
	 * @param encode 字符串原编码
	 * @return
	 */
	protected String encrypt(String srcStr, String encode) {
		String s = "";
		try {
			byte[] ret = srcStr.getBytes(encode);
			for (int j = 0; j < ret.length; j++) {
				ret[j] ^= 0xfe; // 异或
			}
			s = new String(ret, encode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}

	
	/**
	 * 将以BASE64加的密的字符串解密，并返回解密后的字符串
	 * @param s 所要进行运算的字符串
	 * @param encode 字符串原编码
	 * @return
	 */
	protected String getFromBASE64(String s, String encode) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b, encode);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		SyslogForwardThread cdt = new SyslogForwardThread();
		setRunFlag(true);
		Thread t = new Thread(cdt);
		t.start();
	}
}
