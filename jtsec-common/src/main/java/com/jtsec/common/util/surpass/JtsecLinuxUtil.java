package com.jtsec.common.util.surpass;

import com.jtsec.common.Constants.KeyCons;
import com.jtsec.common.Constants.SystemCons;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
public class JtsecLinuxUtil {
	/**
	 * Logger for this class
	 */

	//tc_filter_run.sh add 5m 0 192.168.8.170 6000 1
	public static void callTcFilterRun(String addOrDel, long rate,String priority, String ip, String port, Integer userId) throws Exception{
		String cmd = "sh " + PropertiesUtil.getFilePath(KeyCons.SP_SIPHOLE_BACK_TC) + " " + addOrDel + " "+ rate + "k "+ priority + " " + ip + " " + port + " " + userId;
		LinuxCmdUtil.execCmd(cmd);
	}
	
	/**
	 * 获得磁盘空间
	 * @return
	 * @throws Exception
	 */
	public static int getAvalibleDiskFreeSpace() throws Exception{
		String[] cmds = new String[]{"/bin/sh", "-c", "df -h | grep sda | awk '{total +=$5} END {print total}'"};
		List<String> list = LinuxCmdUtil.execCmdContent(cmds, "100");
		String ads = "0";
		if(NotNullUtil.collectionNotNull(list)){
			if(NotNullUtil.stringNotNull(list.get(0))){
				ads = list.get(0);
			}
		}
		log.info(String.valueOf (Integer.parseInt(ads)));
		return Integer.parseInt(ads);
	}
	
	/**
	 * 执行/etc目录的存盘操作
	 * @return
	 */
	public static boolean saveEtc(){
		try {
			if(SystemCons.OS_IS_LINUX){
				Runtime.getRuntime().exec("/usr/bin/jman save_etc");
			}
			return true;
		}catch (Exception e) {
			log.info("执行[jman save_etc]保存内存中配置信息到硬盘操作失败!");
		}
		return false;
	}
	
}
