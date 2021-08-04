package com.taotao.cloud.web.configuration;

import com.taotao.cloud.web.strategy.BusinessHandler;
import com.taotao.cloud.web.strategy.BusinessHandlerChooser;
import java.util.List;
import org.springframework.context.annotation.Bean;

/**
 * 策略模式自动注入配置
 */
public class StrategyConfiguration {
	@Bean
	public BusinessHandlerChooser businessHandlerChooser(List<BusinessHandler> businessHandlers) {
		BusinessHandlerChooser businessHandlerChooser = new BusinessHandlerChooser();
		businessHandlerChooser.setBusinessHandlerMap(businessHandlers);
		return businessHandlerChooser;
	}

}
