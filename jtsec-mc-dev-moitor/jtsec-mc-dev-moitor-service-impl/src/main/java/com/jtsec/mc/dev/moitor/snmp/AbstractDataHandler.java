package com.jtsec.mc.dev.moitor.snmp;

import com.jtsec.mc.dev.moitor.snmp.util.OidUtil;
import com.jtsec.mc.dev.moitor.snmp.util.SnmpUtil;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.smi.VariableBinding;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 通过指责链模式处理SNMP接收的数据信息
 * 
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 *
 */
@Slf4j
public abstract class AbstractDataHandler {
	/**
	 * Logger for this class
	 */

	public AbstractDataHandler abstractDataHandler;
	protected String ip = "";
	protected String port = "";
	protected String region = "1";
	protected String monitorTime = "";
	protected String valueDetail = "";
	
	protected String dataType;
	
	/**
	 * 处理SNMP数据信息
	 * @param dataType
	 * @param sqlList
	 */
	public abstract void handleRequest(String dataType, List<String> sqlList, List<VariableBinding> vbList);
	
	/**
	 * 处理syslog日志数据
	 * @param dataType
	 * @param sqlList
	 * @param value
	 */
	public abstract void handleRequest(String dataType, List<String> sqlList, String value);
	
	/**
	 * 如果此处不能解析数据，那么调用下一个abstractDataHandler进行数据处理
	 * @param abstractDataHandler
	 */
	public abstract void setNextHandler(AbstractDataHandler abstractDataHandler);
	
	/**
	 * 获得下一个事件的处理的对象
	 */
	public abstract AbstractDataHandler getNextHandler();
	
	/**
	 * 将data传递到下一个handler进行解析
	 * 
	 * @param dataType
	 * @param sqlList
	 * @param vbList
	 */
	protected void callNextHandler(String dataType, List<String> sqlList, List<VariableBinding> vbList){
		if(this.abstractDataHandler != null){
			//将参数传递到下一个handler
			this.abstractDataHandler.ip = this.ip;
			this.abstractDataHandler.port = this.port;
			this.abstractDataHandler.region = this.region;
			this.abstractDataHandler.monitorTime = this.monitorTime;
			
			this.abstractDataHandler.handleRequest(dataType, sqlList, vbList);
		}
	}
	
	protected void callNextHandler(String dataType, List<String> sqlList, String value){
		//将参数传递到下一个handler
		this.abstractDataHandler.ip = this.ip;
		this.abstractDataHandler.port = this.port;
		this.abstractDataHandler.valueDetail = this.valueDetail;
		this.abstractDataHandler.monitorTime = this.monitorTime;
		
		this.abstractDataHandler.handleRequest(dataType, sqlList, value);
	}
	
	/**
	 * 对content内容进行charset转码
	 * @param content
	 * @param charset
	 * @return
	 */
	protected String getStringByEncode(String content, String charset){
		if(charset == null || "".equals(charset.trim())){
			charset = OidUtil.getEncoding();
		}
		log.info(charset);
		String regex = "[\\w|:]*[A-Fa-f]+[\\w|:]*";
		if(content.indexOf(":") > -1 && Pattern.compile(regex).matcher(content).matches()){
			String[] arr = content.split(":");
			if(arr.length == 1){
				return content;
			}
			byte[] buf = new byte[arr.length];
			for(int i=0; i<buf.length; i++){
				//将arr转为字节数组
				buf[i] = (byte)(Character.digit(arr[i].charAt(0), 16) * 16 + Character.digit(arr[i].charAt(1), 16));
			}
			try {
				return new String(buf, charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}else{
			return content;
		}
	}

	/**
	 * 加载tab_monitor_map.properties获得要操作的表名
	 * @param key
	 * @return
	 */
	protected String getTableName(String key){
		Map<String, String> map = SnmpUtil.getTabMonitorMap();
		if(key == null || key.length() <= 0){
			return null;
		}
		return map.get(key);
	}
	
	/**
	 * 获得要操作的表明称
	 * @param oid1 List<VariableBinding> 中第2个元素的variable值
	 * @param oid2 List<VariableBinding> 中第3个或是以后的variable值
	 */
	protected String getTableName(String oid1, String oid2){
		String tableName = 	OidUtil.getTableName(oid1);
		if((tableName == null || tableName.length() <= 0) && oid2 != null && oid2.length() > 0){
			tableName = OidUtil.getTableName(oid2);
		}
		return tableName;
	}
}
