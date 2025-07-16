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

package com.taotao.cloud.rpc.registry.apiregistry;

import com.taotao.cloud.rpc.registry.apiregistry.apiclient.Aspects;
import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryHealthFilter;
import com.taotao.cloud.rpc.registry.apiregistry.loadbalance.BaseLoadBalance;
import com.taotao.cloud.rpc.registry.apiregistry.loadbalance.LoadBalanceFactory;
import com.taotao.cloud.rpc.registry.apiregistry.registry.BaseRegistry;
import com.taotao.cloud.rpc.registry.apiregistry.registry.RegistryFactory;
import com.taotao.cloud.rpc.registry.apiregistry.scan.ApiClientPackageScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * @version : 2022-12-7 14:30
 * @author: chejiangyi *
 * @since 2022-12-7
 **/
@Configuration
@ConditionalOnProperty(
        name = com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties.Enabled,
        havingValue = "true")
@Import(value = {ApiClientPackageScan.ApiClientAnnotationPackageScan.class})
public class ApiRegistryConfiguration {

    @ConditionalOnProperty(
            name =
                    com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties
                            .ApiClientAspectEnabled,
            havingValue = "true")
    @Bean
    @ConditionalOnClass(
            name = {
                "org.aspectj.lang.annotation.Aspect",
                com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties.FeignClientClassPath
            })
    public Aspects.AllClientAspect allClientAspect() {
        return new Aspects.AllClientAspect();
    }

    @ConditionalOnProperty(
            name =
                    com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties
                            .ApiClientAspectEnabled,
            havingValue = "true")
    @Bean
    @ConditionalOnClass(name = {"org.aspectj.lang.annotation.Aspect"})
    @ConditionalOnMissingClass(
            value =
                    com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties
                            .FeignClientClassPath)
    public Aspects.ApiClientAspect apiClientAspect() {
        return new Aspects.ApiClientAspect();
    }

    @Bean
    public BaseLoadBalance getBaseLoadBalance() {
        return LoadBalanceFactory.create();
    }

    @Bean
    public BaseRegistry getBaseRegistry() {
        return RegistryFactory.create();
    }

    //	@Bean
    //	public ApiRegistryApplicationRunner getApiRegistryApplicationRunner() {
    //		return new ApiRegistryApplicationRunner();
    //	}

    @ConditionalOnProperty(
            name = com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties.HealthEnabled,
            havingValue = "true",
            matchIfMissing = true)
    @Bean
    @ConditionalOnWebApplication
    public FilterRegistrationBean apiRegistryStatusFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        // 优先注入
        filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
        filterRegistrationBean.setFilter(new ApiRegistryHealthFilter());
        filterRegistrationBean.setName("ttcApiRegistryStatusFilter");
        filterRegistrationBean.addUrlPatterns("/ttc/*");
        //		LogUtils.info(com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties.Project,
        //				"apiRegistry status filter注册成功!");
        return filterRegistrationBean;
    }
}
