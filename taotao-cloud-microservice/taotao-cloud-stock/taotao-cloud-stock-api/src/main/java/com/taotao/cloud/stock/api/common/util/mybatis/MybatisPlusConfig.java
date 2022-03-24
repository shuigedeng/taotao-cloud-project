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
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus配置
 *
 * @author shuigedeng
 * @date 2021-01-25
 **/
@Slf4j
@Configuration
public class MybatisPlusConfig {

    /**
     * 需要排除的多租户的表
     */
    private List<String> ignoreTables = Arrays.asList("sys_captcha","sys_tenant","sys_account","sys_permission");

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(
                new TenantLineHandler() {

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
                        return ignoreTables.stream().anyMatch(
                                (t) -> t.equalsIgnoreCase(tableName)
                        );
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
