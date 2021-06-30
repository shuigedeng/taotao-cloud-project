/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.utils;

import static cn.hutool.core.date.DatePattern.CHINESE_DATE_PATTERN;
import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;
import static cn.hutool.core.date.DatePattern.NORM_DATE_PATTERN;
import static cn.hutool.core.date.DatePattern.NORM_TIME_PATTERN;

import com.taotao.cloud.common.exception.BaseException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * 日期工具类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 10:23
 */
@Slf4j
public final class DateUtils {

	public static final String DEFAULT_YEAR_FORMAT = "yyyy";
	public static final String DEFAULT_MONTH_FORMAT = "yyyy-MM";
	public static final String DEFAULT_MONTH_FORMAT_SLASH = "yyyy/MM";
	public static final String DEFAULT_MONTH_FORMAT_EN = "yyyy年MM月";
	public static final String DEFAULT_WEEK_FORMAT = "yyyy-ww";
	public static final String DEFAULT_WEEK_FORMAT_EN = "yyyy年ww周";
	public static final String DEFAULT_DATE_FORMAT = NORM_DATE_PATTERN;
	public static final String DEFAULT_DATE_FORMAT_EN = CHINESE_DATE_PATTERN;
	public static final String DEFAULT_DATE_TIME_FORMAT = NORM_DATETIME_PATTERN;
	public static final String DEFAULT_DATE_TIME_FORMAT_EN = "";
	public static final String DEFAULT_TIME_FORMAT = NORM_TIME_PATTERN;
	public static final String DAY = "DAY";
	public static final String MONTH = "MONTH";
	public static final String WEEK = "WEEK";

	public static final String DEFAULT_DATE_FORMAT_MATCHES = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
	public static final String DEFAULT_DATE_TIME_FORMAT_MATCHES = "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$";
	public static final String DEFAULT_DATE_FORMAT_EN_MATCHES = "^\\d{4}年\\d{1,2}月\\d{1,2}日$";
	public static final String DEFAULT_DATE_TIME_FORMAT_EN_MATCHES = "^\\d{4}年\\d{1,2}月\\d{1,2}日\\d{1,2}时\\d{1,2}分\\d{1,2}秒$";
	public static final String SLASH_DATE_FORMAT_MATCHES = "^\\d{4}/\\d{1,2}/\\d{1,2}$";
	public static final String SLASH_DATE_TIME_FORMAT_MATCHES = "^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$";
	public static final String SLASH_DATE_FORMAT = "yyyy/MM/dd";
	public static final String SLASH_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
	public static final String CRON_FORMAT = "ss mm HH dd MM ? yyyy";

	/**
	 * 一个月平均天数
	 */
	public static final long MAX_MONTH_DAY = 30;
	/**
	 * 3个月平均天数
	 */
	public static final long MAX_3_MONTH_DAY = 90;
	/**
	 * 一年平均天数
	 */
	public static final long MAX_YEAR_DAY = 365;


	private DateUtils() {
	}
//--格式化日期start-----------------------------------------

	protected static final Map<String, String> DATE_FORMAT = new LinkedHashMap(5);

	static {
		DATE_FORMAT.put(DEFAULT_DATE_FORMAT, DEFAULT_DATE_FORMAT_MATCHES);
		DATE_FORMAT.put(SLASH_DATE_FORMAT, SLASH_DATE_FORMAT_MATCHES);
		DATE_FORMAT.put(DEFAULT_DATE_FORMAT_EN, DEFAULT_DATE_FORMAT_EN_MATCHES);
	}

	/**
	 * 解析日期
	 *
	 * @param source
	 * @return
	 */
	public static LocalDate parse(String source) {
		String sourceTrim = source.trim();
		Set<Map.Entry<String, String>> entries = DATE_FORMAT.entrySet();
		for (Map.Entry<String, String> entry : entries) {
			if (sourceTrim.matches(entry.getValue())) {
				return LocalDate.parse(source, DateTimeFormatter.ofPattern(entry.getKey()));
			}
		}
		throw new BaseException("解析日期失败, 请传递正确的日期格式");
	}


	/**
	 * 转换 Date 为 cron , eg.  "0 07 10 15 1 ? 2016"
	 *
	 * @param date 时间点
	 * @return cron 表达式
	 */
	public static String getCron(Date date) {
		return format(date, CRON_FORMAT);
	}

	/**
	 * 转换 LocalDateTime 为 cron , eg.  "0 07 10 15 1 ? 2016"
	 *
	 * @param date 时间点
	 * @return cron 表达式
	 */
	public static String getCron(LocalDateTime date) {
		return format(date, CRON_FORMAT);
	}

	/**
	 * 格式化日期,返回格式为 yyyy-MM
	 *
	 * @param date 日期
	 * @return 格式化后的字符串
	 */
	public static String format(LocalDateTime date, String pattern) {
		if (date == null) {
			date = LocalDateTime.now();
		}
		if (pattern == null) {
			pattern = DEFAULT_MONTH_FORMAT;
		}
		return date.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String format(LocalDate date, String pattern) {
		if (date == null) {
			date = LocalDate.now();
		}
		if (pattern == null) {
			pattern = DEFAULT_MONTH_FORMAT;
		}
		return date.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 根据传入的格式格式化日期.默认格式为MM月dd日
	 *
	 * @param d 日期
	 * @param f 格式
	 * @return 格式化后的字符串
	 */
	public static String format(Date d, String f) {
		Date date = d;
		String format = f;
		if (date == null) {
			date = new Date();
		}
		if (format == null) {
			format = DEFAULT_DATE_TIME_FORMAT;
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 格式化日期,返回格式为 yyyy-MM-dd
	 *
	 * @param date 日期
	 * @return 格式化后的字符串
	 */
	public static String formatAsDate(LocalDateTime date) {
		return format(date, DEFAULT_DATE_FORMAT);
	}

	public static String formatAsDate(LocalDate date) {
		return format(date, DEFAULT_DATE_FORMAT);
	}

	public static String formatAsDateEn(LocalDateTime date) {
		return format(date, DEFAULT_DATE_FORMAT_EN);
	}


	public static String formatAsYearMonth(LocalDateTime date) {
		return format(date, DEFAULT_MONTH_FORMAT);
	}

	public static String formatAsYearMonthEn(LocalDateTime date) {
		return format(date, DEFAULT_MONTH_FORMAT_EN);
	}

	/**
	 * 格式化日期,返回格式为 yyyy-ww
	 *
	 * @param date 日期
	 * @return 格式化后的字符串
	 */
	public static String formatAsYearWeek(LocalDateTime date) {
		return format(date, DEFAULT_WEEK_FORMAT);
	}

	public static String formatAsYearWeekEn(LocalDateTime date) {
		return format(date, DEFAULT_WEEK_FORMAT_EN);
	}

	/**
	 * 格式化日期,返回格式为 yyyy-MM
	 *
	 * @param date 日期
	 * @return 格式化后的字符串
	 */
	public static String formatAsYearMonth(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_MONTH_FORMAT);
		return df.format(date);
	}

	/**
	 * 格式化日期,返回格式为 yyyy-ww
	 *
	 * @param date 日期
	 * @return 格式化后的字符串
	 */
	public static String formatAsYearWeek(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_WEEK_FORMAT);
		return df.format(date);
	}

	/**
	 * 格式化日期,返回格式为 HH:mm:ss 例:12:24:24
	 *
	 * @param date 日期
	 * @return 格式化后的字符串
	 */
	public static String formatAsTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
		return df.format(date);
	}

	/**
	 * 格式化日期,返回格式为 yyyy-MM-dd
	 *
	 * @param date 日期
	 * @return 格式化后的字符串
	 */
	public static String formatAsDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return df.format(date);
	}

	/**
	 * 格式化日期,返回格式为 yyyy-MM-dd HH:mm:ss
	 *
	 * @param date 日期
	 * @return 格式化后的字符串
	 */
	public static String formatAsDateTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
		return df.format(date);
	}

	/**
	 * 格式化日期,返回格式为 dd ,即对应的天数.
	 *
	 * @param date 日期
	 * @return 格式化后的字符串
	 */
	public static String formatAsDay(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("dd");
		return df.format(date);
	}

	//--格式化日期end-----------------------------------------

	//--解析日期start-----------------------------------------

	/**
	 * 将字符转换成日期
	 *
	 * @param dateStr 日期字符串
	 * @param format  解析格式
	 * @return 解析后的日期
	 */
	public static Date parse(String dateStr, String format) {
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setLenient(false);
		try {
			date = dateFormat.parse(dateStr);

		} catch (Exception e) {
			LogUtil.info("DateUtils error", e);
		}
		return date;
	}

	/**
	 * 获取当月最后一天
	 *
	 * @param date 日期
	 * @return 当月最后一天
	 */
	public static Date getLastDateOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		return calendar.getTime();
	}

	/**
	 * 根据传入的String返回对应的date
	 *
	 * @param source 日期字符串
	 * @return 日期
	 */
	public static Date parseAsDate(String source) {
		String sourceTrim = source.trim();
		Set<Map.Entry<String, String>> entries = DATE_FORMAT.entrySet();
		try {
			for (Map.Entry<String, String> entry : entries) {
				if (sourceTrim.matches(entry.getValue())) {
					return new SimpleDateFormat(entry.getKey()).parse(source);
				}
			}
		} catch (ParseException e) {
			throw new BaseException("解析日期失败, 请传递正确的日期格式");
		}
		throw new BaseException("解析日期失败, 请传递正确的日期格式");
	}

	/**
	 * 按给定参数返回Date对象
	 *
	 * @param dateTime 时间对象格式为("yyyy-MM-dd HH:mm:ss");
	 * @return 解析后的日期
	 */
	public static Date parseAsDateTime(String dateTime) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
		try {
			return simpledateformat.parse(dateTime);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取指定日期的开始时间 如：00:00:00
	 *
	 * @param value 日期
	 * @return 解析后的日期
	 */
	public static Date getDate0000(LocalDateTime value) {
		return getDate0000(value.toLocalDate());
	}

	/**
	 * 获取指定日期的开始时间 如：00:00:00
	 *
	 * @param value 日期
	 * @return 解析后的日期
	 */
	public static Date getDate0000(Date value) {
		return getDate0000(DateUtils.date2LocalDate(value));
	}

	/**
	 * 获取指定日期的开始时间 如：00:00:00
	 *
	 * @param value 日期
	 * @return 解析后的日期
	 */
	public static Date getDate0000(LocalDate value) {
		LocalDateTime todayStart = LocalDateTime.of(value, LocalTime.MIN);
		return DateUtils.localDateTime2Date(todayStart);
	}

	/**
	 * 获取指定日期的结束时间 如：23:59:59
	 *
	 * @param value 日期
	 * @return 解析后的日期
	 */
	public static Date getDate2359(LocalDateTime value) {
		return getDate2359(value.toLocalDate());

	}

	/**
	 * 获取指定日期的结束时间 如：23:59:59
	 *
	 * @param value 日期
	 * @return 解析后的日期
	 */
	public static Date getDate2359(Date value) {
		return getDate2359(DateUtils.date2LocalDate(value));
	}

	/**
	 * 获取指定日期的结束时间 如：23:59:59
	 *
	 * @param value 日期
	 * @return 解析后的日期
	 */
	public static Date getDate2359(LocalDate value) {
		LocalDateTime dateEnd = LocalDateTime.of(value, LocalTime.MAX);
		return DateUtils.localDateTime2Date(dateEnd);
	}

	/**
	 * LocalDateTime转换为Date
	 *
	 * @param localDateTime 日期
	 * @return 解析后的日期
	 */
	public static Date localDateTime2Date(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		return Date.from(zdt.toInstant());
	}

	//--解析日期 end-----------------------------------------


	/**
	 * Date转换为LocalDateTime
	 *
	 * @param date 日期
	 */
	public static LocalDateTime date2LocalDateTime(Date date) {
		if (date == null) {
			return LocalDateTime.now();
		}
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDateTime();
	}

	/**
	 * 日期转 LocalDate
	 *
	 * @param date 日期
	 * @return 解析后的日期
	 */
	public static LocalDate date2LocalDate(Date date) {
		if (date == null) {
			return LocalDate.now();
		}
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDate();
	}

	/**
	 * 日期转 LocalTime
	 *
	 * @param date 日期
	 * @return 解析后的日期
	 */
	public static LocalTime date2LocalTime(Date date) {
		if (date == null) {
			return LocalTime.now();
		}
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalTime();
	}


	/**
	 * 毫秒转日期
	 *
	 * @param epochMilli 毫秒
	 * @return 解析后的日期
	 */
	public static LocalDateTime getDateTimeOfTimestamp(long epochMilli) {
		Instant instant = Instant.ofEpochMilli(epochMilli);
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	/**
	 * 秒转日期
	 *
	 * @param epochSecond 秒
	 * @return 解析后的日期
	 */
	public static LocalDateTime getDateTimeOfSecond(long epochSecond) {
		Instant instant = Instant.ofEpochSecond(epochSecond);
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	//-计算日期 start------------------------------------------


	/**
	 * 计算结束时间与当前时间间隔的天数
	 *
	 * @param endDate 结束日期
	 * @return 计算结束时间与当前时间间隔的天数
	 */
	public static long until(Date endDate) {
		return LocalDateTime.now().until(date2LocalDateTime(endDate), ChronoUnit.DAYS);
	}

	/**
	 * 计算结束时间与开始时间间隔的天数
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @return 计算结束时间与开始时间间隔的天数
	 */
	public static long until(Date startDate, Date endDate) {
		return date2LocalDateTime(startDate).until(date2LocalDateTime(endDate), ChronoUnit.DAYS);
	}


	/**
	 * 计算结束时间与开始时间间隔的天数
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @return 计算结束时间与开始时间间隔的天数
	 */
	public static long until(LocalDateTime startDate, LocalDateTime endDate) {
		return startDate.until(endDate, ChronoUnit.DAYS);
	}

	public static long until(LocalDate startDate, LocalDate endDate) {
		return startDate.until(endDate, ChronoUnit.DAYS);
	}

	/**
	 * 计算2个日期之间的所有的日期 yyyy-MM-dd 含头含尾
	 *
	 * @param start yyyy-MM-dd
	 * @param end   yyyy-MM-dd
	 * @return 日期区间的所有日期
	 */
	public static List<String> getBetweenDay(Date start, Date end) {
		return getBetweenDay(date2LocalDate(start), date2LocalDate(end));
	}

	/**
	 * 计算2个日期之间的所有的日期 yyyy-MM-dd 含头含尾
	 *
	 * @param start yyyy-MM-dd
	 * @param end   yyyy-MM-dd
	 */
	public static List<String> getBetweenDay(String start, String end) {
		return getBetweenDay(LocalDate.parse(start), LocalDate.parse(end));
	}

	/**
	 * 计算2个日期之间的所有的日期 yyyy-MM-dd 含头含尾
	 *
	 * @param startDate yyyy-MM-dd
	 * @param endDate   yyyy-MM-dd
	 */
	public static List<String> getBetweenDay(LocalDate startDate, LocalDate endDate) {
		return getBetweenDay(startDate, endDate, DEFAULT_DATE_FORMAT);
	}

	public static List<String> getBetweenDayEn(LocalDate startDate, LocalDate endDate) {
		return getBetweenDay(startDate, endDate, DEFAULT_DATE_FORMAT_EN);
	}

	public static List<String> getBetweenDay(LocalDate startDate, LocalDate endDate,
		String pattern) {
		if (pattern == null) {
			pattern = DEFAULT_DATE_FORMAT;
		}
		List<String> list = new ArrayList<>();
		long distance = ChronoUnit.DAYS.between(startDate, endDate);
		if (distance < 1) {
			return list;
		}
		String finalPattern = pattern;
		Stream.iterate(startDate, d -> d.plusDays(1)).
			limit(distance + 1)
			.forEach(f -> list.add(f.format(DateTimeFormatter.ofPattern(finalPattern))));
		return list;
	}


	/**
	 * 计算2个日期之间的所有的周 yyyy-ww 含头含尾
	 *
	 * @param start yyyy-MM-dd
	 * @param end   yyyy-MM-dd
	 */
	public static List<String> getBetweenWeek(Date start, Date end) {
		return getBetweenWeek(date2LocalDate(start), date2LocalDate(end));
	}

	/**
	 * 计算2个日期之间的所有的周 yyyy-ww 含头含尾
	 *
	 * @param start yyyy-MM-dd
	 * @param end   yyyy-MM-dd
	 * @return 2个日期之间的所有的周
	 */
	public static List<String> getBetweenWeek(String start, String end) {
		return getBetweenWeek(LocalDate.parse(start), LocalDate.parse(end));
	}

	/**
	 * 计算2个日期之间的所有的周 yyyy-ww 含头含尾
	 *
	 * @param startDate yyyy-MM-dd
	 * @param endDate   yyyy-MM-dd
	 * @return 2个日期之间的所有的周
	 */
	public static List<String> getBetweenWeek(LocalDate startDate, LocalDate endDate) {
		return getBetweenWeek(startDate, endDate, DEFAULT_WEEK_FORMAT);
	}

	public static List<String> getBetweenWeek(LocalDate startDate, LocalDate endDate,
		String pattern) {
		List<String> list = new ArrayList<>();

		long distance = ChronoUnit.WEEKS.between(startDate, endDate);
		if (distance < 1) {
			return list;
		}
		Stream.iterate(startDate, d -> d.plusWeeks(1)).
			limit(distance + 1)
			.forEach(f -> list.add(f.format(DateTimeFormatter.ofPattern(pattern))));
		return list;
	}

	/**
	 * 计算2个日期之间的所有的月 yyyy-MM
	 *
	 * @param start yyyy-MM-dd
	 * @param end   yyyy-MM-dd
	 * @return 2个日期之间的所有的月
	 */
	public static List<String> getBetweenMonth(Date start, Date end) {
		return getBetweenMonth(date2LocalDate(start), date2LocalDate(end));
	}

	/**
	 * 计算2个日期之间的所有的月 yyyy-MM
	 *
	 * @param start yyyy-MM-dd
	 * @param end   yyyy-MM-dd
	 * @return 2个日期之间的所有的月
	 */
	public static List<String> getBetweenMonth(String start, String end) {
		return getBetweenMonth(LocalDate.parse(start), LocalDate.parse(end));
	}

	/**
	 * 计算2个日期之间的所有的月 yyyy-MM
	 *
	 * @param startDate yyyy-MM-dd
	 * @param endDate   yyyy-MM-dd
	 * @return 2个日期之间的所有的月
	 */
	public static List<String> getBetweenMonth(LocalDate startDate, LocalDate endDate) {
		return getBetweenMonth(startDate, endDate, DEFAULT_MONTH_FORMAT);
	}

	public static List<String> getBetweenMonth(LocalDate startDate, LocalDate endDate,
		String pattern) {
		List<String> list = new ArrayList<>();
		long distance = ChronoUnit.MONTHS.between(startDate, endDate);
		if (distance < 1) {
			return list;
		}

		Stream.iterate(startDate, d -> d.plusMonths(1))
			.limit(distance + 1)
			.forEach(f -> list.add(f.format(DateTimeFormatter.ofPattern(pattern))));
		return list;
	}

	/**
	 * 计算时间区间内的日期列表，并返回
	 *
	 * @param startTime 开始
	 * @param endTime   结束
	 * @param dateList  日期
	 * @return 计算时间区间内的日期列表
	 */
	public static String calculationEn(LocalDateTime startTime, LocalDateTime endTime,
		List<String> dateList) {
		if (startTime == null) {
			startTime = LocalDateTime.now();
		}
		if (endTime == null) {
			endTime = LocalDateTime.now().plusDays(30);
		}
		return calculationEn(startTime.toLocalDate(), endTime.toLocalDate(), dateList);
	}

	public static String calculation(LocalDate startDate, LocalDate endDate,
		List<String> dateList) {
		if (startDate == null) {
			startDate = LocalDate.now();
		}
		if (endDate == null) {
			endDate = LocalDate.now().plusDays(30);
		}
		if (dateList == null) {
			dateList = new ArrayList<>();
		}
		long day = until(startDate, endDate);

		String dateType;
		if (day >= 0 && day <= MAX_MONTH_DAY) {
			dateType = DAY;
			dateList.addAll(DateUtils.getBetweenDay(startDate, endDate, DEFAULT_DATE_FORMAT));
		} else if (day > MAX_MONTH_DAY && day <= MAX_3_MONTH_DAY) {
			dateType = WEEK;
			dateList.addAll(DateUtils.getBetweenWeek(startDate, endDate, DEFAULT_WEEK_FORMAT));
		} else if (day > MAX_3_MONTH_DAY && day <= MAX_YEAR_DAY) {
			dateType = MONTH;
			dateList.addAll(DateUtils.getBetweenMonth(startDate, endDate, DEFAULT_MONTH_FORMAT));
		} else {
			throw new BaseException("日期参数只能介于0-365天之间");
		}
		return dateType;
	}

	public static String calculationEn(LocalDate startDate, LocalDate endDate,
		List<String> dateList) {
		if (startDate == null) {
			startDate = LocalDate.now();
		}
		if (endDate == null) {
			endDate = LocalDate.now().plusDays(30);
		}
		if (dateList == null) {
			dateList = new ArrayList<>();
		}
		long day = until(startDate, endDate);

		String dateType;
		if (day >= 0 && day <= MAX_MONTH_DAY) {
			dateType = DAY;
			dateList.addAll(DateUtils.getBetweenDay(startDate, endDate, DEFAULT_DATE_FORMAT_EN));
		} else if (day > MAX_MONTH_DAY && day <= MAX_3_MONTH_DAY) {
			dateType = WEEK;
			dateList.addAll(DateUtils.getBetweenWeek(startDate, endDate, DEFAULT_WEEK_FORMAT_EN));
		} else if (day > MAX_3_MONTH_DAY && day <= MAX_YEAR_DAY) {
			dateType = MONTH;
			dateList.addAll(DateUtils.getBetweenMonth(startDate, endDate, DEFAULT_MONTH_FORMAT_EN));
		} else {
			throw new BaseException("日期参数只能介于0-365天之间");
		}
		return dateType;
	}

//----------//----------//----------//----------//----------//----------//----------//----------//----------//----------//----------

	/**
	 * 计算开始时间
	 *
	 * @param time 日期
	 * @return 计算开始时间
	 */
	public static LocalDateTime getStartTime(String time) {
		String startTime = time;
		if (time.matches("^\\d{4}-\\d{1,2}$")) {
			startTime = time + "-01 00:00:00";
		} else if (time.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
			startTime = time + " 00:00:00";
		} else if (time.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
			startTime = time + ":00";
		} else if (time
			.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T{1}\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{3}Z$")) {
			startTime = time.replace("T", " ").substring(0, time.indexOf('.'));
		}
		return LocalDateTime
			.parse(startTime, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
	}

	/**
	 * 计算结束时间
	 *
	 * @param time 日期
	 * @return 结束时间
	 */
	public static LocalDateTime getEndTime(String time) {
		String startTime = time;
		if (time.matches("^\\d{4}-\\d{1,2}$")) {
			Date date = DateUtils.parse(time, "yyyy-MM");
			date = DateUtils.getLastDateOfMonth(date);
			startTime = DateUtils.formatAsDate(date) + " 23:59:59";
		} else if (time.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
			startTime = time + " 23:59:59";
		} else if (time.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
			startTime = time + ":59";
		} else if (time
			.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T{1}\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{3}Z$")) {
			time = time.replace("T", " ").substring(0, time.indexOf('.'));
			if (time.endsWith("00:00:00")) {
				time = time.replace("00:00:00", "23:59:59");
			}
			startTime = time;
		}
		return LocalDateTime
			.parse(startTime, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
	}

	/**
	 * 判断当前时间是否在指定时间范围
	 *
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return 结果
	 */
	public static boolean between(LocalTime from, LocalTime to) {
		if (from == null) {
			throw new IllegalArgumentException("开始时间不能为空");
		}
		if (to == null) {
			throw new IllegalArgumentException("结束时间不能为空");
		}
		LocalTime now = LocalTime.now();
		return now.isAfter(from) && now.isBefore(to);
	}
}
