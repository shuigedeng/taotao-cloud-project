package com.taotao.cloud.sys.biz.config.i18n;

import com.taotao.cloud.sys.biz.config.i18n.provider.CustomI18nMessageProvider;
import com.taotao.cloud.sys.biz.service.business.I18nDataService;
import com.taotao.boot.web.i18n.I18nMessageProvider;
import com.taotao.boot.web.i18n.config.I18nMessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 注册一个 I18nMessageProvider
 */
//@AutoConfiguration(before = {I18nMessageSourceAutoConfiguration.class, MessageEventListenerAutoConfiguration.class})
@AutoConfiguration(before = {I18nMessageSourceAutoConfiguration.class})
//@MapperScan("com.hccake.ballcat.i18n.mapper")
//@ComponentScan("com.hccake.ballcat.i18n")
public class AdminI18nAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(I18nMessageProvider.class)
	public CustomI18nMessageProvider i18nMessageProvider(I18nDataService i18nDataService) {
		return new CustomI18nMessageProvider(i18nDataService);
	}

}
