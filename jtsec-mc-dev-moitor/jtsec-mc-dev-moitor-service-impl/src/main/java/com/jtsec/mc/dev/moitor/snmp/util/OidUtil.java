package com.jtsec.mc.dev.moitor.snmp.util;

import com.jtsec.common.util.File.FileUtil;
import com.jtsec.mc.dev.moitor.pojo.model.HandType;
import com.jtsec.mc.dev.moitor.pojo.model.OidPojo;
import com.jtsec.mc.dev.moitor.snmp.DefaultCommandResponder;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * 通过xml方式注册oid信息，通过此类操作jtsec-oid.xml文件、完成数据的加载
 * 
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 *
 */
@Slf4j
public class OidUtil {
	/**
	 * Logger for this class
	 */

	private static final String DEFAULT_ENCODING = "UTF-8";

	private static Map<String, OidPojo> globalMap = new HashMap<String, OidPojo>();
	//存放oid及对应的整条oid配置
	private static Map<String, OidPojo> map = new HashMap<String, OidPojo>();
	private static Map<String, Map<String, OidPojo>> typeMap = new HashMap<String, Map<String, OidPojo>>();

	static{
		initOidMap();
		initGlobalMap();
		initTypeMap();
	}
	/**
	 * 通过全局配置判断数据包是不是可以解析的数据包
	 * 
	 * @param oid1
	 * @param oid2
	 * @return
	 */
	public static boolean isLegal(String oid1, String oid2){
		initGlobalMap();
		if(globalMap.get(oid1) != null && globalMap.get(oid2) != null){
			return true;
		}
		return false;
	}

	/**
	 * 获得编码方式
	 * @return
	 */
	public static String getEncoding(){
		Document doc = getDocument();
		if(doc != null){
			Element root = doc.getRootElement();
			Element e = root.element("global");
			OidPojo global = getOidPojo(e);
			return global.getEncoding();
		}
		return DEFAULT_ENCODING;
	}

	/**
	 * 通过oid获得到对应的数据库表
	 * @param oid
	 * @return
	 */
	public static String getTableName(String oid){
		initOidMap();
		OidPojo op = map.get(oid);
		if(op != null){
			return op.getTableName();
		}
		return null;
	}

	/**
	 * 获得数据标志位
	 * @param oid
	 * @return
	 */
	public static String getDataType(String oid){
		initOidMap();
		OidPojo op = map.get(oid);
		String dataType = null;
		if(op != null){
			dataType = op.getDataType();
		}else{
			op = map.get(oid.substring(0, oid.lastIndexOf(".")));
			return op.getDataType();
		}
		return dataType;
	}

	/**
	 * 获得数据处理的接口标志类型
	 * 
	 * @param oid
	 * @return
	 */
	public static String getHandType(String oid){
		initOidMap();
		OidPojo op = map.get(oid);
		if(op != null){
			return op.getHandType();
		}
		return null;
	}

	/**
	 * 初始化document对象、加载xml文件
	 * @return
	 */
	private static Document getDocument(){
		File file = new File(FileUtil.getFilePath ("jtsec-oid.xml"));
		if(!file.exists()){
			file = new File(FileUtil.getFilePath ("jtsec-oid.xml"));
		}
		try{
			SAXReader reader = new SAXReader();
//			Document doc = reader.read(new File("E:/java/myecplise10/jtsec_mc/src/main/resources/jtsec-oid.xml"));
//			logger.debug(new File(OidUtil.class.getResource("/") + "snmp/jtsec-oid.xml"));
//			logger.debug(OidUtil.class.getResource("/") + "/snmp/jtsec-oid.xml");
			Document doc = reader.read(file);
			return doc;
		}catch (Exception e) {
			log.error("找不到OID库文件{jtsec-oid.xml},[" + file.getAbsolutePath() + "]");
		}
		return null;
	}

	/**
	 * 加载oid全局配置
	 */
	private static void initGlobalMap(){
		if(globalMap == null || globalMap.size() <= 0){
			Document doc = getDocument();
			if(doc != null){
				Element root = doc.getRootElement();
				Element e = root.element("global");
				OidPojo global = getOidPojo(e);
				recursionOid(globalMap, e, global, "oid");
			}
		}
	}
	
	/**
	 * 获得设备类型与设备oid的对应关系
	 * 	此处有待扩展，扩展的内容typeMap,typeMap中的key不应该只有type属性，应该是设备的类型、提供者、设备的型号三个属性的拼接值
	 *  例如，网络里有两台交换机一台CISCO的交换机、一台H3C的交换机此时的type都是100003，因此无法取得xml中对应的数据，如果是三个属性
	 *  的拼接值，则可以直接定位设备的oid信息
	 */
	private static void initTypeMap(){
		if(typeMap == null || typeMap.size() <= 0){
			Document doc = getDocument();
			if(doc != null){
				Element root = doc.getRootElement();
				List<?> list = root.elements("device");
				for(Object obj : list){
					Map<String, OidPojo> map = new HashMap<String, OidPojo>();
					Element e = (Element)obj;
					OidPojo oidPojo = getOidPojo(e);
					recursionOid(map, e, oidPojo, "oid");
					map.put(oidPojo.getValue(), oidPojo);
					typeMap.put(oidPojo.getType(), map);
				}
			}
		}
	}

	/**
	 * 初始化oidmap
	 */
	private static void initOidMap(){
		if(map == null || map.size() <= 0){
			Document doc = getDocument();
			if(doc != null){
				Element root = doc.getRootElement();
				List<?> list = root.elements("device");
				for(Object obj : list){
					Element e = (Element)obj;
					OidPojo oidPojo = getOidPojo(e);
					recursionOid(map, e, oidPojo, "oid");
					map.put(oidPojo.getValue(), oidPojo);
				}
			}
		}
	}

	/**
	 * 递归加载OID节点及子节点
	 * @param map
	 * @param e
	 * @param op
	 * @param name
	 */
	private static void recursionOid(Map<String, OidPojo> map, Element e, OidPojo op, String name){
		List<?> list = e.elements(name);
		if(list != null && list.size() > 0){
			for(Object o : list){
				Element ce = (Element)o;
				OidPojo cop = getOidPojo(ce);
				//递归判断此节点是否含有子节点
				recursionOid(map, ce, cop, name);
				op.getMap().put(cop.getValue(), cop);
				map.put(cop.getValue(), cop);
			}
		}
	}

	/**
	 * 获得OID对象
	 * @param e
	 * @return
	 */
	private static OidPojo getOidPojo(Element e){
		OidPojo op = new OidPojo();
		op.setType(e.attributeValue("type"));
		op.setValue(e.attributeValue("value"));
		op.setTableName(e.attributeValue("tableName"));
		op.setDescript(e.attributeValue("descript"));
		op.setHandType(e.attributeValue("handType"));
		op.setDataType(e.attributeValue("dataType"));
		String encoding = e.attributeValue("encoding");
		if(encoding != null && encoding.length() > 0){
			op.setEncoding(encoding);
		}
		String seq = e.attributeValue("seq");
		if(seq != null && seq.length() > 0){
			op.setSeq(Integer.parseInt(seq));
		}
		return op;
	}

	protected static void showMsg(Map<String, OidPojo> map){
		if(map != null && map.size() > 0){
			for(String key : map.keySet()){
				OidPojo op = map.get(key);
				System.out.println("oid : " + op.getValue() + " \t tableName:" + op.getTableName() + "\t dataType:" + op.getDataType() + "\ttype:" + op.getType() + "\thandType:" + op.getHandType());
				Map<String, OidPojo> cmap = op.getMap();
				if(cmap != null){
					for(String t : cmap.keySet()){
						OidPojo cop = map.get(t);
						System.out.println("\t\toid : " + cop.getValue() + " \t tableName:" + cop.getTableName() + "\t dataType:" + op.getDataType() + "\ttype:" + cop.getType() + "\thandType:" + cop.getHandType());
					}
				}
			}
		}
	}
	
	/**
	 * 加载指定type下的所有匹配handType值得oid，已被listOidPojoByHandType取缔
	 * @param type
	 * @param handType
	 * @return
	 */
	@Deprecated
	public static List<String> listOidByHandType(String type, HandType handType){
		initTypeMap();
		List<String> list = new ArrayList<String>();
		for(String typeValue : typeMap.keySet()){
			if(typeValue.equals(type)){
				Map<String, OidPojo> map = typeMap.get(type);
				for(String value : map.keySet()){
					OidPojo op = map.get(value);
					if((handType.getValue() + "").equals(op.getHandType())){
						list.add(op.getValue());
					}
				}
			}
		}
		Collections.sort(list);
		return list;
	}
	
	/**
	 * 加载指定type下的所有匹配handType值得oid
	 * @param type
	 * @param handType
	 * @return
	 */
	public static List<OidPojo> listOidPojoByHandType(String type, HandType handType){
		initTypeMap();
		List<OidPojo> list = new ArrayList<OidPojo>();
		for(String typeValue : typeMap.keySet()){
			if(typeValue.equals(type)){
				Map<String, OidPojo> map = typeMap.get(type);
				for(String value : map.keySet()){
					OidPojo op = map.get(value);
	                   if(typeValue.equals("100004")){
	                        op.toString();
	                    }
					if((handType.getValue() + "").equals(op.getHandType())){
						list.add(op);
					}
				}
			}
		}
		//当在指定类型的节点无法对应的OID时，去SNMP_BASE节点去寻找
		if(list.size() <= 0){
			Map<String, OidPojo> map = typeMap.get("SNMP_BASE");
			for(String value : map.keySet()){
				OidPojo op = map.get(value);
				if((handType.getValue() + "").equals(op.getHandType())){
					list.add(op);
				}
			}
		}
		Collections.sort(list);
		return list;
	}
	
	
	public static void main(String[] args) {
//		test1();
		test2();
	}
	
	protected static void test1(){
		initOidMap();
		showMsg(map);
		try{
			String param = "udp:0.0.0.0/162";
			SnmpUtil.startSnmp(param, new DefaultCommandResponder ());
			Thread.sleep(3000);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static void test2(){
		initTypeMap();
		for(String type : typeMap.keySet()){
			log.debug(type);
			if("Window".equals(type)){
				log.debug("====================== window START ======================");
				Map<String, OidPojo> map = typeMap.get(type);
				for(String value : map.keySet()){
					log.debug(value);
				}
				log.debug("====================== window END ============================");
			}else if("Linux".equals(type)){
				log.debug("====================== Linux START ======================");
				Map<String, OidPojo> map = typeMap.get(type);
				for(String value : map.keySet()){
					log.debug(value);
				}
				log.debug("======================= Linux END ========================");
			}
		}
	}
}
