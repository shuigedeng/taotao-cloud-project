package com.taotao.cloud.gateway.configuration;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import com.alibaba.cloud.sentinel.gateway.scg.SentinelSCGAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

/**
 * SGW Sentinel配置
 *
 * @author shuigedeng
 */
@Configuration
public class SentinelConfiguration {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler(
		ObjectProvider<List<ViewResolver>> viewResolversProvider,
		ServerCodecConfigurer serverCodecConfigurer) {

		// Register the block exception handler for Spring Cloud Gateway.
		return new SentinelGatewayBlockExceptionHandler(
			viewResolversProvider.getIfAvailable(Collections::emptyList), serverCodecConfigurer);
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public GlobalFilter sentinelGatewayFilter() {
		return new SentinelGatewayFilter();
	}

	@PostConstruct
	public void doInit() {
		initCustomizedApis();
		initGatewayRules();
		initFallback();
	}

	/**
	 * 样例，可以扩展使用 文档参见：https://github.com/alibaba/Sentinel/wiki/%E7%BD%91%E5%85%B3%E9%99%90%E6%B5%81
	 */
	private void initCustomizedApis() {
		Set<ApiDefinition> definitions = new HashSet<>();
		ApiDefinition api1 = new ApiDefinition("some_customized_api")
			.setPredicateItems(new HashSet<>() {{
				add(new ApiPathPredicateItem().setPattern("/ahas"));
				add(new ApiPathPredicateItem().setPattern("/product/**")
					.setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
			}});

		ApiDefinition api2 = new ApiDefinition("another_customized_api")
			.setPredicateItems(new HashSet<>() {{
				add(new ApiPathPredicateItem().setPattern("/**")
					.setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
			}});

		definitions.add(api1);
		definitions.add(api2);

		GatewayApiDefinitionManager.loadApiDefinitions(definitions);
	}

	private void initGatewayRules() {
		Set<GatewayFlowRule> rules = new HashSet<>();
		rules.add(new GatewayFlowRule("aliyun_route")
			.setCount(10)
			.setIntervalSec(1)
		);

		rules.add(new GatewayFlowRule("aliyun_route")
			.setCount(2)
			.setIntervalSec(2)
			.setBurst(2)
			.setParamItem(new GatewayParamFlowItem()
				.setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)
			)
		);

		rules.add(new GatewayFlowRule("httpbin_route")
			.setCount(10)
			.setIntervalSec(1)
			.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
			.setMaxQueueingTimeoutMs(600)
			.setParamItem(new GatewayParamFlowItem()
				.setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_HEADER)
				.setFieldName("X-Sentinel-Flag")
			)
		);

		rules.add(new GatewayFlowRule("httpbin_route")
			.setCount(1)
			.setIntervalSec(1)
			.setParamItem(new GatewayParamFlowItem()
				.setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
				.setFieldName("pa")
			)
		);

		rules.add(new GatewayFlowRule("httpbin_route")
			.setCount(2)
			.setIntervalSec(30)
			.setParamItem(new GatewayParamFlowItem()
				.setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
				.setFieldName("type")
				.setPattern("warn")
				.setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_CONTAINS)
			)
		);

		rules.add(new GatewayFlowRule("taotao-cloud-auth")
			.setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
			.setCount(3)
			.setGrade(RuleConstant.FLOW_GRADE_QPS)
			.setIntervalSec(1)
		);

		GatewayRuleManager.loadRules(rules);
	}


	/**
	 * @author shuigedeng
	 * @see SentinelSCGAutoConfiguration initFallback
	 * @since 2022-01-06 16:04:09
	 */
	private void initFallback() {
		GatewayCallbackManager.setBlockHandler((exchange, t) -> ServerResponse
			.status(200)
			.contentType(MediaType.valueOf(MediaType.APPLICATION_JSON.toString()))
			.body(fromValue(Result.fail("访问频繁,请稍后重试"))));
		LogUtil.info(
			"[Sentinel SpringCloudGateway] using AnonymousBlockRequestHandler");
	}

}
