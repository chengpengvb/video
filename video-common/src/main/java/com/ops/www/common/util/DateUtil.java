package com.ops.www.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 *
 * @author cp
 * @version 创建时间：2020年1月19日 下午4:31:24
 */
public class DateUtil {
	private static final SimpleDateFormat reTimeSft = new SimpleDateFormat("HH:mm:ss MM_dd_yyyy");

	private static final SimpleDateFormat treTimeSft = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");

	private static final SimpleDateFormat tTimeSft = new SimpleDateFormat("yyyy_MM_dd");
	private static final SimpleDateFormat timeSft = new SimpleDateFormat("MM月dd日yyyy年");
	private static final GregorianCalendar gc = new GregorianCalendar();
	private static final String[] WEEKDAYS = { "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	public static String getWeekOfDate(Calendar cal) {
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return WEEKDAYS[w];
	}

	public static enum Time_type {
		YEAR_TYPE(1), MONTH_TYPE(2), WEEK_TYPE(3), DAY_TYPE(5);

		private Time_type(int type) {
			this.type = type;
		}

		private int type;

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

	}

	private DateUtil() {
	}

	/**
	 * 当天的开始时间
	 *
	 * @return
	 */
	public static long startOfTodDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 当天的结束时间
	 *
	 * @return
	 */
	public static long endOfTodDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		Date date = calendar.getTime();
		return date.getTime();
	}

	/**
	 * 获取传入当天的结束时间
	 */
	public static long startOfDay(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date = calendar.getTime();
		return date.getTime();
	}

	/**
	 * 获取传入当天的结束时间
	 */
	public static long endOfDay(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		Date date = calendar.getTime();
		return date.getTime();
	}

	/**
	 * 昨天的开始时间
	 *
	 * @return
	 */
	public static long startOfyesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date = calendar.getTime();
		return date.getTime();
	}

	/**
	 * 昨天的结束时间
	 *
	 * @return
	 */
	public static long endOfyesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.add(Calendar.DATE, -1);
		Date date = calendar.getTime();
		return date.getTime();
	}

	/**
	 * 功能：获取上周的开始时间
	 */
	public static long startOfLastWeek() {// 当周开始时间
		return startOfThisWeek() - 7 * 24 * 60 * 60 * 1000;
	}

	/**
	 * 功能：获取上周的结束时间
	 */
	public static long endOfLastWeek() {// 当周开始时间
		return endOfThisWeek() - 7 * 24 * 60 * 60 * 1000;
	}

	/**
	 * 功能：获取本周的开始时间 示例：2013-05-13 00:00:00
	 */
	public static long startOfThisWeek() {// 当周开始时间
		Calendar currentDate = Calendar.getInstance();
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date date = currentDate.getTime();
		return date.getTime();
	}

	/**
	 * 功能：获取本周的开始时间 示例：2013-05-13 00:00:00
	 */
	public static long startOfThisWeek(Date time) {// 当周开始时间
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(time);
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date date = currentDate.getTime();
		return date.getTime();
	}

	/**
	 * 功能：获取本周的结束时间 示例：2013-05-19 23:59:59
	 */
	public static long endOfThisWeek() {// 当周结束时间
		Calendar currentDate = Calendar.getInstance();
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		currentDate.set(Calendar.MILLISECOND, 999);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Date date = currentDate.getTime();
		return date.getTime();
	}

	/**
	 * 功能：获取本月的开始时间
	 */
	public static long startOfThisMonth() {// 当周开始时间
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		currentDate.set(Calendar.DAY_OF_MONTH, 1);
		Date date = currentDate.getTime();
		return date.getTime();
	}

	/**
	 * 功能：获取本月的结束时间
	 */
	public static long endOfThisMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		Date date = cal.getTime();
		return date.getTime();
	}

	/**
	 * 功能：获取上月的开始时间
	 */
	public static long startOfLastMonth() {// 当周开始时间
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		currentDate.set(Calendar.DAY_OF_MONTH, 1);
		currentDate.add(Calendar.MONTH, -1);
		Date date = currentDate.getTime();
		return date.getTime();
	}

	/**
	 * 功能：获取上月的结束时间
	 */
	public static long endOfLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		cal.add(Calendar.DATE, -1);
		Date date = cal.getTime();
		return date.getTime();
	}

	/**
	 * 功能：获取本年的开始时间
	 */
	public static long startOfTheYear(int year) {// 当周开始时间
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.YEAR, year);
		currentDate.set(Calendar.MONTH, 0);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		currentDate.set(Calendar.DAY_OF_MONTH, 1);
		Date date = currentDate.getTime();
		return date.getTime();
	}

	/**
	 * 功能：获取本年的开始时间
	 */
	public static long endOfTheYear(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		Date date = cal.getTime();
		return date.getTime();
	}

	/**
	 * 获取前一年的开始时间
	 *
	 * @return
	 */
	public static long startOfLastYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, cal.getWeekYear() - 1);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MONTH, -1);
		Date date = cal.getTime();
		return date.getTime();
	}

	/**
	 * 获取前一年的结束时间
	 *
	 * @return
	 */
	public static long endOfLastYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, cal.getWeekYear() - 1);
		cal.set(Calendar.MONTH, 12);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		cal.add(Calendar.MONTH, -1);
		Date date = cal.getTime();
		return date.getTime();
	}

	/**
	 * 功能：传入把毫秒值,返回 ?天?小时?分?秒
	 *
	 * @author sww
	 */
	public static String getDateByLong(long ms) {
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		Long second = (ms - day * dd - hour * hh - minute * mi) / ss;

		StringBuffer sb = new StringBuffer();
		if (day > 0) {
			sb.append(day + "天");
		}
		if (hour > 0) {
			sb.append(hour + "小时");
		}
		if (minute > 0) {
			sb.append(minute + "分");
		}
		if (second > 0) {
			sb.append(second + "秒");
		}
		return sb.toString();
	}

	public static long getTime(String time, boolean isReTime) {
		try {
			if (isReTime) {
				return reTimeSft.parse(time).getTime();

			} else {
				return timeSft.parse(time).getTime();
			}
		} catch (ParseException e) {
			return -1;
		}
	}

	public static String getWeekName(long time) {
		String[] weekDays = { "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static long dayOpera(long time, Time_type time_type, int dist) {
		gc.setTime(new Date(time));
		gc.add(time_type.getType(), dist);
		return gc.getTime().getTime();
	}

	public static String getFormatDate(long time, boolean isReTime) {
		if (isReTime) {
			return reTimeSft.format(new Date(time).getTime());
		} else {
			return timeSft.format(new Date(time).getTime());
		}
	}

	public static String getTFormatDate(long time, boolean isReTime) {
		if (isReTime) {
			return treTimeSft.format(new Date(time).getTime());
		} else {
			return tTimeSft.format(new Date(time).getTime());
		}
	}

}
