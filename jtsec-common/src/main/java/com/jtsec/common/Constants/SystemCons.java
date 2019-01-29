package com.jtsec.common.Constants;

import com.jtsec.common.util.surpass.NotNullUtil;
import com.jtsec.common.util.webconfig.WebConfigUtil;
/**
 * 
 * @author dingzhichao
 * 系统的静态变量
 */
public class SystemCons {
	//超级管理员信息
	public static final String SUPER_USERNAME = "super";
	public static final String SUPER_PASSWORD = "jtsec@123456";
	public static final int SUPER_ROLE_CODE = 1;
	
	//判断当前的操作系统是linux还是windows
	public static boolean OS_IS_LINUX = true;
	//服务器前后置的标志位，0=in 后置(内端机)	1=out 前置(外端机) 2:设备认证服务器(end)  3:用户认证服务器(back) 4：用户服务器
	public static String SERVICE_FLAG = "0";
	//全局配置中，判断open_IPv6参数(是否开启IPv6支持)，默认为0：不开启，1：开启
	public static String open_IPv6 = "0";
	//设备编号
	public static String dev_type = "";
	// 安全文件传输配置对比同步模式，默认为1：配置文件为准
	// 0：不对比，1：配置文件为准模式，2：数据库为准模式，3：数据库与配置文件同时一致模式
	public static String sftsConfCompareSyncMode = MoitorConstants.SFTS_COMPARE_CONF_SHALL_PREVAIL;
	//ftp文件配置对比同步模式，默认为1：配置文件为准
    //0：不对比，1：配置文件为准模式，2：数据库为准模式，3：数据库与配置文件同时一致模式
    public static String ftpConfCompareSyncMode = MoitorConstants.FTP_COMPARE_BOTH;
    
    //sip文件配置对比同步模式，默认为1：配置文件为准
    //0：不对比，1：配置文件为准模式，2：数据库为准模式
    public static String sipConfCompareSyncMode = MoitorConstants.SIP_COMPARE_CONF_SHALL_PREVAIL;
    public static int ftpFileTransVersion = 100;
    // 要执行导出/导入配置文件的模块的列表
    public static String moduleConfigs = "";
    
	static{
		String osName = System.getProperty("os.name");
		if(osName.startsWith("W") || osName.startsWith("w") || osName.matches("^(?i)Windows.*$")) {// Window 系统
			OS_IS_LINUX = false;
		}
	}

	public static void initWeb(){
		// 0-in	1-out
		SERVICE_FLAG = WebConfigUtil.getWebConfigValue(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SERVER_FLAG);
		// 是否开启IPv6支持
		open_IPv6 = WebConfigUtil.getWebConfigValue(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_OPEN_IPv6);
		// 获取设备类别
		dev_type = WebConfigUtil.getWebConfigValue(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_DEV_TYPE);
		// 获取web-config.conf配置文件中sfts同步模式sfts_conf_compare_sync_mode
		sftsConfCompareSyncMode = WebConfigUtil.getWebConfigValue(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.SFTS_CONF_COMPARE_SYNC_MODE);
		// 如果从配置文件中未获取到安全文件传输配置对比同步模式参数配置值，或获取到的配置值不在支持的列表中，则将模式设置为1：配置文件为准模式
		if(sftsConfCompareSyncMode == null || !MoitorConstants.SFTS_COMPARE_MODE_MAP.containsKey(sftsConfCompareSyncMode)){
			sftsConfCompareSyncMode = MoitorConstants.SFTS_COMPARE_CONF_SHALL_PREVAIL;
		}
		// 获取web-config.conf配置文件中ftp同步模式ftp_conf_compare_sync_mode
        ftpConfCompareSyncMode = WebConfigUtil.getWebConfigValue(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.FTP_CONF_COMPARE_SYNC_MODE);
        // 如果从配置文件中未获取到ftp文件传输配置对比同步模式参数配置值，或获取到的配置值不在支持的列表中，则将模式设置为1：配置文件为准模式
        if(ftpConfCompareSyncMode == null || !MoitorConstants.FTP_COMPARE_MODE_MAP.containsKey(ftpConfCompareSyncMode)){
            ftpConfCompareSyncMode = MoitorConstants.FTP_COMPARE_BOTH;
        }
        
        // 获取web-config.conf配置文件中sip同步模式sip_conf_compare_sync_mode
        sipConfCompareSyncMode = WebConfigUtil.getWebConfigValue(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.SIP_CONF_COMPARE_SYNC_MODE);
        // 如果从配置文件中未获取到sip文件传输配置对比同步模式参数配置值，或获取到的配置值不在支持的列表中，则将模式设置为1：配置文件为准模式
        if(sipConfCompareSyncMode == null || !MoitorConstants.SIP_COMPARE_MODE_MAP.containsKey(sipConfCompareSyncMode)){
        	sipConfCompareSyncMode = MoitorConstants.SIP_COMPARE_CONF_SHALL_PREVAIL;
        }
        // 获取FTP文件传输程序版本
        String fftv = WebConfigUtil.getWebConfigValue(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.FTP_FILE_TRANS_VERSION);
        if(NotNullUtil.stringNotNull(fftv)){
        	ftpFileTransVersion = Integer.valueOf(fftv);
        }
		// 备份全局配置文件
		WebConfigUtil.bakWebConfig();
	}
	
	/**
	 * 判断是不是内端机
	 * @return
	 */
	public static boolean isIn(){
		if("0".equals(SERVICE_FLAG)){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是不是外端机
	 * @return
	 */
	public static boolean isOut(){
		if("1".equals(SERVICE_FLAG)){
			return true;
		}
		return false;
	}

	/**
	 * 判断是不是设备认证服务器
	 * @return
	 */
	public static boolean isDevice(){
		if("2".equals(SERVICE_FLAG)){
			return true;
		}
		return false;
	}

	/**
	 * 判断是不是用户认证服务器
	 * @return
	 */
	public static boolean isUser(){
		if("3".equals(SERVICE_FLAG)){
			return true;
		}
		return false;
	}

	/**
	 * 判断是不是普通视频用户服务器
	 * @return
	 */
	public static boolean isVideoUser(){
		if("4".equals(SERVICE_FLAG)){
			return true;
		}
		return false;
	}
	
	
	public static void main(String[] args) {
		new SystemCons();
	}
	
}
