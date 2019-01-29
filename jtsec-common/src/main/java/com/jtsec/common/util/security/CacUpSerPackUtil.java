package com.jtsec.common.util.security;

import com.jtsec.common.util.surpass.FileContentUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 生成和验证服务升级包 
 * CacUpSerPack : create and check upload server package 
 * @author surpassE
 *
 */
@Slf4j
public class CacUpSerPackUtil {
	/**
	 * Logger for this class
	 */
	private static final int MD5_LENGTH = 32;
	private static String MD5_FILE_SUFFIX = ".md5";

	/**
	 * 创建加密的含有md5值的文件
	 * step1:对指定的filePath文件进行des加密操作
	 * step2:计算文件md5值,并将md5值写入到MD5_FILE_SUFFIX后缀名称的临时文件中,
	 * step3:将临时文件更名
	 * @param filePath
	 * @throws Exception
	 */
	public static String createUpSerPack(String filePath) throws Exception{
		//获得加密后升级包的文件路径
		String destFilePath = DESUtil.encryptFile(filePath);
//		String destFilePath = filePath;
		File file = new File(destFilePath);
		File fileTmp = new File(destFilePath + MD5_FILE_SUFFIX);
		//计算文件的md5值
		String md5 = MD5Util.getFileMD5String(destFilePath);
		log.info("md5值：" + md5 + "\t 字节长度：" + md5.getBytes().length);
		//将md5值到文件起始位置
		byte[] buff = new byte[1024];
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(fileTmp);
		//先写入文件的md5值
		fos.write(md5.getBytes());
		int len = 0;
		while((len = fis.read(buff)) > -1){
			fos.write(buff, 0, len);
			fos.flush();
		}
		fis.close();
		fos.close();
		//改变文件名称
		if(file.exists()){
			file.delete();
		}
		fileTmp.renameTo(file);
		return file.getAbsolutePath();
	}
	
	/**
	 * 验证指定路径文件的正确性
	 * step1:从文件头部获得md5值,将去掉md5值的文件保存位还有MD5_FILE_SUFFIX后缀的临时文件
	 * step2:计算临时文件的md5值,判断step1获得md5是否等于临时文件的md5值,相等表示文件没有被改动过
	 * step3:如果step2中返回true那么更新临时文件名称,否则删除创建的临时文件
	 * step4:对加密文件进行解密操作
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static boolean checkUpSerPack(String filePath) throws Exception{
		File file = new File(filePath);
		File fileTmp = new File(filePath + MD5_FILE_SUFFIX);
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(fileTmp);
		byte[] buff = new byte[1024];
		//获得源文件的md5值
		fis.read(buff, 0, MD5_LENGTH);
		String md5Old = new String(buff, 0, MD5_LENGTH);
		int len = 0;
		while((len = fis.read(buff)) > -1 ){
			fos.write(buff, 0, len);
			fos.flush();
		}
		fos.close();
		fis.close();
		//计算文件的md5值
		String md5 = MD5Util.getFileMD5String(fileTmp.getAbsolutePath());
		if(md5.equals(md5Old)){
			if(file.exists()){
				file.delete();
			}
			fileTmp.renameTo(file);
		}else{
			//删除创建的临时文件
			fileTmp.delete();
			return false;
		}
		log.info("md5值对比结果：" + md5Old.equals(md5) + "\n文件中md5值：" + md5Old + "\n计算文件的md5值：" + md5);
		//执行解密的操作，获得解密的文件的路径
//		String destFilePath = DESUtil.decryptFile(filePath);
		return true;
	}
	
	public static void main(String[] args) throws Exception{
		/**
		 * G:/encrypt/demo/web_ROOT.bin	G:/encrypt/demo/web_ROOT.bin.se
		 * 
		 */
//		String upload = "G:/encrypt/firewall-template/ft.conf";
//		createUpSerPack(upload);
//		FileContentUtil.delExistFile(upload);
		
		String download = "G:/encrypt/firewall-template/ft.conf.se";
		checkUpSerPack(download);
		DESUtil.decryptFile(download);
		FileContentUtil.delExistFile(download);
	}
	
}
