package com.taotao.cloud.gateway.configuration;

import com.taotao.cloud.gateway.anti_reptile.AntiReptileProperties;
import com.taotao.cloud.gateway.anti_reptile.ValidateFormService;
import com.taotao.cloud.gateway.anti_reptile.filter.AntiReptileFilter;
import com.taotao.cloud.gateway.anti_reptile.handler.RefreshFormHandler;
import com.taotao.cloud.gateway.anti_reptile.handler.ValidateFormHandler;
import com.taotao.cloud.gateway.anti_reptile.rule.AntiReptileRule;
import com.taotao.cloud.gateway.anti_reptile.rule.RuleActuator;
import com.taotao.cloud.gateway.anti_reptile.rule.rulers.IpRule;
import com.taotao.cloud.gateway.anti_reptile.rule.rulers.UaRule;
import com.taotao.cloud.gateway.anti_reptile.util.VerifyImageUtil;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(RedissonClient.class)
@EnableConfigurationProperties(AntiReptileProperties.class)
@ConditionalOnProperty(prefix = AntiReptileProperties.PREFIX, value = "enabled", havingValue = "true")
public class AntiReptileConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = AntiReptileProperties.PREFIX, name = "ip-rule", value = "enabled", havingValue = "true", matchIfMissing = true)
	public IpRule ipRule(RedissonClient redissonClient, AntiReptileProperties properties) {
		return new IpRule(redissonClient, properties);
	}

	@Bean
	@ConditionalOnProperty(prefix = AntiReptileProperties.PREFIX, name = "ua-rule", value = "enabled", havingValue = "true", matchIfMissing = true)
	public UaRule uaRule(AntiReptileProperties properties) {
		return new UaRule(properties);
	}

	@Bean
	public RuleActuator ruleActuator(final List<AntiReptileRule> rules) {
		final List<AntiReptileRule> antiReptileRules = rules.stream()
			.sorted(Comparator.comparingInt(AntiReptileRule::getOrder))
			.collect(Collectors.toList());
		return new RuleActuator(antiReptileRules);
	}

	@Bean
	public VerifyImageUtil verifyImageUtil(RedissonClient redissonClient) {
		return new VerifyImageUtil(redissonClient);
	}

	@Bean
	public ValidateFormService validateFormService(RuleActuator actuator,
		VerifyImageUtil verifyImageUtil) {
		return new ValidateFormService(actuator, verifyImageUtil);
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
