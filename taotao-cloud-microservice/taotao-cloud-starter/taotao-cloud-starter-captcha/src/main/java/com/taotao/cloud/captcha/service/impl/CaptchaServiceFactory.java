/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

import com.taotao.cloud.captcha.model.CaptchaConst;
import com.taotao.cloud.captcha.service.CaptchaCacheService;
import com.taotao.cloud.captcha.service.CaptchaService;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 * CaptchaServiceFactory
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 21:00:16
 */
public class CaptchaServiceFactory {

	/**
	 * instances
	 */
	public volatile static Map<String, CaptchaService> instances = new HashMap<>();
	/**
	 * cacheService
	 */
	public volatile static Map<String, CaptchaCacheService> cacheService = new HashMap<>();

	static {
		ServiceLoader<CaptchaCacheService> cacheServices = ServiceLoader.load(CaptchaCacheService.class);
		for (CaptchaCacheService item : cacheServices) {
			cacheService.put(item.type(), item);
		}

		ServiceLoader<CaptchaService> services = ServiceLoader.load(CaptchaService.class);
		for (CaptchaService item : services) {
			instances.put(item.captchaType(), item);
		}
	}

	/**
	 * getInstance
	 *
	 * @param config config
	 * @return {@link CaptchaService }
	 * @since 2022-09-02 10:27:21
	 */
	public static CaptchaService getInstance(Properties config) {
		//先把所有CaptchaService初始化，通过init方法，实例字体等，add by lide1202@hotmail.com
        /*try{
            for(CaptchaService item: instances.values()){
                item.init(config);
            }
        }catch (Exception e){
            logger.warn("init captchaService fail:{}", e);
        }*/

		String captchaType = config.getProperty(CaptchaConst.CAPTCHA_TYPE, "default");
		CaptchaService ret = instances.get(captchaType);
		if (ret == null) {
			throw new RuntimeException("unsupported-[captcha.type]=" + captchaType);
		}

		ret.init(config);
		return ret;
	}

	public static CaptchaCacheService getCache(String cacheType) {
		return cacheService.get(cacheType);
	}
}
