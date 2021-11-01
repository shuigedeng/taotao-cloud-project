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
package com.taotao.cloud.captcha.service.impl;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.captcha.model.Captcha;
import com.taotao.cloud.captcha.model.CaptchaCodeEnum;
import com.taotao.cloud.captcha.model.CaptchaException;
import com.taotao.cloud.captcha.model.CaptchaConst;
import com.taotao.cloud.captcha.service.CaptchaCacheService;
import java.util.Objects;
import java.util.Properties;

/**
 * FrequencyLimitHandler
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 21:02:19
 */
public interface FrequencyLimitHandler {

	String LIMIT_KEY = "AJ.CAPTCHA.REQ.LIMIT-%s-%s";

	/**
	 * get 接口限流
	 *
	 * @param captcha captcha
	 * @author shuigedeng
	 * @since 2021-09-03 21:02:43
	 */
	void validateGet(Captcha captcha);

	/**
	 * check接口限流
	 *
	 * @param captcha captcha
	 * @author shuigedeng
	 * @since 2021-09-03 21:02:49
	 */
	void validateCheck(Captcha captcha);

	/**
	 * verify接口限流
	 *
	 * @param captcha captcha
	 * @author shuigedeng
	 * @since 2021-09-03 21:02:53
	 */
	void validateVerify(Captcha captcha);


	/**
	 * 验证码接口限流: 客户端ClientUid 组件实例化时设置一次，如：场景码+UUID，客户端可以本地缓存,保证一个组件只有一个值
	 * <p>
	 * 针对同一个客户端的请求，做如下限制: get 1分钟内check失败5次，锁定5分钟 1分钟内不能超过120次。 check: 1分钟内不超过600次 verify:
	 * 1分钟内不超过600次
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-03 21:02:28
	 */
	class DefaultLimitHandler implements FrequencyLimitHandler {

		private Properties config;
		private CaptchaCacheService cacheService;

		public DefaultLimitHandler(Properties config, CaptchaCacheService cacheService) {
			this.config = config;
			this.cacheService = cacheService;
		}

		private String getClientCId(Captcha input, String type) {
			return String.format(LIMIT_KEY, type, input.getClientUid());
		}

		@Override
		public void validateGet(Captcha captcha) {
			// 无客户端身份标识，不限制
			if (StrUtil.isEmpty(captcha.getClientUid())) {
				throw new CaptchaException("客户端身份标识不能为空");
			}

			// 失败次数过多，锁定
			String lockKey = getClientCId(captcha, "LOCK");
			if (Objects.nonNull(cacheService.get(lockKey))) {
				throw new CaptchaException(CaptchaCodeEnum.API_REQ_LOCK_GET_ERROR);
			}

			String getKey = getClientCId(captcha, "GET");
			String getCnts = cacheService.get(getKey);
			if (Objects.isNull(getCnts)) {
				cacheService.set(getKey, "1", 60);
				getCnts = "1";
			}
			cacheService.increment(getKey, 1);

			// 1分钟内请求次数过多
			if (Long.parseLong(getCnts) > Long.parseLong(
				config.getProperty(CaptchaConst.REQ_GET_MINUTE_LIMIT, "120"))) {
				throw new CaptchaException(CaptchaCodeEnum.API_REQ_LIMIT_GET_ERROR);
			}

			// 失败次数验证
			String failKey = getClientCId(captcha, "FAIL");
			String failCnts = cacheService.get(failKey);
			// 没有验证失败，通过校验
			if (Objects.isNull(failCnts)) {
				return;
			}

			// 1分钟内失败5次
			if (Long.parseLong(failCnts) > Long.parseLong(
				config.getProperty(CaptchaConst.REQ_GET_LOCK_LIMIT, "5"))) {
				// get接口锁定5分钟
				cacheService.set(lockKey, "1",
					Long.parseLong(config.getProperty(CaptchaConst.REQ_GET_LOCK_SECONDS, "300")));
				throw new CaptchaException(CaptchaCodeEnum.API_REQ_LOCK_GET_ERROR);
			}
		}

		@Override
		public void validateCheck(Captcha captcha) {
			// 无客户端身份标识，不限制
			if (StrUtil.isEmpty(captcha.getClientUid())) {
				throw new CaptchaException("客户端身份标识不能为空");
			}
            /*String getKey = getClientCId(d, "GET");
            if(Objects.isNull(cacheService.get(getKey))){
                return ResponseModel.errorMsg(RepCodeEnum.API_REQ_INVALID);
            }*/

			String key = getClientCId(captcha, "CHECK");
			String v = cacheService.get(key);
			if (Objects.isNull(v)) {
				cacheService.set(key, "1", 60);
				v = "1";
			}
			cacheService.increment(key, 1);
			if (Long.parseLong(v) > Long.parseLong(
				config.getProperty(CaptchaConst.REQ_CHECK_MINUTE_LIMIT, "600"))) {
				throw new CaptchaException(CaptchaCodeEnum.API_REQ_LIMIT_CHECK_ERROR);
			}
		}

		@Override
		public void validateVerify(Captcha captcha) {
            /*String getKey = getClientCId(d, "GET");
            if(Objects.isNull(cacheService.get(getKey))){
                return ResponseModel.errorMsg(RepCodeEnum.API_REQ_INVALID);
            }*/
			String key = getClientCId(captcha, "VERIFY");
			String v = cacheService.get(key);
			if (Objects.isNull(v)) {
				cacheService.set(key, "1", 60);
				v = "1";
			}

			cacheService.increment(key, 1);
			if (Long.parseLong(v) > Long.parseLong(
				config.getProperty(CaptchaConst.REQ_VALIDATE_MINUTE_LIMIT, "600"))) {
				throw new CaptchaException(CaptchaCodeEnum.API_REQ_LIMIT_VERIFY_ERROR);
			}
		}
	}
}
