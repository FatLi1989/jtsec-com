package com.jtsec.mc.dev.moitor.pojo.model;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 数据传输
 *
 * @author Paulus
 */
@Data
public class DBTransportMessage implements Serializable {

	private static final long serialVersionUID = 1L;


	private Integer databasetype;
	private String url;
	private String port;
	private String userName;
	private String password;
	private String databaseName;


	private List<SyncConfigDto> list;
	private String sql;


	@Override
	public String toString () {
		return "DBTransportMessage [databasetype=" + databasetype + ", url=" + url + ", port=" + port + ", userName="
				+ userName + ", password=" + password + ", databaseName=" + databaseName + ", list=" + list + ", sql="
				+ sql + "]";
	}
}
