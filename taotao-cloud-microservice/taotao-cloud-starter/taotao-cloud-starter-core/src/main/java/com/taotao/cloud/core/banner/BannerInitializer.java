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
 */
public class BannerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		if (!(applicationContext instanceof AnnotationConfigApplicationContext)) {
			LogoBanner logoBanner = new LogoBanner(BannerInitializer.class, "/taotaocloud/banner.txt", "Welcome to taotaocloud", 5, 6, new Color[5], true);
			CustomBanner.show(logoBanner,
				new Description(BannerConstant.VERSION + ":", CommonConstant.PROJECT_VERSION, 0, 1)
				, new Description("Github:", "https://github.com/shuigedeng", 0, 1)
				, new Description("Blog:", "https://shuigedeng.gitee.io", 0, 1)
			);
		}
	}
}
