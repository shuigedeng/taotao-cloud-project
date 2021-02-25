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
package com.taotao.cloud.core.banner;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.taobao.text.Color;
import com.taotao.cloud.common.constant.CommonConstant;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Banner初始化
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 09:12
 */
public class BannerInitializer implements
	ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		if (!(applicationContext instanceof AnnotationConfigApplicationContext)) {
			LogoBanner logoBanner = new LogoBanner(BannerInitializer.class,
				"/taotaocloud/banner.txt", "Welcome to taotaocloud", 5, 6, new Color[5], true);
			CustomBanner.show(logoBanner,
				new Description(BannerConstant.VERSION + ":", CommonConstant.PROJECT_VERSION, 0, 1)
				, new Description("Github:", "https://github.com/shuigedeng", 0, 1)
				, new Description("Blog:", "https://shuigedeng.gitee.io", 0, 1)
			);
		}
	}
}
