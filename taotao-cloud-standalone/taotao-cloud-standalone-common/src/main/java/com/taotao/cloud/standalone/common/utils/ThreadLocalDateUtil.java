package com.taotao.cloud.standalone.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Classname ThreadLocalDateUtil
 * @Description TODO
 * @Author Created by Lihaodong (alias:小东啊) im.lihaodong@gmail.com
 * @since 2019-09-24 09:54
 * @Version 1.0
 */
public class ThreadLocalDateUtil {

	public static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}
	};


	public static void main(String[] args) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String nowStr = now.format(format);
		System.out.println(nowStr);
	}


}
