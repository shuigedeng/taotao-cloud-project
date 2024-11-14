package com.taotao.cloud.ai.spring;

import com.taotao.cloud.ai.spring.XueQiuFinanceService.FinanceRequest;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	@Description("查询指定公司代码的财务信息")
	public Function<FinanceRequest, String> xueQiuFinanceFunction() {
		return new XueQiuFinanceService();
	}
}
