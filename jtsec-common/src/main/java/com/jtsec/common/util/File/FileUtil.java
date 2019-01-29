package com.jtsec.common.util.File;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/11/23 14:57
 */
public class FileUtil {

	public static String getFilePath(String fileName){
			String url = ClassLoader.getSystemClassLoader ().getResource (fileName).getPath ();
			return url.substring (1, url.length ());
	}

}
