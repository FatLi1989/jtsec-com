package com.jtsec.common.Constants;
/**
 * 
 * @author properties文件中的key属相
 *
 */
public class KeyCons {
	
	//**************************************common**************************
	//windows下测试文件样本路径
	public static final String TEST_CONF_PATH = "test_conf_path";
	//数据库连接属性
	public static final String DB_USERNAME = "conn_username";
	public static final String DB_PASSWORD = "conn_password";
	public static final String DB_DRIVER = "jdbc_driver";
	public static final String DB_URL = "conn_url";
//	//数据库名称
	public static final String DATABASE_NAME = "conn_database";
	
	//rsyslog配置文件
	public static final String RSYSLOG_FILE_PATH = "rsyslog";
	//发送rsyslog日志的编码方式
	public static final String RSYSLOG_HOST = "rsyslog_host";
	public static final String FIREWALL_CONFIG_PATH = "firewall-template";
	public static final String IFCFG_ETH_PATH = "ifcfg-eth";
	//初始化规则文件配置路径:ipv4防火墙脚本
	public static final String INIT_SHELL_FILE_PATH = "init_shell";
	//ipv6防火墙脚本
	public static final String IP6TABLES_FILE_PATH = "ip6tables";
	
	public static final String IFCONFIG_FILE_PATH = "ifconfig";
	public static final String LS_CMD = "ls_cmd";
	public static final String SP_ROUTE_N = "route-n";
	//新加查看ipv6静态路由
	public static final String ROUTEINET6 = "route-inet6";
	//默认管理口
	public static final String MANAGERPORTNAME = "manager_port_name";	//eth0或eth1 ...
	//webLog日志路径catalina.out
	public static final String CATALINA_PATH = "catalina";
	//静态路由文件地址ip_forwar
	public static final String IP_FORWARD_PATH = "ip_forward";
	
	
	//******************************************** 单向 ********************************
	//jtsec配置文件路径
	public static final String JTSEC_FILE_PATH = "jtsec";
	//套接字代理配置文件路径
	public static final String SP_FILE_PATH = "sp";
	//smb配置信息的文件路径
	public static final String SMB_FILE_PATH = "smb";
	//安全文件传输配置文件的路径
	public static final String SFTS_FILE_PATH = "sfts";
	//安全文件传输病毒过滤配置文件
	public static final String AUEXEC_FILE_PATH = "auexec";
	//安全文件传输关键字过滤
	public static final String KEY_FILE_PATH = "key";
	//执行iptables -nL --line-number 命令结果的测试文件
	public static final String IPV4_SOURCE_IP = "ipv4SourceIp";
	//执行ip6tables -nL --line-number 命令结果的测试文件
	public static final String IPV6_SOURCE_IP = "ipv6SourceIp";
	//FTP配置信息的文件路径
	public static final String FTP_FILE_PATH = "ftp";
	//FTP 外端同步过来的配置文件-------内端机端  同步的外端机的配置文件的文件路径
	public static final String FTP_OUT_FILE_PATH = "ftp_out";
	
	//sfts 外端同步过来的配置文件-------内端机端  同步的外端机的配置文件的文件路径
    public static final String SFTS_OUT_FILE_PATH = "sfts_out";
	
	//ftp文件格式过滤的类型
	public static final String FILE_FILTER_STRATEGY = "file_filter_strategy";
	//telnet连接的ip和端口
	public static final String HOSTNAME = "hostname";
	public static final String FTPD_O_PORT = "ftpd_o_port";
	public static final String SSMD_O_PORT = "ssmd_o_port";
	//发送telnet命令后等待时间
	public static final String COMMAND_DEPLAY = "command_deplay";

	
	//*********************************************** 视频 ******************************************
	public static final String SP_SIPHOLE_BACK_FILE_PATH = "siphole_back";
	public static final String SP_SIPHOLE_END_FILE_PAHT = "siphole_end";
	public static final String SP_SIPHOLE_BACK_TC = "siphole_back_tc";
	//视频程序注册
	public static final String SP_SIPHOLE_BACK_REG = "siphole_back_reg";
	public static final String SP_SIPHOLE_END_REG = "siphole_end_reg";
	
	// 登录认证模式，0用户名口令认证，1用户名口令+角色证书认证，2用户名口令+用户证书认证
	public static final String LOGIN_AUTH_MODE = "login_auth_mode";
	// 菜单授权模式，0角色授权模式，1用户+角色授权模式
	public static final String MENU_AUTH_MODE = "menu_auth_mode";
	public static final String CERT_CER_PASSWORD = "cert_cer_password";
	public static final String CERT_KEYSTORE_PASSWORD = "cert_keystore_password";
	public static final String CERT_CER_PATH = "cert_cer_path";
	public static final String CERT_KEYSTORE_PATH = "cert_keystore_path";
	
	public static final String CERT_TRUSTSTORE_PATH = "cert_truststore_path";
	public static final String CERT_TRUSTSTORE_PASSWORD = "cert_truststore_password";
	
	//*********************************************hyx http yttcp************************************
	public static final String HYX_HTTP_CONF_PATH = "http";
	public static final String HYX_HTTPS_CONF_PATH = "https";
	public static final String HYX_YTTCP_CONF_PATH = "yttcp";
	//********************************************TwHotStandbySubnet*********************************
	public static final String TW_HOT_STANDBY_SUBNET_FILE_PATH = "tw_igap_hotstd_network";
	//****废弃
	public static final String TEST_SP_CONF_PATH = "test_sp_conf_path";
	public static final String TEST_HYX_CONF_PATH = "test_hyx_conf_path";
	public static final String TEST_DX_CONF_PATH = "test_dx_conf_path";
	public static final String NETWORK_PATH = "network";
	
	//安全文件传输服务端
	public static final String GSFTC_CONF_PATH = "gsftc";
	public static final String GSFTC_RSYSLOG_PATH = "rsyslog";
	public static final String GSFTC_SFTC_PATH = "sftc";
	
	//双机心跳配置文件
	public static final String TW_IGAP_HOTSTD_HELLO_FILE_PATH = "tw_igap_hotstd_hello";
	//双机模式配置文件
	public static final String TW_IGAP_HOTSTD_GAP_STATUS_FILE_PATH = "tw_igap_hotstd_gap_status";
	
	//新双向网闸-负载均衡配置
	public static final String TW_IGAP_KEEPALIVED_FILE_PATH = "tw_igap_keepalived";
	//新双向网闸-负载均衡开关配置
	public static final String TW_IGAP_KEEPALIVED_ONOFF_FILE_PATH = "tw_igap_keepalived_onoff";
	//新双向网闸-负载均衡开关运行脚本
	public static final String TW_IGAP_KEEPALIVED_RUN_FILE_PATH = "tw_igap_keepalived_run";
	//设备配置信息
	public static final String DEVICE_CONFIG_INFO_DEVSN_FILE_PATH = "device_config_info_devSn";
	
}
