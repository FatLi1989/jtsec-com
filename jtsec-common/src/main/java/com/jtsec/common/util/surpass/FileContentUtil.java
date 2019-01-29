package com.jtsec.common.util.surpass;


import com.jtsec.common.util.security.MD5Util;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 文件内容操作
 * 文件夹及文件操作
 * </p>
 * @author surpassE
 * 
 */
@Slf4j
public class FileContentUtil {

	/**
	 * 将指定的内容添加文件指定起始位置，最多添加三个参数开始位置(start)、停止位置(end)、要添加的内容(text)
	 * 如果params长度为3并且end为null，则将text添加到start的下第一行。
	 * 如果params长度为3并且都不为null,那么删除start和end之间原有的数据，将text添加到start下第一行
	 * @param filePath
	 * @param start
	 * @param end
	 * @param text
	 * @throws Exception
	 */
	public static void addTextToFile(String filePath, String start, String end, String text) throws Exception{
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = null;
		boolean notComment = true;
		boolean flag = true;
		while((line = br.readLine()) != null){
			//判断是否保存节点 
			if(flag){
				sb.append(line).append("\n");
			}
			//在文件指定起始位置添加text
			if(NotNullUtil.stringNotNull(start) && NotNullUtil.stringNotNull(text) && end == null){
				if(line.trim().equals(start)){
					sb.append(text).append("\n");
					notComment = false;
				}
			}
			//先删除起始位置中的内容，将text添加到指定起始位置中间
			if(end != null){
				if(line.trim().equals(start)){
					sb.append(text).append("\n");
					flag = false;
					notComment = false;
				}
				if(line.trim().equals(end)){
					sb.append(line).append("\n");
					flag = true;
					notComment = false;
				}
			}
		}
		if(notComment){
			sb.append("\n").append(start).append("\n");
			sb.append(text).append("\n");
			if(NotNullUtil.stringNotNull(end)){
				sb.append(end).append("\n");
			}
		}
		line = null;
		br.close();
		coverFile(filePath, sb.toString());
		sb = null;
	}
	
	/**
	 * 删除文件中指定位置的数据信息，接收参数为start,end,text, 可以不填或是用null占位
	 * params为空，删除文件中所有的内容
	 * 如果params长度为3并且start、end为null,text非空，则删除文件所有含有包含text的行数据,text可以时多个关键字的组合中间用，分隔开
	 * 如果params长度为3并且start、end不为null,text非空，则删除文件start和end节点之间所有含有包含text的行数据,text可以时多个关键字的组合中间用，分隔开
	 * @param filePath
	 * @param params
	 * @throws Exception
	 */							//INIT_SHELL_FILE_PATH, SD_KIT_START, SD_KIT_END, cmd
	public static void delTextFromFile(String filePath, String... params) throws Exception{
		String start = getStart(params); 	//index = 0  sd_kit_start 
		String end = getEnd(params);		//index = 1  sd_kit_end  
		String text = getText(params);		//index = 2  cmd

		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = null;
		boolean flag = true;
		while((line = br.readLine()) != null){
			//如果参数为空表示删除文件中所有的内容
			if(!NotNullUtil.objectArrayNotNull(params)){
				break;
			}
			//如果起始位置节点为空表示删除文件中所有包含text的值
			if(start == null && end == null && NotNullUtil.stringNotNull(text)){
				if(text.indexOf(",") > -1){
					String[] arr = text.split(",");
					for(String tmp : arr){
						if(line.indexOf(tmp) > -1){
							flag = false;
							break;
						}
						flag = true;
					}
				}else{
					if(line.indexOf(text) > -1){
						flag = false;
					}else{
						flag = true;
					}
				}
			}
			//删除指定节点中的所有内容
			if(NotNullUtil.stringNotNull(start) && NotNullUtil.stringNotNull(end) && text == null){
				if(line.trim().equals(start)){
					sb.append(line).append("\n");
					flag = false;
				}
				if(line.trim().equals(end)){
					flag = true;
				}
			}
			if(NotNullUtil.stringNotNull(start) && NotNullUtil.stringNotNull(end) && NotNullUtil.stringNotNull(text)){
				if(line.trim().equals(end)){
					flag = true;
				}
				if(!flag){
					//					&& line.indexOf(text) < 0
					if(text.indexOf(",") > -1 ){
						boolean f = false;
						String[] arr = text.split(",");
						for(String tmp : arr){
							if(line.indexOf(tmp) > -1){
								f = true;
								break;
							}
						}
						if(!f){
							sb.append(line).append("\n");
						}
					}else{
						if(line.indexOf(text) < 0){
							sb.append(line).append("\n");
						}
					}
				}
				if(line.trim().equals(start)){
					sb.append(line).append("\n");
					flag = false;
				}
			}
			//判断是否添加此行
			if(flag){
				sb.append(line).append("\n");
			}
		}
		line = null;
		br.close();
		coverFile(filePath, sb.toString());
		sb = null;
	}
	
	/**
	 * 删除文件中指定text的数据信息(多行数据一起删除)
	 * text为拼接好的多行数据字符串
	 * @param filePath
	 * @param text
	 * @throws Exception
	 */
	public static void delMultiLineFromFile (String filePath, String text) throws Exception{
		String result = "";
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = null;
		while((line = br.readLine()) != null){
			sb.append(line).append("\n");
		}
		result = sb.toString();
		if(NotNullUtil.stringNotNull(text)){
			result = result.replaceAll(text, "");
		}
		line = null;
		br.close();
		coverFile(filePath, result);
		sb = null;
	}

	/**
	 * 获得文件中指定起始位置的数据信息或是文件中所有行数据保存到list中
	 * params为null 获得全文数据行，start、end不为null获得节点之间的所有数据行
	 * @param filePath
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static List<String> getTextFromFile(String filePath, String... params) throws Exception{
		String start = getStart(params);
		String end = getEnd(params);
		int size = getSize(params);

		List<String> list = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = null;
		boolean flag = false;
		while((line = br.readLine()) != null){
			if(size == 0){
				flag = true;
			}
			//获得指定节点中间的数据
			if(start != null && end != null){
				if(line.trim().equals(end)){
					break;
				}
				if(line.trim().equals(start)){
					flag = true;
					continue;
				}
			}
			//判断是不是要获得数据信息
			if(flag){
				list.add(line);
			}
		}
		line = null;
		br.close();
		return list;
	}

	/**
	 * 获得文件中指定起始位置的数据信息或是文件中所有行数据(以字符串的形式返回)
	 * params为null 获得全文数据行，start、end不为null获得节点之间的所有数据行
	 * @param filePath
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String getTextFromFileAsString(String filePath, String... params) throws Exception{
		String start = getStart(params);
		String end = getEnd(params);
		int size = getSize(params);

		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = null;
		boolean flag = false;
		while((line = br.readLine()) != null){
			if(size == 0){
				flag = true;
			}
			//获得指定节点中间的数据
			if(start != null && end != null){
				if(line.trim().equals(end)){
					break;
				}
				if(line.trim().equals(start)){
					flag = true;
					continue;
				}
			}
			//判断是不是要获得数据信息
			if(flag){
				sb.append(line).append("\n");
			}
		}
		line = null;
		br.close();
		return sb.toString();
	}

	/**
	 * 用newText替换指定文件中的oldText的内容
	 * @param filePath
	 * @param oldText	要被替换的文件内容
	 * @param newText	替换所使用数据
	 * @throws Exception
	 */
	public static void replaceTextInFile(String filePath, String oldText, String newText) throws Exception{
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = null;
		while((line = br.readLine()) != null){
			if(line.indexOf(oldText) > -1){
				sb.append(newText).append("\n");
			}else{
				sb.append(line).append("\n");
			}
		}
		line = null;
		br.close();
		coverFile(filePath, sb.toString());
		sb = null;
	}
	
	/**
	 * 用newText替换指定文件中的oldText的从第start次出现到第ent次出现的内容
	 * @param filePath
	 * @param oldText	要被替换的文件内容
	 * @param newText	替换所使用数据
	 * @throws Exception
	 */
	public static void replaceTextInFile(String filePath, String oldText, String newText,int start,int ent){
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line = null;
			int sum = 1;
			while((line = br.readLine()) != null){
				if(line.indexOf(oldText) > -1 && sum >= start && sum <= ent){
					sb.append(newText).append("\n");
					sum ++;
				}else{
					sb.append(line).append("\n");
				}
			}
			line = null;
			br.close();
			coverFile(filePath, sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sb = null;
	}
	
	/**
	 * 获取指定文件中含有text的行中属性的值
	 * @param filePath
	 * @throws Exception
	 */
	public static String getTextLine(String filePath, String text,int count){
		BufferedReader br = null;
		String line = null;
		String indexText = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			int sum = 0;
			while((line = br.readLine()) != null){
				if(line.indexOf(text) > -1){
					sum++;
					if(sum == count){
						indexText = line;
					}
				}
			}
			
				br.close();
			
			br = null;
			if(indexText != null && indexText.trim().length() > 1){
				indexText = indexText.substring(indexText.indexOf("=") + 1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return indexText.trim();
	}
	

	/**
	 * 去掉文件中键值对中=两边的空格 eg key = value  格式化后  key=value
	 * @param filePath
	 * @throws Exception
	 */
	public static void trimFileText(String filePath) throws Exception{
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = null;
		while((line = br.readLine()) != null){
			sb.append(line.replaceAll(" +", "")).append("\n");
		}
		line = null;
		br.close();
		coverFile(filePath, sb.toString());
		sb = null;
	}

	/**
	 * 从指定文件的起始位置读取指定行数的数据，保存到list中
	 * @param filePath
	 * @param start
	 * @param totalLines	读取的总行数
	 * @return
	 * @throws Exception
	 */
	public static List<String> getTextFromFile(String filePath, String start, int totalLines) throws Exception{
		List<String> list = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = null;
		boolean flag = false;
		while((line = br.readLine()) != null){
			if(list.size() == totalLines){
				break;
			}
			if(flag){
				list.add(line);
			}
			if(line.trim().equals(start)){
				flag = true;
			}
		}
		line = null;
		br.close();
		return list;
	}

	/**
	 * 将text追加到文件的末尾
	 * @param filePath
	 * @param text
	 * @throws Exception
	 */
	public static void addTextToFileTail(String filePath, String text) throws Exception{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath),true)));
		bw.append(text + "\n");
		bw.flush();
		bw.close();
	}

	/**
	 * 将text写入文件中
	 * @param filePath
	 * @param text
	 * @throws Exception
	 */
	public static void coverFile(String filePath, String text) throws Exception{
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
		bw.write(text);
		bw.flush();
		bw.close();
	}

	/**
	 * 删除文件中指定start和end之间的空白行
	 * @param filePath
	 * @param start
	 * @param end
	 * @throws Exception
	 */
	public static void delBlankLine(String filePath, String start, String end) throws Exception{
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = null;
		boolean flag = false;
		while((line = br.readLine()) != null){
			if(line.indexOf(start) > -1){
				flag = true;
			}
			if(flag){
				if(NotNullUtil.stringNotNull(line)){
					sb.append(line).append("\n");
				}
			}else{
				sb.append(line).append("\n");
			}
			if(line.indexOf(end) > -1){
				flag = false;
			}
		}
		line = null;
		br.close();
		coverFile(filePath, sb.toString());
		sb = null;
	}


	private static int getSize(String... params){
		if(NotNullUtil.objectArrayNotNull(params)){
			return params.length;
		}
		return 0;
	}

	private static String getStart(String... params){
		if(NotNullUtil.objectArrayNotNull(params)){
			if(params.length >= 1 && NotNullUtil.stringNotNull(params[0])){
				return params[0];
			}
		}
		return null;
	}

	private static String getEnd(String... params){
		if(NotNullUtil.objectArrayNotNull(params)){
			if(params.length >= 2 && NotNullUtil.stringNotNull(params[1])){
				return params[1];
			}
		}
		return null;
	}

	private static String getText(String... params){
		if(NotNullUtil.objectArrayNotNull(params)){
			if(params.length >= 3 && NotNullUtil.stringNotNull(params[2])){
				return params[2];
			}
		}
		return null;
	}


	//****************************** 文件及文件夹操作*********************************

	/**
	 * 文件复制,文件中空格被去掉
	 * 
	 * @param src
	 * @param dest
	 * @throws Exception
	 */
	public static void copyFile(String src, String dest) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(src));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while((line = br.readLine()) != null){
			sb.append(line.replaceAll(" +", "")).append("\n");
		}
		line = null;
		br.close();
		coverFile(dest, sb.toString());
		sb = null;
	}

	/**
	 * 将流中的数据写入到指定的路径中
	 * @param filePath
	 * @param is
	 * @throws Exception
	 */
	public static void fileUpload(String filePath, InputStream is) throws Exception{
		FileOutputStream fos = new FileOutputStream(filePath);
		byte[] buf = new byte[1024];
		int len = 0;
		while((len = is.read(buf)) > -1){
			fos.write(buf, 0, len);
		}
		fos.flush();
		fos.close();
		is.close();
	}


	/**
	 * 递归删除指定路径及路径下的所有文件
	 * @param filePath
	 */
	public static void delCascadeDir(String filePath){
		File file = new File(filePath);
		if(file.isDirectory()){
			File[] arr = file.listFiles();
			if(arr != null){
				for(File tmp:arr){
					if(tmp.isDirectory()){
						delCascadeDir(tmp.getAbsolutePath());
					}
					tmp.delete();
				}
			}
		}
		file.delete();
	}


	/**
	 * 判断指定路径是否包含子文件夹
	 * @param filePath
	 * @return
	 */
	public static boolean hasChildFolder(String filePath){
		File file = new File(filePath);
		boolean flag = true;
		if(file != null){
			File[] arr = file.listFiles();
			if(NotNullUtil.objectArrayNotNull(arr)){
				for(File fileTmp : arr){
					if(fileTmp.isDirectory()){
						flag = false;
						break;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 获得指定路径path下所有文件夹或是子文件夹路径，路径是相对于root的相对路径
	 * eg root：F:/aa	path:F:/aa  aa下有文件夹bb和cc，呢么list中的值为aa和bb
	 * @param list
	 * @param root
	 * @param path
	 */
	public static void getAllFolderPath(List<String> list, String root, String path){
		File file = new File(path);
		File[] arr = file.listFiles();
		if(NotNullUtil.objectArrayNotNull(arr)){
			for(File fileTmp : arr){
				if(fileTmp.isDirectory()){
					//判断文件是否包含子文件夹
					if(FileContentUtil.hasChildFolder(fileTmp.getAbsolutePath())){
						if(list != null){
							list.add(fileTmp.getAbsolutePath().replaceAll(root, ""));
						}
					}
					getAllFolderPath(list, root, fileTmp.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * 创建文件路径
	 * @param dirPath
	 */
	public static void createPath(String dirPath){
		File file = new File(dirPath);
		if(!file.isDirectory()){
			file.mkdirs();
		}
	}

	/**
	 * 删除已经存在的文件
	 * @param filePath
	 */
	public static void delExistFile(String filePath){
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
	}

	/**
	 * 判断路径是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean fileExist(String filePath){
		File file = new File(filePath);
		return file.exists();
	}
	
	/**
	 * 判断路径是不是文件夹，如果不是文件夹，是否需要创建文件夹
	 * @param filePath
	 * @param flag
	 * @return
	 */
	public static boolean isFolder(String filePath, boolean flag){
		boolean f = true;
		File file = new File(filePath);
		if(file.isDirectory()){
			f = true;
		}else{
			f = false;
		}
		if(flag && !f){
			createPath(filePath);
		}
		return f;
	}
	
	/**
	 * 指定路径下创建指定大小、文件名称、计算文件md5值的文件
	 * @param filePath
	 * @param fileName
	 * @param fileSize
	 */
	public static String createFileWithMd5(String filePath, String fileName, long fileSize) {
		try{
			File file = new File(filePath, fileName);
			if(file.exists()){
				file.delete();
			}
			StringBuffer sb = new StringBuffer();
			while(sb.length() < 1024){
				sb.append(System.currentTimeMillis() + "" + System.currentTimeMillis());
			}
			coverFile(file.getAbsolutePath(), sb.toString());
			String md5 = MD5Util.getFileMD5String(file.getAbsolutePath());
			File newFile = new File(filePath, fileName.replaceAll(".tmp", "") + "_" + md5);
			file.renameTo(newFile);
			return newFile.getAbsolutePath();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}
	
	/**
	 * 校验制定路径下文件的md5值
	 * @param filePath
	 * @param delFlag	是否删除
	 * @return
	 */
	public static String checkFileCmd(String filePath, String delFlag){
		String flag = "0";
		try{
			File file = new File(filePath);
			if(file.isDirectory()){
				File[] arr = file.listFiles();
				if(NotNullUtil.objectArrayNotNull(arr)){
					for(File tmp: arr){
						if(tmp.isFile()){
							String old_md5 = tmp.getName().replaceAll("jtsec_", "");
							String md5 = MD5Util.getFileMD5String(tmp.getAbsolutePath());
							log.info("old_md5 : " + old_md5 + "\tmd5:" + md5);
							if(md5.equals(old_md5)){
								flag = "1";
							}
							if("1".equals(delFlag)){
								delCascadeDir(tmp.getAbsolutePath());
							}
						}
					}
				}else{
					flag = "error";
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public static void saveFile(byte[] b, String path) throws Exception {
		FileOutputStream fs = new FileOutputStream(path,true);
		int byteread = b.length;
		fs.write(b, 0, byteread);
		fs.flush();
		fs.close();
	}
	
	/**
     * 改变文件名称
     * @param srcFilePath
     * @param dstFilePath
     */
    public static void renameFile(String srcFilePath, String dstFilePath){
        File file = new File(srcFilePath);
        File dstFile = new File(dstFilePath);
        file.renameTo(dstFile);
    }
    
  //****************************** 文件及文件夹操作*********************************

    /**
     * 文件复制
     * @param src
     * @param dest
     * @return
     */
    public static boolean copyFiles(String src, String dest) {
        File dstFile = new File(dest);
        createPath(dstFile.getParent());
        boolean flag = false;
        try {
            //判断源文件是否存在
            if(!new File(src).exists()){
                return flag;
            }
            BufferedReader br = new BufferedReader(new FileReader(src));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line).append("\n");
            }
            line = null;
            br.close();
            coverFile(dest, sb.toString());
            sb = null;
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
	
    /**
     * 文件剪切、粘贴操作
     * @param srcFile   要移动的源文件
     * @param dst   目的路劲或是含有指定文件名称的路径、默认为路径(不含指定的文件名称)
     * @param params    标志位默认为null标识dst为路径(不含指定的文件名称)，如果params[0]为非0,标识dst是含有文件名称的路径
     * @return
     */
    public static boolean moveFile(String srcFile, String dst, String... params) {
        boolean flag = false;
        String dstPath = dst;
        File file = new File(srcFile);
        //判断dst是不是以/或\或是\\结尾
        if(dst.endsWith("/") || dst.endsWith("\\") || dst.endsWith("\\\\")){
            dstPath = dst + file.getName();
        }else{
            dstPath = dst + "/" + file.getName();
        }
        //通过params中参数判断dst是不是含有文件名称的路径
        if(NotNullUtil.objectArrayNotNull(params)){
            if(!"0".equals(params[0])){
                dstPath = dst;
            }
        }
        if(copyFiles(srcFile, dstPath)){
            //复制完成后删除源文件
            file.delete();
            flag = true;
        }
        return flag;
    }
    
    /**
     * 将text写入文件中
     * @param filePath
     * @param text
     * @throws Exception
     */
    public static void coverFiles(String filePath, String text) {
        try {
            File file = new File(filePath);
            //创建子父级路径
            createPath(file.getParent());
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 以指定的编码向文件末尾添加数据信息
     * 
     * @param path
     * @param text
     * @param encode
     */
    public static void addTextToFileTail(String path, String text, String encode){
        try{
            File file = new File(path);
            createPath(file.getParent());
            BufferedWriter bw = null;
            if(encode != null && encode.length() > 0){
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true), encode));
            }else{
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
            }
            bw.write(text);
            bw.flush();
            bw.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	
	/*public static void main(String[] args) {
		String start = "#custom_firewall_start";
		String end = "#custom_firewall_end";
		String filePath = "E:/java/myecplise10/jtsec_com/src/main/webapp/testconf/init_shell";
		//		String start = "";
		//		String end = "";
		String text = "hello word";
		try {
			String msg = getTextFromFileAsString(filePath, "#default_gateway_start", "#default_gateway_end");
			
			System.out.println(msg);
			
			
			addTextToFile(filePath, start, end, text);
			System.out.println("over.........");
			String fh = "G:/beta/aa/bb/cc";
			isFolder(fh, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
