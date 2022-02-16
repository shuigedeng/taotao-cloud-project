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
package com.taotao.cloud.common.constant;

/**
 * RedisConstant
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:35:46
 */
public class RedisConstant {

	/**
	 * 用户退出时JWT标识KEY
	 */
	public static final String LOGOUT_JWT_KEY_PREFIX = "LOGOUT:JWT:KEY:";

	/**
	 * 图形验证码KEY
	 */
	public static final String CAPTCHA_KEY_PREFIX = "CAPTCHA:KEY:";

	/**
	 * 短信验证码KEY
	 */
	public static final String SMS_KEY_PREFIX = "SMS:KEY:";

	/**
	 * 短信校验码KEY
	 */
	public static final String SMS_VERIFICATION_CODE_KEY_PREFIX = "SMS:VERIFICATION:CODE:KEY:";

	/**
	 * 分布式锁key
	 */
	public static final String LOCK_KEY_PREFIX = "LOCK:KEY:";

	/**
	 * 敏感词key
	 */
	public static final String SENSITIVE_WORDS_KEY = "SENSITIVE:WORDS:KEY";

	/**
	 * regions patten key
	 */
	public static final String REGIONS_PATTERN = "REGIONS:";
	/**
	 * regions key
	 */
	public static final String REGIONS_KEY = "REGIONS:KEY";
	/**
	 * regions all city key
	 */
	public static final String REGIONS_ALL_CITY_KEY = "REGIONS:ALL:CITY:KEY";
	/**
	 * region parent id key
	 */
	public static final String REGIONS_PARENT_ID_KEY = "REGIONS:PARENT:ID:KEY:";

	// ============================ topic =======================================

	/**
	 * QUARTZ_JOB_ADD_TOPIC
	 */
	public static final String QUARTZ_JOB_ADD_TOPIC = "QUARTZ:JOB:ADD:TOPIC";
	/**
	 * QUARTZ_JOB_DELETE_TOPIC
	 */
	public static final String QUARTZ_JOB_DELETE_TOPIC = "QUARTZ:JOB:DELETE:TOPIC";
	/**
	 * QUARTZ_JOB_RESUME_TOPIC
	 */
	public static final String QUARTZ_JOB_RESUME_TOPIC = "QUARTZ:JOB:RESUME:TOPIC";
	/**
	 * QUARTZ_JOB_PAUSE_TOPIC
	 */
	public static final String QUARTZ_JOB_PAUSE_TOPIC = "QUARTZ:JOB:PAUSE:TOPIC";
	/**
	 * QUARTZ_JOB_RUN_NOW_TOPIC
	 */
	public static final String QUARTZ_JOB_RUN_NOW_TOPIC = "QUARTZ:JOB:RUN:NOW:TOPIC";
	/**
	 * QUARTZ_JOB_UPDATE_CRON_TOPIC
	 */
	public static final String QUARTZ_JOB_UPDATE_CRON_TOPIC = "QUARTZ:JOB:UPDATE:CR0N:TOPIC";
	/**
	 * QUARTZ_JOB_UPDATE_TOPIC
	 */
	public static final String QUARTZ_JOB_UPDATE_TOPIC = "QUARTZ:JOB:UPDATE:TOPIC";
	/**
	 * QUARTZ_JOB_LOG_ADD_TOPIC
	 */
	public static final String QUARTZ_JOB_LOG_ADD_TOPIC = "QUARTZ:JOB:LOG:ADD:TOPIC";
	/**
	 * SENSITIVE_WORDS_TOPIC
	 */
	public static final String SENSITIVE_WORDS_TOPIC = "SENSITIVE:WORDS:TOPIC";
	/**
	 * SCHEDULED_UPDATE_CRON_TOPIC
	 */
	public static final String SCHEDULED_UPDATE_CRON_TOPIC = "SCHEDULED:UPDATE:CRON:TOPIC";
	/**
	 * SCHEDULED_ADD_CRON_TOPIC
	 */
	public static final String SCHEDULED_ADD_CRON_TOPIC = "SCHEDULED:ADD:CRON:TOPIC";
	/**
	 * SCHEDULED_UPDATE_FIXED_DELAY_TOPIC
	 */
	public static final String SCHEDULED_UPDATE_FIXED_DELAY_TOPIC = "SCHEDULED:UPDATE:FIXED:DELAY:TOPIC";
	/**
	 * SCHEDULED_ADD_FIXED_DELAY_TOPIC
	 */
	public static final String SCHEDULED_ADD_FIXED_DELAY_TOPIC = "SCHEDULED:ADD:FIXED:DELAY:TOPIC";
	/**
	 * SCHEDULED_UPDATE_FIXED_RATE_TOPIC
	 */
	public static final String SCHEDULED_UPDATE_FIXED_RATE_TOPIC = "SCHEDULED:UPDATE:FIXED:RATE:TOPIC";
	/**
	 * SCHEDULED_ADD_FIXED_RATE_TOPIC
	 */
	public static final String SCHEDULED_ADD_FIXED_RATE_TOPIC = "SCHEDULED:ADD:FIXED:RATE:TOPIC";
	/**
	 * SCHEDULED_CANCEL_TOPIC
	 */
	public static final String SCHEDULED_CANCEL_TOPIC = "SCHEDULED:CANCEL:TOPIC";
	/**
	 * SCHEDULED_RUN_ONCE_TOPIC
	 */
	public static final String SCHEDULED_RUN_ONCE_TOPIC = "SCHEDULED:RUN:ONCE:TOPIC";
	/**
	 * SCHEDULED_CALL_OFF_TOPIC
	 */
	public static final String SCHEDULED_CALL_OFF_TOPIC = "SCHEDULED:CALL:OFF:TOPIC";
	/**
	 * SCHEDULED_JOB_LOG_ADD_TOPIC
	 */
	public static final String SCHEDULED_JOB_LOG_ADD_TOPIC = "SCHEDULED:JOB:LOG:ADD:TOPIC";
	/**
	 * REQUEST_LOG_TOPIC
	 */
	public static final String REQUEST_LOG_TOPIC = "REQUEST:LOG:TOPIC";
	/**
	 * REQUEST_LOG
	 */
	public static final String REQUEST_LOG = "REQUEST:LOG:";

}
