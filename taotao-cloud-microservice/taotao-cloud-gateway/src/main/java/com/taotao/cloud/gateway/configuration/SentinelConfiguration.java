/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.log.LogUtils;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.ObjectProvider;
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

    // @Bean
    // public Customizer<ReactiveSentinelCircuitBreakerFactory> defaultCustomizer() {
    //	return factory -> factory.configureDefault(id -> new SentinelConfigBuilder(id)
    //		.build());
    // }
    //
    // @Bean
    // public Customizer<ReactiveSentinelCircuitBreakerFactory> customizer() {
    //	List<DegradeRule> rules = Collections.singletonList(
    //		new DegradeRule().setGrade(RuleConstant.DEGRADE_GRADE_RT)
    //			.setCount(100)
    //			.setTimeWindow(10)
    //	);
    //	return factory -> factory.configure(builder -> builder.rules(rules), "foo", "bar");
    // }

    // *************************************************************

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler(
            ObjectProvider<List<ViewResolver>> viewResolversProvider,
            ServerCodecConfigurer serverCodecConfigurer) {

        // Register the block exception handler for Spring Cloud Gateway.
        return new SentinelGatewayBlockExceptionHandler(
                viewResolversProvider.getIfAvailable(Collections::emptyList),
                serverCodecConfigurer);
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
        ApiDefinition api1 =
                new ApiDefinition("some_customized_api")
                        .setPredicateItems(
                                new HashSet<>() {
                                    {
                                        add(new ApiPathPredicateItem().setPattern("/ahas"));
                                        add(
                                                new ApiPathPredicateItem()
                                                        .setPattern("/product/**")
                                                        .setMatchStrategy(
                                                                SentinelGatewayConstants
                                                                        .URL_MATCH_STRATEGY_PREFIX));
                                    }
                                });

        ApiDefinition api2 =
                new ApiDefinition("another_customized_api")
                        .setPredicateItems(
                                new HashSet<>() {
                                    {
                                        add(
                                                new ApiPathPredicateItem()
                                                        .setPattern("/**")
                                                        .setMatchStrategy(
                                                                SentinelGatewayConstants
                                                                        .URL_MATCH_STRATEGY_PREFIX));
                                    }
                                });

        definitions.add(api1);
        definitions.add(api2);

        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        rules.add(new GatewayFlowRule("aliyun_route").setCount(10).setIntervalSec(1));

        rules.add(
                new GatewayFlowRule("aliyun_route")
                        .setCount(2)
                        .setIntervalSec(2)
                        .setBurst(2)
                        .setParamItem(
                                new GatewayParamFlowItem()
                                        .setParseStrategy(
                                                SentinelGatewayConstants
                                                        .PARAM_PARSE_STRATEGY_CLIENT_IP)));

        rules.add(
                new GatewayFlowRule("httpbin_route")
                        .setCount(10)
                        .setIntervalSec(1)
                        .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
                        .setMaxQueueingTimeoutMs(600)
                        .setParamItem(
                                new GatewayParamFlowItem()
                                        .setParseStrategy(
                                                SentinelGatewayConstants
                                                        .PARAM_PARSE_STRATEGY_HEADER)
                                        .setFieldName("X-Sentinel-Flag")));

        rules.add(
                new GatewayFlowRule("httpbin_route")
                        .setCount(1)
                        .setIntervalSec(1)
                        .setParamItem(
                                new GatewayParamFlowItem()
                                        .setParseStrategy(
                                                SentinelGatewayConstants
                                                        .PARAM_PARSE_STRATEGY_URL_PARAM)
                                        .setFieldName("pa")));

        rules.add(
                new GatewayFlowRule("httpbin_route")
                        .setCount(2)
                        .setIntervalSec(30)
                        .setParamItem(
                                new GatewayParamFlowItem()
                                        .setParseStrategy(
                                                SentinelGatewayConstants
                                                        .PARAM_PARSE_STRATEGY_URL_PARAM)
                                        .setFieldName("type")
                                        .setPattern("warn")
                                        .setMatchStrategy(
                                                SentinelGatewayConstants
                                                        .PARAM_MATCH_STRATEGY_CONTAINS)));

        rules.add(
                new GatewayFlowRule("taotao-cloud-auth")
                        .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
                        .setCount(3)
                        .setGrade(RuleConstant.FLOW_GRADE_QPS)
                        .setIntervalSec(1));

        GatewayRuleManager.loadRules(rules);
    }

    /**
     * @author shuigedeng
     * @see SentinelSCGAutoConfiguration initFallback
     * @since 2022-01-06 16:04:09
     */
    private void initFallback() {
        GatewayCallbackManager.setBlockHandler(
                (exchange, t) ->
                        ServerResponse.status(200)
                                .contentType(
                                        MediaType.valueOf(MediaType.APPLICATION_JSON.toString()))
                                .body(fromValue(Result.fail("sentinel访问频繁,请稍后重试"))));
        LogUtils.info("[Sentinel SpringCloudGateway] using AnonymousBlockRequestHandler");
    }

    // # 五大规则

    // # 流控规则：流量控制（flow control）
    //	定义：其原理是监控应用流量的 QPS 或并发线程数等指标，当达到指定的阈值时对流量进行控制，
    //	以避免被瞬时的流量高峰冲垮，从而保障应用的高可用性。

    // # 降级规则： 熔断降级（Degrade Service）
    //	定义：其原理是监控应用中资源调用请求，达到指定阈值时自动触发熔断降级

    // # 热点规则： 热点参数（ParamFlow）
    //	热点：何为热点？ 热点即经常访问的数据
    //    定义：其原理很多时候我们希望统计某个热点数据中访问频次最高的Top K 数据，并对其访问进行限制。

    // # 系统规则： SystemFlow
    //	定义：其原理是 Sentinel 系统自适应限流从整体纬度对应用入口流量进行控制

    // # 授权规则： 黑白名单控制规则
    //	定义：很多时候，我们需要根据调用来源判断该次请求是否允许放行，
    //	这时候可以使用 Sentinel 的来源访问控制（黑白名单控制）的功能。
    //	来源访问控制根据资源的请求来源（origin）限制资源是否通过，
    //	若配置白名单则只有请求来源位于白名单内时才可通过：
    //	若配置黑名单则请求来源位于黑名单时不通过，其余的请求通过。

    // DegradeRule.json （降级规则） 熔断降级规则 (DegradeRule)
    // [
    //	{
    //		资源名，即规则的作用对象
    //		"resource": "abc0",
    //		慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值
    //		"count": 20.0,
    //		熔断策略，支持慢调用比例/异常比例/异常数策略
    //		"grade": 0,
    //		"passCount": 0,
    //		熔断时长，单位为 s
    //		"timeWindow": 10
    //      slowRatioThreshold	慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）
    //      statIntervalMs	统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
    //      minRequestAmount	熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入）
    //	},
    //	{
    //		"resource": "abc1",
    //		"count": 15.0,
    //		"grade": 0,
    //		"passCount": 0,
    //		"timeWindow": 10
    //	}
    // ]

    // FlowRule.json 流量控制规则 (FlowRule)
    // [
    //  {
    //    资源名，资源名是限流规则的作用对象
    //    "resource": "/sys/api/v1/user/login",
    //    限流阈值
    //    "count": 20.0,
    //    限流阈值类型，1-QPS模式 0-并发线程数模式
    //    "grade": 1,
    //    流控效果（0-快速失败直接拒绝/1-WarmUp/2-匀速排队等待），不支持按调用关系限流
    //    "controlBehavior": 0,
    //    流控针对的调用来源
    //    "limitApp": "default",
    //    流控模式：0-直接、1-链路、2-关联
    //    "strategy": 0
    //    是否集群限流
    //    "clusterMode": false
    //  },
    //  {
    //    "resource": "abc1",
    //    "controlBehavior": 0,
    //    "count": 20.0,
    //    "grade": 1,
    //    "limitApp": "default",
    //    "strategy": 0
    //  }
    // ]

    // SystemRule.json （系统规则） 系统保护规则 (SystemRule)
    //
    //   highestSystemLoad	load1 触发值，用于触发自适应控制阶段	-1 (不生效)
    //   avgRt	所有入口流量的平均响应时间	-1 (不生效)
    //   maxThread	入口流量的最大并发数	-1 (不生效)
    //   qps	所有入口资源的 QPS	-1 (不生效)
    //   highestCpuUsage	当前系统的 CPU 使用率（0.0-1.0）	-1 (不生效)

    // [
    //  {
    //    "avgRt": 10,
    //    "highestSystemLoad": 5.0,
    //    "maxThread": 10,
    //    "qps": 20.0
    //  }
    // ]
    //

    // 来源访问控制规则（AuthorityRule）非常简单，主要有以下配置项：
    //
    // resource：资源名，即限流规则的作用对象。
    // limitApp：对应的黑名单/白名单，不同 origin 用 , 分隔，如 appA,appB。
    // strategy：限制模式，AUTHORITY_WHITE 为白名单模式，AUTHORITY_BLACK 为黑名单模式，默认为白名单模式。
    //
    // 比如我们希望控制对资源 test 的访问设置白名单，只有来源为 appA 和 appB 的请求才可通过，则可以配置如下白名单规则：
    //
    // AuthorityRule rule = new AuthorityRule();
    // rule.setResource("test");
    // rule.setStrategy(RuleConstant.AUTHORITY_WHITE);
    // rule.setLimitApp("appA,appB");
    // AuthorityRuleManager.loadRules(Collections.singletonList(rule));

    // 热点参数规则（ParamFlowRule）类似于流量控制规则（FlowRule）：
    //
    // 属性	说明	默认值
    // resource	资源名，必填
    // count	限流阈值，必填
    // grade	限流模式	QPS 模式
    // durationInSec	统计窗口时间长度（单位为秒），1.6.0 版本开始支持	1s
    // controlBehavior	流控效果（支持快速失败和匀速排队模式），1.6.0 版本开始支持	快速失败
    // maxQueueingTimeMs	最大排队等待时长（仅在匀速排队模式生效），1.6.0 版本开始支持	0ms
    // paramIdx	热点参数的索引，必填，对应 SphU.entry(xxx, args) 中的参数索引位置
    // paramFlowItemList	参数例外项，可以针对指定的参数值单独设置限流阈值，不受前面 count 阈值的限制。仅支持基本类型和字符串类型
    // clusterMode	是否是集群参数流控规则	false
    // clusterConfig	集群流控相关配置

    // 我们可以通过 ParamFlowRuleManager 的 loadRules 方法更新热点参数规则，下面是一个示例：
    //
    // ParamFlowRule rule = new ParamFlowRule(resourceName)
    //    .setParamIdx(0)
    //    .setCount(5);
    //// 针对 int 类型的参数 PARAM_B，单独设置限流 QPS 阈值为 10，而不是全局的阈值 5.
    // ParamFlowItem item = new ParamFlowItem().setObject(String.valueOf(PARAM_B))
    //    .setClassType(int.class.getName())
    //    .setCount(10);
    // rule.setParamFlowItemList(Collections.singletonList(item));
    //
    // ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
}
