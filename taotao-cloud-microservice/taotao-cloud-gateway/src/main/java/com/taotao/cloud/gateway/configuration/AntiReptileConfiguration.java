package com.taotao.cloud.gateway.configuration;

import com.taotao.cloud.gateway.anti_reptile.AntiReptileProperties;
import com.taotao.cloud.gateway.anti_reptile.ValidateFormService;
import com.taotao.cloud.gateway.anti_reptile.filter.AntiReptileFilter;
import com.taotao.cloud.gateway.anti_reptile.handler.RefreshFormHandler;
import com.taotao.cloud.gateway.anti_reptile.handler.ValidateFormHandler;
import com.taotao.cloud.gateway.anti_reptile.rule.AntiReptileRule;
import com.taotao.cloud.gateway.anti_reptile.rule.IpRule;
import com.taotao.cloud.gateway.anti_reptile.rule.RuleActuator;
import com.taotao.cloud.gateway.anti_reptile.rule.UaRule;
import com.taotao.cloud.gateway.anti_reptile.util.VerifyImageUtil;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RedissonAutoConfiguration 的 AutoConfigureOrder 为默认值(0)，此处在它后面加载
 */
@Configuration
@EnableConfigurationProperties(AntiReptileProperties.class)
@ConditionalOnProperty(prefix = "anti.reptile.manager", value = "enabled", havingValue = "true")
public class AntiReptileConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "anti.reptile.manager.ip-rule", value = "enabled", havingValue = "true", matchIfMissing = true)
	public IpRule ipRule() {
		return new IpRule();
	}

	@Bean
	@ConditionalOnProperty(prefix = "anti.reptile.manager.ua-rule", value = "enabled", havingValue = "true", matchIfMissing = true)
	public UaRule uaRule() {
		return new UaRule();
	}

	@Bean
	public VerifyImageUtil verifyImageUtil() {
		return new VerifyImageUtil();
	}

	@Bean
	public RuleActuator ruleActuator(final List<AntiReptileRule> rules) {
		final List<AntiReptileRule> antiReptileRules = rules.stream()
				.sorted(Comparator.comparingInt(AntiReptileRule::getOrder))
				.collect(Collectors.toList());
		return new RuleActuator(antiReptileRules);
	}

	@Bean
	public ValidateFormService validateFormService() {
		return new ValidateFormService();
	}

	@Bean
	public AntiReptileFilter antiReptileFilter() {
		return new AntiReptileFilter();
	}

	@Bean
	public RefreshFormHandler refreshFormHandler() {
		return new RefreshFormHandler();
	}

	@Bean
	public ValidateFormHandler validateFormHandler() {
		return new ValidateFormHandler();
	}
}
