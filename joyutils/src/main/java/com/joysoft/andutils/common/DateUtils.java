package com.joysoft.andutils.common;

import com.joysoft.andutils.lg.Lg;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期转换类
 * @author fengmiao
 *
 */
public class DateUtils {
	
	private final static SimpleDateFormat Y_M_D = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
	
	private final static SimpleDateFormat Y_M_D_H_M_S = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
	
	private final static SimpleDateFormat MDHM = new SimpleDateFormat(
			"MM月dd日 HH:mm",Locale.ENGLISH);
	
	private final static SimpleDateFormat YMD = new SimpleDateFormat(
			"yyyy年MM月dd日",Locale.ENGLISH);
	
	private final static long nd = 1000*24*60*60;    //一天的毫秒数
	private final static long nh = 1000*60*60;       //一小时的毫秒数
	private final static long nm = 1000*60;          //一分钟的毫秒数
	/**
	 * 获取系统当前时间的年、月、日拼成的int数据
	 */
	public static int getToday() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumIntegerDigits(2);
		nf.setMinimumIntegerDigits(2);

		Calendar calendar = Calendar.getInstance();
		String year = calendar.get(Calendar.YEAR) + "";
		String month = nf.format(calendar.get(Calendar.MONTH) + 1);
		String day = nf.format(calendar.get(Calendar.DAY_OF_MONTH));
		StringBuffer today = new StringBuffer(year);
		today.append(month).append(day);

		return Integer.valueOf(today.toString());
	}

	
	/**
	 * 获取系统当前日期 格式:“年/月/日 星期几”
	 */
	public static String getToday2() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		String weekdayStr = "";
		switch (weekday) {
		case 1:
			weekdayStr = "星期日";
			break;
		case 2:
			weekdayStr = "星期一";
			break;
		case 3:
			weekdayStr = "星期二";
			break;
		case 4:
			weekdayStr = "星期三";
			break;
		case 5:
			weekdayStr = "星期四";
			break;
		case 6:
			weekdayStr = "星期五";
			break;
		case 7:
			weekdayStr = "星期六";
			break;
		}
		StringBuffer today = new StringBuffer(year + "");
		today.append("/" + month).append("/" + day).append("  " + weekdayStr);
		return today.toString();
	}
	
	/**
	 * 获取系统当前日期 返回格式:“年-月-日”
	 */
	public static String getToday3() {
		Calendar calendar = Calendar.getInstance();
//		SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy-MM-dd",
//				Locale.ENGLISH);
		return Y_M_D.format(calendar.getTime());
	}

	/**
	 * 将“yyyy-MM-dd HH:mm:ss”格式的日期 转换为“刚刚”“**分钟前”“**小时前”或“”MM月dd日
	 * 时:分”“yyyy年MM月dd日”
	 */
	public static String transformDateFormat(String src) {
		String result = "";
		if (src == null || src.equals("")) {
			return "";
		}
		try {
//			SimpleDateFormat srcFormat = new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//			SimpleDateFormat resultFormat = new SimpleDateFormat(
//					"MM月dd日 HH:mm", Locale.ENGLISH);
//			SimpleDateFormat resultFormat2 = new SimpleDateFormat(
//					"yyyy年MM月dd日", Locale.ENGLISH);
			Calendar today_cal = Calendar.getInstance();
			Calendar publish_cal = Calendar.getInstance();
			publish_cal.setTime(Y_M_D_H_M_S.parse(src));
			int today_year = today_cal.get(Calendar.YEAR);
			int publish_year = publish_cal.get(Calendar.YEAR);
			if (today_year > publish_year) {// 不是今年发布的
				result = YMD.format(publish_cal.getTime());
				return result;
			}
			long interval = today_cal.getTimeInMillis()
					- publish_cal.getTimeInMillis();
			long today = interval/nd;
			if (today < 1) {// 今天发表的
				long hour = interval/nh;
				long minute = interval/nm;
				if (hour >= 1) {// 超过一小时
					result = hour + "小时前";
				} else if (minute >= 1) {
					result = minute + "分钟前";
				} else {
					result = "刚刚";
				}
			} else {
				result = MDHM.format(Y_M_D_H_M_S.parse(src));
			}

		} catch (ParseException e) {
			Lg.e(e.toString());
		}
		return result;
	}

	/**
	 * 将“yyyy-MM-dd HH:mm:ss”格式的日期 转换为“今天 时:分”或“MM月dd日 时:分”“yyyy年MM月dd日”
	 */
	public static String transformDateFormat3(String src) {
		String result = "";
		if (src == null || src.equals("")) {
			return "";
		}
		try {
//			SimpleDateFormat srcFormat = new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//			SimpleDateFormat resultFormat = new SimpleDateFormat(
//					"MM月dd日 HH:mm", Locale.ENGLISH);
//			SimpleDateFormat resultFormat2 = new SimpleDateFormat(
//					"yyyy年MM月dd日", Locale.ENGLISH);
			Calendar today_cal = Calendar.getInstance();
			Calendar publish_cal = Calendar.getInstance();
			publish_cal.setTime(Y_M_D_H_M_S.parse(src));
			int today_year = today_cal.get(Calendar.YEAR);
			int publish_year = publish_cal.get(Calendar.YEAR);
			if (today_year > publish_year) {// 不是今年发布的
				result = YMD.format(publish_cal.getTime());
				return result;
			}
			int publish_hour = publish_cal.get(Calendar.HOUR_OF_DAY);
			int publish_minute = publish_cal.get(Calendar.MINUTE);
			publish_cal.set(Calendar.MILLISECOND, 0);
			publish_cal.set(Calendar.SECOND, 0);
			publish_cal.set(Calendar.MINUTE, 0);
			publish_cal.set(Calendar.HOUR_OF_DAY, 0);
			today_cal.set(Calendar.MILLISECOND, 0);
			today_cal.set(Calendar.SECOND, 0);
			today_cal.set(Calendar.MINUTE, 0);
			today_cal.set(Calendar.HOUR_OF_DAY, 0);
			if (publish_cal.compareTo(today_cal) >= 0) {// 今天发表的
				result = "今天 " + new DecimalFormat("00").format(publish_hour)
						+ ":" + new DecimalFormat("00").format(publish_minute);
			} else {
				result = MDHM.format(Y_M_D_H_M_S.parse(src));
			}

		} catch (ParseException e) {
			Lg.e(e.toString());
		}
		return result;
	}

	/**
	 * 将“yyyy-MM-dd HH:mm:ss”格式的日期 转换为“yyyy年MM月dd日”
	 */
	public static String transformDateFormat2(String src) {
		String result = "";
		if (src == null || src.equals("")) {
			return "";
		}
		try {
//			SimpleDateFormat srcFormat = new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//			SimpleDateFormat resultFormat2 = new SimpleDateFormat(
//					"yyyy年MM月dd日", Locale.ENGLISH);
			Calendar publish_cal = Calendar.getInstance();
			publish_cal.setTime(Y_M_D_H_M_S.parse(src));
			result = YMD.format(publish_cal.getTime());
			return result;

		} catch (ParseException e) {
			Lg.e(e.toString());
		}
		return result;
	}
	
	/**
	 * 获取“yyyy-MM-dd”格式的日期9点时的毫秒数
	 * 
	 * @param src
	 * @return
	 */
	public static long getDate_9h(String src) {
		if (src == null || src.equals("")) {
			return 0;
		}
		long result = 0;
		try {
//			SimpleDateFormat srcFormat = new SimpleDateFormat("yyyy-MM-dd",
//					Locale.ENGLISH);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(Y_M_D.parse(src));
			calendar.set(Calendar.HOUR_OF_DAY, 9);
			result = calendar.getTimeInMillis();
		} catch (ParseException e) {
			Lg.e(e.toString());
		}
		return result;
	}

	/**
	 * 获取dateStr对应的Calendar。dateStr格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Calendar getCalendar(String dateStr) {
		Calendar calendar = Calendar.getInstance();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
//				Locale.ENGLISH);
		try {
			calendar.setTime(Y_M_D_H_M_S.parse(dateStr));
		} catch (ParseException e) {
			Lg.e(e.toString());
		}
		return calendar;
	}
	
	/**
	 * 将 服务器返回的时间戳 转化为 "yyyy-MM-dd HH:mm:ss"格式
	 * 然后再转变为 “刚刚”“**分钟前”“**小时前”或“”MM月dd日
	 * 时:分”“yyyy年MM月dd日”
	 */
	public static String timeStamp2Data(int timeStamp){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = Y_M_D_H_M_S.format(new Date(timeStamp*1000L));
//		Lg.e("date:"+date);
		String time = transformDateFormat(date);
		return time;
	}
}
