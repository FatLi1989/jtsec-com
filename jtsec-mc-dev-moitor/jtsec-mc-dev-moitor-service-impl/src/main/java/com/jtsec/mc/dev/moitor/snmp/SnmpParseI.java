package com.jtsec.mc.dev.moitor.snmp;

import org.snmp4j.PDU;
import java.util.Vector;

public interface SnmpParseI {

	/**
	 * 解析VariableBinding信息
	 * @param
	 */
	public void parseVb (Vector<?> vector);

	/**
	 * 保存请求的oid集合
	 * @param requestPdu
	 */
	public void setRequestPdu(PDU requestPdu);
}
