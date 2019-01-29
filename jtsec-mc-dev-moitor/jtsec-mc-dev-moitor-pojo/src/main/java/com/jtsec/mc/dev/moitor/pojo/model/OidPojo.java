package com.jtsec.mc.dev.moitor.pojo.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class OidPojo implements Comparable<OidPojo>{

		//oid 的类型 双向、单向、视频
		private String type;
		//设备提供者
		private String provider;
		//设备型号
		private String version;
		//oid的值
		private String value;
		//描述信息
		private String descript;
		//数据类型
		private String dataType;
		//对应数据库的表
		private String tableName;
		//数据处理的类型，执行处理接口
		private String handType;
		//snmp variable值的编码方式
		private String encoding = "UTF-8";
		//顺序
		private int seq = 0;
		//用于存放子节点
		private Map<String, OidPojo> map = new HashMap<String, OidPojo>();

		@Override
		public int compareTo(OidPojo o) {
			return this.seq - o.getSeq(); 
		}
}
