package com.taotao.cloud.pinyin.roses;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.pinyin.roses.api.PinYinApi;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 拼音的自动配置
 */
@AutoConfiguration
public class GunsPinyinAutoConfiguration implements InitializingBean {


	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(GunsPinyinAutoConfiguration.class, StarterName.PINYIN_STARTER);
	}

	/**
	 * 拼音工具接口的封装
	 */
	@Bean
	@ConditionalOnMissingBean(PinYinApi.class)
	public PinYinApi pinYinApi() {
		return new PinyinServiceImpl();
	}

}
