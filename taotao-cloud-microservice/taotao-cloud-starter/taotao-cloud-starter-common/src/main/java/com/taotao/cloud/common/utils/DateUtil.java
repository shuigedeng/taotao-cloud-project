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

import com.taotao.cloud.common.constant.CommonConstant;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	/**
	 * 判断当前时间是否在指定时间范围
	 *
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return 结果
	 */
	public static boolean between(LocalTime from, LocalTime to) {
		LocalTime now = LocalTime.now();
		return now.isAfter(from) && now.isBefore(to);
	}

}
