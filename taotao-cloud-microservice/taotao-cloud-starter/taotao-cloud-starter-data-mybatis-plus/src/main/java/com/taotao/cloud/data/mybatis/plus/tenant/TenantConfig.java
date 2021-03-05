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
package com.taotao.cloud.data.mybatis.plus.tenant;

import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.context.TenantContextHolder;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.data.mybatis.plus.constant.MybatisPlusConstant;
import com.taotao.cloud.data.mybatis.plus.properties.TenantProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 多租户自动配置
 *
 * @author dengtao
 * @since 2020/5/2 11:20
 * @version 1.0.0
 */
@AllArgsConstructor
@ConditionalOnProperty(prefix = MybatisPlusConstant.BASE_MYBATIS_PLUS_TENANT_PREFIX,
	name = MybatisPlusConstant.ENABLED, havingValue = MybatisPlusConstant.TRUE)
public class TenantConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        LogUtil.info("[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_TENANT_STARTER + "]" + "tenant模式已开启");
    }

    private final TenantProperties tenantProperties;

    @Bean
    public TenantLineHandler tenantHandler() {
        return new TenantLineHandler() {
            /**
             * 获取租户id
             */
            @Override
            public Expression getTenantId() {
                String tenant = TenantContextHolder.getTenant();
                if (tenant != null) {
                    return new StringValue(TenantContextHolder.getTenant());
                }
                return new NullValue();
            }

            /**
             * 获取租户列名
             */
            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            /**
             * 过滤不需要根据租户隔离的表
             * @param tableName 表名
             */
            @Override
            public boolean ignoreTable(String tableName) {
                return tenantProperties.getIgnoreTables().stream().anyMatch(
                        (e) -> e.equalsIgnoreCase(tableName)
                );
            }
        };
    }

    /**
     * 过滤不需要根据租户隔离的MappedStatement
     */
    @Bean
    public ISqlParserFilter sqlParserFilter() {
        return metaObject -> {
            MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
            return tenantProperties.getIgnoreSqlList().stream().anyMatch(
                    (e) -> e.equalsIgnoreCase(ms.getId())
            );
        };
    }
}
