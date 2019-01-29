package com.jtsec.mc.dev.moitor.snmp;

import com.jtsec.common.Constants.LogCons;
import com.jtsec.common.Constants.SystemCons;
import com.jtsec.common.util.cache.log.model.NormalLogCache;
import com.jtsec.common.util.surpass.DateUtil;
import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.mc.dev.moitor.pojo.model.NemDeviceInfo;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.PDU;
import org.snmp4j.smi.VariableBinding;
import java.io.File;
import java.util.*;

/**
 * @author Administrator
 *
 */
@Slf4j
public class AbstractSnmpParse {
	/**
	 * Logger for this class
	 */

	protected String tableName;
	protected String devId;
	protected String devOsType;
	protected PDU requestPdu;
	protected String getherTime;
	protected String[][] arr = new String[65535][200];
	protected Map<String, List<String>> map = new HashMap<String, List<String>>();
	protected NemDeviceInfo nemDeviceInfo = new NemDeviceInfo();
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public PDU getRequestPdu() {
		return requestPdu;
	}
	public void setRequestPdu(PDU requestPdu) {
		this.requestPdu = requestPdu;
	}
	public String getGetherTime() {
		return getherTime;
	}
	public void setGetherTime(String getherTime) {
		this.getherTime = getherTime;
	}
	public String getDevOsType() {
		return devOsType;
	}
	public void setDevOsType(String devOsType) {
		this.devOsType = devOsType;
	}
    public NemDeviceInfo getNemDeviceInfo() {
        return nemDeviceInfo;
    }
    public void setNemDeviceInfo(NemDeviceInfo nemDeviceInfo) {
        this.nemDeviceInfo = nemDeviceInfo;
    }
    protected String getNowGatherTime(){
		return DateUtil.getNowTime();
	}
	/**
	 * 获得VariableBinding的值
	 * @param obj
	 * @return
	 */
	public String getVbValue(Object obj){
		return ((VariableBinding)obj).getVariable().toString();
	}
	
	/**
	 * 
	 * @param i
	 * @param vector
	 * @return
	 */
	public VariableBinding getVb(int i, Vector<?> vector){
		return ((VariableBinding)(vector.get(i)));
	}
	
	/**
	 * 
	 * @param i
	 * @param vector
	 * @return
	 */
	public String getVbValue(int i, Vector<?> vector){
		return ((VariableBinding)(vector.get(i))).getVariable().toString();
	}

	/**
	 * 
	 * @param i
	 * @param vector
	 * @return
	 */
	public String getVbOidValue(int i, Vector<?> vector){
		return ((VariableBinding)(vector.get(i))).getOid().toString();
	}
	
	
	/**
	 * 获得long类型的数据
	 * @param obj
	 * @return
	 */
	public long parseVbToLong(Object obj){
		long tmp = 0;
		String str = ((VariableBinding)obj).getVariable().toString();
		String regex = "[0-9]+";
		if(str.matches(regex)){
			tmp = Long.parseLong(str);
		}
		return tmp;
	}
	
	/**
	 * 
	 * @param i
	 * @param vector
	 * @return
	 */
	public long parseVbToLong(int i, Vector<?> vector){
		long tmp = 0;
		String str = ((VariableBinding)(vector.get(i))).getVariable().toString();
		String regex = "[0-9]+";
		if(str.matches(regex)){
			tmp = Long.parseLong(str);
		}
		return tmp;
	}
	
	/**
	 * 获得整型的数据
	 * @param obj
	 * @return
	 */
	public int parseVbToInteger(Object obj){
		int tmp = 0;
		String str = ((VariableBinding)obj).getVariable().toString();
		String regex = "[0-9]+";
		if(str.matches(regex)){
			tmp = Integer.parseInt(str);
		}
		return tmp;
	}
	
	/**
	 * 
	 * @param i
	 * @param vector
	 * @return
	 */
	public int parseVbToInteger(int i, Vector<?> vector){
		int tmp = 0;
		String str = ((VariableBinding)(vector.get(i))).getVariable().toString();
		String regex = "[0-9]+";
		if(str.matches(regex)){
			tmp = Integer.parseInt(str);
		}
		return tmp;
	}
	
	/**
	 * 判断响应的结果是否以请求的OID开始
	 * @param i
	 * @param vector
	 * @return
	 */
	public boolean isMatch(int i, Vector<?> vector){
		if(this.requestPdu != null && this.requestPdu.size() > 0){
			for(int j = 0; j < this.requestPdu.size(); j++){
				String requestOid = this.requestPdu.get(j).getOid().toString();
				if(((VariableBinding)(vector.get(i))).getOid().toString().startsWith(requestOid)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param i
	 * @param vector
	 * @param regex
	 * @return
	 */
	public boolean isMatch(int i, Vector<?> vector, String regex){
		if(this.requestPdu != null && this.requestPdu.size() > 0){
			for(int j = 0; j < this.requestPdu.size(); j++){
				String requestOid = this.requestPdu.get(i).getOid().toString();
				if(((VariableBinding)(vector.get(i))).getOid().toString().startsWith(requestOid) && ((VariableBinding)(vector.get(i))).getVariable().toString().startsWith(regex)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 批量执行添加操作时，先删除就的数据信息
	 * @param sqlList
	 * @param
	 */
	public void execSqlList(List<String> sqlList, String... tableNames){
		if(tableNames != null && tableNames.length > 0){
			for(int i = 0; i < tableNames.length; i++){
				String delSql = "DELETE FROM " + tableNames[i] + " WHERE dev_id='" + this.devId + "'";
				sqlList.add(i, delSql);
			}
		}
//		for(String sql : sqlList){
//			logger.debug("执行添加的SQL:" + sql);
//		}
		try {
			if(LogCons.DEV_TYPE_CODE_TZ.equals(SystemCons.dev_type)){
				NormalLogCache.getInstance().putSql(sqlList);
			}
			DbUtil.executeBetchSql(DbUtil.getConn(DbUtil.DB_JTSEC_LOG), sqlList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将vector存储到map中, map中的vlaue中的list中的第一个值是oid
	 * @param vector
	 * @param params oid保留的位数
	 */
	public void vectorToMap(Vector<?> vector, int... params){
//		map = new HashMap<String, List<String>>();
		for(int i = 0; i < vector.size(); i++){
			String oid = this.getVbOidValue(i, vector);
			String key = this.getVbOidValue(i, vector);
			if(params != null && params.length > 0){
				key = key.substring(key.length() - params[0], key.length());
			}
			if(map.containsKey(key)){
				map.get(key).add(this.getVbValue(i, vector));
			}else{
				List<String> list = new ArrayList<String>();
				if(!list.contains(oid)){
					list.add(0, oid);
				}
				list.add(this.getVbValue(i, vector));
				map.put(key, list);
			}
		}
	}

	/**
	 * 将vector中的数据转存到数组中
	 * @param vector
	 */
	public void vectorToArr(Vector<?> vector){
		String start = "-1";
		int j = 0;
		String row = "0";
		for(int i = 0; i < vector.size(); i++){
			String oid = this.getVbOidValue(i, vector);
			row = oid.substring(oid.lastIndexOf(".") + 1);
			//如果row的大于65535Name表示行数已经超过创建的数组的长度，对row进行截取处理
			int tmp = Integer.parseInt(row);
			if(tmp > 65535){
				row = row.substring(row.length() - 3, row.length());
			}
			if(start.equals(row)){
				j++;
			}
			if(i == 0){
				start = row;
			}
//			logger.debug(this.getVbOidValue(i, vector) + "====>" + this.getVbValue(i, vector) + "\t" + row + "<======>" + start + "<=====>" + j);
			arr[Integer.parseInt(row)][j] = this.getVbValue(i, vector);
		}
//		arr[(Integer.parseInt(row) + 1)][0] = "JTSEC_END";
//		for(int a=0;a<arr.length;a++){
//			int i=0;
//			logger.debug(arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++]);
//		}
	}
	
	/**
	 * oid中的最后一个.的后面的值作为数据的第一个参数
	 * @param vector
	 */
	public void vectorToArr2(Vector<?> vector){
		for(int i = 0; i < vector.size(); i++){
			String oid = this.getVbOidValue(i, vector);
			String lastParam = oid.substring(oid.lastIndexOf(".") + 1, oid.length());
			arr[Integer.parseInt(lastParam)][0] = lastParam;
			arr[Integer.parseInt(lastParam)][1] = this.getVbValue(i, vector);
		}
	}
	
	/**
	 * 通过分析OID的后几位值将snmpwalk结果转为arr[][]数组
	 * @param vector
	 * @param length 截取oid的长度
	 */
	public void vectorToArr2(Vector<?> vector, int length){
		CycleOid co = new CycleOid();
		int j = 0;
		for(int i = 0; i < vector.size(); i++){
			String oid = this.getVbOidValue(i, vector);
			this.isCycle(co, oid.substring(oid.length() - length, oid.length()));
			if(co.isCycle && i != 0){
				j++;
			}
//			logger.debug(this.getVbOidValue(i, vector) + "====>" + this.getVbValue(i, vector) + "\t" + row + "<======>" + start + "<=====>" + j);
			arr[co.getRowIndex()][j] = this.getVbValue(i, vector);
		}
//		for(int a=0;a<arr.length;a++){
//			int i=0;
//			logger.debug(arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++]);
//		}
	}
	
	/**
	 * 判断结果是否是否循环
	 * @param co
	 * @param oid
	 */
	private void isCycle(CycleOid co, String oid){
		co.setCycle(false);
		List<String> list = co.getList();
		if(list.contains(oid)){
			int rowIndex = list.indexOf(oid);
			if(rowIndex == 0 && list.size() > 0){
				co.setCycle(true);
			}
			co.setRowIndex(rowIndex);
		}else{
			co.getList().add(oid);
			co.setRowIndex(co.getList().indexOf(oid));
			co.setCycle(false);
		}
	}
	
	/**
	 * 获得含有日期后缀的表名
	 * @param tableName
	 * @return
	 */
	protected String getTableName(String tableName){
		String suffix = DateUtil.format(new Date(), "yyMMdd");
		return tableName + "_" + suffix;
	}
	
	/**
	 * 将16进制的字符串转为时间日期
	 * eg 07:dd:0a:17:0a:0b:25:00:2b:08:00 ==>2013-10-23,10:11:37.0,+8:0
	 * @param data
	 * @return
	 */
	protected String parseHexToTime(String data){
		data = data.substring(0,23).replaceAll(":", "");
		int year = Integer.valueOf(data.substring(0, 4), 16);
		int month = Integer.valueOf(data.substring(4, 6), 16);
		int day = Integer.valueOf(data.substring(6, 8), 16);
		int hour = Integer.valueOf(data.substring(8, 10), 16);
		int min = Integer.valueOf(data.substring(10, 12), 16);
		int sec = Integer.valueOf(data.substring(12, 14), 16);
		String time = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
		return time;
	}
	
	/**
	 * 将16进制的字符串进行解析处理
	 * @param str
	 * @return
	 */
	public static String preToStringHex(String str, String encode){
		String regex = "([0-9a-zA-Z]{2}[:]{0,1})+";
		if(str.indexOf(":") > -1 && str.matches(regex)){
			log.info(str);
			str = toStringHex(str.replaceAll(":", ""), encode);
		}
		if(str.endsWith(File.separator) || str.indexOf("\\") > -1){
//			str = str.substring(0, str.length() - 1);
			str = str.replaceAll("\\\\", "/");
		}
		return str;
	}
	
	/**
	 * 转化十六进制编码为字符串 
	 * @param s
	 * @return
	 */
	public static String toStringHex(String s, String encode) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
//			s = new String(baKeyword, "utf-8");// UTF-16le:Not
			s = new String(baKeyword, encode);// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 去掉msg末尾的/或是\
	 * @param
	 * @return
	 */
	public String formatTrail(String str){
		if(str != null && (str.endsWith(File.separator) || str.endsWith("\\"))){
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
	
	/**
	 * 初始化时用于设置共同的属性参数
	 * @param devId
	 * @param devOsType
	 */
	public void setBaseParams(String devId, String devOsType){
		this.devId = devId;
		this.devOsType = devOsType;
	}

	class CycleOid{
		private List<String> list = new ArrayList<String>();
		private boolean isCycle = false;
		private int rowIndex = 0;

		public List<String> getList() {
			return list;
		}
		public void setList(List<String> list) {
			this.list = list;
		}
		public boolean isCycle() {
			return isCycle;
		}
		public void setCycle(boolean isCycle) {
			this.isCycle = isCycle;
		}
		public int getRowIndex() {
			return rowIndex;
		}
		public void setRowIndex(int rowIndex) {
			this.rowIndex = rowIndex;
		}
	}
	
	
	public void showArrMsg(){
		for(int i = 0; i < arr.length; i++){
			if(arr[i][0] != null && arr[i][0].length() > 0){
				int j=0;
//				logger.debug(arr[i][j++] + "\t" + arr[i][j++] + "\t" + arr[i][j++] + "\t" + arr[i][j++] + "\t" + arr[i][j++] + "\t" + arr[i][j++]);
			}
		}
	}
}
