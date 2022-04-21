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
package com.taotao.cloud.core.initializer;

import static com.taotao.cloud.common.constant.CommonConstant.SPRING_BOOT_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_BACKEND;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_BACKEND_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_BLOG;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_BLOG_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_DATAV;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_DATAV_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_DEFAULT;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_DEFAULT_RESOURCE_LOCATION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_GITEE;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_GITEE_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_GITHUB;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_GITHUB_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_M;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_MANAGER;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_MANAGER_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_MERCHANT;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_MERCHANT_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_M_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_OPEN;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_OPEN_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_BANNER_URL;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_SPRING_BOOT_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_SPRING_CLOUD_ALIBABA_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_SPRING_CLOUD_DEPENDENCIES_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_SPRING_CLOUD_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_SPRING_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.TAOTAO_CLOUD_VERSION;
import static com.taotao.cloud.common.constant.CommonConstant.VERSION;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.DescriptionBanner;
import com.nepxion.banner.LogoBanner;
import com.taobao.text.Color;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.version.SpringCloudAlibabaVersion;
import com.taotao.cloud.core.version.SpringCloudDependenciesVersion;
import com.taotao.cloud.core.version.SpringCloudVersion;
import com.taotao.cloud.core.version.TaoTaoCloudVersion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		LogUtil.started(BannerInitializer.class, StarterName.CORE_STARTER);

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
				new Description(TAOTAO_CLOUD_VERSION,
					TaoTaoCloudVersion.getVersion() == null ? environment.getProperty(VERSION, "")
						: TaoTaoCloudVersion.getVersion(),
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_SPRING_VERSION, SpringVersion.getVersion(),
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_SPRING_BOOT_VERSION,
					environment.getProperty(SPRING_BOOT_VERSION, SpringBootVersion.getVersion()),
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_SPRING_CLOUD_VERSION, SpringCloudVersion.getVersion(),
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_SPRING_CLOUD_DEPENDENCIES_VERSION,
					SpringCloudDependenciesVersion.getVersion(),
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_SPRING_CLOUD_ALIBABA_VERSION,
					SpringCloudAlibabaVersion.getVersion(),
					leftCellPadding, rightCellPadding),

				new Description("", "",
					leftCellPadding, rightCellPadding),

				new Description(TAOTAO_CLOUD_BANNER, TAOTAO_CLOUD_BANNER_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_BLOG, TAOTAO_CLOUD_BANNER_BLOG_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_M, TAOTAO_CLOUD_BANNER_M_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_DATAV, TAOTAO_CLOUD_BANNER_DATAV_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_MANAGER, TAOTAO_CLOUD_BANNER_MANAGER_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_MERCHANT, TAOTAO_CLOUD_BANNER_MERCHANT_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_OPEN, TAOTAO_CLOUD_BANNER_OPEN_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_BACKEND, TAOTAO_CLOUD_BANNER_BACKEND_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_GITEE, TAOTAO_CLOUD_BANNER_GITEE_URL,
					leftCellPadding, rightCellPadding),
				new Description(TAOTAO_CLOUD_BANNER_GITHUB, TAOTAO_CLOUD_BANNER_GITHUB_URL,
					leftCellPadding, rightCellPadding)
			);
		}
	}

	/**
	 * show
	 *
	 * @param logoBanner      logoBanner
	 * @param descriptionList descriptionList
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

		List<Description> descriptions = new ArrayList<>(Arrays.asList(descriptionList));

		DescriptionBanner descriptionBanner = new DescriptionBanner();
		System.out.println(descriptionBanner.getBanner(descriptions));
	}
}
