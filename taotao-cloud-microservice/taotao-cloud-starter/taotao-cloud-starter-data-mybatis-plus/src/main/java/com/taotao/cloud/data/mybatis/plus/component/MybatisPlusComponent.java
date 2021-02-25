/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.data.mybatis.plus.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.core.utils.BeanUtil;
import com.taotao.cloud.data.mybatis.plus.handler.DateMetaObjectHandler;
import com.taotao.cloud.data.mybatis.plus.properties.MybatisPlusAutoFillProperties;
import com.taotao.cloud.data.mybatis.plus.properties.TenantProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * mybatis plus 组件
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 11:20
 */
@Slf4j
@AllArgsConstructor
public class MybatisPlusComponent implements InitializingBean {

	private final TenantProperties tenantProperties;
	private final MybatisPlusAutoFillProperties autoFillProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_MYBATIS_PLUS_STARTER + "]"
			+ "mybatis-plus模式已开启");
	}

	@Bean
	public PaginationInnerInterceptor paginationInterceptor() {
		return new PaginationInnerInterceptor();
	}

	@Bean
	public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
		TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor();
		boolean enableTenant = tenantProperties.getEnabled();

		TenantLineHandler tenantHandler = BeanUtil.getBean(TenantLineHandler.class, false);
		ISqlParserFilter sqlParserFilter = BeanUtil.getBean(ISqlParserFilter.class, false);

		if (enableTenant && tenantHandler != null && sqlParserFilter != null) {
			tenantLineInnerInterceptor.setTenantLineHandler(tenantHandler);
		}

		return tenantLineInnerInterceptor;
	}

	@Bean
	public OptimisticLockerInnerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInnerInterceptor();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "taotao.cloud.data.mybatis-plus.auto-fill", name = "enabled", havingValue = "true")
	public MetaObjectHandler metaObjectHandler() {
		return new DateMetaObjectHandler(autoFillProperties);
	}
}
