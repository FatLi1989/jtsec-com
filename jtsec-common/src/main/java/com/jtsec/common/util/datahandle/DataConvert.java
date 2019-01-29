package com.jtsec.common.util.datahandle;

import com.jtsec.common.exception.JtsecException;
import com.jtsec.common.util.date.CommonTools;
import com.jtsec.common.util.enums.ExceptionEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author NovLi
 * @Description: 数据转换工具类
 * @date 2018/6/27 16:13
 */
@Data
@Slf4j
public class DataConvert {

	public static SimpleDateFormat MIN = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

	public static SimpleDateFormat HOUR = new SimpleDateFormat ("yyyy-MM-dd HH:00:00");

	public static SimpleDateFormat DAY = new SimpleDateFormat ("yyyy-MM-dd 00:00:00");


	/**
	 * @param data
	 * @return Map
	 * @throws
	 * @Description: 实体类转换成map
	 * @author NovLi
	 * @date 2018/6/27 16:14
	 */
	public static Map<String, Object> convertMap (Object data)
			throws IntrospectionException, InvocationTargetException, IllegalAccessException {
		Map<String, Object> returnMap = new HashMap<String, Object> ();

		if (data != null) {
			Class type = data.getClass ();

			BeanInfo beanInfo = Introspector.getBeanInfo (type);

			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors ();

			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName ();
				if (!propertyName.equals ("class")) {
					Method readMethod = descriptor.getReadMethod ();
					Object result = readMethod.invoke (data, new Object[0]);
					if (result != null) {
						returnMap.put (propertyName, result);
					} else {
						returnMap.put (propertyName, "");
					}
				}
			}
		}
		return returnMap;
	}

	/**
	 * @Description: 处理转换异常
	 * @author NovLi
	 * @date 2018/8/13 17:24
	 */
	public static Map<String, Object> ConvertDataToMapWithoutException (Object data) {
		Map<String, Object> map = new HashMap<> ();
		try {
			map = DataConvert.convertMap (data);
		} catch (Exception e) {
			log.error (DataConvert.class + ":" + data.toString () + "转换异常", e.getMessage ());
			throw new JtsecException (ExceptionEnum.SystemError.getCode ());
		}
		return map;
	}

	/**
	 * @Description: list集合间相互转换
	 * @author NovLi
	 * @date 2018/8/15 9:21
	 */
	public static List ConvertListToList (List list, Object data) {
		List<Object> objectList = new ArrayList<> ();
		for (Object object : list) {
			try {
				BeanUtils.copyProperties (data, object);
				objectList.add (data);
			} catch (IllegalAccessException e) {
				log.error (DataConvert.class + ":" + data.toString () + "转换异常", e.getMessage ());
				throw new JtsecException (ExceptionEnum.SystemError.getCode ());
			} catch (InvocationTargetException e) {
				log.error (DataConvert.class + ":" + data.toString () + "转换异常", e.getMessage ());
				throw new JtsecException (ExceptionEnum.SystemError.getCode ());
			}
		}
		return objectList;
	}

	/**
	 * @Description: 实体类之间转换
	 * @author NovLi
	 * @date 2018/8/21 10:20
	 */
	public static Object convertVoToEntity (Object obj, Object vo) {
		{
			try {
				BeanUtils.copyProperties (obj, vo);
			} catch (IllegalAccessException e) {
				log.error (DataConvert.class + ":" + vo.toString () + "转换异常", e.getMessage ());
				throw new JtsecException (ExceptionEnum.DataConvertError.getCode ());
			} catch (InvocationTargetException e) {
				log.error (DataConvert.class + ":" + vo.toString () + "转换异常", e.getMessage ());

				throw new JtsecException (ExceptionEnum.DataConvertError.getCode ());
			}
			return obj;
		}
	}
	/**
	 * @Description: 根据开始时间获取推后一时段时间
	 * @param  timeType  事件类型
     * @param  startTime 开始时间
	 * @return
	 * @author NovLi
	 * @date 2018/9/17 15:51 
	 */
	public static String genEndTime (String timeType, String  startTime) {
		String endTime = null;
		if ("min".equals (timeType)) {
			endTime = CommonTools.offsetMin (startTime, 1);
		} else if ("hour".equals (timeType)) {
			endTime = CommonTools.offsetHour (startTime, 1);
		} else if ("day".equals (timeType)) {
			endTime = CommonTools.offsetDay (startTime, 1);
		}
		return endTime;
	}

	public static String genStartTime (String timeType, String countTime) {
		String startTime = null;
		if ("min".equals (timeType)) {
			startTime = DataConvert.MIN.format (countTime);
		} else if ("hour".equals (timeType)) {
			startTime = DataConvert.HOUR.format (countTime);
		} else if ("day".equals (timeType)) {
			startTime = DataConvert.DAY.format (countTime);
		}
		return startTime;
	}
}
