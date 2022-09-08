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
package com.taotao.cloud.data.mybatisplus.configuration;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.data.mybatis.plus.datascope.DataScopeInterceptor;
import com.taotao.cloud.data.mybatisplus.handler.objecthandler.AutoFieldMetaObjectHandler;
import com.taotao.cloud.data.mybatisplus.handler.typehandler.like.FullLikeTypeHandler;
import com.taotao.cloud.data.mybatisplus.handler.typehandler.like.LeftLikeTypeHandler;
import com.taotao.cloud.data.mybatisplus.handler.typehandler.like.RightLikeTypeHandler;
import com.taotao.cloud.data.mybatisplus.injector.MateSqlInjector;
import com.taotao.cloud.data.mybatisplus.interceptor.SqlLogInterceptor;
import com.taotao.cloud.data.mybatisplus.interceptor.SqlCollectorInterceptor;
import com.taotao.cloud.data.mybatisplus.interceptor.SqlPaginationInnerInterceptor;
import com.taotao.cloud.data.mybatisplus.tenant.SchemaInterceptor;
import com.taotao.cloud.data.mybatisplus.utils.DatabaseProperties;
import com.taotao.cloud.data.mybatisplus.tenant.MultiTenantType;
import com.taotao.cloud.data.mybatisplus.properties.MybatisPlusAutoFillProperties;
import com.taotao.cloud.data.mybatisplus.properties.MybatisPlusProperties;
import com.taotao.cloud.data.mybatisplus.properties.TenantProperties;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.type.EnumTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * MybatisPlusAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:02
 */
@MapperScan(basePackages = {"com.taotao.cloud.*.biz.mapper"})
@EnableTransactionManagement
@AutoConfiguration(after = TenantAutoConfiguration.class)
@EnableConfigurationProperties({MybatisPlusAutoFillProperties.class, MybatisPlusProperties.class, TenantProperties.class})
@ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class MybatisPlusAutoConfiguration implements InitializingBean {

	private final TenantProperties tenantProperties;
	private final MybatisPlusAutoFillProperties autoFillProperties;
	private final MybatisPlusProperties mybatisPlusProperties;

	@Autowired(required = false)
	private TenantLineInnerInterceptor tenantLineInnerInterceptor;

	/**
	 * 单页分页条数限制(默认无限制,参见 插件#handlerLimit 方法)
	 */
	private static final Long MAX_LIMIT = 1000L;

	public MybatisPlusAutoConfiguration(
		TenantProperties tenantProperties,
		MybatisPlusAutoFillProperties autoFillProperties,
		MybatisPlusProperties mybatisPlusProperties) {
		this.tenantProperties = tenantProperties;
		this.autoFillProperties = autoFillProperties;
		this.mybatisPlusProperties = mybatisPlusProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(MybatisPlusAutoConfiguration.class, StarterName.MYBATIS_PLUS_STARTER);
	}

	/**
	 * sql 注入配置
	 */
	@Bean
	public ISqlInjector sqlInjector() {
		return new MateSqlInjector();
	}

	/**
	 * 日志收集插件
	 */
	@Bean
	@ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = "sqlLogEnable", havingValue = "true", matchIfMissing = true)
	public SqlLogInterceptor sqlLogInterceptor() {
		return new SqlLogInterceptor();
	}

	/**
	 * sql收集插件
	 */
	@Bean
	@ConditionalOnClass(name = "org.apache.ibatis.plugin.Interceptor")
	@ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = "sqlCollectorEnable", havingValue = "true")
	public SqlCollectorInterceptor sqlMybatisInterceptor(Collector collector) {
		return new SqlCollectorInterceptor(collector);
	}

	/**
	 * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
	 * <p>
	 * 注意:
	 * 如果内部插件都是使用,需要注意顺序关系,建议使用如下顺序
	 * 多租户插件,动态表名插件
	 * 分页插件,乐观锁插件
	 * sql性能规范插件,防止全表更新与删除插件
	 * 总结: 对sql进行单次改造的优先放入,不对sql进行改造的最后放入
	 * <p>
	 * 参考：
	 * https://mybatis.plus/guide/interceptor.html#%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F-%E4%BB%A5%E5%88%86%E9%A1%B5%E6%8F%92%E4%BB%B6%E4%B8%BE%E4%BE%8B
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

		//5.多租户插件
		interceptor.addInnerInterceptor(getTenantInnerInterceptor());

		//6.数据权限插件
		if (tenantProperties.getDataScope()) {
			interceptor.addInnerInterceptor(getDataScopeInnerInterceptor());
		}

		//3.乐观锁插件
		interceptor.addInnerInterceptor(getOptimisticLockerInnerInterceptor());

		// if (tenantProperties.getEnabled() && Objects.nonNull(tenantLineInnerInterceptor)) {
		// 	// 多租户插件
		// 	interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
		// }

		//1.分页插件
		interceptor.addInnerInterceptor(getPaginationInnerInterceptor());

		//2.防止全表更新与删除插件
		interceptor.addInnerInterceptor(getBlockAttackInnerInterceptor());

		//4.sql规范插件
		interceptor.addInnerInterceptor(getIllegalSQLInnerInterceptor());

		return interceptor;
	}

	/**
	 * 自动填充数据配置
	 */
	@Bean
	@ConditionalOnProperty(prefix = MybatisPlusAutoFillProperties.PREFIX, name = "enabled", havingValue = "true")
	public AutoFieldMetaObjectHandler metaObjectHandler() {
		return new AutoFieldMetaObjectHandler(autoFillProperties);
	}

	/**
	 * 数据库配置
	 *
	 * @return 配置
	 */
	@Bean
	public DatabaseIdProvider getDatabaseIdProvider() {
		DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
		Properties properties = new Properties();
		properties.setProperty("Oracle", DbType.ORACLE.getDb());
		properties.setProperty("MySQL", DbType.MYSQL.getDb());
		properties.setProperty("Microsoft SQL Server", DbType.SQL_SERVER.getDb());
		databaseIdProvider.setProperties(properties);
		return databaseIdProvider;
	}

	/**
	 * IEnum 枚举配置
	 */
	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		return configuration -> {
			configuration.setDefaultEnumTypeHandler(EnumTypeHandler.class);
			// 关闭 mybatis 默认的日志
			configuration.setLogPrefix("log.mybatis");
		};
	}

	/**
	 * Mybatis 自定义的类型处理器： 处理XML中  #{name,typeHandler=leftLike} 类型的参数
	 * 用于左模糊查询时使用
	 * <p>
	 * eg：
	 * and name like #{name,typeHandler=leftLike}
	 *
	 * @return 左模糊处理器
	 */
	@Bean
	public LeftLikeTypeHandler getLeftLikeTypeHandler() {
		return new LeftLikeTypeHandler();
	}

	/**
	 * Mybatis 自定义的类型处理器： 处理XML中  #{name,typeHandler=rightLike} 类型的参数
	 * 用于右模糊查询时使用
	 * <p>
	 * eg：
	 * and name like #{name,typeHandler=rightLike}
	 *
	 * @return 右模糊处理器
	 */
	@Bean
	public RightLikeTypeHandler getRightLikeTypeHandler() {
		return new RightLikeTypeHandler();
	}

	/**
	 * Mybatis 自定义的类型处理器： 处理XML中  #{name,typeHandler=fullLike} 类型的参数
	 * 用于全模糊查询时使用
	 * <p>
	 * eg：
	 * and name like #{name,typeHandler=fullLike}
	 *
	 * @return 全模糊处理器
	 */
	@Bean
	public FullLikeTypeHandler getFullLikeTypeHandler() {
		return new FullLikeTypeHandler();
	}


	public void getTenantInnerInterceptor() {
		LogUtils.info("检测到 lamp.database.multiTenantType={}，已启用 {} 模式", databaseProperties.getMultiTenantType().name(), databaseProperties.getMultiTenantType().getDescribe());
		if (StrUtil.equalsAny(databaseProperties.getMultiTenantType().name(),
			MultiTenantType.SCHEMA.name(), MultiTenantType.SCHEMA_COLUMN.name())) {
			ArgumentAssert.notNull(databaseProperties.getDbType(), "SCHEMA 模式请在mysql.yml、oracle.yml、sqlserver.yml中配置: {}.dbType", DatabaseProperties.PREFIX);

			// SCHEMA 动态表名插件
			SchemaInterceptor schemaInterceptor = new SchemaInterceptor(databaseProperties.getTenantDatabasePrefix(), databaseProperties.getOwner(), databaseProperties.getDbType());
			interceptor.addInnerInterceptor(schemaInterceptor);
		}

		if (StrUtil.equalsAny(databaseProperties.getMultiTenantType().name(),
			MultiTenantType.COLUMN.name(), MultiTenantType.SCHEMA_COLUMN.name(), MultiTenantType.DATASOURCE_COLUMN.name())) {
			// COLUMN 模式 多租户插件
			TenantLineInnerInterceptor tli = new TenantLineInnerInterceptor();
			tli.setTenantLineHandler(new TenantLineHandler() {
				@Override
				public String getTenantIdColumn() {
					return databaseProperties.getTenantIdColumn();
				}

				@Override
				public boolean ignoreTable(String tableName) {
					return databaseProperties.getIgnoreTables() != null && databaseProperties.getIgnoreTables().contains(tableName);
				}

				@Override
				public Expression getTenantId() {
					return MultiTenantType.COLUMN.eq(databaseProperties.getMultiTenantType()) ?
						new StringValue(ContextUtil.getTenant()) :
						new StringValue(ContextUtil.getSubTenant());
				}
			});
			interceptor.addInnerInterceptor(tli);
		}


	}

	public DataScopeInterceptor getDataScopeInnerInterceptor(){
		return new DataScopeInterceptor();
	}


	public PaginationInnerInterceptor getPaginationInnerInterceptor(){
		// 分页插件
		SqlPaginationInnerInterceptor paginationInterceptor = new SqlPaginationInnerInterceptor();
		// 单页分页条数限制
		paginationInterceptor.setMaxLimit(MAX_LIMIT);
		// 数据库类型
		// if (databaseProperties.getDbType() != null) {
		// 	paginationInterceptor.setDbType(DbType.MYSQL);
		// }
		paginationInterceptor.setDbType(DbType.MYSQL);
		// 溢出总页数后是否进行处理
		paginationInterceptor.setOverflow(true);
		// 生成 countSql 优化掉 join 现在只支持 left join
		paginationInterceptor.setOptimizeJoin(true);
		return paginationInterceptor;
	}

	public BlockAttackInnerInterceptor getBlockAttackInnerInterceptor(){
		return new BlockAttackInnerInterceptor();
	}

	public OptimisticLockerInnerInterceptor getOptimisticLockerInnerInterceptor(){
		return new OptimisticLockerInnerInterceptor();
	}

	public IllegalSQLInnerInterceptor getIllegalSQLInnerInterceptor(){
		return new IllegalSQLInnerInterceptor();
	}

}
