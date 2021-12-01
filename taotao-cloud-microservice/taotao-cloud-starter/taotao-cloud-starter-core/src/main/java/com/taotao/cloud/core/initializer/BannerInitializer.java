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
package com.taotao.cloud.core.initializer;

import static com.taotao.cloud.common.constant.CommonConstant.SPRING_BOOT_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.SPRING_CLOUD_ALIBABA_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.SPRING_CLOUD_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.SPRING_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_BACKEND;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_BACKEND_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_BLOG;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_BLOG_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_DATAX;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_DATAX_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_DEFAULT;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_DEFAULT_RESOURCE_LOCATION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_GITHUB;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_GITHUB_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_SPRING_BOOT_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_SPRING_CLOUD_ALIBABA_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_SPRING_CLOUD_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_SPRING_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.VERSION;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.DescriptionBanner;
import com.nepxion.banner.LogoBanner;
import com.taobao.text.Color;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Banner初始化
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:22:33
 */
@Order(2)
public class BannerInitializer implements
	ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		LogUtil.started(BannerInitializer.class, StarterNameConstant.CLOUD_STARTER);

		if (!(applicationContext instanceof AnnotationConfigApplicationContext)) {
			ConfigurableEnvironment environment = applicationContext.getEnvironment();
			System.out.println();

			LogoBanner logoBanner = new LogoBanner(
				BannerInitializer.class,
				TAOTAO_CLOUD_BANNER_DEFAULT_RESOURCE_LOCATION,
				TAOTAO_CLOUD_BANNER_DEFAULT,
				1,
				6,
				new Color[]{Color.red},
				true
			);

			int leftCellPadding = 0;
			int rightCellPadding = 1;

			show(logoBanner,
				new Description(TAOTAO_CLOUD_BANNER, TAOTAO_CLOUD_BANNER_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_DATAX, TAOTAO_CLOUD_BANNER_DATAX_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_BACKEND, TAOTAO_CLOUD_BANNER_BACKEND_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_GITHUB, TAOTAO_CLOUD_BANNER_GITHUB_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_BLOG, TAOTAO_CLOUD_BANNER_BLOG_URL,
					leftCellPadding, rightCellPadding),

				new Description(TAOTAO_CLOUD_VERSION, environment.getProperty(VERSION, ""),
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_SPRING_VERSION, environment.getProperty(SPRING_VERSION,
					Objects.requireNonNull(SpringVersion.getVersion())),
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_SPRING_BOOT_VERSION,
					environment.getProperty(SPRING_BOOT_VERSION, SpringBootVersion.getVersion()),
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_SPRING_CLOUD_VERSION,
					environment.getProperty(SPRING_CLOUD_VERSION, ""),
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_SPRING_CLOUD_ALIBABA_VERSION,
					environment.getProperty(SPRING_CLOUD_ALIBABA_VERSION, ""),
					leftCellPadding, rightCellPadding)
			);
		}
	}

	/**
	 * show
	 *
	 * @param logoBanner      logoBanner
	 * @param descriptionList descriptionList
	 * @author shuigedeng
	 * @since 2021-09-02 20:22:45
	 */
	public void show(LogoBanner logoBanner, Description... descriptionList) {
		String bannerShown = System.getProperty(BannerConstant.BANNER_SHOWN, "true");
		if (!Boolean.parseBoolean(bannerShown)) {
			return;
		}

		String bannerShownAnsiMode = System
			.getProperty(BannerConstant.BANNER_SHOWN_ANSI_MODE, "true");
		if (Boolean.parseBoolean(bannerShownAnsiMode)) {
			System.out.println(logoBanner.getBanner());
		} else {
			System.out.println(logoBanner.getPlainBanner());
		}

		List<Description> descriptions = new ArrayList<>();
		descriptions.addAll(Arrays.asList(descriptionList));

		DescriptionBanner descriptionBanner = new DescriptionBanner();
		System.out.println(descriptionBanner.getBanner(descriptions));
	}
}
