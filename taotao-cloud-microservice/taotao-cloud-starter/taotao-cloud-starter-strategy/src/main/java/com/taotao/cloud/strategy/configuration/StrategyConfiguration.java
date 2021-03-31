package com.taotao.cloud.strategy.configuration;

import com.taotao.cloud.strategy.service.BusinessHandler;
import com.taotao.cloud.strategy.service.BusinessHandlerChooser;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 策略模式自动注入配置
 *
 * @date 2020-9-5
 */
@Configuration
public class StrategyConfiguration {

	@Bean
	public BusinessHandlerChooser businessHandlerChooser(List<BusinessHandler> businessHandlers) {
		BusinessHandlerChooser businessHandlerChooser = new BusinessHandlerChooser();
		businessHandlerChooser.setBusinessHandlerMap(businessHandlers);
		return businessHandlerChooser;
	}

}
