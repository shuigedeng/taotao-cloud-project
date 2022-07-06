package com.taotao.cloud.web.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.web.strategy.BusinessHandler;
import com.taotao.cloud.web.strategy.BusinessHandlerChooser;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 策略模式自动注入配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:13:57
 */
@AutoConfiguration
public class StrategyAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(StrategyAutoConfiguration.class, StarterName.WEB_STARTER);
	}

	@Bean
	public BusinessHandlerChooser businessHandlerChooser(List<BusinessHandler> businessHandlers) {
		LogUtil.started(BusinessHandlerChooser.class, StarterName.WEB_STARTER);

		BusinessHandlerChooser businessHandlerChooser = new BusinessHandlerChooser();
		businessHandlerChooser.setBusinessHandlerMap(businessHandlers);
		return businessHandlerChooser;
	}

}
