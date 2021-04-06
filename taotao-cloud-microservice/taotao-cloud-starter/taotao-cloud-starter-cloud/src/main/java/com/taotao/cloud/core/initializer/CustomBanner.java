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

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.DescriptionBanner;
import com.nepxion.banner.LogoBanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义Banner
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 09:12
 */
public class CustomBanner {

	public static void show(LogoBanner logoBanner, Description... descriptionList) {
		String bannerShown = System.getProperty(BannerConstant.BANNER_SHOWN, "true");
		if (!Boolean.parseBoolean(bannerShown)) {
			return;
		}

		System.out.println("");
		String bannerShownAnsiMode = System
			.getProperty(BannerConstant.BANNER_SHOWN_ANSI_MODE, "false");
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
