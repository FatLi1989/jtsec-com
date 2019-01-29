package com.jtsec.common.util.webconfig;

import java.io.Serializable;

public class WebConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//服务标志位
	private String server_flag = "0";
	//不允许删除的用户id 默认系统的3个管理员
	private String not_delete_userId = "1,2,3";
	//是否允许在管理口网卡上创建虚拟ip
	private String create_virtual_ip = "1";
	//管理口网卡
	private String manager_port = "eth0";
	//发送验证session存活状态的间隔时间
	private String gap_time = "45000";
	//进程列表
	private String proc_list = "";
	//应用配置
	//sfts中是否保存ausoket属性值
	private String save_ausocket = "1";
	//视频厂商排序
	private String spcsinfo_sort;
	//SSO-Sync
	private String auth_type = "0";
	//系统间数据交互方式 0： http 1:https
	private String sycn_type = "1";
	private String sync_deal = "http://";
	private String sync_ip = "";
	private String sync_port = "8080";
	private String sync_web_name = "she_com";
	private String sync_character = "UTF-8";
	private String sso_deal = "http://";
	private String sso_ip = "";
	private String sso_port = "8080";
	private String sso_web_name = "jtsec_ims";
	/**
	 * 导出配置文件操作中，不导出指定的配置信息
	 * -1：
	 * 1：FtpConfigImportAndExportServiceImpl
	 * 2：SftsConfigImportAndExportServiceImpl
	 * 3：SocketAgentImportAndExportServiceImpl
	 * 4：NetConfigSerivceImpl
	 * 5：LogConfigServiceImpl
	 */
	private String not_iec = "";
	private String iec = "";//需要导出的配置
	private String northDescMsg = "";
	private String welcomeMsg = "";
	//0：右下角提示更换密码 1:强制密码修改
	private String password_period = "0";
	//要隐藏的菜单id
	private String hide_menuId_list;
	//普通视频用户登录时时间任务中，线程sleep时间
	private String sleep_time = "120000";
	//普通视频用户登录是否验证客户端的ip或是mac
	private String validate_ip_mac = "1";
	//需要隐藏的网卡列表
	private String hidden_eth_list = "";
	
	public String getHidden_eth_list() {
		return hidden_eth_list;
	}
	public void setHidden_eth_list(String hidden_eth_list) {
		this.hidden_eth_list = hidden_eth_list;
	}
	public String getIec() {
		return iec;
	}
	public void setIec(String iec) {
		this.iec = iec;
	}
	public String getServer_flag() {
		return server_flag;
	}
	public void setServer_flag(String server_flag) {
		this.server_flag = server_flag;
	}
	public String getNot_delete_userId() {
		return not_delete_userId;
	}
	public void setNot_delete_userId(String not_delete_userId) {
		this.not_delete_userId = not_delete_userId;
	}

	public String getSycn_type() {
		return sycn_type;
	}
	public void setSycn_type(String sycn_type) {
		this.sycn_type = sycn_type;
	}
	public String getSync_deal() {
		return sync_deal;
	}
	public void setSync_deal(String sync_deal) {
		this.sync_deal = sync_deal;
	}
	public String getSync_ip() {
		return sync_ip;
	}
	public void setSync_ip(String sync_ip) {
		this.sync_ip = sync_ip;
	}
	public String getSync_port() {
		return sync_port;
	}
	public void setSync_port(String sync_port) {
		this.sync_port = sync_port;
	}
	public String getSync_web_name() {
		return sync_web_name;
	}
	public void setSync_web_name(String sync_web_name) {
		this.sync_web_name = sync_web_name;
	}
	public String getSync_character() {
		return sync_character;
	}
	public void setSync_character(String sync_character) {
		this.sync_character = sync_character;
	}
	public String getManager_port() {
		return manager_port;
	}
	public void setManager_port(String manager_port) {
		this.manager_port = manager_port;
	}
	public String getGap_time() {
		return gap_time;
	}
	public void setGap_time(String gap_time) {
		this.gap_time = gap_time;
	}
	public String getNorthDescMsg() {
		return northDescMsg;
	}
	public void setNorthDescMsg(String northDescMsg) {
		this.northDescMsg = northDescMsg;
	}
	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	public void setWelcomeMsg(String welcomeMsg) {
		this.welcomeMsg = welcomeMsg;
	}
	public String getPassword_period() {
		return password_period;
	}
	public void setPassword_period(String password_period) {
		this.password_period = password_period;
	}
	public String getHide_menuId_list() {
		return hide_menuId_list;
	}
	public void setHide_menuId_list(String hide_menuId_list) {
		this.hide_menuId_list = hide_menuId_list;
	}
	public String getSleep_time() {
		return sleep_time;
	}
	public void setSleep_time(String sleep_time) {
		this.sleep_time = sleep_time;
	}
	public String getValidate_ip_mac() {
		return validate_ip_mac;
	}
	public void setValidate_ip_mac(String validate_ip_mac) {
		this.validate_ip_mac = validate_ip_mac;
	}
	public String getCreate_virtual_ip() {
		return create_virtual_ip;
	}
	public void setCreate_virtual_ip(String create_virtual_ip) {
		this.create_virtual_ip = create_virtual_ip;
	}
	public String getNot_iec() {
		return not_iec;
	}
	public void setNot_iec(String not_iec) {
		this.not_iec = not_iec;
	}
	public String getSave_ausocket() {
		return save_ausocket;
	}
	public void setSave_ausocket(String save_ausocket) {
		this.save_ausocket = save_ausocket;
	}
	public String getSpcsinfo_sort() {
		return spcsinfo_sort;
	}
	public void setSpcsinfo_sort(String spcsinfo_sort) {
		this.spcsinfo_sort = spcsinfo_sort;
	}
	public String getProc_list() {
		return proc_list;
	}
	public void setProc_list(String proc_list) {
		this.proc_list = proc_list;
	}
	public String getSso_deal() {
		return sso_deal;
	}
	public void setSso_deal(String sso_deal) {
		this.sso_deal = sso_deal;
	}
	public String getSso_ip() {
		return sso_ip;
	}
	public void setSso_ip(String sso_ip) {
		this.sso_ip = sso_ip;
	}
	public String getSso_port() {
		return sso_port;
	}
	public void setSso_port(String sso_port) {
		this.sso_port = sso_port;
	}
	public String getSso_web_name() {
		return sso_web_name;
	}
	public void setSso_web_name(String sso_web_name) {
		this.sso_web_name = sso_web_name;
	}
	public String getAuth_type() {
		return auth_type;
	}
	public void setAuth_type(String auth_type) {
		this.auth_type = auth_type;
	}
}
