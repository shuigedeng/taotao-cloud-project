package com.taotao.cloud.web.configuration;

import com.baomidou.mybatisplus.extension.api.R;
import com.taotao.cloud.web.strategy.BusinessHandler;
import com.taotao.cloud.web.strategy.BusinessHandlerChooser;
import java.util.List;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.context.annotation.Bean;

/**
 * 策略模式自动注入配置
 *
 * @version 1.0.0
 * @author shuigedeng
 * @since 2021/8/24 23:48
 */
public class StrategyConfiguration {

	@Bean
	public BusinessHandlerChooser businessHandlerChooser(List<BusinessHandler> businessHandlers) {
		BusinessHandlerChooser businessHandlerChooser = new BusinessHandlerChooser();
		businessHandlerChooser.setBusinessHandlerMap(businessHandlers);
		return businessHandlerChooser;
	}

}
