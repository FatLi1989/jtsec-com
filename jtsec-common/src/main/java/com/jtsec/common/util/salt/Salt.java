package com.jtsec.common.util.salt;

import lombok.Data;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author NovLi
 * @Title:
 * @ProjectName jtsec-dpaim-plat
 * @Description: TODO
 * @date 2018/12/27 20:40
 */
@Data
public class Salt {

	public final static  String salt = "111111";

	/**
	 * 生成随机盐
	 */
	public static String randomSalt() {
		// 一个Byte占两个字节，此处生成的3字节，字符串长度为6
		SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator ();
		String hex = secureRandom.nextBytes(3).toHex();
		return hex;
	}


	public static String encryptPassword(String username, String password, String salt)
	{
		return new Md5Hash (username + password + salt).toHex();
	}
}
