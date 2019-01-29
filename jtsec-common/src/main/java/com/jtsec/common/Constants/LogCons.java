package com.jtsec.common.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogCons {
    //单向光闸内端机
    public static final String DEV_TYPE_CODE_OW_IN = "000100001";
    //单向光闸外端机
    public static final String DEV_TYPE_CODE_OW_OUT = "000100002";
    
    //双向网闸内端机
    public static final String DEV_TYPE_CODE_TW_IN = "000200001";
    //双向网闸外端机
    public static final String DEV_TYPE_CODE_TW_OUT = "000200002";
    
    //双向网闸V2内端机
    public static final String DEV_TYPE_CODE_TW_IN_V2 = "000200003";
    //双向网闸V2外端机
    public static final String DEV_TYPE_CODE_TW_OUT_V2 = "000200004";
    
    //数据前置
    public static final String DEV_TYPE_CODE_QZ = "000300001";
    //数据后置
    public static final String DEV_TYPE_CODE_HZ = "000300002";
    
    //接入认证服务器
    public static final String DEV_TYPE_CODE_QZ_JRRZ = "000400001";
    //用户认证服务器
    public static final String DEV_TYPE_CODE_HZ_YHRZ = "000400002";
    //视频用户服务器
    public static final String DEV_TYPE_CODE_HZ_SPYH = "000400003";
    
    //集控
    public static final String DEV_TYPE_CODE_JK = "000500001";
    //探针
    public static final String DEV_TYPE_CODE_TZ = "000500002";
    
    //防火墙
    public static final String DEV_TYPE_CODE_FW = "000900001";
    
    //入侵防御ips
    public static final String DEV_TYPE_CODE_IPS = "001000001";
    
    // 事件类型字段名为event
    public static final String LOG_WARNING = "log_warning";  // 存储告警(公有表)
    // 事件类型字段名为action
    public static final String LOG_HTTP_MSG = "log_http_msg";  //管理日志归类表

    // 以下表，无事件类型字段，含有类型标识字段flag
    public static final String LOG_SNORT_MSG = "log_snort_msg"; //snort检测日志归类表
    public static final String LOG_SFTS_MSG = "log_sfts_msg";   //安全文件传输日志归类表
    public static final String LOG_FTP_MSG = "log_ftp_msg";     //ftp文件传输日志归类表
    public static final String LOG_VIDEO_MSG = "log_video_msg"; //视频代理日志归类表
    public static final String LOG_GSTMEDIA_MSG = "log_gstmedia_msg"; 		//单向视频代理日志归类表
    public static final String LOG_LBPROXY_MSG = "log_lbproxy_msg";     	//协议视频代理日志归类表
    public static final String LOG_HTTPPROXY_MSG = "log_httpproxy_msg";    	//HTTP代理日志归类表
    public static final String LOG_FW_MSG = "log_fw_msg";       //防火墙日志归类表
    public static final String LOG_IPS_MSG = "log_ips_msg";     //IPS日志归类表
    public static final String LOG_GAP_MSG = "log_gap_msg";
    
    
    // 以下表，表结构对应标准SYSLOG日志存储形式，时间字段名为ReceivedAt，事件类型字段名为EventLogType
    public static final String LOG_VIDEO = "log_video";			//视频代理日志
    public static final String LOG_GSTMEDIA = "log_gstmedia"; 	//单向视频代理日志
    public static final String LOG_LBPROXY = "log_lbproxy"; 	//协议代理日志
    public static final String LOG_HTTPPROXY = "log_httpproxy";	//HTTP代理日志
    public static final String FW_LOG = "fw_log";       	//防火墙日志
    public static final String IPS_LOG = "ips_log";     	//IPS日志
    public static final String OTHER_LOG = "other_log";		//其它类型日志表
    public static final String SFTS_LOG = "sfts_log";   	//安全文件传输日志
    public static final String FTP_LOG = "ftp_log";     	//FTP文件传输日志
    public static final String HTTP_LOG = "http_log";      	//管理员日志
    public static final String FLOOD_ATTACK_LOG = "flood_attack_log";  	//泛洪攻击日志
    public static final String ICMP_FLOOD_LOG = "icmp_flood_log";      	//ICMP攻击日志
    public static final String SYN_FLOOD_LOG = "syn_flood_log";      	//SYNC攻击日志
    public static final String UDP_FLOOD_LOG = "udp_flood_log";      	//UDP攻击日志

    
    // 以下表，表结构对应标准业务及服务日志的存储形式，时间字段名为time/eve_time，事件类型字段名为eve_action
    public static final String TW_OPER_LOG = "tw_oper_log";     //审计日志
    public static final String TW_AUDIT_LOG = "tw_audit_log";   //操作日志
    public static final String TW_OS_LOG = "tw_os_log";         //系统日志
    public static final String TW_SERV_LOG = "tw_serv_log";     //服务日志
    public static final String TW_FILTER_LOG = "tw_filter_log"; //非法过滤日志

    public static final String HTTP_PROXY_LOG = "http_proxy_log";   //HTTP服务日志
    public static final String HTTPS_PROXY_LOG = "https_proxy_log";    //HTTPS服务日志
    public static final String SMTP_PROXY_LOG = "smtp_proxy_log";     //SMTP服务日志
    public static final String POP_PROXY_LOG = "pop_proxy_log"; //POP服务日志
    public static final String FTP_PROXY_LOG = "ftp_proxy_log"; //FTP服务日志

    // 以下表，表结构对应标准业务及服务日志的存储形式，时间字段名为time/eve_time，事件类型字段名为eve_action
    public static final String LOG_OS = "log_os";         //系统管理操作日志
    public static final String OPER_LOG = "oper_log";     //审计日志
    public static final String AUDIT_LOG = "audit_log";   //操作日志
    public static final String OS_LOG = "os_log";         //操作系统日志表(公有表)
    public static final String SERV_LOG = "serv_log";     //服务日志    
    public static final String FILTER_LOG = "filter_log"; //非法过滤日志
    
    
    

    // 表结构是标准SYSLOG日志存储形式的表，时间字段名为ReceivedAt，事件类型字段名为EventLogType
    public static List<String> stdSyslogTabList = new ArrayList<String>();
    // 表结构是标准业务、服务日志存储形式的表，时间字段名为eve_time，事件类型字段名为eve_type的日志表
    public static List<String> busiSyslogTabList = new ArrayList<String>();
    //jtesc_com库中的日志表
    public static List<String> initJtsecComList = new ArrayList<String>();
    //jtesc_log库中的日志表:目前只添加新双向网闸在log库中的表
    public static List<String> initJtsecLogList = new ArrayList<String>();
    
    //双向网闸的所有日志集合
    public static List<String> twLogTpyeListCache = new ArrayList<String>();
    //单向光闸的所有日志集合
    public static List<String> owLogTpyeListCache = new ArrayList<String>();
    //集控的所有日志集合
    public static List<String> jkLogTpyeListCache = new ArrayList<String>();
    //探针的所有日志集合
    public static List<String> tzLogTpyeListCache = new ArrayList<String>();
    //视频交换系统的所有日志集合
    public static List<String> spLogTypeListCache = new ArrayList<String>();
    //数据交换系统的所有日志集合
    public static List<String> qzLogTypeLishCache = new ArrayList<String>();
    //防火墙的所有日志集合
    public static List<String> fwLogTypeListCache = new ArrayList<String>();
    //ips的所有日志集合
    public static List<String> ipsLogTypeListCache = new ArrayList<String>();
    
    //新双向网闸v2的所有日志集合
    public static List<String> twV2LogTpyeListCache = new ArrayList<String>();
    
    public static Map<String, List<String>> devTypeLogClassMapCache = new HashMap<String, List<String>>();
    
    static{
        LogCons.initDevTypeLogClassMap();
        LogCons.initStandardSyslogTabList();
        LogCons.initBusiSyslogTabList();
        LogCons.initJtsecComTabList();
        LogCons.initJtsecLogTabList();
    }
    
    public static void initDevTypeLogClassMap(){
        twLogTpyeListCache.add(LOG_WARNING);
        // 标准业务、服务存储表
        twLogTpyeListCache.add(LOG_OS);
        twLogTpyeListCache.add(TW_OPER_LOG);
        twLogTpyeListCache.add(TW_AUDIT_LOG);
        twLogTpyeListCache.add(TW_OS_LOG);
        twLogTpyeListCache.add(TW_SERV_LOG);
        twLogTpyeListCache.add(TW_FILTER_LOG);
        twLogTpyeListCache.add(OPER_LOG);     	//审计日志
        twLogTpyeListCache.add(AUDIT_LOG);   	//操作日志
        twLogTpyeListCache.add(OS_LOG);         //系统日志
        twLogTpyeListCache.add(SERV_LOG);     	//服务日志    
        twLogTpyeListCache.add(FILTER_LOG); 	//非法过滤日志     
        // 标准SYSLOG格式存储表
        twLogTpyeListCache.add(HTTP_LOG);
    	twLogTpyeListCache.add(SFTS_LOG);
        twLogTpyeListCache.add(ICMP_FLOOD_LOG);
        twLogTpyeListCache.add(SYN_FLOOD_LOG);   
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_TW_IN, twLogTpyeListCache);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_TW_OUT, twLogTpyeListCache);
        
        owLogTpyeListCache.add(LOG_WARNING);
        owLogTpyeListCache.add(LOG_OS);
        owLogTpyeListCache.add(ICMP_FLOOD_LOG);
        owLogTpyeListCache.add(SYN_FLOOD_LOG);
        owLogTpyeListCache.add(HTTP_LOG);
        owLogTpyeListCache.add(SFTS_LOG);
        owLogTpyeListCache.add(FTP_LOG);
        owLogTpyeListCache.add(LOG_HTTP_MSG);
        owLogTpyeListCache.add(LOG_SFTS_MSG);
        owLogTpyeListCache.add(LOG_FTP_MSG);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_OW_IN, owLogTpyeListCache);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_OW_OUT, owLogTpyeListCache);
        
        qzLogTypeLishCache.add(LOG_WARNING);
        qzLogTypeLishCache.add(LOG_OS);
        qzLogTypeLishCache.add(ICMP_FLOOD_LOG);
        qzLogTypeLishCache.add(SYN_FLOOD_LOG);
        qzLogTypeLishCache.add(HTTP_LOG);
        qzLogTypeLishCache.add(SFTS_LOG);
        qzLogTypeLishCache.add(LOG_HTTP_MSG);
        qzLogTypeLishCache.add(LOG_SFTS_MSG);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_QZ, qzLogTypeLishCache);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_HZ, qzLogTypeLishCache);
        
        spLogTypeListCache.add(LOG_WARNING);
        spLogTypeListCache.add(LOG_OS);
        spLogTypeListCache.add(LOG_HTTP_MSG);
        spLogTypeListCache.add(LOG_VIDEO_MSG);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_HZ_SPYH, spLogTypeListCache);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_HZ_YHRZ, spLogTypeListCache);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_QZ_JRRZ, spLogTypeListCache);
            
        fwLogTypeListCache.add(LOG_WARNING);
        fwLogTypeListCache.add(LOG_OS);
        fwLogTypeListCache.add(LOG_FW_MSG);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_FW, fwLogTypeListCache);
        
        ipsLogTypeListCache.add(LOG_WARNING);
        ipsLogTypeListCache.add(LOG_OS);
        ipsLogTypeListCache.add(LOG_IPS_MSG);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_IPS, ipsLogTypeListCache);
        
        jkLogTpyeListCache.add(LOG_OS);
        jkLogTpyeListCache.add(ICMP_FLOOD_LOG);
        jkLogTpyeListCache.add(SYN_FLOOD_LOG);
        jkLogTpyeListCache.add(LOG_HTTP_MSG);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_JK, jkLogTpyeListCache);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_TZ, jkLogTpyeListCache);
        
        twV2LogTpyeListCache.add(LOG_WARNING);
        twV2LogTpyeListCache.add(LOG_HTTP_MSG);
        twV2LogTpyeListCache.add(ICMP_FLOOD_LOG);
        twV2LogTpyeListCache.add(SYN_FLOOD_LOG);
        twV2LogTpyeListCache.add(LOG_OS);
        twV2LogTpyeListCache.add(AUDIT_LOG);
        twV2LogTpyeListCache.add(OPER_LOG);
        twV2LogTpyeListCache.add(OS_LOG);
        twV2LogTpyeListCache.add(FILTER_LOG);
        twV2LogTpyeListCache.add(HTTP_PROXY_LOG);
        twV2LogTpyeListCache.add(HTTPS_PROXY_LOG);
        twV2LogTpyeListCache.add(SMTP_PROXY_LOG);
        twV2LogTpyeListCache.add(POP_PROXY_LOG);
        twV2LogTpyeListCache.add(FTP_PROXY_LOG);
        twV2LogTpyeListCache.add(UDP_FLOOD_LOG);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_TW_IN_V2, twV2LogTpyeListCache);
        devTypeLogClassMapCache.put(DEV_TYPE_CODE_TW_OUT_V2, twV2LogTpyeListCache);

    }
      
    public static void initStandardSyslogTabList(){
    	stdSyslogTabList.add(LOG_VIDEO); 	//视频代理日志 
    	stdSyslogTabList.add(LOG_GSTMEDIA);  //单向视频代理日志
    	stdSyslogTabList.add(LOG_HTTPPROXY);  //HTTP代理日志
    	stdSyslogTabList.add(FW_LOG);  //防火墙日志
    	stdSyslogTabList.add(IPS_LOG);  //IPS日志
    	stdSyslogTabList.add(OTHER_LOG);  //其它类型日志表
    	stdSyslogTabList.add(SFTS_LOG);  //安全文件传输日志
    	stdSyslogTabList.add(FTP_LOG);  //FTP文件传输日志
    	stdSyslogTabList.add(HTTP_LOG);  //管理员日志
    	stdSyslogTabList.add(ICMP_FLOOD_LOG);  //ICMP攻击日志
    	stdSyslogTabList.add(SYN_FLOOD_LOG);  //SYNC攻击日志
    	stdSyslogTabList.add(UDP_FLOOD_LOG);  //SYNC攻击日志
    }

    public static void initBusiSyslogTabList(){
    	// 时间字段均为time或eve_time
    	busiSyslogTabList.add(LOG_OS); // 系统管理操作日志
    	busiSyslogTabList.add(OS_LOG);	// 操作系统日志
    	busiSyslogTabList.add(OPER_LOG);
    	busiSyslogTabList.add(AUDIT_LOG);
    	busiSyslogTabList.add(SERV_LOG);
    	busiSyslogTabList.add(FILTER_LOG);
    	busiSyslogTabList.add(HTTP_PROXY_LOG);
    	busiSyslogTabList.add(HTTPS_PROXY_LOG);
    	busiSyslogTabList.add(SMTP_PROXY_LOG);
    	busiSyslogTabList.add(POP_PROXY_LOG);
    	busiSyslogTabList.add(FTP_PROXY_LOG);
    	busiSyslogTabList.add(TW_OPER_LOG);
    	busiSyslogTabList.add(TW_AUDIT_LOG);
    	busiSyslogTabList.add(TW_OS_LOG);
    	busiSyslogTabList.add(TW_SERV_LOG);
    	busiSyslogTabList.add(TW_FILTER_LOG);

    }
    
    public static void initJtsecComTabList() {
        initJtsecComList.add(LOG_FTP_MSG);  //ftp文件传输日志归类表
        initJtsecComList.add(LOG_FW_MSG);  //防火墙日志归类表
        initJtsecComList.add(LOG_GAP_MSG); 
        initJtsecComList.add(LOG_GSTMEDIA_MSG);  //单向视频代理日志归类表
        initJtsecComList.add(LOG_HTTP_MSG);  //管理日志归类表
        initJtsecComList.add(LOG_HTTPPROXY_MSG);  //HTTP代理日志归类表
        initJtsecComList.add(LOG_IPS_MSG);  //管理员日志IPS日志归类表
        initJtsecComList.add(LOG_LBPROXY_MSG);  //协议视频代理日志归类表
        initJtsecComList.add(LOG_OS);  //系统管理操作日志
        initJtsecComList.add(LOG_SFTS_MSG);  //安全文件传输日志归类表
        initJtsecComList.add(LOG_SNORT_MSG);  //snort检测日志归类表管理员日志
        initJtsecComList.add(LOG_VIDEO_MSG);  //视频代理日志归类表
        initJtsecComList.add(LOG_WARNING);  //存储告警(公有表)
        
        initJtsecComList.add(TW_OPER_LOG);  //审计日志
        initJtsecComList.add(TW_AUDIT_LOG);  //操作日志
        initJtsecComList.add(TW_OS_LOG);  //系统日志
        initJtsecComList.add(TW_SERV_LOG);  //服务日志
        initJtsecComList.add(TW_FILTER_LOG);  ////非法过滤日志
    }
    
    public static void initJtsecLogTabList() {
    	// 时间字段均为time或eve_time
        initJtsecLogList.add(LOG_OS);
        initJtsecLogList.add(AUDIT_LOG);
        initJtsecLogList.add(OPER_LOG);
        initJtsecLogList.add(OS_LOG);
        initJtsecLogList.add(FILTER_LOG);
        initJtsecLogList.add(HTTP_PROXY_LOG);
        initJtsecLogList.add(HTTPS_PROXY_LOG);
        initJtsecLogList.add(SMTP_PROXY_LOG);
        initJtsecLogList.add(POP_PROXY_LOG);
        initJtsecLogList.add(FTP_PROXY_LOG);
    }
    

}
