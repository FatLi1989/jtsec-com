package com.jtsec.common.util.surpass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日志工具类
 * @author lrq
 */
public final class DateUtil {
	/** 采用Singleton设计模式而具有的唯一实例 */
	private static DateUtil instance = new DateUtil();

	/** 格式化器存储器 */
	private static Map<String, SimpleDateFormat> formats;

	private DateUtil () {
		resetFormats();
	}

	/**
	 * 通过缺省日期格式得到的工具类实例
	 * 
	 * @return <code>DateUtilities</code>
	 */
	public static DateUtil getInstance() {
		return instance;
	}

	/** Reset the supported formats to the default set. */
	public void resetFormats() {
		formats = new HashMap<String, SimpleDateFormat>();

		// alternative formats
		formats.put("yyyy-MM-dd HH:mm:ss", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		// alternative formats
		formats.put("yyyy-MM-dd", new SimpleDateFormat("yyyy-MM-dd"));

		// XPDL examples format
		formats.put("MM/dd/yyyy HH:mm:ss a", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a"));

		// alternative formats
		formats.put("yyyy-MM-dd HH:mm:ss a", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a"));

		// ISO formats
		formats.put("yyyy-MM-dd'T'HH:mm:ss'Z'", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
		formats.put("yyyy-MM-dd'T'HH:mm:ssZ", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
		formats.put("yyyy-MM-dd'T'HH:mm:ssz", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz"));
		
		formats.put("yyyyMMddHHmmssSSS", new SimpleDateFormat("yyyyMMddHHmmssSSS"));

	}

	/**
	 * 对日期进行格式化 格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            需格式化的日期
	 * @return 格式化后的字符串
	 */
	public String format(Date date) {
		Object obj = formats.get("yyyy-MM-dd HH:mm:ss");
		if (obj == null) {
			obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		return ((DateFormat) obj).format(date);
	}

	/**
	 * 解析字符串到日期型
	 * 
	 * @param dateString
	 *            日期字符串
	 * @return 返回日期型对象
	 */
	public static Date parse(String dateString) {
		Iterator<SimpleDateFormat> iter = formats.values().iterator();
		while (iter.hasNext()) {
			try {
				return ((DateFormat) iter.next()).parse(dateString);
			} catch (ParseException e) {
				// do nothing
			}
		}
		return null;
	}

	public static final long SECOND = 1000;

	public static final long MINUTE = SECOND * 60;

	public static final long HOUR = MINUTE * 60;

	public static final long DAY = HOUR * 24;

	public static final long WEEK = DAY * 7;

	public static final long YEAR = DAY * 365;

	private static Log log = LogFactory.getLog(DateUtil.class);

	private static DateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 解析日期
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static Date parse(String date, String pattern) {
		Date resultDate = null;
		try {
			resultDate = new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			log.error(e);
		}
		return resultDate;
	}

	/**
	 * 解析日期 yyyy-MM-dd
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static Date parseSimple(String date) {
		Date resultDate = null;
		try {
			resultDate = YYYY_MM_DD.parse(date);
		} catch (ParseException e) {
			log.error(e);
		}
		return resultDate;
	}

	/**
	 * 格式化日期字符串
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static String format(Date date, String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	/**
	 * 对日期进行格式化 格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time 需格式化的时间
	 * @return 格式化后的字符串
	 */
	public static String format(long time) {
		SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return ((DateFormat) obj).format(new Date(time));
	}
	
	/**
	 * 将时间格式化为执行的样式
	 * 
	 * @param time 需格式化的时间
	 * @param pattern 需格式化的样式
	 * @return 格式化后的字符串
	 */
	public static String format(long time, String pattern) {
		SimpleDateFormat obj = DateUtil.getInstance().formats.get(pattern);
		if(obj == null){
			obj = new SimpleDateFormat(pattern);
		}
		return ((DateFormat) obj).format(new Date(time));
	}	
	
	/**
	 * 取得当前日期
	 * 
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @param offsetYear
	 * @return 当前时间 + offsetYear
	 */
	public static Timestamp getTimestampExpiredYear(int offsetYear) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, offsetYear);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offsetMonth
	 * @return 当前时间 + offsetMonth
	 */
	public static Timestamp getCurrentTimestampExpiredMonth(int offsetMonth) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, offsetMonth);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offsetDay
	 * @return 当前时间 + offsetDay
	 */
	public static Timestamp getCurrentTimestampExpiredDay(int offsetDay) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, offsetDay);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offsetSecond
	 * @return 当前时间 + offsetSecond
	 */
	public static Timestamp getCurrentTimestampExpiredSecond(int offsetSecond) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.SECOND, offsetSecond);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offsetDay
	 * @return 指定时间 + offsetDay
	 */
	public static Timestamp getTimestampExpiredDay(Date givenDate, int offsetDay) {
		Calendar date = Calendar.getInstance();
		date.setTime(givenDate);
		date.add(Calendar.DATE, offsetDay);
		return new Timestamp(date.getTime().getTime());
	}

	/**
	 * 实现ORACLE中ADD_MONTHS函数功能
	 * 
	 * @param offsetMonth
	 * @return 指定时间 + offsetMonth
	 */
	public static Timestamp getTimestampExpiredMonth(Date givenDate, int offsetMonth) {
		Calendar date = Calendar.getInstance();
		date.setTime(givenDate);
		date.add(Calendar.MONTH, offsetMonth);
		return new Timestamp(date.getTime().getTime());
	}

	/**
	 * @param offsetSecond
	 * @return 指定时间 + offsetSecond
	 */
	public static Timestamp getTimestampExpiredSecond(Date givenDate, int offsetSecond) {
		Calendar date = Calendar.getInstance();
		date.setTime(givenDate);
		date.add(Calendar.SECOND, offsetSecond);
		return new Timestamp(date.getTime().getTime());
	}

	/**
	 * 根据指定的时间和偏移的小时数，对时间进行偏移计算
	 * 
	 * @param givenDate		指定的时间
	 * @param offsetHour	偏移小时数
	 * @return 指定时间 + offsetHour
	 */
	public static Timestamp getTimestampExpiredHour(Date givenDate, int offsetHour) {
		Calendar date = Calendar.getInstance();
		// 设定日历时间
		date.setTime(givenDate);
		// 进行时间量计算，时间量的单位为小时
		date.add(Calendar.HOUR, offsetHour);
		Timestamp timestamp = new Timestamp(date.getTime().getTime());
		return timestamp;
	}

	/**
	 * @return 当前日期 yyyy-MM-dd
	 */
	public static String getCurrentDay() {
		return DateUtil.format(new Date(), "yyyy-MM-dd");
	}

	/**
	 * @return 当前的时间戳 yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowTime(String... regex) {
		if(NotNullUtil.objectArrayNotNull(regex)){
			return DateUtil.format(new Date(), regex[0]);
		}
		return DateUtil.getInstance().format(new Date());
	}
	
	public static String getNowTime(Date date){
		return DateUtil.getInstance().format(new Date());
	}

	/**
	 * @return 给出指定日期的月份的第一天
	 */
	public static Date getMonthFirstDay(Date givenDate) {
		Date date = DateUtil.parse(DateUtil.format(givenDate, "yyyy-MM"), "yyyy-MM");
		return date;
	}

	/**
	 * @return 给出指定日期的月份的最后一天
	 */
	public static Date getMonthLastDay(Date givenDate) {
		Date firstDay = getMonthFirstDay(givenDate);
		Date lastMonthFirstDay = DateUtil.getTimestampExpiredMonth(firstDay, 1);
		Date lastDay = DateUtil.getTimestampExpiredDay(lastMonthFirstDay, -1);
		return lastDay;
	}
	
	/**
	 * 对日期进行格式化 格式为format字符串格式
	 * @param timestamp  时间戳
	 * @param format  时间字符串格式
	 * @return 格式化后的字符串
	 * */
	public static String getDateStr(long timestamp, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return ((DateFormat) sdf).format(timestamp);
	}
	/**
	 * 计算两个时间点之间相差几分钟
	 * @param start 
	 * @param end 
	 * @return 分钟数
	 */
	public static long minOfTimeDiff(String bigTime, String limitTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期字符串格式
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse(bigTime);
			date2 = sdf.parse(limitTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}//按照格式解析字符串，返回Date对象
		
		long seconds = (date1.getTime() - date2.getTime()) / 1000;//得出两个时间的毫秒差，再除以1000得到相差秒数
		long minutes = seconds / 60;//将秒数换算成分钟数
		return minutes;
	}
	
	/**
	 * 计算两个时间点之间相差几小时
	 * @param start 
	 * @param end 
	 * @return 小时数
	 */
	public static long hourOfTimeDiff(String bigTime, String limitTime){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		Date date2 = null;
		try{
			date1 = df.parse(bigTime);
			date2 = df.parse(limitTime);
		}catch (Exception e){
				e.printStackTrace();
		}
		long diff = date1.getTime() - date2.getTime();
		long hours = diff / (1000 * 60 * 60);
		return hours;
	}
	
	/**
	 * 计算两个时间点之间相差几天
	 * @param start 
	 * @param end 
	 * @return 天数
	 */
	public static long dayOfTimeDiff(String bigTime, String limitTime){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		Date date2 = null;
		try{
			date1 = df.parse(bigTime);
			date2 = df.parse(limitTime);
		}catch (Exception e){
				e.printStackTrace();
		}
		long diff = date1.getTime() - date2.getTime();
		long days = diff / (1000 * 60 * 60 * 24);
		return days;
	}
	
	/**
	  * 将该时间向前移动一秒
	  * @param time
	  * @return
	  */
	 public static String dateMoveBefore(String time){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		 Date date = new Date();
		 Calendar c = Calendar.getInstance();
		 try {
		 	 c.setTime(sdf.parse(time));
		 } catch (ParseException e) {
			 e.printStackTrace();
		 }
		 c.add(Calendar.SECOND, -1);
		 date = c.getTime();
		 String beforeTime = sdf.format(date);
		 return beforeTime;
	 }
	 
	 /**
	  * 显示指定时间前的日期时间
	  * @return
	  */
	 public static String getDateAfter(String time, String taskTime){ 
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
         Calendar now = Calendar.getInstance();  
         try {
			now.setTime(sdf.parse(taskTime));
		 } catch (ParseException e) {
			e.printStackTrace();
		 } 
         now.set(Calendar.HOUR, now.get(Calendar.HOUR) + Integer.parseInt(time.substring(0, 2)));  
         now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + Integer.parseInt(time.substring(3, 5)));
         return sdf.format(now.getTime());  
    } 
	 
	 /**
     * 判断当前日期是星期几
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
	 public static int dayForWeek(String pTime) {
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  Calendar c = Calendar.getInstance();
		  try {
			  c.setTime(format.parse(pTime));
		  } catch (ParseException e) {
			  e.printStackTrace();
		  }
		  int dayForWeek = 0;
		  if(c.get(Calendar.DAY_OF_WEEK) == 1){
			  dayForWeek = 7;
		  }else{
			  dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		  }
		  return dayForWeek;
	 }
	 
	 /**
	  * 显示指定时间前的日期时间
	  * @return
	  */
	 public static String getDate(String time, String taskTime){ 
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
         Calendar now = Calendar.getInstance();  
         try {
			now.setTime(sdf.parse(taskTime));
		 } catch (ParseException e) {
			e.printStackTrace();
		 } 
         now.set(Calendar.HOUR, now.get(Calendar.HOUR) - Integer.parseInt(time.substring(0, 2)));  
         now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - Integer.parseInt(time.substring(3, 5)));
         return sdf.format(now.getTime());  
    } 
			 
	 /**
     * 将分钟转换为时、分
     * @param minues 小于1440分钟（即小于24小时）
     * @return
     * @throws ParseException
     */
    public static Map<String, String> getMinConver(int  minues) throws ParseException{
		Integer hours = minues/60;
		Integer mins = minues - hours*60;
		Map<String, String> convetTimeMap = new HashMap<String, String>();
		List<String> timeKey = new ArrayList<String>();
		timeKey.add("second");
		timeKey.add("min");
		timeKey.add("hour");
		String minsStr = "", hoursStr = "";
    	if(mins >= 0){
    		minsStr = Integer.toString(mins);
    	}
    	if(hours >= 0){
    		hoursStr = Integer.toString(hours);
    	}

    	if(NotNullUtil.stringNotNull(minsStr) || NotNullUtil.stringNotNull(hoursStr)){
    		for (String str : timeKey) {
    			if(str == "second"){
    				convetTimeMap.put(str, "0");
    			}
    			if(str == "min"){
    				if(NotNullUtil.stringNotNull(minsStr)){
    					convetTimeMap.put(str, minsStr);
    				}
    			}
    			if(str == "hour"){
    				if(NotNullUtil.stringNotNull(hoursStr)){
    					convetTimeMap.put(str, hoursStr);
    				}
    			}    			
    		}
    	}
    	return convetTimeMap;
    }
    
    /**
     * 根据时间 获取对应的月份 天数
     * @param time
     * @return
     */
    public static int getDaysByTime(String time){
        Date date = DateUtil.parse(getLimitTime(time), "yyyy-MM-dd hh:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        return getDaysByYearMonth(year, month);
    }
    
    /**
     * 获取一天中的最后一分钟和最后一秒的时间，如2018-05-08 23:59:59
     * 
     * @param time
     * @return
     */
    private static String getLimitTime(String time){
        if(time.endsWith("23:59:59")){
            return time;
        }
        Date date = DateUtil.parse(time, "yyyy-MM-dd hh:mm:ss");
        time = format(date, "yyyy-MM-dd") + " 23:59:59";
        return time;
    }
    
    /**
     * 根据年 月 获取对应的月份 天数
     * */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
    
    /**
     * 
     * 
     * @param time
     * @param diff 间隔时间
     * @return
     */
    public static String getDayFirstHour(String time){
        Date date = DateUtil.parse(getLimitTime(time), "yyyy-MM-dd hh:mm:ss");
        int day = getDateValue(Calendar.HOUR_OF_DAY, date);
        long t = date.getTime() - (day + 1) * HOUR;
        return format(t);
    }
    
    /**
     * 获得时间的指定值eg
     * 获得时间的小时值
     * getValue(Calendar.HOUR_OF_DAY)
     * @param type
     * @param date
     * @return
     */
    public static int getDateValue(int type, Date... date){
        Calendar cal = Calendar.getInstance();
        if(date != null){
            cal.setTime(date[0]);
        }else{
            cal.setTime(new Date());
        }
        return cal.get(type);
    }
    
    /**
     * 
     * @param time
     * @return
     */
    public static String getWeekFirstDay(String time){
        return getWeekFirstDay(getLimitTime(time), 0);
    }
    
    /**
     * 获得当前时间所在周的第一天,也可以几周前的第一天
     * @param time
     * @param diff 间隔时间
     * @return
     */
    public static String getWeekFirstDay(String time, int diff){
        Date date = DateUtil.parse(getLimitTime(time), "yyyy-MM-dd hh:mm:ss");
        int day = getDateValue(Calendar.DAY_OF_WEEK, date);
        if(day == 1){
            day = 7;
        }else{
            day -= 1;
        }
        long t = date.getTime() - day * DAY + (diff * WEEK);
        return format(t);
    }
    
    /**
     * 
     * @param time
     * @return
     */
    public static String getMonthFirstDay(String time){
        return getMonthFirstDay(getLimitTime(getLimitTime(time)), 0);
    }
    
    /**
     * 
     * @param time
     * @param diff
     * @return
     */
    public static String getMonthFirstDay(String time, int diff){
        Date date = DateUtil.parse(getLimitTime(time), "yyyy-MM-dd hh:mm:ss");
        int day = getDateValue(Calendar.DAY_OF_MONTH, date);
        long t = date.getTime() - day * DAY;
        if(diff == 0){
            return format(t);
        }
        diff--;
        return getMonthFirstDay(format(t), diff);
    }
    
    /**
     * 
     * @param time
     * @return
     */
    public static String getYearFirstDay(String time){
        return getYearFirstDay(getLimitTime(time), 0);
    }
    
    /**
     * 
     * @param time
     * @param diff
     * @return
     */
    public static String getYearFirstDay(String time, int diff){
        Date date = DateUtil.parse(getLimitTime(time), "yyyy-MM-dd hh:mm:ss");
        int day = getDateValue(Calendar.DAY_OF_YEAR, date);
        long t = date.getTime() - day * DAY;
        if(diff == 0){
            return format(t);
        }
        diff--;
        return getYearFirstDay(format(t), diff);
    }
    
    
    
    /**
     * 将yyyyMMddhhmmssSSS格式的时间字符串时间字符串转换为yyyy-MM-dd HH:mm:ss格式的时间
     * @param timeStr
     * @return
     * @throws ParseException
     */
    public static String timeConv(String timeStr) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        long millionSeconds = sdf.parse(timeStr).getTime();//毫秒;
        return format(millionSeconds);
    }
    

	/*
	 * 将10 or 13 位时间戳转为时间字符串 convert the number 1407449951 1407499055617 to
	 * date/time format timestamp
	 */
	public static String timestamp2Date(String timeStr, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (timeStr.length() == 13) {
			String date = sdf.format(new Date(Long.parseLong(timeStr)));
			return date;
		} else {
			String date = sdf.format(new Date(Integer.parseInt(timeStr) * 1000L));
			return date;
		}
	}    
    
    public static void main(String[] args) throws ParseException {
        test2();
    }
    
    
    public static void test(){
        String str = "C:\\Users\\zhanghx\\Desktop\\tw_os_log_20171113034050593.zip";
        String fileName = str.substring(str.lastIndexOf("\\") +1);
        String timeStr = fileName.substring(fileName.lastIndexOf("_")+1);
        
        String newStr = timeStr.substring(0, timeStr.indexOf("."));
        System.out.println(newStr);
        
        
        String yearStr = newStr.substring(0, 4);
        String monthStr = newStr.substring(4, 6);
        String dayStr = newStr.substring(6, 8);
        String hourStr = newStr.substring(8,10);
        String minStr = newStr.substring(10,12);
        String secdStr = newStr.substring(12, 14);
        System.out.println(yearStr);
        System.out.println(monthStr);
        System.out.println(dayStr);
        System.out.println(hourStr);
        System.out.println(minStr);
        System.out.println(secdStr);
    }
    
    
    public static void test2(){
    	String currTime = "2018-05-26 00:07:58";
    	String time = DateUtil.getDayFirstHour(currTime);
    	System.out.println(time);
    }
    
}
