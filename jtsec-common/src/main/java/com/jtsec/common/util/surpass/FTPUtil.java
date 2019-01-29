package com.jtsec.common.util.surpass;

import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;
import java.net.InetAddress;

public class FTPUtil {
	
	/**
	 * 测试FTP连通性
	 * 
	 * @param ip	
	 * @param port
	 * @param user
	 * @param password
	 * @return 成功则返success，失败则返回其余的错误信息
	 */
	public static String ftpTest(String ip, String port, String user, String password){
		String msg = "success";
		FTPClient ftp = new FTPClient();
		try {
			//绑定ip和port
			ftp.connect(InetAddress.getByName(ip), Integer.parseInt(port));
			Boolean flag = ftp.login(user, password);
			if(!flag){
				msg = "user or password error!";
			}
		} catch (Exception e) {
			msg = e.getMessage();
		}finally{
			if(ftp.isConnected()){
				try {
					ftp.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return msg;
	}
}
