package com.jtsec.mc.dev.moitor.snmp.parse;

import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.mc.dev.moitor.pojo.model.HandType;
import com.jtsec.mc.dev.moitor.pojo.model.NemDeviceInfo;
import com.jtsec.mc.dev.moitor.snmp.AbstractSnmpParse;
import com.jtsec.mc.dev.moitor.snmp.SnmpParseI;
import com.jtsec.mc.dev.moitor.snmp.util.SnmpGetUtil;
import lombok.extern.slf4j.Slf4j;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 采集网卡配置信息
 *
 * @author surpassE
 * @version 1.0.0
 * @see 2015-09-24
 */
@Slf4j
public class InterfaceParse extends AbstractSnmpParse implements SnmpParseI {

	/**
	 * Logger for this class
	 */
	private static final String DEV_INTERFACE_INFO = "dev_interface_info";
	private static final String DEV_INTERFACE_ITEM_INFO = "dev_interface_item_info";

	private NemDeviceInfo ndi;

	public InterfaceParse () {
	}

	public InterfaceParse (String devId) {
		super.devId = devId;
	}

	public InterfaceParse (NemDeviceInfo ndi) {
		super.devId = ndi.getDevIdentify ();
		this.ndi = ndi;
	}

	@Override
	public void parseVb (Vector<?> vector) {
		super.vectorToArr (vector);
		List<String> sqlList = new ArrayList<String> ();
		for (int a = 0; a < arr.length; a++) {
			String sql = "INSERT INTO " + DEV_INTERFACE_INFO + " VALUES('" + super.devId + "', PARAM_0, 'PARAM_1', PARAM_2, 'PARAM_3', 'PARAM_4', 'PARAM_5', PARAM_6, PARAM_7, '','')";
			String msg = arr[a][0];
			if (msg != null && msg.length () > 0) {
				String encode = "UTF-8";
				if ("100004".equalsIgnoreCase (super.devOsType)) {
					encode = "GBK";
				}
				String ifName = arr[a][1];
				String ifAdmStatus = arr[a][4];
				if ("lo".equals (ifName) || "2".equals (ifAdmStatus)) {
					continue;
				}
				sql = sql.replace ("PARAM_0", (arr[a][0] == null ? "-2" : arr[a][0])).replace ("PARAM_1", (arr[a][1] == null ? "" : super.preToStringHex (arr[a][1], encode))).replace ("PARAM_2", (arr[a][2] == null ? "0" : arr[a][2]));
				sql = sql.replace ("PARAM_3", (arr[a][3] == null ? "" : arr[a][3])).replace ("PARAM_4", (arr[a][4] == null ? "" : arr[a][4])).replace ("PARAM_5", (arr[a][5] == null ? "" : arr[a][5]));
				sql = sql.replace ("PARAM_6", (arr[a][6] == null ? "0" : arr[a][6])).replace ("PARAM_7", (arr[a][7] == null ? "0" : arr[a][7]));
//				logger.debug(arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++]);
				sqlList.add (sql);

			}
		}
		super.execSqlList (sqlList, DEV_INTERFACE_INFO);

		SnmpGetUtil.snmpWalkSync (this.ndi, this.new InterfaceEditParse (super.devId), HandType.INTERFACE_EDIT);
		if ("100003".equals (ndi.getDevOsType ())) {
			super.execSqlList (new ArrayList<String> (), DEV_INTERFACE_ITEM_INFO);
			//获得三层交换机的vlan值
			List<String> vlanList = this.loadVlanList (ndi.getDevIdentify ());
			//这里可以执行清空表中的历史数据，重新采集添加到数据库中
			for (String vlan : vlanList) {
				ndi.setSnmpRcommunityParam ("@" + vlan);
				SnmpGetUtil.snmpWalkSync (ndi, this.new InterfaceItemParse (ndi.getDevIdentify ()), HandType.SW_ITEM);
				SnmpGetUtil.snmpWalkSync (ndi, this.new InterfaceItemEditParse (ndi.getDevIdentify ()), HandType.SW_ITEM_EDIT);
			}
		}
	}

	/**
	 * 加载三层交换机中Vlan网段划分的信息
	 *
	 * @param devId
	 * @return
	 */
	public List<String> loadVlanList (String devId) {
		List<String> list = new ArrayList<String> ();
		String sql = "SELECT if_index FROM " + DEV_INTERFACE_INFO + " WHERE dev_id='" + devId + "' AND LOWER(if_descr) LIKE '%vlan%'";
		log.info (sql);
		Connection conn = null;
		try {
			conn = DbUtil.getConn (DbUtil.DB_JTSEC_LOG);
			PreparedStatement ps = conn.prepareStatement (sql);
			ResultSet rs = ps.executeQuery ();
			while (rs.next ()) {
				list.add (rs.getString ("if_index"));
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return list;
	}

//	=========================  内部类  ==========================================

	/**
	 * 更新网卡信息的ip地址和子网掩码
	 *
	 * @author surpassE
	 * @time 2015-12-16
	 */
	public class InterfaceEditParse extends AbstractSnmpParse implements SnmpParseI {

		public InterfaceEditParse () {
		}

		public InterfaceEditParse (String devId) {
			super.devId = devId;
		}

		@Override
		public void parseVb (Vector<?> vector) {
//			super.vectorToArr2(vector, 8);
			super.vectorToMap (vector, 8);
			List<String> sqlList = new ArrayList<String> ();
			for (String key : map.keySet ()) {
				List<String> l = map.get (key);
				String sql = "UPDATE " + DEV_INTERFACE_INFO + " SET if_addr='PARAM_0', if_netmask='PARAM_2' WHERE dev_id='" + this.devId + "' AND if_index=PARAM_1";
				int i = 0;
				sql = sql.replace ("PARAM_" + (i++), (l.get (1) == null ? "" : l.get (1))).replace ("PARAM_" + (i++), (l.get (2) == null ? "-1" : l.get (2))).replace ("PARAM_" + (i++), (l.get (3) == null ? "" : l.get (3)));
				sqlList.add (sql);
			}
			super.execSqlList (sqlList);
		}
	}

	/**
	 * 获得三层交换机每个网段下的所有连接的设备的mac地址
	 *
	 * @author surpassE
	 * @time 2015-12-16
	 */
	public class InterfaceItemParse extends AbstractSnmpParse implements SnmpParseI {

		public InterfaceItemParse () {
		}

		public InterfaceItemParse (String devId) {
			super.devId = devId;
		}

		@Override
		public void parseVb (Vector<?> vector) {
//			super.vectorToArr(vector);
			super.vectorToMap (vector, 8);
			List<String> sqlList = new ArrayList<String> ();
			for (String key : super.map.keySet ()) {
				List<String> l = map.get (key);
				String sql = "INSERT INTO " + DEV_INTERFACE_ITEM_INFO + "(dev_id, item_mac, item_if_index) VALUES('" + super.devId + "', 'PARAM_1', 'PARAM_2')";
				sql = sql.replace ("PARAM_1", l.get (1)).replace ("PARAM_2", l.get (2));
				log.info ("1");
				sqlList.add (sql);
			}
			super.execSqlList (sqlList);
		}
	}

	/**
	 * 更新三层交换机，将mac与交换机的网口索引对应上
	 *
	 * @author surpassE
	 * @time 2015-12-16
	 */
	public class InterfaceItemEditParse extends AbstractSnmpParse implements SnmpParseI {

		public InterfaceItemEditParse () {
		}

		public InterfaceItemEditParse (String devId) {
			super.devId = devId;
		}

		@Override
		public void parseVb (Vector<?> vector) {
			super.vectorToMap (vector);
			List<String> sqlList = new ArrayList<String> ();
			for (String key : super.map.keySet ()) {
				List<String> l = map.get (key);
				String sql = "UPDATE " + DEV_INTERFACE_ITEM_INFO + " SET if_index=PARAM_0 WHERE dev_id = '" + super.devId + "' AND item_if_index='PARAM_1'";
				log.debug ("1");
				sql = sql.replace ("PARAM_0", l.get (1)).replace ("PARAM_1", l.get (0).substring (l.get (0).lastIndexOf (".") + 1, l.get (0).length ()));
				sqlList.add (sql);
			}
			super.execSqlList (sqlList);
		}
	}

}
