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

package com.taotao.cloud.stock.api.common.util.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.xtoon.boot.common.util.CommonConstant;
import com.xtoon.boot.common.util.TenantContext;
import java.util.Arrays;
import java.util.List;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * mybatis-plus配置
 *
 * @author shuigedeng
 * @since 2021-01-25
 */
@AutoConfiguration
public class MybatisPlusConfig {

    /** 需要排除的多租户的表 */
    private List<String> ignoreTables = Arrays.asList("sys_captcha", "sys_tenant", "sys_account", "sys_permission");

    /** 分页插件 */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {

            // 获取租户 ID 值表达式，只支持单个 ID 值
            @Override
            public Expression getTenantId() {
                String tenant = TenantContext.getTenantId();
                if (tenant != null) {
                    return new StringValue(TenantContext.getTenantId());
                }
                return new NullValue();
            }
            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件,
            // 这里设置 role表不需要该条件
            @Override
            public boolean ignoreTable(String tableName) {
                return ignoreTables.stream().anyMatch((t) -> t.equalsIgnoreCase(tableName));
            }

            @Override
            public String getTenantIdColumn() {
                return CommonConstant.TENANT_ID;
            }
        }));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }
}
