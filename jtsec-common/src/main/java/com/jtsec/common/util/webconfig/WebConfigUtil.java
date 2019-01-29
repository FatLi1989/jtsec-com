package com.jtsec.common.util.webconfig;

import com.jtsec.common.Constants.MoitorConstants;
import com.jtsec.common.Constants.SystemCons;
import com.jtsec.common.util.File.FileUtil;
import com.jtsec.common.util.security.DescMsgUtil;
import com.jtsec.common.util.surpass.FileContentUtil;
import com.jtsec.common.util.surpass.JtsecLinuxUtil;
import com.nikhaldimann.inieditor.IniEditor;
import lombok.extern.slf4j.Slf4j;
import java.io.File;

/**
 * 
 * <p>将一些可能会变属性放到web依赖的配置文件中，linux环境下webconfig.conf在项目的同级目录的conf目录下，开发环境在
 * uploads文件夹下
 * @author surpassE
 * @version v1.0.0
 */
@Slf4j
public class WebConfigUtil {
	/**
	 * Logger for this class
	 */
	
	public static IniEditor webConfigIe = null;

	//	IniEditor webConfigIe = null;

	/**
	 * 加载web依赖的配置文件
	 * @return
	 */
	protected static IniEditor init() throws Exception {
		if(webConfigIe != null){
			return webConfigIe;
		}
		webConfigIe = new IniEditor(true);
		webConfigIe.load(getWebConfFilePath());
		return webConfigIe;
	}

	protected static void saveWebConf(IniEditor webConfigIe) throws Exception{
		webConfigIe.save(getWebConfFilePath());
		webConfigIe = null;
		JtsecLinuxUtil.saveEtc();
	}

	private static File getWebConfFilePath(){
		String filePath = MoitorConstants.WEB_PATH + "shell";
		if(SystemCons.OS_IS_LINUX){
			filePath = new File(MoitorConstants.WEB_PATH).getParent() + "/conf";
		}
		File file = new File(FileUtil.getFilePath ("web-config.conf"));
		log.info(file.getAbsolutePath());
		return file;
	}
	
	/**
	 * 备份全局配置文件
	 */
	public static void bakWebConfig(){
		String filePath = MoitorConstants.WEB_PATH + "shell";
		if(SystemCons.OS_IS_LINUX){
			filePath = new File(MoitorConstants.WEB_PATH).getParent() + "/conf";
		}
		File file = new File(FileUtil.getFilePath ("web-config.conf"));
		//备份文件以便用于回复默认值
		File destFile = new File(file.getParent (), "web-config.conf-bak");
		try {
			FileContentUtil.copyFile(file.getAbsolutePath(), destFile.getAbsolutePath());
			JtsecLinuxUtil.saveEtc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 获得web系统配置文件中的属性字段值
	 * @param params	参数数组，如果一个参数表示是key,如果两个参数表示那个节点的key
	 * @return
	 */
	public static String getWebConfigValue(String... params) {
		IniEditor webConfigIe = null;
		String value = "";
		try {
			webConfigIe = init();
			value = getWebConfigValue(webConfigIe, params);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(webConfigIe != null){
//					saveWebConf(webConfigIe);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * 获得web系统配置文件中的属性字段值
	 * 根据服务端标识不同，判断工程部署在哪种服务器环境下，获取对应环境下相应的配置信息
	 * 
	 * @param webConfigIe
	 * @param params	参数数组，如果一个参数表示是key,如果两个参数表示那个节点的key
	 * @return
	 */
	protected static String getWebConfigValue(IniEditor webConfigIe, String... params) {
		String value = "";
		if(params.length == 1){
			// 单导内端机/数据后置
			if(MoitorConstants.JTSEC_OW_IGAP_IN_CODE.equals(SystemCons.dev_type)
				|| MoitorConstants.JTSEC_BACK_SERVER_CODE.equals(SystemCons.dev_type)
			){
				value = webConfigIe.get("jtsec_in", params[0]);
			// 单导外端机/数据前置
			}else if(MoitorConstants.JTSEC_OW_IGAP_OUT_CODE.equals(SystemCons.dev_type)
				|| MoitorConstants.JTSEC_FRONT_SERVER_CODE.equals(SystemCons.dev_type)
			){
				value = webConfigIe.get("jtsec_out", params[0]);
			// 视频前置（end），设备认证服务器
			}else if("2".equals(SystemCons.SERVICE_FLAG)){
				value = webConfigIe.get("device_inspect", params[0]);
			// 视频后置（back），用户认证服务器
			}else if("3".equals(SystemCons.SERVICE_FLAG)){
				value = webConfigIe.get("user_inspect", params[0]);
			// 视频后置，视频普通用户认证服务器
			}else if("4".equals(SystemCons.SERVICE_FLAG)){
				value = webConfigIe.get("agent_user_inspect", params[0]);
			}
		}else{
			value = webConfigIe.get(params[0], params[1]);
		}
		return value;
	}

	/**
	 * 获得指定节点key对应value值，同时对其进行英文转中文的操作
	 * @param params
	 * @return
	 */
	public static String getWebConfigChineseValue(String... params) {
		IniEditor webConfigIe = null;
		String value = "";
		try {
			webConfigIe = init();
			//return DESUtil.decryptStr(webConfigIe.get(params[0], params[1]));
			value = DescMsgUtil.decryptMsg(webConfigIe.get(params[0], params[1]));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(webConfigIe != null){
					saveWebConf(webConfigIe);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		if(NotNullUtil.objectArrayNotNull(params)){
//			for(String key : params){
//				logger.info("获得中文值:" + key);
//			}
//		}
		return value;
	}

	/**
	 * 加载所有webConfig中配置信息
	 * @return
	 */
	public static WebConfig loadWebConfig(){
		WebConfigUtil.webConfigIe = null;
		WebConfig wc = new WebConfig();
		IniEditor webConfigIe = null;
		try {
			webConfigIe = init();
			//		wc.setServer_flag(getWebConfigValue(Constants.WEB_CONFIG_GLOBAL, Constants.WEB_CONFIG_GLOBAL_SERVER_FLAG));
			//		wc.setGap_time(getWebConfigValue(Constants.WEB_CONFIG_GLOBAL, Constants.WEB_CONFIG_GLOBAL_GAP_TIME));
			//		wc.setNot_delete_userId(getWebConfigValue(Constants.WEB_CONFIG_GLOBAL, Constants.WEB_CONFIG_GLOBAL_NOT_DELETE_USER_ID));

			wc.setNorthDescMsg(DescMsgUtil.decryptMsg(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_NORTH_DESC_MSG)));
			wc.setWelcomeMsg(DescMsgUtil.decryptMsg(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_WELCOME_MSG)));
			
			wc.setPassword_period(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_PASSWORD_PERIOD));

			wc.setAuth_type(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_AUTH_TYPE));
			
			wc.setSync_deal(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SYNC_DEAL));
			wc.setSync_ip(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SYNC_IP));
			wc.setSync_port(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SYNC_PORT));
			wc.setSync_web_name(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SYNC_WEB_NAME));

			wc.setSso_deal(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SSO_DEAL));
			wc.setSso_ip(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SSO_IP));
			wc.setSso_port(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SSO_PORT));
			wc.setSso_web_name(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SSO_WEB_NAME));
			
			wc.setManager_port(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_MANAGER_PORT));
			wc.setCreate_virtual_ip(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_CREATE_VIRTUAL_IP));
			wc.setNot_iec(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_NOT_IEC));
			wc.setIec(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_IEC));
			wc.setSave_ausocket(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_SAVE_AUSOCKET));
			wc.setSpcsinfo_sort(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_AGENT_SPCSINFO_SORT));
			wc.setProc_list(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_PROC_LIST));
			wc.setHidden_eth_list(getWebConfigValue(webConfigIe, MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_HIDDEN_ETH_LIST));
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(webConfigIe != null){
					saveWebConf(webConfigIe);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return wc;
	}


	/**
	 * 
	 * @param obj
	 */
	public static void editWebConfig(WebConfig obj){
		IniEditor webConfigIe = null;
		try {
			webConfigIe = init();
			//			webConfigIe.set(Constants.WEB_CONFIG_GLOBAL, Constants.WEB_CONFIG_GLOBAL_GAP_TIME, obj.getGap_time());
			//			webConfigIe.set(Constants.WEB_CONFIG_GLOBAL, Constants.WEB_CONFIG_GLOBAL_NOT_DELETE_USER_ID, obj.getNot_delete_userId());

			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_NORTH_DESC_MSG, DescMsgUtil.encryptMsg(obj.getNorthDescMsg()));
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_WELCOME_MSG, DescMsgUtil.encryptMsg(obj.getWelcomeMsg()));

			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_PASSWORD_PERIOD, obj.getPassword_period());
			
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_AUTH_TYPE, obj.getAuth_type());
			
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SYNC_DEAL, obj.getSync_deal());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SYNC_IP, obj.getSync_ip());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SYNC_PORT, obj.getSync_port());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SYNC_WEB_NAME, obj.getSync_web_name());
			
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SSO_DEAL, obj.getSso_deal());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SSO_IP, obj.getSso_ip());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SSO_PORT, obj.getSso_port());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_SSO_WEB_NAME, obj.getSso_web_name());

			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_MANAGER_PORT, obj.getManager_port());

			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_CREATE_VIRTUAL_IP, obj.getCreate_virtual_ip());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_NOT_IEC, obj.getNot_iec());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_IEC, obj.getIec());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_SAVE_AUSOCKET, obj.getSave_ausocket());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_AGENT_SPCSINFO_SORT, obj.getSpcsinfo_sort());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_PROC_LIST, obj.getProc_list());
			webConfigIe.set(MoitorConstants.WEB_CONFIG_GLOBAL, MoitorConstants.WEB_CONFIG_GLOBAL_HIDDEN_ETH_LIST, obj.getHidden_eth_list());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(webConfigIe != null){
					saveWebConf(webConfigIe);
					WebConfigUtil.webConfigIe = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 还原默认配置文件
	 */
	public static void reset(){
		String filePath = MoitorConstants.WEB_PATH + "shell";
		if(SystemCons.OS_IS_LINUX){
			filePath = new File(MoitorConstants.WEB_PATH).getParent() + "/conf";
		}
		File fileSrc = new File(filePath, "web-config.conf-bak");
		File file = new File(filePath, "web-config.conf");
		try {
			FileContentUtil.copyFile(fileSrc.getAbsolutePath(), file.getAbsolutePath());
			WebConfigUtil.webConfigIe = null;
			JtsecLinuxUtil.saveEtc();
		} catch (Exception e) {
			log.info("找不到源文件路径!");
			e.printStackTrace();
		}
	}
}
