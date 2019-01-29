package com.jtsec.common.util.surpass;

import com.alibaba.fastjson.JSON;
import com.jtsec.common.Constants.SystemCons;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * java执行linux命令行工具类
 * @author dingzhichao
 */
@Slf4j
public class LinuxCmdUtil {
	/**
	 * 执行命令不返回任何参数
	 * @param cmd
	 */
	public static void execCmdsFinal(Object cmd){
		showMsg("execCmdsFinal", cmd);
		try{
			if(cmd instanceof String){
				Runtime.getRuntime().exec((String)cmd);
			}else if(cmd instanceof String[]){
				Runtime.getRuntime().exec((String[])cmd);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行cmd命令，不抛出异常
	 * @param cmd
	 * @return
	 */
	public static BufferedReader execCmdFinal(Object cmd){
		BufferedReader br = null;
		if(SystemCons.OS_IS_LINUX){
			InputStream is = null;
			try {
				if( (is = execCmdReturnInputStream(cmd)) != null){
					br = new BufferedReader(new InputStreamReader(is));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return br;
	}

	/**
	 * 在Linux/Windows系统下执行cmd命令，将命令的执行结果封装到List<String>集合中
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	public static List<String> exec(Object cmd) throws Exception{
		List<String> list = null;
		BufferedReader br = null;
		InputStream is = null;
		try{
			if( (is = execRtnIs(cmd)) != null){
				br = new BufferedReader(new InputStreamReader(is));
			}
			list = getCmdResult(br);			
		} finally{
			is.close();
		}
		return list;
	}		
	
	/**
	 * 在操作系统是linux的条件执行cmd命令，将命令的执行结果封装到List<String>集合中
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	public static List<String> execCmd(Object cmd) throws Exception{
		BufferedReader br = null;
		if(SystemCons.OS_IS_LINUX){
			InputStream is = null;
			if( (is = execCmdReturnInputStream(cmd)) != null){
				br = new BufferedReader(new InputStreamReader(is));
			}
		}
		return getCmdResult(br);
	}
	
	/**
	 * linux下执行cmd命令，如果是windows那么从指定的文件路径中读取配置信息
	 * @param cmd
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<String> execCmd(Object cmd, String filePath) throws Exception{
		BufferedReader br = null;
		if(SystemCons.OS_IS_LINUX){
			InputStream is = null;
			if( (is = execCmdReturnInputStream(cmd)) != null){
				br = new BufferedReader(new InputStreamReader(is));
			}
		}else{
			if(filePath != null){
				br = new BufferedReader(new FileReader(filePath));
			}
		}
		return getCmdResult(br);
	}
	
	
	/**
	 * linux下执行cmd命令，如果是windows那么从指定的文件路径中读取配置信息
	 * @param cmd
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static List<String> execCmdOrReadFile(Object cmd) throws Exception{
		BufferedReader br = null;
		if(SystemCons.OS_IS_LINUX){
			InputStream is = null;
			if( (is = execCmdReturnInputStream(cmd)) != null){
				br = new BufferedReader(new InputStreamReader(is));
			}
		}else{
			showMsg("execCmdOrReadFile", cmd);
			if(cmd instanceof String[]){
				String[] tmp = (String[])cmd;
				br = new BufferedReader(new FileReader(tmp[2]));
			}else{
				br = new BufferedReader(new FileReader((String)cmd));
			}
		}
		return getCmdResult(br);
	}
	
	
	
	/**
	 * linux下执行cmd命令，如果是windows那么将content添加到返回的集合中
	 * @param cmd
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static List<String> execCmdContent(Object cmd, String... content) throws Exception{
		BufferedReader br = null;
		if(SystemCons.OS_IS_LINUX){
			InputStream is = null;
			if( (is = execCmdReturnInputStream(cmd)) != null){
				br = new BufferedReader(new InputStreamReader(is));
			}
		}
		return getCmdResult(br, content);
	}
	
	/**
	 * 在操作系统是linux的条件执行cmd命令,返回命令执行的结果流对象
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	public static BufferedReader execCmdOverride(Object cmd) throws Exception{
		BufferedReader br = null;
		if(SystemCons.OS_IS_LINUX){
			InputStream is = null;
			if( (is = execCmdReturnInputStream(cmd)) != null){
				br = new BufferedReader(new InputStreamReader(is));
			}
		}
		return br;
	}
	
	/**
	 * 在操作系统是linux的条件执行cmd命令,如果是windows操作系统，那么返回指定文件的操作流
	 * @param cmd
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static BufferedReader execCmdOverride(Object cmd, String filePath) throws Exception{
		BufferedReader br = null;
		if(SystemCons.OS_IS_LINUX){
			InputStream is = null;
			if( (is = execCmdReturnInputStream(cmd)) != null){
				br = new BufferedReader(new InputStreamReader(is));
			}
		}else{
			showMsg("execCmdOverride", cmd);
			if(filePath != null){
				br = new BufferedReader(new FileReader(filePath));
			}
		}
		return br;
	}

	/**
	 * 执行cmd命令，并返回执行结果的正确输入流或错误输入流
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	public static InputStream execRtnIs(Object cmd) throws Exception{
		showMsg("execRtnIs", cmd);
		InputStream is = null;
		InputStream err = null;
		Process proc = null;
		if(cmd instanceof String){
			proc = Runtime.getRuntime().exec((String)cmd);
		}else if(cmd instanceof String[]){
			proc = Runtime.getRuntime().exec((String[])cmd);
		}
		//判断是否有命令执行的错误提示
		err = proc.getErrorStream();
		//如果没有错误提示信息，表示命令执行成功
		if (err == null || (err != null && err.read() < 0) ){
			is = proc.getInputStream();
		}else{
			is = err;
			log.error("执行命令失败");
			// 若需要将错误输入流返回，则不可对其进行读取和打印，否则流中信息将被取空
			//printCmdErrorMsg(err);
		}
		return is;
	}	
	
	/**
	 * 如果是linux系统，执行cmd命令，并返回执行结果的正确输入流或错误输入流
	 * @param cmd
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static InputStream execCmdReturnInputStream(Object cmd) throws Exception{
		showMsg("execCmdReturnInputStream", cmd);
		InputStream is = null;
		InputStream err = null;
		Process proc = null;
		if(SystemCons.OS_IS_LINUX){
			if(cmd instanceof String){
				proc = Runtime.getRuntime().exec((String)cmd);
			}else if(cmd instanceof String[]){
				proc = Runtime.getRuntime().exec((String[])cmd);
			}
			//判断是否有命令执行的错误提示
			err = proc.getErrorStream();
			//如果没有错误提示信息，表示命令执行成功
			if (err == null || (err != null && err.read() < 0) ){
				is = proc.getInputStream();
			}else{
				is = err;
				log.error("执行命令失败");
				// 若需要将错误输入流返回，则不可对其进行读取和打印，否则流中信息将被取空
				//printCmdErrorMsg(err);
			}
		}
		return is;
	}

	/**
	 * 如果是linux系统，执行cmd命令，并返回执行结果的正确输入流或错误输入流</br>
	 * 如果是windows操作系统，那么返回指定文件的操作流
	 * 
	 * @param cmd
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static InputStream execCmdReturnInputStream(Object cmd, String filePath) throws Exception{
		showMsg("execCmdReturnInputStream", cmd);
		InputStream is = null;
		InputStream err = null;
		Process proc = null;
		if(SystemCons.OS_IS_LINUX){
			if(cmd instanceof String){
				proc = Runtime.getRuntime().exec((String)cmd);
			}else if(cmd instanceof String[]){
				proc = Runtime.getRuntime().exec((String[])cmd);
			}
			//判断是否有命令执行的错误提示
			err = proc.getErrorStream();
			//如果没有错误提示信息，表示命令执行成功
			if (err == null || (err != null && err.read() < 0) ){
				is = proc.getInputStream();
			}else{
				is = err;
				log.error("执行命令失败");
				// 若需要将错误输入流返回，则不可对其进行读取和打印，否则流中信息将被取空
				//printCmdErrorMsg(err);
			}
		}else{
			is = new FileInputStream(filePath);
		}
		return is;
	}

	/**
	 * 将cmd命令执行的错误信息打印到控制台中
	 * @param br
	 * @throws Exception
	 */
	protected static void printCmdErrorMsg(BufferedReader br) throws Exception{
		String line = null;
		while((line = br.readLine()) != null){
			//这里通过log4j打印信息
			log.debug(line);
		}
		line = null;
	}
	
	/**
	 * 将cmd命令执行的错误信息打印到控制台中
	 * @param
	 * @throws Exception
	 */
	private static void printCmdErrorMsg(InputStream is) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while((line = br.readLine()) != null){
			//这里通过log4j打印信息
			//System.out.println(line);
			log.debug(line);
		}
		line = null;
	}
	
	/**
	 * 获得linux执行cmd命令的返回结果
	 * @param br
	 * @return
	 * @throws Exception
	 */
	private static List<String> getCmdResult(BufferedReader br) throws Exception{
		List<String> list = new ArrayList<String>();
		if(br != null){
			String line = null;
			while((line = br.readLine()) != null){
				list.add(line);
			}
			line = null;
			br.close();
		}
		return list;
	}
	
	/**
	 * 获得linux执行cmd命令的返回结果,如果没有返回结果，那么将content的内容添加到list集合中
	 * @param br
	 * @return
	 * @throws Exception
	 */
	private static List<String> getCmdResult(BufferedReader br, String... content) throws Exception{
		List<String> list = new ArrayList<String>();
		if(br != null){
			String line = null;
			while((line = br.readLine()) != null){
				list.add(line);
			}
			line = null;
		}else{
			if(NotNullUtil.objectArrayNotNull(content)){
				for(String tmp : content){
					list.add(tmp);
				}
			}
		}
		return list;
	}
	
	/**
	 * 通过返回值判断cmd命令执行是否成功
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	public static int execCmdStatus(Object cmd) throws Exception{
		showMsg("execCmdStatus", cmd);
		int flag = 1;
		if(SystemCons.OS_IS_LINUX){
//			flag = Runtime.getRuntime().exec((String) cmd).waitFor();
			if(cmd instanceof String){
				flag = Runtime.getRuntime().exec((String)cmd).waitFor();
			}else if(cmd instanceof String[]){
				flag = Runtime.getRuntime().exec((String[])cmd).waitFor();
			}
		}
		showMsg("execCmdStatus", cmd, flag + "");
		return flag;
	}
	
	/**
     * linux执行linux，window执行winCmd命令
     * @param linuxCmd
     * @param winCmd
     * @return
     * @throws Exception
     */
    public static BufferedReader mustExec(Object linuxCmd, Object winCmd) throws Exception{
        BufferedReader br = null;
        Object cmd = "";
        if(SystemCons.OS_IS_LINUX){
            cmd = linuxCmd;
        }else{
            cmd = winCmd;
        }
        InputStream is = null;
        
        InputStream err = null;
        Process proc = null;
        if(cmd instanceof String){
            proc = Runtime.getRuntime().exec((String)cmd);
        }else if(cmd instanceof String[]){
            proc = Runtime.getRuntime().exec((String[])cmd);
        }
        //判断是否有命令执行的错误提示
        err = proc.getErrorStream();
        //如果没有错误提示信息，表示命令执行成功
        if (err != null && err.read() < 0){
            is = proc.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
        }else{
            br = new BufferedReader(new InputStreamReader(err));
        }
        return br;
    }
	
	public static void showMsg(String methodName, Object cmd, String ... result){
		String msg = methodName + " 执行的cmd命令 ：" + JSON.toJSONString(cmd);
		if(NotNullUtil.stringNotNull(result)){
			msg = msg + " " + ((result.equals("1") || result.equals("true")) ? "成功。" : "失败！");
		}
		log.debug(msg);
	}
	
	/**
     * 在操作系统是linux的条件执行cmd命令，将命令的执行结果封装到List<String>集合中
     * 如果cmd执行异常，那么将异常结果封装到list中
     * @param cmd
     * @return
     * @throws Exception
     */
    public static List<String> execCmdForReport(Object cmd) throws Exception{
        showMsg("execCmdStatus", cmd);
        BufferedReader br = null;
        if(SystemCons.OS_IS_LINUX){
            InputStream is = null;
            if( (is = execCmdReturnInputStream(cmd, "1")) != null){
                br = new BufferedReader(new InputStreamReader(is));
            }
        }
        return getCmdResult(br);
    }
}
