package com.taotao.cloud.core.banner;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.DescriptionBanner;
import com.nepxion.banner.LogoBanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zlt
 * @date 2019/8/28
 */
public class CustomBanner {
	public static void show(LogoBanner logoBanner, Description... descriptionList) {
		String bannerShown = System.getProperty(BannerConstant.BANNER_SHOWN, "true");
		if (!Boolean.parseBoolean(bannerShown)) {
			return;
		}

		System.out.println("");
		String bannerShownAnsiMode = System.getProperty(BannerConstant.BANNER_SHOWN_ANSI_MODE, "false");
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
