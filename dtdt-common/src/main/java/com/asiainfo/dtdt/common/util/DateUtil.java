package com.asiainfo.dtdt.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 
 * Description: 日期工具类 Date: 2016年7月1日
 */
public class DateUtil {

	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String STANDARD24 = "yyyy-MM-dd HH:mm:ss";
	public static final String ENDTIME = "23:59:59";
	

	public static String getWCpayDate(Date date) {
		// 获取date的YYYY-MM-DD格式的日期
		String dateYYYYMMDD = getDateTimeToDate(date);
		// 获取date的YYYY-MM-DD HH:MI:SS格式的日期
		String dateStr = getDateTime(date);

		// 获取当前日期
		Date today = new Date();
		// 获取当前日期的YYYY-MM-DD格式的日期
		String str = getDateTimeToDate(today);

		// 获取当前日期的前一天日期

		String yestoday = afterNDayStr(-1);

		// 获取当前日期的前两天日期
		// String beforTowDate = afterNDayStr(-2);

		// 获取一周以前的日期
		// String beforWeekDate = afterNDayStr(-7);

		if (dateYYYYMMDD.equals(str)) {

			return "今天 " + dateStr.substring(dateStr.indexOf(" "), dateStr.lastIndexOf(":"));
		} else if (dateYYYYMMDD.equals(yestoday)) {

			return "昨天";
		} else if (date.getTime() < afterNDay(-1).getTime() && date.getTime() >= afterNDay(-7).getTime()) {
			// 超过48小时;
			return getWeekOfDate(date);

		} else {
			return getDateTime(date);
		}

	}

	/**
	 * 把中国标准时间转换成YYYY-MM-DD HH24:MI:SS
	 * 
	 * @return YYYY-MM-DD HH24:MI:SS
	 */
	public static String getDateTime(Date d) {
		String time = df.format(d);
		return time;
	}

	/**
	 * 时间转换成YYYY-MM-DD
	 * 
	 * @param d
	 * @return YYYY-MM-DD
	 */
	public static String getDateTimeToDate(Date date) {
		String time = d.format(date);
		return time;
	}

	/**
	 * 根据日期获得星期
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		// String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysName[intWeek];
	}

	/**
	 * 当前日期前几天或者后几天的日期
	 * 
	 * @param n
	 * @return yyyy-MM-dd
	 */
	public static String afterNDayStr(int n) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date());

		calendar.add(Calendar.DATE, n);

		Date date = calendar.getTime();

		String s = d.format(date);

		return s;

	}

	/**
	 * 当前日期前几天或者后几天的日期
	 * 
	 * @param n
	 * @return yyyy-MM-dd
	 */
	public static Date afterNDay(int n) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date());

		calendar.add(Calendar.DATE, n);

		Date date = calendar.getTime();

		return date;

	}

	/**
	 * 获取当前时间的前几个小时或后几个小时的时间
	 * 
	 * @param h
	 * @return
	 */
	public static Date afterNHour(int h) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR, h);
		Date date = calendar.getTime();
		return date;
	}

	public static String getSysdateYYYYMMDDHHMMSS() {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date());

		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
		return sdf.format(calendar.getTime());
	}

	/**
	 * 
	 * Description: 获取系统当前时间，并格式化为YYYY-MM-DD HH:mm:ss
	 * 
	 * @return
	 * 
	 */
	public static String getSysdate() {
		String date = df.format(new Date());
		return date;
	}

	/**
	 * 
	 * Description: 将yyyymmdd格式日期字符串转换为yyyy-mm-dd格式日期字符串
	 * 
	 * @param dateStr
	 *            yyyymmdd格式日期字符串
	 * @return
	 * @throws ParseException
	 * 
	 */
	public static String yyyymmddToDateStr(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
		Date date = sdf.parse(dateStr);
		String result = d.format(date);
		return result;
	}

	public static String getSysdate(String pattern) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date());

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(calendar.getTime());
	}

	public static String getSysTime() {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date());

		SimpleDateFormat sdf = new SimpleDateFormat(STANDARD24);
		return sdf.format(calendar.getTime());
	}

	public static Date stringToDate(String str) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(STANDARD24);
		return sdf.parse(str);
	}

	/**
	 * 
	 * Description: 获取指定日期的前几天或后几天日期
	 * 
	 * @param date
	 * @param n
	 * @return yyyy-MM-dd
	 */
	public static String dateAfterNdayStr(Date date, int n) {
		String s = d.format(dateAfterNdayDate(date, n));
		return s;
	}

	/**
	 * 
	 * Description:
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date dateAfterNdayDate(Date date, int n) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		calendar.add(Calendar.DATE, n);

		Date dateResult = calendar.getTime();
		return dateResult;
	}

	public static String getSysTime(int length) throws Exception {
		Calendar calendar = Calendar.getInstance();
		String tmp = String.valueOf(calendar.getTime().getTime());
		if (length < tmp.length()) {
			tmp = tmp.substring(0, length);
		}

		return tmp;
	}

	/**
	 * 
	 * Description: 获取data到n天后00点的秒数
	 * 
	 * @param date
	 * @param n
	 * @return
	 * @throws ParseException
	 */
	public static int getSecondsNowToNDateAfter(Date date, Integer n) throws ParseException {
		String afterDateStr = dateAfterNdayStr(date, n);
		Date afterDate = df.parse(afterDateStr + " 00:00:00");
		long sec = afterDate.getTime() - date.getTime();
		return (int) sec / 1000;
	}

	/**
     * 当月最后一天
     * @return
     */
    public static String getLastDay(Date dateTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); 
        Date theDate = calendar.getTime();
        String s = df.format(theDate);
        StringBuffer str = new StringBuffer().append(s).append(" 23:59:59");
        return str.toString();

    }
    
    public static   Date getCurrentMonthEndTime(Date dateTime) {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
        	c.setTime(d.parse(d.format(dateTime)));
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            StringBuffer str = new StringBuffer().append(d.format(c.getTime())).append(" 23:59:59");
            now = df.parse(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    
    public static Date getNextNMonthStartTime(int n) {
        Calendar c = Calendar.getInstance();
        try {
        	c.setTime(d.parse(d.format(new Date())));
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, n);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }
    
    public static   Date getCurrentNextYear(Date dateTime,int n) {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
        	c.setTime(d.parse(d.format(dateTime)));
        	c.add(Calendar.MONTH, n-1);
            StringBuffer str = new StringBuffer().append(d.format(c.getTime())).append(" 23:59:59");
            now = df.parse(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    
    public static   Date getHalfYearStartTime(){
        Calendar c = Calendar.getInstance();
        System.out.println(getDateTime(c.getTime()));
        c.set(Calendar.YEAR, c.getTime().getYear());
        c.set(Calendar.MONTH, c.getTime().getMonth());
        c.set(Calendar.DAY_OF_MONTH, c.getTime().getDay());
        System.out.println(c.getTime().getDay());
        c.add(Calendar.MONTH, 6);
        return c.getTime();
        
    }
    
    public static Long getTodayEndTime() throws ParseException
    {
    	String today = getDateTimeToDate(new Date());
    	return df.parse(today + " " + ENDTIME).getTime();
    }
	public static void main(String[] args) throws ParseException {
		System.out.println(getNextNMonthStartTime(1));
	}
}
