package com.taotao.cloud.data.mybatis.plus.configuration;

import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.context.TenantContextHolder;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.data.mybatis.plus.constant.MybatisPlusConstant;
import com.taotao.cloud.data.mybatis.plus.properties.TenantProperties;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多租户配置中心
 *
 * @author pangu
 * @Date 2020-9-7
 */
@Configuration
@AutoConfigureBefore(MybatisPlusConfiguration.class)
@ConditionalOnProperty(prefix = TenantProperties.PREFIX,
	name = "enabled", havingValue ="true")
public class TenantConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.info("[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_TENANT_STARTER + "]"
			+ "tenant模式已开启");
	}

	private final TenantProperties tenantProperties;

	public TenantConfiguration(
		TenantProperties tenantProperties) {
		this.tenantProperties = tenantProperties;
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

	/**
	 * 新多租户插件配置,一缓和二缓遵循mybatis的规则, 需要设置 MybatisConfiguration#useDeprecatedExecutor = false
	 * 避免缓存万一出现问题
	 */
	@Bean
	public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
		return new TenantLineInnerInterceptor(new TenantLineHandler() {
			/**
			 * 获取租户ID
			 * @return Expression
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
			 * 获取多租户的字段名
			 * @return String
			 */
			@Override
			public String getTenantIdColumn() {
				return tenantProperties.getColumn();
			}

			/**
			 * 过滤不需要根据租户隔离的表
			 * 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
			 * @param tableName 表名
			 */
			@Override
			public boolean ignoreTable(String tableName) {
				return tenantProperties.getIgnoreTables().stream().anyMatch(
					(t) -> t.equalsIgnoreCase(tableName)
				);
			}
		});
	}
}
