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
package com.taotao.cloud.captcha.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CacheUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 21:03:03
 */
public final class CacheUtil {

	private static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);

	private static final Map<String, Object> CACHE_MAP = new ConcurrentHashMap<String, Object>();

	/**
	 * 缓存最大个数
	 */
	private static Integer CACHE_MAX_NUMBER = 1000;

	/**
	 * 初始化
	 *
	 * @param cacheMaxNumber 缓存最大个数
	 * @param second         定时任务 秒执行清除过期缓存
	 * @since 2021-09-03 21:03:14
	 */
	public static void init(int cacheMaxNumber, long second) {
		CACHE_MAX_NUMBER = cacheMaxNumber;
		if (second > 0L) {
            /*Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    refresh();
                }
            }, 0, second * 1000);*/
			scheduledExecutor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "thd-captcha-cache-clean");
				}
			}, new ThreadPoolExecutor.CallerRunsPolicy());

			scheduledExecutor.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			}, 10, second, TimeUnit.SECONDS);
		}
	}

	private static ScheduledExecutorService scheduledExecutor;

	/**
	 * 缓存刷新,清除过期数据
	 *
	 * @since 2021-09-03 21:03:21
	 */
	public static void refresh() {
		logger.debug("local缓存刷新,清除过期数据");
		for (String key : CACHE_MAP.keySet()) {
			exists(key);
		}
	}

	/**
	 * set
	 *
	 * @param key              key
	 * @param value            value
	 * @param expiresInSeconds expiresInSeconds
	 * @since 2021-09-03 21:03:24
	 */
	public static void set(String key, String value, long expiresInSeconds) {
		//设置阈值，达到即clear缓存
		if (CACHE_MAP.size() > CACHE_MAX_NUMBER * 2) {
			logger.info("CACHE_MAP达到阈值，clear map");
			clear();
		}
		CACHE_MAP.put(key, value);
		if (expiresInSeconds > 0) {
			CACHE_MAP.put(key + "_HoldTime",
				System.currentTimeMillis() + expiresInSeconds * 1000);//缓存失效时间
		}
	}

	/**
	 * delete
	 *
	 * @param key key
	 * @since 2021-09-03 21:03:27
	 */
	public static void delete(String key) {
		CACHE_MAP.remove(key);
		CACHE_MAP.remove(key + "_HoldTime");
	}

	/**
	 * exists
	 *
	 * @param key key
	 * @return boolean
	 * @since 2021-09-03 21:03:30
	 */
	public static boolean exists(String key) {
		Long cacheHoldTime = (Long) CACHE_MAP.get(key + "_HoldTime");
		if (cacheHoldTime == null || cacheHoldTime == 0L) {
			return false;
		}
		if (cacheHoldTime < System.currentTimeMillis()) {
			delete(key);
			return false;
		}
		return true;
	}

	/**
	 * get
	 *
	 * @param key key
	 * @return {@link java.lang.String }
	 * @since 2021-09-03 21:03:33
	 */
	public static String get(String key) {
		if (exists(key)) {
			return (String) CACHE_MAP.get(key);
		}
		return null;
	}

	/**
	 * 删除所有缓存
	 *
	 * @since 2021-09-03 21:03:39
	 */
	public static void clear() {
		logger.debug("have clean all key !");
		CACHE_MAP.clear();
	}
}
