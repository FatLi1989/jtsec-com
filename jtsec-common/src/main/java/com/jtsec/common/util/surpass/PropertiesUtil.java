package com.jtsec.common.util.surpass;

import com.jtsec.common.Constants.KeyCons;
import com.jtsec.common.Constants.MoitorConstants;
import com.jtsec.common.Constants.SystemCons;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import java.io.File;
import java.util.ResourceBundle;


/**
 * key=value文件操作工具类
 * @author surpassE
 *	
 */
public class PropertiesUtil {

	private static ResourceBundle bundle = null;
	private static final String commonPath = "jtsec_com";
	
	private static ResourceBundle dbBundle = null;
	private static final String dbConfPath = "config";
	private static PropertiesConfiguration config;
	
	static{
		if(bundle == null){
			bundle = ResourceBundle.getBundle(commonPath);
			try {
				config = new PropertiesConfiguration(PropertiesUtil.class.getResource("/jtsec_com.properties"));
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
		}
		if(dbBundle == null){
			dbBundle = ResourceBundle.getBundle(dbConfPath);
		}
	}
	
	/**
	 * 通过key获得配置文件中的value值
	 * @param key
	 * @return
	 */
	public static String getDbValue(String key) {
		return dbBundle.getString(key);
	}
	
	/**
	 * 通过key获得配置文件中的value值
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		return bundle.getString(key);
	}
	
	/**
	 * 不同的操作系统下获得不同的配置文件的路径
	 * @param key
	 * @return
	 */
	public static String getFilePath(String key){
		if(SystemCons.OS_IS_LINUX){
			return bundle.getString(key+"_linux");
		}
		String dir = bundle.getString(KeyCons.TEST_CONF_PATH);
		String path = MoitorConstants.WEB_PATH + (dir.endsWith(File.separator) ? dir : dir + File.separator)  + bundle.getString(key);
		return path;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getWinFilePath(String key){
		String dir = bundle.getString(KeyCons.TEST_CONF_PATH);
		String path = MoitorConstants.WEB_PATH + (dir.endsWith(File.separator) ? dir : dir + File.separator)  + bundle.getString(key);
		return path;
	}
	
	public synchronized static void setValue(String key, String value) {
		PropertiesConfiguration config = null;
		try {
			if (null == config) {
				config = new PropertiesConfiguration(PropertiesUtil.class.getResource("/"+ commonPath + ".properties"));
				config.setReloadingStrategy(new FileChangedReloadingStrategy());
			}
			config.setProperty(key, value);
			config.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 不同的操作系统下获得不同的配置文件的路径
     * @param key
     * @return
     */
    public static String getComFilePath(String key){
        if(SystemCons.OS_IS_LINUX){
            return bundle.getString(key+"_linux");
        }
        return bundle.getString(key);
    }
	
	/*public synchronized static void setValue(String key, String value) {
		PropertiesConfiguration pro = new PropertiesConfiguration();
		try {
			pro.load(PropertiesUtil.class.getClassLoader().getResource(commonPath + ".properties").getPath());
			pro.setProperty(key, value);
			pro.save(pro.getFile());
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}*/
}
