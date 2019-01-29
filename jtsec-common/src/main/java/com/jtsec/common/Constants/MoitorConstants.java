package com.jtsec.common.Constants;

import com.jtsec.common.util.surpass.CommonUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @ProjectName jtsec
 * @Description: moitor变量
 * @date 2018/11/15 13:38
 */
@Slf4j
public class MoitorConstants {


	public final static String FLASE_ZERO = "0";
	public final static String TRUE_ONE = "1";

	// 安全文件传输内部保留自用用户名单，该名单内的用户，除了super管理员意外，不允许其它管理员或用户通过前端页面进行添加及修改
	public final static List<String> SFTS_INNER_USER = new ArrayList<String> ();
	public final static Map<String, String> SFTS_COMPARE_MODE_MAP = new HashMap<String, String> ();
	public final static Map<String, String> FTP_COMPARE_MODE_MAP = new HashMap<String, String>();
	public final static Map<String, String> SIP_COMPARE_MODE_MAP = new HashMap<String, String>();

	//eg:E:\java\tomcat\apache-tomcat-7.0.54\webapps\she_com\
	public static String WEB_PATH = "";

	static{
		SFTS_INNER_USER.add("out2in");
		SFTS_INNER_USER.add("in2out");
		SFTS_INNER_USER.add("dbsync");
		SFTS_COMPARE_MODE_MAP.put(MoitorConstants.SFTS_COMPARE_NO, "安全文件传输配置比对同步：不比对模式");
		SFTS_COMPARE_MODE_MAP.put(MoitorConstants.SFTS_COMPARE_CONF_SHALL_PREVAIL, "安全文件传输配置比对同步：以配置文件为准模式");
		SFTS_COMPARE_MODE_MAP.put(MoitorConstants.SFTS_COMPARE_DB_SHALL_PREVAIL, "安全文件传输配置比对同步：以数据库为准模式");
		SFTS_COMPARE_MODE_MAP.put(MoitorConstants.SFTS_COMPARE_BOTH, "安全文件传输配置比对同步：配置文件与数据库同时一致模式");
		SFTS_COMPARE_MODE_MAP.put(MoitorConstants.SFTS_COMPARE_CONF_SHADOW, "安全文件传输配置比对同步：配置文件影子模式");

		FTP_COMPARE_MODE_MAP.put(MoitorConstants.FTP_COMPARE_NO, "FTP文件传输配置比对同步：不比对模式");
		FTP_COMPARE_MODE_MAP.put(MoitorConstants.FTP_COMPARE_CONF_SHALL_PREVAIL, "FTP文件传输配置比对同步：以配置文件为准模式");
		FTP_COMPARE_MODE_MAP.put(MoitorConstants.FTP_COMPARE_DB_SHALL_PREVAIL, "FTP文件传输配置比对同步：以数据库为准模式");
		FTP_COMPARE_MODE_MAP.put(MoitorConstants.FTP_COMPARE_BOTH, "FTP文件传输配置比对同步：配置文件与数据库同时一致模式");
		FTP_COMPARE_MODE_MAP.put(MoitorConstants.FTP_COMPARE_CONF_SHADOW, "FTP文件传输配置比对同步：配置文件影子模式");

		SIP_COMPARE_MODE_MAP.put(MoitorConstants.SIP_COMPARE_NO, "视频配置配置比对同步：不比对模式");
		SIP_COMPARE_MODE_MAP.put(MoitorConstants.SIP_COMPARE_CONF_SHALL_PREVAIL, "视频配置比对同步：以配置文件为准模式");
		SIP_COMPARE_MODE_MAP.put(MoitorConstants.SIP_COMPARE_DB_SHALL_PREVAIL, "视频配置比对同步：以数据库为准模式");

		File classPath = CommonUtil.getFile("", "");
		WEB_PATH = classPath.getParentFile().getParentFile().getAbsolutePath();
		if(!WEB_PATH.endsWith(File.separator)){
			WEB_PATH = WEB_PATH + File.separator;
		}
		log.debug("通过Class的getResource获得项目路径WEB_PATH：" + WEB_PATH);
	}

	public static String DWONLOAD_FIAL_PATH = "";

	public final static String SESSION_USER = "USERINFO";
	public final static String CURRNET_USER_FUNCTION = "current_user_functions";
	public final static String SESSION_USER_PAGESIZE = "everyPageSize";
	public final static String  OS_WIN = "WINDOWS";
	public final static String  OS_LINUX = "LINUX";


	//****************************** 系统配置信息   start  ************************************************
	public static final String SYS_GATEWAY = "GATEWAY";
	public static final String SYS_IPADDR = "IPADDR";
	public static final String SYS_NETMASK = "NETMASK";
	public static final String SYS_NETWORK = "NETWORK";


	public static final String ETH_DEVICE = "DEVICE";
	public static final String ETH_IPADDR = "IPADDR";
	public static final String ETH_GATEWAY = "GATEWAY";
	public static final String ETH_NETMASK = "NETMASK";
	public static final String ETH_NETWORK = "NETWORK";
	public static final String ETH_BOOTPROTO = "BOOTPROTO";
	public static final String ETH_MASTER = "MASTER";



	//****************************** 系统配置信息   start  ************************************************

	/******************************* ipv6配置start ***************************************************/
	public static final String ETH_NETWORKING_IPV6 = "NETWORKING_IPV6";
	public static final String ETH_IPV6_AUTOCONF = "IPV6_AUTOCONF";
	public static final String ETH_IPV6INIT = "IPV6INIT";
	public static final String ETH_IPV6ADDR = "IPV6ADDR";
	public static final String ETH_IPV6_DEFAULTGW = "IPV6_DEFAULTGW";
	public static final String ETH_IPV6TO4INIT = "IPV6TO4INIT";
	public static final String ETH_IPV6FORWADING = "IPV6FORWADING";

	/******************************* ipv6配置end  *****************************************************/

	//******************************FTP 配置信息   start  ************************************************
	public static final String FTP_ERROR_CODE = "FTP_ERROR_CODE";

	//FTP全局变量配置
	public static final String FTP_GLOBAL = "global";
	public static final String FTP_MAX_SIZE = "max_size";
	public static final String FTP_MAX_PACKET_SIZE = "max_packet_size";
	public static final String FTP_TIME_OUT = "time_out";
	public static final String FTP_RETRY_TIMES = "retry_times";
	public static final String FTP_INTERVAL = "interval";
	//内端机私有属性
	public static final String FTP_BACKUPTYPE = "backuptype";
	//外端机私有属性
	public static final String FTP_BACKUP = "backup";
	public static final String FTP_ANTIVIR = "antivir";
	public static final String FTP_ENABLE_ANTIVIR = "enable_antivir";


	//ftp server keyName
	public static final String FTP_PROTOCOL = "protocol";
	public static final String FTP_ADDRESS = "address";
	public static final String FTP_PORT = "port";
	public static final String FTP_USER_NAME = "user_name";
	public static final String FTP_PASSWORD = "password";
	public static final String FTP_DESTINATION = "destination";
	public static final String FTP_MIRROR = "mirror";
	public static final String FTP_INNER_SERVER_HASH = "inner_server_hash";
	public static final String FTP_PRIORITY = "priority";
	public static final String FTP_TRIGGER = "trigger";

	public static final String FTP_TASK = "task";
	public static final String FTP_SECONDS = "seconds";
	public static final String FTP_LIMIT = "limit";
	public static final String FTP_FORMAT = "format";
	public static final String FTP_SUFFIX = "suffix";
	public static final String FTP_POLICY = "policy";
	public static final String FTP_CUSTOMIZE = "customize";
	public static final String FTP_KEEP_DIR_LEVEL = "keep_dir_level";

	public static final String FTP_FILE_TRANS_VERSION = "ftp_file_trans_version";
	//******************************FTP 配置信息   end  ************************************************


	//******************************SFTS 配置信息  start  **********************************************
	//SFTS全局变量配置
	public static final String SFTS_GLOBAL = "global";

	public static final String SFTS_IP = "ip";
	public static final String SFTS_LISTEN = "listen";
	public static final String SFTS_TIMEOUT = "timeout";
	public static final String SFTS_ALIVE = "alive";
	public static final String SFTS_CACHE = "cache";
	public static final String SFTS_ENCRYPT = "encrypt";
	public static final String SFTS_LOGLEVEL = "loglevel";
	public static final String SFTS_TOSYSLOG = "tosyslog";
	public static final String SFTS_SYSLOGLEVEL = "sysloglevel";
	public static final String SFTS_REGIP = "regip";

	public static final String SFTS_VERIFY = "verify";
	public static final String SFTS_CACRT = "cacrt";
	public static final String SFTS_SRVCRT = "srvcrt";
	public static final String SFTS_SRVKEY = "srvkey";
	public static final String SFTS_BLOCKSIZE = "blocksize";
	public static final String SFTS_ACKCOUNT = "ackcount";
	public static final String SFTS_IDLE = "idle";
	public static final String SFTS_WEB_PORT = "web_port";
	public static final String SFTS_WEB_USER = "web_user";
	public static final String SFTS_WEB_PASS = "web_pass";

	public static final String SFTS_WEB_HEADER = "web_header";
	public static final String SFTS_WEB_FOOT = "web_foot";

	public static final String SFTS_UPRATELIMIT = "uprate_limit";
	//server keyName
	public static final String SFTS_PASS = "pass";
	public static final String SFTS_UPLOADS = "uploads";
	public static final String SFTS_DOWNLOADS = "downloads";
	public static final String SFTS_AFTER_UPLOAD = "afterupload";
	public static final String SFTS_BEFORE_DOWNLOAD = "beforedownload";
	public static final String SFTS_AUSOCKET = "ausocket";
	public static final String SFTS_AUEXEC = "auexec";
	public static final String SFTS_FTYPE = "ftype";
	public static final String SFTS_VTYPE = "vtype";
	public static final String SFTS_FILE_MARK = "file_mark";
	public static final String SFTS_UP_DOWN_PATH = "/tmpdisk/root/";

	public static final String SFTS_VPCRE = "vpcre";
	public static final String SFTS_FPCRE = "fpcre";

	//
	public static final String CLAMD = "clamd";
	public static final String SCAN_KEY = "scan_key";

	public static final String SFTSPWPATHNAME = "sfts_pw_path";
	//新增全局属性
	public static final String SFTS_REGDEV = "regdev";
	public static final String SFTS_LOGIN_BY_CERT = "login_by_cert";
	public static final String SFTS_CIPHER_LIST = "cipher_list";
	public static final String SFTS_LOGIN_LOCK = "login_lock";
	public static final String SFTS_LOGIN_BREAKPOINT = "login_breakpoint";
	public static final String SFTS_LOGIN_LOCK_TIME = "login_lock_time";
	public static final String SFTS_LOGIN_SESSION_TIME = "login_session_time";
	public static final String SFTS_LOGIN_RETRY_MAX = "login_retry_max";
	public static final String SFTS_LOGIN_PASSWORD_PERIOD = "login_password_period";

	public static final String SFTS_UPLOAD_FROM = "upload_from";
	public static final String SFTS_DOWNLOAD_FROM = "download_from";
	public static final String SFTS_ALL_FROM = "all_from";


	//******************************SFTS 配置信息  end  ************************************************

	//******************************SMB 配置信息  start  ***********************************************
	//com.danxiang.fs.smb.service.Mount
	public static final String SMB_MNT = "/bin/mount -t ";
	public static final String SMB_MNT_L = "/bin/mount -l";
	public static final String SMB_UMNT = "/bin/umount ";
	//	public static final String SMB_MOUNT_LIST_REG = "(\\/\\/.+)\\son\\s(\\/.+)+\\stype\\s(.+)\\s\\((.+),.+\\)";
//	public static final String SMB_MOUNT_LIST_REG = "(\\/\\/.+)\\son\\s(\\/.+)+\\stype\\s(.+)\\s\\((.*?),.+\\)";
	public static final String SMB_MOUNT_LIST_REG = "(\\/\\/.+)\\son\\s(\\/.+)+\\stype\\s(.+)\\s\\((.*?),username=(.+?),.*|.+,username=(.+?),.*\\)$";
	public static final String SMB_MNT_CFG_RECORD_REG = "\\/bin\\/mount\\s-t(.+)(\\//.+)\\s(\\/.+)\\s-o\\susername=(.+)";
	public static final String SMB_SCANDIR_BASE_PATH = "/tmpdisk/root/samba/";
	public static final String SMB_SCANDIR_BASE_PATH_REGEX = "^\\/tmpdisk\\/root";

	//******************************SMB 配置信息  end  *************************************************

	//******************************SNMP 配置信息  start  **********************************************
	public static String SNMP_TRAP_REG = "^trap2sink";
	public static String SNMP_GET_REG = "^com2sec";
	public static String SNMP_ROCOMMUNITY_REG = "^rocommunity";
	public static String SNMP_TRAP = "trap2sink";
	public static String SNMP_GET = "com2sec";
	public static String SNMP_ROCOMMUNITY = "rocommunity";
	public static String SNMP_PORT = "162";
	public static String SNMP_SNMPIP_BEGIN = "#[SNMPIP_BEGIN]";
	public static String SNMP_SNMPIP_END = "#[SNMPIP_END]";

	public static String SNMP_SNMPD = "/usr/local/netsnmp/sbin/snmpd";
	public static String SNMP_CONF_DIR = "/usr/local/netsnmp/share/snmp/jtsec.conf";

	//******************************SNMP 配置信息  end  ************************************************

	//******************************SocketAgent 配置信息  start  ************************************************
	//	global
	public static final String GLOBALNAME = "global";
	public static final String UDP_AUTHPORT = "udp_authport";
	public static final String TCP_COMMUPORT = "tcp_commuport";
	public static final String TCP_SPEED = "tcp_speed";
	public static final String TCP_TIMEVAL = "tcp_timeval";

	//service
	public static final String SYSTEM_SERVERNAME_SUFFIX = "_SYSTEM";
	public static final String TLP = "tlp";
	public static final String CASTMODE = "castmode";
	public static final String SERVICE_ID = "server_id";
	public static final String ADDRESS = "address";
	public static final String PORT = "port";
	public static final String LOCAL_ADDRESS = "local_address";
	public static final String LOCAL_PORT = "local_port";
	public static final String AUTHFLAG = "authflag";
	public static final String USER_NAME = "user_name";
	public static final String PASSWORD = "password";

	//******************************SocketAgent 配置信息  end  ************************************************



	//****************************** SQL关键字 ****************************************************************
	public final static String SQL_FLAG = "JTSEC_SQL_PARAM_";
	public final static String SQL_PARAM_NOT_LIKE = "JTSEC_SQL_PARAM_NOT_LIKE_";
	public final static String SQL_PARAM_NOT_UPPER = "JTSEC_SQL_PARAM_NOT_UPPER_";
	public final static String SQL_GE= "JTSEC_SQL_PARAM_GE_";		//>=
	public final static String SQL_LE = "JTSEC_SQL_PARAM_LE_";		//<=
	public final static String SQL_EQ = "JTSEC_SQL_PARAM_EQ_";		//=
	public final static String SQL_NEQ = "JTSEC_SQL_PARAM_NEQ_";	//!=
	public final static String SQL_JTSEC_IDS = "JTSEC_IDS";

	//****************************** SQL关键字****************************************************************


	//****************************** 服务升级包文件名称 *******************************************************
	public final static String PACKAGENAME_WEB = "web_ROOT.bin.se";
	public final static String PACKAGENAME_FTP = "ftp.bin.se";
	public final static String PACKAGENAME_SFTS = "sfts.bin.se";
	public final static String PACKAGENAME_SNMP = "snmp.bin.se";
	public final static String PACKAGENAME_SOCKETAGENT = "socketAgent.bin.se";

	//****************************** 服务升级包文件名称 *******************************************************


	//****************************** web-config.conf配置文件中 *******************************************************
	//****[global节点属性]
	public final static String WEB_CONFIG_GLOBAL = "global";
	public final static String WEB_CONFIG_GLOBAL_NORTH_DESC_MSG = "northDescMsg";
	public final static String WEB_CONFIG_GLOBAL_WELCOME_MSG = "welcomeMsg";
	// 密码超期后处理策略属性，在配置文件中该属性值为0时，表示仅提醒，如果值为1时，表示强制更换
	public final static String WEB_CONFIG_GLOBAL_PASSWORD_PERIOD = "password_period";

	//系统标志位
	public final static String WEB_CONFIG_GLOBAL_SERVER_FLAG = "server_flag";
	//是否开启ipv6支持
	public final static String WEB_CONFIG_GLOBAL_OPEN_IPv6 = "open_IPv6";

	//获取设备类型
	public final static String WEB_CONFIG_GLOBAL_DEV_TYPE = "dev_type";

	// 安全文件传输配置对比同步模式
	public final static String SFTS_CONF_COMPARE_SYNC_MODE = "sfts_conf_compare_sync_mode";

	// FTP文件传输配置对比同步模式
	public final static String FTP_CONF_COMPARE_SYNC_MODE = "ftp_conf_compare_sync_mode";
	// FTP文件传输配置对比同步模式
	public final static String SIP_CONF_COMPARE_SYNC_MODE = "sip_conf_compare_sync_mode";

	//不允许删除的管理员用户id
	public final static String WEB_CONFIG_GLOBAL_NOT_DELETE_USER_ID = "not_delete_userId";
	//允许管理口创建虚拟ip
	public final static String WEB_CONFIG_GLOBAL_CREATE_VIRTUAL_IP = "create_virtual_ip";
	//默认管理口网卡
	public final static String WEB_CONFIG_GLOBAL_MANAGER_PORT = "manage_port";
	//默认网关信息所在的配置文件
	public final static String WEB_CONFIG_GLOBAL_DEFAULT_GATEWAY = "default_gateway";
	//模拟http请求的协议、地址、端口及项目名称
	public final static String WEB_CONFIG_GLOBAL_AUTH_TYPE = "auth_type";
	//发送异步数据的方式0：http	1:https
	public final static String WEB_CONFIG_GLOBAL_SYNC_TYPE = "sycn_type";

	public final static String WEB_CONFIG_GLOBAL_SYNC_DEAL = "sync_deal";
	public final static String WEB_CONFIG_GLOBAL_SYNC_IP = "sync_ip";
	public final static String WEB_CONFIG_GLOBAL_SYNC_PORT = "sync_port";
	public final static String WEB_CONFIG_GLOBAL_SYNC_WEB_NAME = "sync_web_name";
	//模拟http中的数据的默认编码(UTF-8)
	public final static String WEB_CONFIG_GLOBAL_SYNC_CHARACTER = "sync_character";

	public final static String WEB_CONFIG_GLOBAL_SSO_DEAL = "sso_deal";
	public final static String WEB_CONFIG_GLOBAL_SSO_IP = "sso_ip";
	public final static String WEB_CONFIG_GLOBAL_SSO_PORT = "sso_port";
	public final static String WEB_CONFIG_GLOBAL_SSO_WEB_NAME = "sso_web_name";

	//配置文件导出操作中，不导出指定的配置信息
	public final static String WEB_CONFIG_GLOBAL_NOT_IEC = "not_iec";
	//配置文件导出操作中，指定要导出的配置信息
	public final static String WEB_CONFIG_GLOBAL_IEC = "iec";
	//验证连接请求的时间间隔(north.js中定时器间隔)
	public final static String WEB_CONFIG_GLOBAL_GAP_TIME = "gap_time";
	public final static String WEB_CONFIG_GLOBAL_PROC_LIST = "proc_list";

	//****[agent_user_inspect节点属性]
	public final static String WEB_CONFIG_AGENT_USER_INSPECT_SECNAME = "agent_user_inspect";
	//SessionMonitorThread中run中sleep方法需要的时间值
	public final static String WEB_CONFIG_AGENT_USER_INSPECT_SLEEP_TIME = "sleep_time";
	//session失效的时间间隔
	public final static String WEB_CONFIG_AGENT_USER_INSPECT_GAP_TIME = "gap_time";
	//普通视频用户登录时是否验证访问主机的ip和mac(认证方式是web认证方式时)
	public final static String WEB_CONFIG_AGENT_USER_INSPECT_VALIDATE_IP_MAC = "validate_ip_mac";

	//sfts服务中是否保存ausocket属性
	public final static String WEB_CONFIG_SAVE_AUSOCKET = "save_ausocket";
	//视频管理模块中视频厂商的排序
	public final static String WEB_CONFIG_AGENT_SPCSINFO_SORT = "spcsinfo_sort";

	//需要隐藏的网卡列表
	public final static String WEB_CONFIG_GLOBAL_HIDDEN_ETH_LIST = "hidden_eth_list";

	//****************************** web-config.conf配置文件中 *******************************************************


	//****************************** yttcp *************************************************************************
	public final static String YTTCP_SUPPORT_HTTP_PREFIX = "JTSEC_";
	public final static String YTTCP_SUPPORT_HTTP_SUFFIX = "_SUPPORT_HTTP";
	//User role extend
//	public final static Integer USERROLEEXTEND = 2;

	// 用户名口令登录认证模式
	public final static String LOGIN_AUTH_USER_PWD = "0";
	// 用户名口令+角色证书登录认证模式
	public final static String LOGIN_AUTH_ROLE_CERT = "1";
	// 用户名口令+个人用户证书登录认证模式
	public final static String LOGIN_AUTH_USER_CERT = "2";
	// 菜单角色授权模式
	public final static String MENU_AUTH_ROLE = "0";
	// 菜单用户+角色授权模式
	public final static String MENU_AUTH_USER_ROLE = "1";
	// 安全文件传输配置对比同步模式，0：不对比
	public final static String SFTS_COMPARE_NO = "0";
	// 安全文件传输配置对比同步模式，1：以配置文件为准模式（将配置文件中有，数据库中没有的写入到库中）
	public final static String SFTS_COMPARE_CONF_SHALL_PREVAIL = "1";
	// 安全文件传输配置对比同步模式，2：以数据库为准模式（将数据库中有，配置文件中没有的写入到库中）
	public final static String SFTS_COMPARE_DB_SHALL_PREVAIL = "2";
	// 安全文件传输配置对比同步模式，3：数据库与配置文件同时一致模式
	public final static String SFTS_COMPARE_BOTH = "3";
	// 安全文件传输配置对比同步模式，4：配置文件影子模式（配置文件中有，数据库中没有的，可在页面中显示，但不记录到数据库）
	public final static String SFTS_COMPARE_CONF_SHADOW = "4";

	// ftp文件传输配置对比同步模式，0：不对比
	public final static String FTP_COMPARE_NO = "0";
	// ftp文件传输配置对比同步模式，1：以配置文件为准模式（将配置文件中有，数据库中没有的写入到库中）
	public final static String FTP_COMPARE_CONF_SHALL_PREVAIL = "1";
	// ftp文件传输配置对比同步模式，2：以数据库为准模式（将数据库中有，配置文件中没有的写入到库中）
	public final static String FTP_COMPARE_DB_SHALL_PREVAIL = "2";
	// ftp文件传输配置对比同步模式，3：数据库与配置文件同时一致模式
	public final static String FTP_COMPARE_BOTH = "3";
	// ftp文件传输配置对比同步模式，4：配置文件影子模式（配置文件中有，数据库中没有的，可在页面中显示，但不记录到数据库）
	public final static String FTP_COMPARE_CONF_SHADOW = "4";

	// sip文件传输配置对比同步模式，0：不对比
	public final static String SIP_COMPARE_NO = "0";
	// sip文件传输配置对比同步模式，1：以配置文件为准模式（将配置文件中有，数据库中没有的写入到库中）
	public final static String SIP_COMPARE_CONF_SHALL_PREVAIL = "1";
	// sip文件传输配置对比同步模式，2：以数据库为准模式（将数据库中有，配置文件中没有的写入到库中）
	public final static String SIP_COMPARE_DB_SHALL_PREVAIL = "2";

	public final static String JTSEC_OW_IGAP_IN_CODE = "000100001";
	public final static String JTSEC_OW_IGAP_OUT_CODE = "000100002";
	public final static String JTSEC_TW_IGAP_IN_CODE = "000200001";
	public final static String JTSEC_TW_IGAP_OUT_CODE = "000200002";
	public final static String JTSEC_FRONT_SERVER_CODE = "000300001";
	public final static String JTSEC_BACK_SERVER_CODE = "000300002";
	public final static String JTSEC_JRRZ_SERVER_CODE = "000400001";
	public final static String JTSEC_YHRZ_SERVER_CODE = "000400002";
	public final static String JTSEC_SPYH_SERVER_CODE = "000400003";
	public final static String JTSEC_JK_SERVER_CODE = "000500001";
	public final static String JTSEC_TZ_SERVER_CODE = "000500002";




}
