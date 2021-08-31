package com.taotao.cloud.web.configuration;

import com.baomidou.mybatisplus.extension.api.R;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.web.strategy.BusinessHandler;
import com.taotao.cloud.web.strategy.BusinessHandlerChooser;
import java.util.List;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 策略模式自动注入配置
 *
 * @version 1.0.0
 * @author shuigedeng
 * @since 2021/8/24 23:48
 */
@Configuration
public class StrategyConfiguration  implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(StrategyConfiguration.class, StarterName.WEB_STARTER);
	}

	@Bean
	public BusinessHandlerChooser businessHandlerChooser(List<BusinessHandler> businessHandlers) {
		LogUtil.started(BusinessHandlerChooser.class, StarterName.WEB_STARTER);

		BusinessHandlerChooser businessHandlerChooser = new BusinessHandlerChooser();
		businessHandlerChooser.setBusinessHandlerMap(businessHandlers);
		return businessHandlerChooser;
	}

}
