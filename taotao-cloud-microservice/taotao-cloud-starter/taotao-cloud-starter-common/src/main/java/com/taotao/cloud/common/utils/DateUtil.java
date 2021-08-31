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

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.exception.BaseException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 日期工具类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/2 16:39
 */
public class DateUtil {

	/**
	 * 24小时时间正则表达式
	 */
	public static final String MATCH_TIME_24 = "(([0-1][0-9])|2[0-3]):[0-5][0-9]:[0-5][0-9]";
	/**
	 * 日期正则表达式
	 */
	public static final String REGEX_DATA = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

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

	//--格式化日期start-----------------------------------------

	protected static final Map<String, String> DATE_FORMAT = new LinkedHashMap(5);

	static {
		DATE_FORMAT.put(DEFAULT_DATE_FORMAT, DEFAULT_DATE_FORMAT_MATCHES);
		DATE_FORMAT.put(SLASH_DATE_FORMAT, SLASH_DATE_FORMAT_MATCHES);
		DATE_FORMAT.put(DEFAULT_DATE_FORMAT_EN, DEFAULT_DATE_FORMAT_EN_MATCHES);
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
			dateList.addAll(DateUtil.getBetweenDay(startDate, endDate, DEFAULT_DATE_FORMAT));
		} else if (day > MAX_MONTH_DAY && day <= MAX_3_MONTH_DAY) {
			dateType = WEEK;
			dateList.addAll(DateUtil.getBetweenWeek(startDate, endDate, DEFAULT_WEEK_FORMAT));
		} else if (day > MAX_3_MONTH_DAY && day <= MAX_YEAR_DAY) {
			dateType = MONTH;
			dateList.addAll(DateUtil.getBetweenMonth(startDate, endDate, DEFAULT_MONTH_FORMAT));
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
			dateList.addAll(DateUtil.getBetweenDay(startDate, endDate, DEFAULT_DATE_FORMAT_EN));
		} else if (day > MAX_MONTH_DAY && day <= MAX_3_MONTH_DAY) {
			dateType = WEEK;
			dateList.addAll(DateUtil.getBetweenWeek(startDate, endDate, DEFAULT_WEEK_FORMAT_EN));
		} else if (day > MAX_3_MONTH_DAY && day <= MAX_YEAR_DAY) {
			dateType = MONTH;
			dateList.addAll(DateUtil.getBetweenMonth(startDate, endDate, DEFAULT_MONTH_FORMAT_EN));
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
			Date date = DateUtil.parse(time, "yyyy-MM");
			date = DateUtil.getLastDateOfMonth(date);
			startTime = DateUtil.formatAsDate(date) + " 23:59:59";
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
		return getDate2359(DateUtil.date2LocalDate(value));
	}

	/**
	 * 获取指定日期的结束时间 如：23:59:59
	 *
	 * @param value 日期
	 * @return 解析后的日期
	 */
	public static Date getDate2359(LocalDate value) {
		LocalDateTime dateEnd = LocalDateTime.of(value, LocalTime.MAX);
		return DateUtil.localDateTime2Date(dateEnd);
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
	 * 解析日期
	 *
	 * @param source source
	 */
	public static LocalDate parse(String source) {
		String sourceTrim = source.trim();
		Set<Entry<String, String>> entries = DATE_FORMAT.entrySet();
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
			LogUtil.error("DateUtil error", e);
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
		return getDate0000(DateUtil.date2LocalDate(value));
	}

	/**
	 * 获取指定日期的开始时间 如：00:00:00
	 *
	 * @param value 日期
	 * @return 解析后的日期
	 */
	public static Date getDate0000(LocalDate value) {
		LocalDateTime todayStart = LocalDateTime.of(value, LocalTime.MIN);
		return DateUtil.localDateTime2Date(todayStart);
	}

	/**
	 * 获取当前时间戳
	 *
	 * @return timestamp
	 */
	public static long getTimestamp() {
		// LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
		// LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		// Timestamp.valueOf(LocalDateTime.now()).getTime();
		// Instant.now().toEpochMilli();
		return Instant.now().toEpochMilli();
	}

	/**
	 * 获取当前年份
	 *
	 * @return yyyy
	 */
	public static int getCurrentYear() {
		return LocalDate.now().getYear();
	}

	/**
	 * 获取当前年月
	 *
	 * @return yyyy-MM
	 */
	public static String getCurrentYearMonth() {
		return getCurrentDate(CommonConstant.YEAR_MONTH_FORMATTER);
	}

	/**
	 * 获取当前日期
	 *
	 * @return yyyy-MM-dd
	 */
	public static String getCurrentDate() {
		return getCurrentDate(CommonConstant.DATE_FORMATTER);
	}

	/**
	 * 获取下一天日期
	 *
	 * @return yyyy-MM-dd
	 */
	public static String getNextDate() {
		return getNextDate(CommonConstant.DATE_FORMATTER);
	}

	/**
	 * 获取当前时间
	 *
	 * @return HHmmss
	 */
	public static String getCurrentTime() {
		return getCurrentTime(CommonConstant.TIME_FORMATTER);
	}

	/**
	 * 获取当前日期时间
	 *
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentDateTime() {
		return getCurrentDateTime(CommonConstant.DATETIME_FORMAT);
	}

	/**
	 * 获取当前年月
	 *
	 * @return yyyyMM
	 */
	public static String getCurrentYearMonthShort() {
		return getCurrentDate(CommonConstant.YEAR_MONTH_FORMATTER_SHORT);
	}

	/**
	 * 获取当前日期
	 *
	 * @return yyyyMMdd
	 */
	public static String getCurrentDateShort() {
		return getCurrentDate(CommonConstant.DATE_FORMATTER_SHORT);
	}

	/**
	 * 获取下一天日期
	 *
	 * @return yyyy-MM-dd
	 */
	public static String getNextDateShort() {
		return getNextDate(CommonConstant.DATE_FORMATTER_SHORT);
	}

	/**
	 * 获取当前时间
	 *
	 * @return HHmmss
	 */
	public static String getCurrentTimeShort() {
		return getCurrentTime(CommonConstant.TIME_FORMATTER_SHORT);
	}

	/**
	 * 获取当前日期时间
	 *
	 * @return yyyyMMddHHmmss
	 */
	public static String getCurrentDateTimeShort() {
		return getCurrentDateTime(CommonConstant.DATETIME_FORMATTER_SHORT);
	}

	/**
	 * 获取当前日期
	 *
	 * @param pattern 格式化
	 * @return 当前时间
	 */
	public static String getCurrentDate(String pattern) {
		return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 获取下一天日期
	 *
	 * @param pattern 格式化
	 */
	public static String getNextDate(String pattern) {
		return LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 获取当前时间
	 *
	 * @param pattern 格式化
	 */
	public static String getCurrentTime(String pattern) {
		return LocalTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 获取当前日期时间
	 *
	 * @param pattern 格式化
	 */
	public static String getCurrentDateTime(String pattern) {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 时间戳转日期
	 *
	 * @param timestamp 时间戳
	 */
	public static LocalDateTime timestampToLocalDateTime(long timestamp) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	/**
	 * 日期转时间戳
	 *
	 * @param localDateTime 日期
	 */
	public static long localDateTimeToTimestamp(LocalDateTime localDateTime) {
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * 时间戳转日期时间
	 *
	 * @param timestamp 时间戳
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String formatTimestamp(long timestamp) {
		return formatTimestamp(timestamp, CommonConstant.DATETIME_FORMAT);
	}

	/**
	 * 时间戳转日期时间
	 *
	 * @param timestamp 时间戳
	 * @return yyyyMMddHHmmss
	 */
	public static String formatTimestampShort(long timestamp) {
		return formatTimestamp(timestamp, CommonConstant.DATETIME_FORMATTER_SHORT);
	}

	/**
	 * 时间戳转日期
	 *
	 * @param timestamp 时间戳
	 * @param pattern   格式化
	 */
	public static String formatTimestamp(long timestamp, String pattern) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return formatLocalDateTime(localDateTime, pattern);
	}

	/**
	 * 日期格式化字符串
	 *
	 * @param localDate 日期
	 * @return yyyy-MM-dd
	 */
	public static String formatLocalDate(LocalDate localDate) {
		return formatLocalDate(localDate, CommonConstant.DATE_FORMATTER);
	}

	/**
	 * 日期格式化字符串
	 *
	 * @param localDate 日期
	 * @return yyyyMMdd
	 */
	public static String formatLocalDateShort(LocalDate localDate) {
		return formatLocalDate(localDate, CommonConstant.DATE_FORMATTER_SHORT);
	}

	/**
	 * 时间格式化字符串
	 *
	 * @param localTime 时间
	 * @return HH:mm:ss
	 */
	public static String formatLocalTime(LocalTime localTime) {
		return formatLocalTime(localTime, CommonConstant.TIME_FORMATTER);
	}

	/**
	 * 时间格式化字符串
	 *
	 * @param localTime 时间
	 * @return HHmmss
	 */
	public static String formatLocalTimeShort(LocalTime localTime) {
		return formatLocalTime(localTime, CommonConstant.TIME_FORMATTER_SHORT);
	}

	/**
	 * 日期时间格式化字符串
	 *
	 * @param localDateTime 日期时间
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String formatLocalDateTime(LocalDateTime localDateTime) {
		return formatLocalDateTime(localDateTime, CommonConstant.DATETIME_FORMAT);
	}

	/**
	 * 日期时间格式化字符串
	 *
	 * @param localDateTime 日期时间
	 * @return yyyyMMddHHmmss
	 */
	public static String formatLocalDateTimeShort(LocalDateTime localDateTime) {
		return formatLocalDateTime(localDateTime, CommonConstant.DATETIME_FORMATTER_SHORT);
	}

	/**
	 * 日期格式化字符串
	 *
	 * @param localDate 日期
	 * @param pattern   格式化
	 */
	public static String formatLocalDate(LocalDate localDate, String pattern) {
		return localDate.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 时间格式化字符串
	 *
	 * @param localTime 时间
	 * @param pattern   格式化
	 */
	public static String formatLocalTime(LocalTime localTime, String pattern) {
		return localTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 日期时间格式化字符串
	 *
	 * @param localDateTime 日期时间
	 * @param pattern       格式化
	 */
	public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
		return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 日期字符串转日期
	 *
	 * @param date    日期字符串
	 * @param pattern 格式化
	 */
	public static LocalDate parseLocalDate(String date, String pattern) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 时间字符串转时间
	 *
	 * @param time    时间字符串
	 * @param pattern 格式化
	 */
	public static LocalTime parseLocalTime(String time, String pattern) {
		return LocalTime.parse(time, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 日期时间字符串转日期时间
	 *
	 * @param dateTime 日期时间字符串
	 * @param pattern  格式化
	 */
	public static LocalDateTime parseLocalDateTime(String dateTime, String pattern) {
		return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 获取本周第一天
	 */
	public static LocalDate getCurrentWeekFirstDate() {
		return LocalDate.now().minusWeeks(0).with(DayOfWeek.MONDAY);
	}

	/**
	 * 获取本周最后一天
	 */
	public static LocalDate getCurrentWeekLastDate() {
		return LocalDate.now().minusWeeks(0).with(DayOfWeek.SUNDAY);
	}

	/**
	 * 获取本月第一天
	 */
	public static LocalDate getCurrentMonthFirstDate() {
		return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
	}

	/**
	 * 获取本月最后一天
	 */
	public static LocalDate getCurrentMonthLastDate() {
		return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
	}

	/**
	 * 获取指定周第一天
	 *
	 * @param date    日期字符串
	 * @param pattern 格式化
	 */
	public static LocalDate getWeekFirstDate(String date, String pattern) {
		return parseLocalDate(date, pattern).minusWeeks(0).with(DayOfWeek.MONDAY);
	}

	/**
	 * 获取指定周第一天
	 *
	 * @param localDate 日期
	 */
	public static LocalDate getWeekFirstDate(LocalDate localDate) {
		return localDate.minusWeeks(0).with(DayOfWeek.MONDAY);
	}

	/**
	 * 获取指定周最后一天
	 *
	 * @param date    日期字符串
	 * @param pattern 格式化
	 */
	public static LocalDate getWeekLastDate(String date, String pattern) {
		return parseLocalDate(date, pattern).minusWeeks(0).with(DayOfWeek.SUNDAY);
	}

	/**
	 * 获取指定周最后一天
	 *
	 * @param localDate 日期
	 */
	public static LocalDate getWeekLastDate(LocalDate localDate) {
		return localDate.minusWeeks(0).with(DayOfWeek.SUNDAY);
	}


	/**
	 * 获取指定月份月第一天
	 *
	 * @param date    日期字符串
	 * @param pattern 格式化
	 */
	public static LocalDate getMonthFirstDate(String date, String pattern) {
		return parseLocalDate(date, pattern).with(TemporalAdjusters.firstDayOfMonth());
	}

	/**
	 * 获取指定月份月第一天
	 *
	 * @param localDate
	 */
	public static LocalDate getMonthFirstDate(LocalDate localDate) {
		return localDate.with(TemporalAdjusters.firstDayOfMonth());
	}

	/**
	 * 获取当前星期
	 *
	 * @return 1:星期一、2:星期二、3:星期三、4:星期四、5:星期五、6:星期六、7:星期日
	 */
	public static int getCurrentWeek() {
		return LocalDate.now().getDayOfWeek().getValue();
	}


	/**
	 * 获取当前星期
	 *
	 * @param localDate 日期
	 */
	public static int getWeek(LocalDate localDate) {
		return localDate.getDayOfWeek().getValue();
	}

	/**
	 * 获取指定日期的星期
	 *
	 * @param date    日期字符串
	 * @param pattern 格式化
	 */
	public static int getWeek(String date, String pattern) {
		return parseLocalDate(date, pattern).getDayOfWeek().getValue();
	}

	/**
	 * 日期相隔天数
	 *
	 * @param startLocalDate 起日期
	 * @param endLocalDate   止日期
	 */
	public static long intervalDays(LocalDate startLocalDate, LocalDate endLocalDate) {
		return endLocalDate.toEpochDay() - startLocalDate.toEpochDay();
	}

	/**
	 * 日期相隔小时
	 *
	 * @param startLocalDateTime 起日期时间
	 * @param endLocalDateTime   止日期时间
	 */
	public static long intervalHours(LocalDateTime startLocalDateTime,
		LocalDateTime endLocalDateTime) {
		return Duration.between(startLocalDateTime, endLocalDateTime).toHours();
	}

	/**
	 * 日期相隔分钟
	 *
	 * @param startLocalDateTime 起日期时间
	 * @param endLocalDateTime   止日期时间
	 */
	public static long intervalMinutes(LocalDateTime startLocalDateTime,
		LocalDateTime endLocalDateTime) {
		return Duration.between(startLocalDateTime, endLocalDateTime).toMinutes();
	}

	/**
	 * 日期相隔毫秒数
	 *
	 * @param startLocalDateTime 起日期时间
	 * @param endLocalDateTime   止日期时间
	 */
	public static long intervalMillis(LocalDateTime startLocalDateTime,
		LocalDateTime endLocalDateTime) {
		return Duration.between(startLocalDateTime, endLocalDateTime).toMillis();
	}

	/**
	 * 当前是否闰年
	 */
	public static boolean isCurrentLeapYear() {
		return LocalDate.now().isLeapYear();
	}

	/**
	 * 是否闰年
	 */
	public static boolean isLeapYear(LocalDate localDate) {
		return localDate.isLeapYear();
	}

	/**
	 * 是否当天
	 *
	 * @param localDate 日期
	 */
	public static boolean isToday(LocalDate localDate) {
		return LocalDate.now().equals(localDate);
	}

	/**
	 * 获取此日期时间与默认时区<Asia/Shanghai>组合的时间毫秒数
	 *
	 * @param localDateTime 日期时间
	 */
	public static Long toEpochMilli(LocalDateTime localDateTime) {
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * 获取此日期时间与指定时区组合的时间毫秒数
	 *
	 * @param localDateTime 日期时间
	 */
	public static Long toSelectEpochMilli(LocalDateTime localDateTime, ZoneId zoneId) {
		return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
	}

	/**
	 * 计算距今天指定天数的日期
	 */
	public static String getDateAfterDays(int days) {
		Calendar date = Calendar.getInstance();// today
		date.add(Calendar.DATE, days);
		SimpleDateFormat simpleDate = new SimpleDateFormat(CommonConstant.DATE_FORMATTER);
		return simpleDate.format(date.getTime());
	}

	/**
	 * 在指定的日期的前几天或后几天
	 *
	 * @param source 源日期(yyyy-MM-dd)
	 * @param days   指定的天数,正负皆可
	 */
	public static String addDays(String source, int days) {
		Date date = localDateToDate(parseLocalDate(source, CommonConstant.DATE_FORMATTER));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		SimpleDateFormat dateFormat = new SimpleDateFormat(CommonConstant.DATE_FORMATTER);
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * LocalDate转换成Date
	 *
	 * @param localDate
	 * @return
	 */
	public static Date localDateToDate(LocalDate localDate) {
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
		return Date.from(instant);
	}

	/**
	 * 24小时时间校验
	 */
	public static boolean isValidate24(String time) {
		Pattern p = Pattern.compile(MATCH_TIME_24);
		return p.matcher(time).matches();
	}

	/**
	 * 日期校验
	 */
	public static boolean isDate(String date) {
		Pattern pat = Pattern.compile(REGEX_DATA);
		Matcher mat = pat.matcher(date);
		return mat.matches();
	}


}
