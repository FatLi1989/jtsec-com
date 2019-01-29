package com.jtsec.common.util.surpass;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class FileUtil {
	

	public static String osName = System.getProperty("os.name");
	
	public static final String SYNC_FILE_TMP_EXTENSION = ".tmp";
	
	public static void writeFile(File file, byte[] bytes, boolean flag){
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(file, flag);
			fos.write(bytes);
			fos.write("\n".getBytes());
			fos.flush();	
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void writeFile(File file, byte[] bytes, int off, int len, boolean flag){
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(file, flag);
			fos.write(bytes, off, len);
			fos.write("\n".getBytes());
			fos.flush();	
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 将字符换队列中的数据写入文件
	 * @param file  要写入的文件
	 * @param queue 存储字符串的队列
	 * @param maxWriteNum 一次最多从队列中取出多少数据写入文件
	 * @param isLineFeed  是否自动换行 
	 * @param isAppend  是否追加方式写入
	 * @param encode 设置字符串的写入文件时的编码
	 * @return 
	 */
	public static boolean write2File(File file, ConcurrentLinkedQueue<String> queue, int maxWriteNum, boolean isLineFeed, boolean isAppend, String encode) {
		boolean flag = FileUtil.write2File(file.getAbsolutePath(), queue, maxWriteNum, isLineFeed, isAppend, encode);
		return flag;
	}	
	
	/**
	 * 将字符换队列中的数据写入文件
	 * @param filePath  要写入的文件的绝对路径
	 * @param queue 存储字符串的队列
	 * @param maxWriteNum 一次最多从队列中取出多少数据写入文件
	 * @param isLineFeed  是否自动换行 
	 * @param isAppend  是否追加方式写入
	 * @param encode 设置字符串的写入文件时的编码
	 * @return 
	 */
	public static boolean write2File(String filePath, ConcurrentLinkedQueue<String> queue, int maxWriteNum, boolean isLineFeed, boolean isAppend, String encode) {
		OutputStream os = null;
		OutputStreamWriter ow = null;
		BufferedWriter bw = null;
		try {
			os = new FileOutputStream(filePath, isAppend);
			ow = new OutputStreamWriter(os, encode);
			bw = new BufferedWriter(ow);
			int count = 0;
			while(!queue.isEmpty()){
				String text = queue.poll();
				if(text != null){    
					bw.write(text);
					if(isLineFeed && !queue.isEmpty()){
						bw.newLine();				
					}
					bw.flush();
					count++;
				    //每次最多累计写入指定数量
				    if(count == maxWriteNum){
					    break;
				    }
				}
			}
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bw = null;
			}
			if(ow != null){
				try {
					ow.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ow = null;
			}	
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
		}
		return false;

	}
	
	
	/**
	 * 将text写入文件中
	 * @param filePath  要写入的文件
	 * @param text 要写入的字符串
	 * @param isLineFeed  是否自动换行 
	 * @param isAppend  是否追加方式写入
	 * @param encode  设置字符串的写入文件时的编码
	 * @return 
	 */
	public static boolean write2File(String filePath, String text, boolean isLineFeed, boolean isAppend, String encode) {
		OutputStream os = null;
		OutputStreamWriter ow = null;
		BufferedWriter bw = null;
		try {
			os = new FileOutputStream(filePath, isAppend);
			ow = new OutputStreamWriter(os, encode);
			bw = new BufferedWriter(ow);		
			bw.write(text);
			if(isLineFeed){
				bw.newLine();				
			}
			bw.flush();
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bw = null;
			}
			if(ow != null){
				try {
					ow.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ow = null;
			}	
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
		}
		return false;

	}
	
	
	public static String readFile(String Path) {
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer laststr = new StringBuffer();
        try {
            fileInputStream = new FileInputStream(Path);
            inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
            	laststr.append(tempString).append("and");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStreamReader != null){
            	try {
            		inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileInputStream != null){
            	try {
            		fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return laststr.substring(0, laststr.length()-3).toString();
	}	


	public static boolean parserFile2Queue(File file, ConcurrentLinkedQueue<String> queue) {
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = reader.readLine()) != null) {
            	queue.offer(str);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStreamReader != null){
            	try {
            		inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileInputStream != null){
            	try {
            		fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	
	/**
	 * 创建文件
	 * 
	 * @param path
	 * @return
	 */
	public static File createFile(String path){
		File file = FileUtil.createDirAndFile(path);
		if(file == null){
			return null;
		}
		try {
			int count = 0;
			while(!file.exists() && count < 5){
				file = FileUtil.createDirAndFile(path);
				count++;
				Thread.sleep(100);
			}
			if(!file.exists()){
				log.error("create file [" + path + "] fail!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}	
	
	
	/**
	 * 创建文件及所属的目录
	 * 
	 * @param path
	 * @return
	 */
	public static File createDirAndFile(String path) {
        File file = new File(path);
        if(file.exists()) {
        	/*if(logger.isEnabledFor(Level.WARN)){
        		logger.warn("Create file " + destFileName + "failed!目标文件已存在！");
        	}*/
            return file;
        }
        if (file.isDirectory()) {
        	if(log.isWarnEnabled ()){
        		log.warn("The file " + path + " to be created cannot be a folder.!");
        	}
            return null;
        }
        //判断目标文件所在的目录是否存在
        if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
        	if(log.isInfoEnabled()){
        		log.info("The dir is no exists, begin create it！");
        	}
            if(!file.getParentFile().mkdirs()) {
            	if(log.isWarnEnabled ()){
            		log.warn("Create target dir failed！");
            	}
                return null;
            }
        }
        //创建目标文件
        try {
            if (!file.createNewFile()) {
            	if(log.isWarnEnabled ()){
					log.warn("Create file " + path + " failed!");
            	}
                return null;
            } 
        	if(log.isInfoEnabled()){
				log.info("Create file " + path + " success.");
        	}
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            if(log.isWarnEnabled ()){
        		log.warn("Create file " + path + " failed！The reason " + e.getMessage());
        	}
            return null;
        }
    }
   
	/**
	 * 目录创建
	 */
    public static boolean createDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
        	if(log.isWarnEnabled ()){
				log.warn("Create dir " + path + " failed! The dir is exists");
        	}
            return false;
        }
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
        	if(log.isInfoEnabled()){
				log.info("Create dir " + path + " success.");
        	}
            return true;
        } else {
        	if(log.isWarnEnabled ()){
				log.warn("Create dir " + path + " failed！");
        	}
            return false;
        }
    }
    
	/**
	 * 
	 * 如果该文件的文件名以.extension结尾，则去除掉该扩展名
	 * 如果传入的所要重命名的file不存在或不是一个文件，则返回空字符串
	 * 
	 * @param file
	 * @param extension
	 * @return filePath
	 */
	public static String trimExtension(File file, String extension){
		
		if(!file.exists() || !file.isFile()){
			return "";
		}
		
		String fileSrcName = file.getName();
		if(!fileSrcName.endsWith(extension)){
			return "";
		}
		
		int lastIndex = fileSrcName.lastIndexOf(".");
		String fileDstName = fileSrcName.substring(0,lastIndex);
		String dstFilePath = file.getParent() + File.separator + fileDstName;
		File dstFile = new File(dstFilePath);
		
		if(dstFile.exists() && dstFile.isDirectory()){
			dstFile.delete();
		}
		
		if(!file.renameTo(dstFile)){
			return "";
		}
		
		return dstFile.getPath();
	}

	
	/**
	 * 将数据写入到同步文件
	 * 写入的文件路径中应包含临时扩展名
	 * 写入完毕后自动去掉参数中指定的临时扩展名
	 * 
	 * @param
	 */
	public static void sync2File(String path, String str, String tmpExtension){
		try {
			File file = FileUtil.createFile(path);
			if(file == null){
				return;
			}
			boolean flag = FileUtil.write2File(path, str, true, false, "UTF-8");
			if(flag == false){
				log.error("write data to tmp file " + path + " failed!");
				return;
			}
			long fileSize = file.length();
			//去掉临时扩展名
			String fpath = FileUtil.trimExtension(file, tmpExtension);
			if("".equals(fpath)){
				log.error("config tmp file rename to fpath " + fpath + ", fsize " + fileSize + " failed!");
				return;
			}
			if(log.isDebugEnabled()){
				log.debug("config tmp file rename to fpath " + fpath + ", fsize " + fileSize + " success.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	
	
	/**
	 * 把两个需要拼接的路径拼接在一起</br>
	 * 要求：第一个路径不能以路径分隔符结尾，第二个路径必须以路径分隔符结尾</br>
	 * 如果第一个路径以路径分隔符结尾，会被强制节去掉</br>
	 * 例：C:/  --->  C:  </br>
	 * 如果第二个路径未以路径分隔符结尾，会被自动添加上</br>
	 * 例：tmpdisk/root/ -->  /tmpdisk/root/</br>
	 * 这样防止路径拼接时，第一个路径的结尾符，和第二个路径的开始符重复，导致路径拼接错误。</br></br>
	 * 
	 * @param firstDir  要进行拼接的第一个路径，必须为目录</br>
	 * @param secondPath  
	 * 				要进行拼接的第二个路径</br>
	 * 				可以为目录或完整的路径，如：/root/dbsync 
	 * 				也可以为一个相对的完整路径，如：root/dbsync/out/file.data</br>
	 * @return
	 */
	public static String appendPath(String firstDir, String secondPath){
		firstDir = FileUtil.formatStandardPath(firstDir);
		secondPath = FileUtil.formatStandardPath(secondPath);
		if(firstDir.endsWith(File.separator)){
			firstDir = firstDir.substring(0, firstDir.length()-1);
		}
		if(!secondPath.startsWith(File.separator)){
			secondPath = File.separator + secondPath;
		}
		if(osName.startsWith("Windows")){
			if(!secondPath.startsWith(firstDir)){
				secondPath = firstDir + secondPath;
				return secondPath;				
			}
		}
		if(secondPath.startsWith(firstDir)){
			secondPath = secondPath.replace(firstDir, "");
		}
		return secondPath;
	}
	
	/**
	 * 根据不同的操作系统，自动转换路径中的分割符为当前系统的标准格式
	 * 
	 * @param path
	 * @return
	 */
	public static String formatStandardPath(String path){
		if(osName.startsWith("Windows")){
				path = path.replaceAll("/", "\\\\");
				return path;				
		}
		path = path.replaceAll("\\\\", "/");
		return path;
	}
	
	
	/**
	 * 
	 * 目录文件排序
	 * 排序规则：目录排在前面，按字母顺序排序文件列表
	 * 
	 * @param fileDir
	 * @return
	 */
	public static List<File> fileSort(String fileDir){
		List<File> files = Arrays.asList(new File(fileDir).listFiles());
		Collections.sort(files, new Comparator<File>(){
		    @Override
		    public int compare(File o1, File o2) {
		    if(o1.isDirectory() && o2.isFile())
		        return -1;
		    if(o1.isFile() && o2.isDirectory())
		            return 1;
		    return o1.getName().compareTo(o2.getName());
		    }
		});
		return files;
	}	
	/**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
	
    
    public static void main(String[] args) {
        deleteFile("C:\\Users\\zhanghx\\Desktop\\settings.xml");
    }
}
