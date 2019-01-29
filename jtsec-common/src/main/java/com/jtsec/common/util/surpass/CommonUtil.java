package com.jtsec.common.util.surpass;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class CommonUtil {
	// 其他方法的定义 //
	/**
	 * *getAppPath需要一个当前程序使用的Java类的class属性参数，它可以 返回打包过的
	 * *Java可执行文件（jar，war）所处的系统目录名或非打包Java程 序所处的目录
	 * 
	 * @param cls为Class类型
	 * @return 返回值为该类所在的 Java程序运行的目录
	 */
	public static String getAppPath(Class cls) {
		ClassLoader loader = cls.getClassLoader();
		String clsName = cls.getName() + ".class";
		Package pack = cls.getPackage();
		String path = "";
		if (pack != null) {
			String packName = pack.getName();
			clsName = clsName.substring(packName.length() + 1);
			if (packName.indexOf(".") < 0)
				path = packName + "/";
			else {
				int start = 0, end = 0;
				end = packName.indexOf(".");
				while (end != -1) {
					path = path + packName.substring(start, end) + "/";
					start = end + 1;
					end = packName.indexOf(".", start);
				}
				path = path + packName.substring(start) + "/";
			}
		}
		URL url = loader.getResource(path + clsName);
		String realPath = url.getPath();
		int pos = realPath.indexOf("file:");
		if (pos > -1)
			realPath = realPath.substring(pos + 5);
		pos = realPath.indexOf(path + clsName);
		realPath = realPath.substring(0, pos - 5);
		if (realPath.endsWith("!"))
			realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		return realPath;
	}// getAppPath定义 结束

	
	/**
	 * 
	 * 配置文件寻找过程
	 * 1.未打包时，构建目录classpath或其子目录中的配置文件
	 * 2.未打包时，项目源文件目录下或其子目录中的配置文件；或打包后，jar包外的配置文件
	 * 
	 * 
	 * @param fileName
	 * @param fileDir
	 * @return
	 */
	public static String getFilePath(String fileName, String fileDir) {
		String path = null;
		fileName = CommonUtil.formatURLPath(fileName);
		fileDir = CommonUtil.formatURLPath(fileDir);
		// 未打包时的配置文件
		URL url = CommonUtil.class.getResource("/" + ((fileDir != null && fileDir.length()>0) ? (fileDir + "/") : "") + fileName);
		if(log.isDebugEnabled()){
			
		}
		System.out.println("getFilePath 未打包时，构建目录classpath或其子目录中的配置文件:" + url.toString());
		if(url != null){
			path = url.getFile();
			File file = new File(path);
			if (file.exists()) {
				if(log.isDebugEnabled()){
					log.debug("getFilePath 未打包时，构建目录classpath或其子目录中的配置文件");
				}
				try {
					return file.getCanonicalPath();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				path = null;
			}
		}
		
		fileName = CommonUtil.formatFilePath(fileName);
		fileDir = CommonUtil.formatFilePath(fileDir);
		// jar包外的配置文件
		path = System.getProperty("user.dir") + File.separator + ((fileDir !=null && fileDir.length()>0) ? (fileDir + File.separator) : "") + fileName;
		File file = new File(path);
		if (file.exists()) {
			if(log.isDebugEnabled()){
				log.debug("getFilePath 未打包时，项目源文件目录下或其子目录中的配置文件；或打包后，jar包外的配置文件");
			}
			return path;
		}else{
			path = null;
		}
		
		return path;
	}		
	
	/**
	 * 
	 * 1.未打包时，构建目录classpath或其子目录中的配置文件
	 * 2.未打包时，项目源文件目录下或其子目录中的配置文件；或打包后，jar包外的配置文件
	 * 
	 * @param fileName
	 * @param fileDir
	 * @return
	 */
	public static File getFile(String fileName, String fileDir) {
		File file = null;
		fileName = CommonUtil.formatURLPath(fileName);
		fileDir = CommonUtil.formatURLPath(fileDir);
		// 未打包时的配置文件
		URL url = CommonUtil.class.getResource("/" + ((fileDir != null && fileDir.length()>0) ? (fileDir + "/") : "") + fileName);
		if(url != null){
			String path = url.getFile().replaceAll("%20", " ");
			file = new File(path);
			if (file.exists()) {
				if(log.isDebugEnabled()){
					log.debug("getFile 未打包时，构建目录classpath或其子目录中的配置文件");
				}
				return file;
			}
		}
		fileName = CommonUtil.formatFilePath(fileName);
		fileDir = CommonUtil.formatFilePath(fileDir);
		// jar包外的配置文件
		String path = System.getProperty("user.dir") + File.separator + ((fileDir !=null && fileDir.length()>0) ? (fileDir + File.separator) : "") + fileName;
		file = new File(path);
		if (file.exists()) {
			if(log.isDebugEnabled()){
				log.debug("getFile 未打包时，项目源文件目录下或其子目录中的配置文件；或打包后，jar包外的配置文件");
			}
			return file;
		}		
		return file;
	}		
	
	/**
	 * 
	 * 1.未打包时，构建目录classpath或其子目录中的配置文件
	 * 2.未打包时，项目源文件目录下或其子目录中的配置文件；或打包后，jar包外的配置文件
	 * 
	 * @param fileName
	 * @param fileDir
	 * @return
	 */
	public static InputStream getFileInputStream(String fileName, String fileDir) {
		InputStream in = null;
		// 未打包时读取配置
		File file = null;
		try {
			fileName = CommonUtil.formatURLPath(fileName);
			fileDir = CommonUtil.formatURLPath(fileDir);
			URL url = CommonUtil.class.getResource("/" + ((fileDir != null && fileDir.length()>0) ? (fileDir + "/") : "") + fileName);
			if(url != null){
				String path = url.getFile();
				file = new File(path);
				if (file.exists()) {
					if(log.isDebugEnabled()){
						log.debug("getFileInputStream 未打包时，构建目录classpath或其子目录中的配置文件");
					}
					in = new FileInputStream(new File(path));
					return in;
				}
			}
			
			fileName = CommonUtil.formatFilePath(fileName);
			fileDir = CommonUtil.formatFilePath(fileDir);
			// 读取jar包外配置文件
			String path = System.getProperty("user.dir") + File.separator + ((fileDir !=null && fileDir.length()>0) ? (fileDir + File.separator) : "") + fileName;
			file = new File(path);
			if (file.exists()) {
				if(log.isDebugEnabled()){
					log.debug("getFileInputStream 未打包时，项目源文件目录下或其子目录中的配置文件；或打包后，jar包外的配置文件");
				}
				in = new FileInputStream(new File(path));
				return in;
			}
			
			fileName = CommonUtil.formatURLPath(fileName);
			fileDir = CommonUtil.formatURLPath(fileDir);
			// 读取jar包内配置文件
			in = CommonUtil.class.getResourceAsStream("/" + ((fileDir != null && !fileDir.isEmpty()) ? (fileDir + "/") : "") + fileName);
			if(log.isDebugEnabled()){
				log.debug("getFileInputStream(getResourceAsStream) 未打包时，构建目录classpath或其子目录中的配置文件；或打包后，jar中的配置文件");
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;

	}
		
	
	public static OutputStream getFileOutputStream(String fileName, String fileDir) {
		OutputStream out = null;
		// 未打包时的配置文件
		try {
			fileName = CommonUtil.formatURLPath(fileName);
			fileDir = CommonUtil.formatURLPath(fileDir);
			URL url = CommonUtil.class.getResource("/" + ((fileDir != null && fileDir.length()>0) ? (fileDir + "/") : "") + fileName);
			if(url != null){
				String path = url.getFile();
				File file = new File(path);
				if (file.exists()) {
					if(log.isDebugEnabled()){
						log.debug("getFileOutputStream 未打包时，构建目录classpath或其子目录中的配置文件");
					}
					out = new FileOutputStream(new File(path));
					return out;
				}
			}

			fileName = CommonUtil.formatFilePath(fileName);
			fileDir = CommonUtil.formatFilePath(fileDir);
			// jar包外的配置文件
			String path = System.getProperty("user.dir") + File.separator + ((fileDir !=null && fileDir.length()>0) ? (fileDir + File.separator) : "") + fileName;
			File file = new File(path);
			if (file.exists()) {
				if(log.isDebugEnabled()){
					log.debug("getFileOutputStream 未打包时，项目源文件目录下或其子目录中的配置文件；或打包后，jar包外的配置文件");
				}
				out = new FileOutputStream(new File(path));
				return out;
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;

	}	
	

	public static URL getFileURL(String fileName, String fileDir) {
		URL url = null;
		// 未打包时的配置文件
		try {
			fileName = CommonUtil.formatURLPath(fileName);
			fileDir = CommonUtil.formatURLPath(fileDir);
			url = CommonUtil.class.getResource("/" + ((fileDir != null && fileDir.length()>0) ? (fileDir + "/") : "") + fileName);
			if(url != null){
				System.out.println("getFileURL[0]:" + url.toString());
				String path = url.getFile();
				File file = new File(path);
				if (file.exists()) {
					if(log.isDebugEnabled()){
						log.debug("getFileURL 未打包时，构建目录classpath或其子目录中的配置文件");
					}
					return url;
				}else{
					url = null;
				}
			}
			
			fileName = CommonUtil.formatFilePath(fileName);
			fileDir = CommonUtil.formatFilePath(fileDir);
			// jar包外的配置文件
			String path = System.getProperty("user.dir") + File.separator + ((fileDir !=null && fileDir.length()>0) ? (fileDir + File.separator) : "") + fileName;
			File file = new File(path);
			if (file.exists()) {
				if(log.isDebugEnabled()){
					log.debug("getFileURL 未打包时，项目源文件目录下或其子目录中的配置文件；或打包后，jar包外的配置文件");
				}
				url = file.toURI().toURL();
				return url;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;

	}	
	
	/**
	 * 将资源路径字符串，进行标准的URL表达形式处理
	 * 具体操作为：把资源路径中的反斜线（\）转换为斜线（/）
	 * 
	 * @param str
	 * @return
	 */
	public static String formatURLPath(String str){
		if(str ==null || str.isEmpty()){
			return str;
		}
		str = str.replaceAll("\\\\", "/");
		return str;
	}

	/**
	 * 将资源路径字符串，进行标准的操作系统文件路径表达形式处理
	 * 具体操作为：
	 * 如果是Windows系统，则把资源路径中的反斜线（/）转换为斜线（\）
	 * 如果是Linux系统，则把资源路径中的反斜线（\）转换为斜线（/）
	 * 
	 * @param str
	 * @return
	 */
	public static String formatFilePath(String str){
		if(str ==null || str.isEmpty()){
			return str;
		}
		String os_name = System.getProperty("os.name");
		if(os_name.startsWith("W") || os_name.startsWith("w")){
			str = str.replaceAll("/", "\\\\");			
		}else{
			str = str.replaceAll("\\\\", "/");
		}
		return str;
	}
	
	public static void main(String[] args){
		String fileDir = "config";
		String fileName = "siphole_data_parser.properties";
		String filePath = CommonUtil.getFilePath(fileName, fileDir);
		System.out.println(filePath);
	}
	

}
