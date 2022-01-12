/*
 * Copyright 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.service;

import com.taotao.cloud.sms.model.NoticeData;
import com.taotao.cloud.sms.utils.StringUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * 短信通知服务
 *
 * @author shuigedeng
 */
public interface NoticeService {

	/**
	 * 手机号码规则验证
	 *
	 * @param phone 手机号码
	 * @return 是否验证通过
	 */
	boolean phoneRegValidation(String phone);

	/**
	 * 发送通知
	 *
	 * @param noticeData 通知内容
	 * @param phones     手机号列表
	 * @return 是否发送成功
	 */
	boolean send(NoticeData noticeData, Collection<String> phones);

	/**
	 * 发送通知
	 *
	 * @param noticeData 通知内容
	 * @param phone      手机号
	 * @return 是否发送成功
	 */
	default boolean send(NoticeData noticeData, String phone) {
		if (StringUtils.isBlank(phone)) {
			return false;
		}

		return send(noticeData, Collections.singletonList(phone));
	}

	/**
	 * 发送通知
	 *
	 * @param noticeData 通知内容
	 * @param phones     手机号列表
	 * @return 是否发送成功
	 */
	default boolean send(NoticeData noticeData, String... phones) {
		if (phones.length <= 0) {
			return false;
		}

		return send(noticeData, Arrays.asList(phones));
	}

	/**
	 * 异步发送通知,当未启用异步支持的时候默认会直接同步发送
	 *
	 * @param noticeData 通知内容
	 * @param phones     手机号列表
	 */
	void asyncSend(NoticeData noticeData, Collection<String> phones);

	/**
	 * 异步发送通知,当未启用异步支持的时候默认会直接同步发送
	 *
	 * @param noticeData 通知内容
	 * @param phone      手机号
	 */
	default void asyncSend(NoticeData noticeData, String phone) {
		if (!StringUtils.isBlank(phone)) {
			asyncSend(noticeData, Collections.singletonList(phone));
		}
	}

	/**
	 * 异步发送通知,当未启用异步支持的时候默认会直接同步发送
	 *
	 * @param noticeData 通知内容
	 * @param phones     手机号列表
	 */
	default void asyncSend(NoticeData noticeData, String... phones) {
		if (phones.length > 0) {
			asyncSend(noticeData, Arrays.asList(phones));
		}
	}
}
