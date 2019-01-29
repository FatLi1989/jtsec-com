package com.jtsec.common.util.surpass;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;


/**
 * 判断集合对象或是字符串是不是空值
 * @author surpassE
 * 
 */
public class NotNullUtil {

	/**
	 * 判断指定的字符串是不是null或是""
	 * @param msg
	 * @return
	 */
	public static boolean stringNotNull(String msg){
		if(msg != null && msg.trim().length() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断指定的字符串数组中所有元素都不是null或是""
	 * @param msg
	 * @return
	 */
	public static boolean stringNotNull(String... params){
		if(params != null && params.length > 0){
			for (int i = 0; i < params.length; i++) {
				if(params[i] == null || params[i].trim().length() == 0){
					return false;
				}
			}
		}else{
			return false;
		}
		return true;
	}
	
	/**
	 * 判断集合是不是Null或集合的长度为0
	 * @param c
	 * @return
	 */
	public static boolean collectionNotNull(Collection<?> c){
		if(c != null && c.size() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断map集合是不是空对象
	 * @param map
	 * @return
	 */
	public static boolean mapNotNull(Map<?, ?> map){
		if(map != null && map.size() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断对象串数组是不是空对象或是长度为0
	 * @param arr
	 * @return
	 */
	public static boolean objectArrayNotNull(Object[] arr){
		if(arr != null && arr.length > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断对象是不是空对象、同时判断对象的中的属性是不是空(String),是不是大于0(Integer)
	 * @param obj
	 * @param params
	 * @return
	 */
	public static boolean objectNotNull(Object obj, String... params){
		if(obj != null){
			boolean flag = false;
			Class<?> clazz = obj.getClass();
			if(objectArrayNotNull(params)){
				for(String key : params){
					try {
						String head = key.substring(0,1);
						//将首字符转为大写
						String upperCaseHead = head.toUpperCase();
						String methodName = "get" + key.replaceFirst(head, upperCaseHead);
						Method method = clazz.getDeclaredMethod(methodName);
						Class<?> typeClazz = method.getReturnType();
						Object value = method.invoke(obj);
						if(typeClazz.toString().equals(Integer.class.toString())){
							Integer tmp = (Integer)value;
							if(tmp > 0){
								flag = true;
							}else{
								flag = false;
							}
						}else if(typeClazz.toString().equals(String.class.toString())){
							String tmp = (String)value;
							if(stringNotNull(tmp)){
								flag = true;
							}else{
								flag = false;
							}
						}else{
							if(value != null){
								flag = true;
							}else{
								flag = false;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return flag;
		}
		return false;
	}
	
}
