///*
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.taotao.cloud.gateway.configuration;
//
//import com.taotao.cloud.gateway.anti_reptile.AntiReptileProperties;
//import com.taotao.cloud.gateway.anti_reptile.ValidateFormService;
//import com.taotao.cloud.gateway.anti_reptile.filter.AntiReptileFilter;
//import com.taotao.cloud.gateway.anti_reptile.handler.RefreshFormHandler;
//import com.taotao.cloud.gateway.anti_reptile.handler.ValidateFormHandler;
//import com.taotao.cloud.gateway.anti_reptile.rule.AntiReptileRule;
//import com.taotao.cloud.gateway.anti_reptile.rule.RuleActuator;
//import com.taotao.cloud.gateway.anti_reptile.rule.rulers.IpRule;
//import com.taotao.cloud.gateway.anti_reptile.rule.rulers.UaRule;
//import com.taotao.cloud.gateway.anti_reptile.util.VerifyImageUtil;
//import java.util.Comparator;
//import java.util.List;
//import org.redisson.api.RedissonClient;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 反爬配置
// *
// * @author shuigedeng
// * @version 2023.04
// * @since 2023-05-06 17:01:21
// */
//@Configuration
//@ConditionalOnBean(RedissonClient.class)
//@EnableConfigurationProperties(AntiReptileProperties.class)
//@ConditionalOnProperty(
//        prefix = AntiReptileProperties.PREFIX,
//        value = "enabled",
//        havingValue = "true")
//public class AntiReptileConfiguration {
//
//    @Bean
//    @ConditionalOnProperty(
//            prefix = AntiReptileProperties.PREFIX,
//            name = "ip-rule",
//            value = "enabled",
//            havingValue = "true",
//            matchIfMissing = true)
//    public IpRule ipRule(RedissonClient redissonClient, AntiReptileProperties properties) {
//        return new IpRule(redissonClient, properties);
//    }
//
//    @Bean
//    @ConditionalOnProperty(
//            prefix = AntiReptileProperties.PREFIX,
//            name = "ua-rule",
//            value = "enabled",
//            havingValue = "true",
//            matchIfMissing = true)
//    public UaRule uaRule(AntiReptileProperties properties) {
//        return new UaRule(properties);
//    }
//
//    @Bean
//    public RuleActuator ruleActuator(final List<AntiReptileRule> rules) {
//        final List<AntiReptileRule> antiReptileRules =
//                rules.stream().sorted(Comparator.comparingInt(AntiReptileRule::getOrder)).toList();
//        return new RuleActuator(antiReptileRules);
//    }
//
//    @Bean
//    public VerifyImageUtil verifyImageUtil(RedissonClient redissonClient) {
//        return new VerifyImageUtil(redissonClient);
//    }
//
//    @Bean
//    public ValidateFormService validateFormService(
//            RuleActuator actuator, VerifyImageUtil verifyImageUtil) {
//        return new ValidateFormService(actuator, verifyImageUtil);
//    }
//
//    @Bean
//    public AntiReptileFilter antiReptileFilter(
//            RuleActuator actuator,
//            VerifyImageUtil verifyImageUtil,
//            AntiReptileProperties antiReptileProperties) {
//        return new AntiReptileFilter(actuator, verifyImageUtil, antiReptileProperties);
//    }
//
//    @Bean
//    public RefreshFormHandler refreshFormHandler(ValidateFormService validateFormService) {
//        return new RefreshFormHandler(validateFormService);
//    }
//
//    @Bean
//    public ValidateFormHandler validateFormHandler(ValidateFormService validateFormService) {
//        return new ValidateFormHandler(validateFormService);
//    }
//}
