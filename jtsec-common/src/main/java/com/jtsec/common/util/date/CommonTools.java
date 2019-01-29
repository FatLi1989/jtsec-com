package com.jtsec.common.util.date;

import com.jtsec.common.Constants.Constants;
import com.jtsec.common.util.datahandle.DataConvert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 用于判断分钟、小时、天格式 ：传入时间为当前系统时间
 * 分钟：2015-10-16 12:26:00
 * 小时：2015-10-16 12:00:00
 * 天：   2015-10-16 00:00:00
 *
 * @author zhanghx
 */
public class CommonTools {

	public static String getNowTime (String type) {
		Date dt = new Date ();//如果不需要格式,可直接用dt,dt就是当前系统时间
		String beforeTime = CommonTools.getTime (dt, type, 1);
		if (beforeTime == null || beforeTime.equals ("")) {
			beforeTime = "";
		}
		return beforeTime;
	}

	//一天内每小时0-23的参照数组
	private static List<String> HOURLIST = new ArrayList<String> ();

	public static List<String> hourList() {
		if (HOURLIST.isEmpty ()) {
			for (int i = 1; i < 25; i++) {
				String str = i + "";
				if (i < 10) {
					str = "0" + str;
				}
				HOURLIST.add (str);
			}
		}
		return HOURLIST;
	}

	/**
	 * 按需求，返回指定类型的时间格式         同抽取和加载日志表中时间格式一致
	 * 返回时间的显示格式为：yyyy-MM-dd HH:mm:ss
	 *
	 * @param type 返回时间格式类型
	 *             分钟：Constants.WHOLE_MIN_TYPE，如 2015-10-15 15:27:00
	 *             小时：Constants.WHOLE_HOUR_TYPE，如 2015-10-15 15:00:00
	 *             天：Constants.WHOLE_DAY_TYPE，如 2015-10-15 00:00:00
	 *             若不是指定中的任何一种，则原形式返回
	 * @return
	 */
	public static String getTime (Date dt, String type, int offset) {
		DateFormat df = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");//设置显示格式
		Calendar calendar = Calendar.getInstance ();
		String beforeTime = "";
		if (Constants.WHOLE_SECOND_TYPE.equals (type)) {
			calendar.set (Calendar.SECOND, calendar.get (Calendar.SECOND));
			beforeTime = df.format (calendar.getTime ()).substring (0, 19);
			return beforeTime;
		}
		//时间类型为分钟
		if (Constants.TIME_TYPE_MIN.equals (type)) {
			//2015-10-15 15:27:00  将当前时间向前推一分钟
			calendar.set (Calendar.MINUTE, calendar.get (Calendar.MINUTE));
			beforeTime = df.format (calendar.getTime ()).substring (0, 16) + ":00";
			return beforeTime;
		}
		//时间类型为小时
		if (Constants.TIME_TYPE_HOUY.equals (type)) {
			//2015-10-15 15:00:00  将当前时间向前推一小时
			calendar.set (Calendar.HOUR_OF_DAY, calendar.get (Calendar.HOUR_OF_DAY));

			beforeTime = df.format (calendar.getTime ()).substring (0, 14) + "00:00";
			return beforeTime;
		}
		//时间类型为天
		if (Constants.TIME_TYPE_DAY.equals (type)) {
			//2015-10-15 00:00:00 将当前时间向前推一天
			calendar.set (Calendar.DAY_OF_MONTH, calendar.get (Calendar.DAY_OF_MONTH));
			beforeTime = df.format (calendar.getTime ()).substring (0, 11) + "00:00:00";
			return beforeTime;
		}
		//时间类型为月
		if (Constants.WHOLE_MONTH_TYPE.equals (type)) {
			//2015-10-01 00:00:00 将当前时间向前推一月
			calendar.add (Calendar.MONTH, -offset);
			beforeTime = df.format (calendar.getTime ()).substring (0, 7) + "-01 00:00:00";
			return beforeTime;
		}
		//时间类型为年
		if (Constants.WHOLE_YEAR_TYPE.equals (type)) {
			//2015-01-01 00:00:00 将当前时间向前推一年
			calendar.add (Calendar.YEAR, -offset);
			beforeTime = df.format (calendar.getTime ()).substring (0, 4) + "-01-01 00:00:00";
			return beforeTime;
		}
		//时间类型为季度
		if (Constants.WHOLE_QUARTER_TYPE.equals (type)) {
			beforeTime = getFirstDayOfQuarter (calendar.getTime ());
			return beforeTime;
		}
		beforeTime = df.format (dt);//用DateFormat的format()方法在dt中获取并以yyyy-MM-dd HH:mm:ss格式显示
		return beforeTime;
	}

	public static String everySecond (String second, int offset) {
		Date date = new Date ();
		//注意format的格式要与日期String的格式相匹配
		DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse (second);
			long time = date.getTime () + offset * 1000;
			second = sdf.format (time);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return second;
	}

	/**
	 * 将当前时间，在本分钟内，向前或向后偏移offset秒
	 * 只支持对格式为yyyy-MM-dd HH:mm:ss的时间的偏移处理
	 * 例，2015-10-15 15:27:03，后移一秒为2015-10-15 15:27:02，前移一分钟为2015-10-15 15:27:04
	 * 当原有时间，向前向后偏移offset秒后，得到的分钟时间最小为0，最大为59
	 *
	 * @param min    需要偏移的时间
	 * @param offset 偏移数值：1：向后偏移一位，-1：向前偏移一位
	 * @return
	 */
	public static String offsetSecond (String min, int offset) {
		Date date = new Date ();
		//注意format的格式要与日期String的格式相匹配
		DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse (min);
			long time = date.getTime () + offset * 1000;
			min = sdf.format (time);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return min;
	}

	/**
	 * 将当前时间，在本小时内，向前或向后偏移offset分钟
	 * 只支持对格式为yyyy-MM-dd HH:mm:ss的时间的偏移处理
	 * 例，2015-10-15 15:27:03，后移一分钟为2015-10-15 15:28:03，前移一分钟为2015-10-15 15:26:03
	 * 当原有时间，向前向后偏移offset分钟后，得到的分钟时间最小为0，最大为59
	 *
	 * @param min    需要偏移的时间
	 * @param offset 偏移数值：1：向后偏移一位，-1：向前偏移一位
	 * @return
	 */
	public static String offsetMin (String min, int offset) {
		Date date = new Date ();
		//注意format的格式要与日期String的格式相匹配
		DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse (min);
			long time = date.getTime () + offset * 60 * 1000;
			min = sdf.format (time);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return min;
	}

	/**
	 * 将当前时间，在本天内，向前或向后偏移offset小时
	 * 只支持时间格式为yyyy-MM-dd HH:mm:ss的时间偏移处理
	 * 例，2015-10-15 15:00:00，后移一小时为2015-10-15 16:00:00，前移一小时为2015-10-15 14:00:00
	 * 当原有时间，向前向后偏移offset小时后，得到的小时时间最小为0，最大为59
	 *
	 * @param hour   需要偏移的时间
	 * @param offset 偏移数值：1：向后偏移一位，-1：向前偏移一位
	 * @return
	 */
	public static String offsetHour (String hour, int offset) {
		Date date = new Date ();
		DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

		try {
			date = sdf.parse (hour);
			long time = date.getTime () - offset * 60 * 60 * 1000;
			hour = DataConvert.HOUR.format (time);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return hour;
	}

	/**
	 * 将当前时间，向前或向后偏移offset天
	 * 只支持时间格式为yyyy-MM-dd HH:mm:ss的时间偏移处理
	 * 例，2015-10-15 00:00:00，后移一天为2015-10-16 00:00:00，前移一天为2015-10-14 00:00:00
	 *
	 * @param day    需要偏移的时间
	 * @param offset 偏移数值：1：向后偏移一位，-1：向前偏移一位
	 * @return
	 */
	public static String offsetDay (String day, int offset) {
		Date date = new Date ();
		DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

		try {
			date = sdf.parse (day);
			long time = date.getTime () - offset * 24 * 60 * 60 * 1000;
			day = DataConvert.DAY.format (time);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return day;
	}

	/**
	 * 将当前时间，向前或向后偏移offset月
	 * 只支持时间格式为yyyy-MM-dd HH:mm:ss的时间偏移处理
	 * 例：2015-01-01 00:00:00，后移一月为2014-12-01 00:00:00，前移一月为2015-02-01 00:00:00
	 *
	 * @param month  需要偏移的时间
	 * @param offset 偏移数值：1：向前偏移，-1：向后偏移
	 * @return
	 */
	public static String offsetMonth (String month, int offset) {
		DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance ();
		try {
			calendar.setTime (sdf.parse (month));
			calendar.add (Calendar.MONTH, offset);
			month = sdf.format (calendar.getTime ());
		} catch (Exception e) {
		}
		return month;
	}

	/**
	 * 将当前时间向前后向后偏移offset年
	 * 只支持时间格式为yyyy-MM-dd HH:mm:ss的时间偏移处理
	 *
	 * @param year   需要偏移的时间
	 * @param offset 偏移数值：1：向前偏移，-1：向后偏移
	 * @return
	 */
	public static String offsetYear (String year, int offset) {
		DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance ();
		try {
			calendar.setTime (sdf.parse (year));
			calendar.add (Calendar.YEAR, offset);
			year = sdf.format (calendar.getTime ());
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return year;
	}

	/**
	 * 得到本季度第一天的日期
	 *
	 * @param date
	 * @return
	 */
	public static String getFirstDayOfQuarter (Date date) {
		DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Calendar cDay = Calendar.getInstance ();
		cDay.setTime (date);
		int curMonth = cDay.get (Calendar.MONTH);
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) { //一月到三月
			cDay.set (Calendar.MONTH, Calendar.JANUARY);
		}
		if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {//四月到六月
			cDay.set (Calendar.MONTH, Calendar.APRIL);
		}
//        if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {//  Calendar.AUGUST八月
		if (curMonth >= Calendar.JULY && curMonth <= Calendar.SEPTEMBER) { //七月到九月
			cDay.set (Calendar.MONTH, Calendar.JULY);
		}
		if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) { //十月到十二月
			cDay.set (Calendar.MONTH, Calendar.OCTOBER);
		}
		cDay.set (Calendar.DAY_OF_MONTH, cDay.getActualMinimum (Calendar.DAY_OF_MONTH));
		return sdf.format (cDay.getTime ()).substring (0, 10) + " 00:00:00";
	}

	/**
	 * 得到本季度最后一天的日期
	 *
	 * @param date
	 * @return
	 */
	public static String getLastDayOfQuarter (Date date) {
		DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Calendar cDay = Calendar.getInstance ();
		cDay.setTime (date);
		int curMonth = cDay.get (Calendar.MONTH);
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {//一月到三月
			cDay.set (Calendar.MONTH, Calendar.MARCH);
		}
		if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {//四月到六月
			cDay.set (Calendar.MONTH, Calendar.JUNE);
		}
		if (curMonth >= Calendar.JULY && curMonth <= Calendar.SEPTEMBER) {//七月到九月
			cDay.set (Calendar.MONTH, Calendar.AUGUST);
		}
		if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) { //十月到十二月
			cDay.set (Calendar.MONTH, Calendar.DECEMBER);
		}
		cDay.set (Calendar.DAY_OF_MONTH, cDay.getActualMaximum (Calendar.DAY_OF_MONTH));
		return sdf.format (cDay.getTime ()).substring (0, 10) + " 23:59:59";
	}


	public static void main (String[] args) {

//		String date = getFirstDayOfQuarter(new Date());
//		
//		System.out.println(date);

		//str = CommonTools.getNowTime(Constants.WHOLE_YEAR_TYPE);
		System.out.println (minList());
	}

	private static List<String> MINLIST = new ArrayList<String> ();

	//一小时内每分钟0-59的参照数组
	public static List<String> minList () {
		if (MINLIST.isEmpty ()) {
			for (int i = 0; i < 60; i++) {
				String str = i + "";
				if (i < 10) {
					str = "0" + i;
				}
				MINLIST.add (str);
			}
		}
		return MINLIST;
	}

	public static String genDelayOneDay (String day) {
		Date date = new Date ();
		DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

		try {
			date = sdf.parse (day);
			long time = date.getTime () +  1*24 * 60 * 60 * 1000;
			day = DataConvert.DAY.format (time);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return day;
	}


}
