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
package com.taotao.cloud.data.mybatis.plus.configuration;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.constant.StrPool;
import com.taotao.cloud.common.utils.IdGeneratorUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.SecurityUtil;
import com.taotao.cloud.data.mybatis.plus.datascope.DataScopeInterceptor;
import com.taotao.cloud.data.mybatis.plus.entity.Entity;
import com.taotao.cloud.data.mybatis.plus.entity.SuperEntity;
import com.taotao.cloud.data.mybatis.plus.injector.MateSqlInjector;
import com.taotao.cloud.data.mybatis.plus.properties.MybatisPlusAutoFillProperties;
import com.taotao.cloud.data.mybatis.plus.properties.TenantProperties;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * mybatis plus 组件
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/2 11:20
 */
@Configuration
public class MybatisPlusConfiguration implements InitializingBean {

	private final TenantProperties tenantProperties;
	private final MybatisPlusAutoFillProperties autoFillProperties;
	private final TenantLineInnerInterceptor tenantLineInnerInterceptor;

	/**
	 * 单页分页条数限制(默认无限制,参见 插件#handlerLimit 方法)
	 */
	private static final Long MAX_LIMIT = 1000L;

	public MybatisPlusConfiguration(
		TenantProperties tenantProperties,
		MybatisPlusAutoFillProperties autoFillProperties,
		TenantLineInnerInterceptor tenantLineInnerInterceptor) {
		this.tenantProperties = tenantProperties;
		this.autoFillProperties = autoFillProperties;
		this.tenantLineInnerInterceptor = tenantLineInnerInterceptor;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(MybatisPlusConfiguration.class, StarterName.MYBATIS_PLUS_STARTER);
	}

	/**
	 * sql 注入配置
	 */
	@Bean
	public ISqlInjector sqlInjector() {
		LogUtil.started(MateSqlInjector.class, StarterName.MYBATIS_PLUS_STARTER);
		return new MateSqlInjector();
	}

	/**
	 * sql 日志
	 */
	@Bean
	@Profile({"local", "dev", "test"})
	@ConditionalOnProperty(value = "mybatis-plus.sql-log.enable", matchIfMissing = true)
	public SqlLogInterceptor sqlLogInterceptor() {
		LogUtil.started(SqlLogInterceptor.class, StarterName.MYBATIS_PLUS_STARTER);
		return new SqlLogInterceptor();
	}

	/**
	 * 新的分页插件,一缓和二缓遵循mybatis的规则, 需要设置 MybatisConfiguration#useDeprecatedExecutor = false
	 * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		LogUtil.started(MybatisPlusInterceptor.class, StarterName.MYBATIS_PLUS_STARTER);

		boolean enableTenant = tenantProperties.getEnabled();
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

		//分页插件
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		paginationInnerInterceptor.setMaxLimit(MAX_LIMIT);
		paginationInnerInterceptor.setDbType(DbType.MYSQL);
		paginationInnerInterceptor.setOverflow(true);

		if (enableTenant) {
			// 多租户插件
			interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
		}

		if (tenantProperties.getDataScope()) {
			// 数据权限插件
			interceptor.addInnerInterceptor(new DataScopeInterceptor());
		}

		//防止全表更新与删除插件: BlockAttackInnerInterceptor
		BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();

		//乐观锁插件
		OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();

		//sql规范插件
		IllegalSQLInnerInterceptor illegalSQLInnerInterceptor = new IllegalSQLInnerInterceptor();
		interceptor.addInnerInterceptor(paginationInnerInterceptor);
		interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
		interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor);
		interceptor.addInnerInterceptor(illegalSQLInnerInterceptor);
		return interceptor;
	}

	/**
	 * 自动填充数据配置
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = MybatisPlusAutoFillProperties.PREFIX, name = "enabled", havingValue = "true")
	public MetaObjectHandler metaObjectHandler() {
		LogUtil.started(MetaObjectHandler.class, StarterName.MYBATIS_PLUS_STARTER);
		return new DateMetaObjectHandler(autoFillProperties);
	}

	/**
	 * IEnum 枚举配置
	 */
	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		LogUtil.started(ConfigurationCustomizer.class, StarterName.MYBATIS_PLUS_STARTER);
		return configuration -> {
			configuration.setDefaultEnumTypeHandler(EnumTypeHandler.class);
			// 关闭 mybatis 默认的日志
			configuration.setLogPrefix("log.mybatis");
		};
	}

	/**
	 * 自定义填充公共字段
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/5/2 11:22
	 */
	public static class DateMetaObjectHandler implements MetaObjectHandler {

		private final MybatisPlusAutoFillProperties autoFillProperties;

		public DateMetaObjectHandler(MybatisPlusAutoFillProperties autoFillProperties) {
			this.autoFillProperties = autoFillProperties;
		}

		/**
		 * 是否开启了插入填充
		 */
		@Override
		public boolean openInsertFill() {
			return autoFillProperties.getEnableInsertFill();
		}

		/**
		 * 是否开启了更新填充
		 */
		@Override
		public boolean openUpdateFill() {
			return autoFillProperties.getEnableUpdateFill();
		}

		/**
		 * 插入填充，字段为空自动填充
		 */
		@Override
		public void insertFill(MetaObject metaObject) {
			fillId(metaObject);

			Object createTime = getFieldValByName(autoFillProperties.getCreateTimeField(),
				metaObject);
			Object updateTime = getFieldValByName(autoFillProperties.getUpdateTimeField(),
				metaObject);
			if (createTime == null || updateTime == null) {
				LocalDateTime date = LocalDateTime.now();
				if (createTime == null) {
					fillCreated(metaObject);
				}
				if (updateTime == null) {
					fillUpdated(metaObject);
				}
			}
		}

		private void fillId(MetaObject metaObject) {
			Long id = IdGeneratorUtil.getId();

			//1. 继承了SuperEntity 若 ID 中有值，就不设置
			if (metaObject.getOriginalObject() instanceof SuperEntity) {
				Object oldId = ((SuperEntity) metaObject.getOriginalObject()).getId();
				if (oldId != null) {
					return;
				}
				Object idVal = StrPool.STRING_TYPE_NAME.equals(
					metaObject.getGetterType(autoFillProperties.getIdField()).getName())
					? String.valueOf(id)
					: id;
				this.setFieldValByName(autoFillProperties.getIdField(), idVal, metaObject);
				return;
			}

			// 2. 没有继承SuperEntity， 但主键的字段名为：  id
			if (metaObject.hasGetter(autoFillProperties.getIdField())) {
				Object oldId = metaObject.getValue(autoFillProperties.getIdField());
				if (oldId != null) {
					return;
				}

				Object idVal = StrPool.STRING_TYPE_NAME.equals(
					metaObject.getGetterType(autoFillProperties.getIdField()).getName())
					? String.valueOf(id)
					: id;
				this.setFieldValByName(autoFillProperties.getIdField(), idVal, metaObject);
				return;
			}

			// 3. 实体没有继承 Entity 和 SuperEntity，且 主键名为其他字段
			TableInfo tableInfo = TableInfoHelper.getTableInfo(
				metaObject.getOriginalObject().getClass());
			if (tableInfo == null) {
				return;
			}
			// 主键类型
			Class<?> keyType = tableInfo.getKeyType();
			if (keyType == null) {
				return;
			}

			// id 字段名
			String keyProperty = tableInfo.getKeyProperty();
			Object oldId = metaObject.getValue(keyProperty);
			if (oldId != null) {
				return;
			}

			// 反射得到 主键的值
			Field idField = ReflectUtil.getField(metaObject.getOriginalObject().getClass(),
				keyProperty);
			Object fieldValue = ReflectUtil.getFieldValue(metaObject.getOriginalObject(), idField);
			// 判断ID 是否有值，有值就不
			if (ObjectUtil.isNotEmpty(fieldValue)) {
				return;
			}
			Object idVal =
				keyType.getName().equalsIgnoreCase(StrPool.STRING_TYPE_NAME) ? String.valueOf(id)
					: id;
			this.setFieldValByName(keyProperty, idVal, metaObject);
		}

		private void fillCreated(MetaObject metaObject) {
			// 设置创建时间和创建人
			if (metaObject.getOriginalObject() instanceof SuperEntity) {
				created(metaObject);
				return;
			}

			if (metaObject.hasGetter(Entity.CREATED_BY)) {
				Object oldVal = metaObject.getValue(Entity.CREATED_BY);
				if (oldVal == null) {
					this.setFieldValByName(Entity.CREATED_BY, SecurityUtil.getUserId(), metaObject);
				}
			}
			if (metaObject.hasGetter(autoFillProperties.getCreateTimeField())) {
				Object oldVal = metaObject.getValue(autoFillProperties.getCreateTimeField());
				if (oldVal == null) {
					this.setFieldValByName(autoFillProperties.getCreateTimeField(),
						LocalDateTime.now(), metaObject);
				}
			}
		}

		private void created(MetaObject metaObject) {
			SuperEntity entity = (SuperEntity) metaObject.getOriginalObject();
			if (entity.getCreateTime() == null) {
				this.setFieldValByName(autoFillProperties.getCreateTimeField(), LocalDateTime.now(),
					metaObject);
			}

			if (entity.getCreatedBy() == null || entity.getCreatedBy().equals(0)) {
				Object userIdVal = StrPool.STRING_TYPE_NAME.equals(
					metaObject.getGetterType(SuperEntity.CREATED_BY).getName()) ? String.valueOf(
					SecurityUtil.getUserId()) : SecurityUtil.getUserId();
				this.setFieldValByName(Entity.CREATED_BY, userIdVal, metaObject);
			}
		}

		private void fillUpdated(MetaObject metaObject) {
			// 修改人 修改时间
			if (metaObject.getOriginalObject() instanceof Entity) {
				update(metaObject);
				return;
			}

			if (metaObject.hasGetter(Entity.UPDATED_BY)) {
				Object oldVal = metaObject.getValue(Entity.UPDATED_BY);
				if (oldVal == null) {
					this.setFieldValByName(Entity.UPDATED_BY, SecurityUtil.getUserId(), metaObject);
				}
			}
			if (metaObject.hasGetter(autoFillProperties.getUpdateTimeField())) {
				Object oldVal = metaObject.getValue(autoFillProperties.getUpdateTimeField());
				if (oldVal == null) {
					this.setFieldValByName(autoFillProperties.getUpdateTimeField(),
						LocalDateTime.now(), metaObject);
				}
			}
		}

		private void update(MetaObject metaObject) {
			Entity entity = (Entity) metaObject.getOriginalObject();
			if (entity.getUpdatedBy() == null || entity.getUpdatedBy().equals(0)) {
				Object userIdVal = StrPool.STRING_TYPE_NAME.equals(
					metaObject.getGetterType(Entity.UPDATED_BY).getName()) ? String.valueOf(
					SecurityUtil.getUserId()) : SecurityUtil.getUserId();
				this.setFieldValByName(Entity.UPDATED_BY, userIdVal, metaObject);
			}

			if (entity.getUpdateTime() == null) {
				this.setFieldValByName(autoFillProperties.getUpdateTimeField(), LocalDateTime.now(),
					metaObject);
			}
		}

		/**
		 * 更新填充
		 */
		@Override
		public void updateFill(MetaObject metaObject) {
			fillUpdated(metaObject);
		}
	}

	/**
	 * 用于输出每条 SQL 语句及其执行时间
	 */
	@Intercepts({
		@Signature(type = StatementHandler.class, method = "query", args = {Statement.class,
			ResultHandler.class}),
		@Signature(type = StatementHandler.class, method = "update", args = Statement.class),
		@Signature(type = StatementHandler.class, method = "batch", args = Statement.class)
	})
	public static class SqlLogInterceptor implements Interceptor {

		private static final String DRUID_POOLED_PREPARED_STATEMENT = "com.alibaba.druid.pool.DruidPooledPreparedStatement";
		private static final String T4C_PREPARED_STATEMENT = "oracle.jdbc.driver.T4CPreparedStatement";
		private static final String ORACLE_PREPARED_STATEMENT_WRAPPER = "oracle.jdbc.driver.OraclePreparedStatementWrapper";

		private Method oracleGetOriginalSqlMethod;
		private Method druidGetSqlMethod;

		@Override
		public Object intercept(Invocation invocation) throws Throwable {
			Statement statement;
			Object firstArg = invocation.getArgs()[0];
			if (Proxy.isProxyClass(firstArg.getClass())) {
				statement = (Statement) SystemMetaObject.forObject(firstArg)
					.getValue("h.statement");
			} else {
				statement = (Statement) firstArg;
			}
			MetaObject stmtMetaObj = SystemMetaObject.forObject(statement);
			try {
				statement = (Statement) stmtMetaObj.getValue("stmt.statement");
			} catch (Exception e) {
				// do nothing
			}
			if (stmtMetaObj.hasGetter("delegate")) {
				//Hikari
				try {
					statement = (Statement) stmtMetaObj.getValue("delegate");
				} catch (Exception ignored) {

				}
			}

			String originalSql = null;
			String stmtClassName = statement.getClass().getName();
			if (DRUID_POOLED_PREPARED_STATEMENT.equals(stmtClassName)) {
				try {
					if (druidGetSqlMethod == null) {
						Class<?> clazz = Class.forName(DRUID_POOLED_PREPARED_STATEMENT);
						druidGetSqlMethod = clazz.getMethod("getSql");
					}
					Object stmtSql = druidGetSqlMethod.invoke(statement);
					if (stmtSql instanceof String) {
						originalSql = (String) stmtSql;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (T4C_PREPARED_STATEMENT.equals(stmtClassName)
				|| ORACLE_PREPARED_STATEMENT_WRAPPER.equals(stmtClassName)) {
				try {
					if (oracleGetOriginalSqlMethod != null) {
						Object stmtSql = oracleGetOriginalSqlMethod.invoke(statement);
						if (stmtSql instanceof String) {
							originalSql = (String) stmtSql;
						}
					} else {
						Class<?> clazz = Class.forName(stmtClassName);
						oracleGetOriginalSqlMethod = getMethodRegular(clazz, "getOriginalSql");
						if (oracleGetOriginalSqlMethod != null) {
							//OraclePreparedStatementWrapper is not a public class, need set this.
							oracleGetOriginalSqlMethod.setAccessible(true);
							if (null != oracleGetOriginalSqlMethod) {
								Object stmtSql = oracleGetOriginalSqlMethod.invoke(statement);
								if (stmtSql instanceof String) {
									originalSql = (String) stmtSql;
								}
							}
						}
					}
				} catch (Exception e) {
					//ignore
				}
			}
			if (originalSql == null) {
				originalSql = statement.toString();
			}
			originalSql = originalSql.replaceAll("[\\s]+", StringPool.SPACE);
			int index = indexOfSqlStart(originalSql);
			if (index > 0) {
				originalSql = originalSql.substring(index);
			}

			// 计算执行 SQL 耗时
			long start = SystemClock.now();
			Object result = invocation.proceed();
			long timing = SystemClock.now() - start;

			// SQL 打印执行结果
			Object target = PluginUtils.realTarget(invocation.getTarget());
			MetaObject metaObject = SystemMetaObject.forObject(target);
			MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

			// 打印 sql
			String sqlLogger = "\n\n==============  Sql Start  ==============" +
				"\nExecute ID  ：{}" +
				"\nExecute SQL ：{}" +
				"\nExecute Time：{} ms" +
				"\n==============  Sql  End   ==============\n";
			LogUtil.info(sqlLogger, ms.getId(), originalSql, timing);
			return result;
		}

		@Override
		public Object plugin(Object target) {
			if (target instanceof StatementHandler) {
				return Plugin.wrap(target, this);
			}
			return target;
		}

		/**
		 * 获取此方法名的具体 Method
		 *
		 * @param clazz      class 对象
		 * @param methodName 方法名
		 * @return 方法
		 */
		private Method getMethodRegular(Class<?> clazz, String methodName) {
			if (Object.class.equals(clazz)) {
				return null;
			}
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.getName().equals(methodName)) {
					return method;
				}
			}
			return getMethodRegular(clazz.getSuperclass(), methodName);
		}

		/**
		 * 获取sql语句开头部分
		 *
		 * @param sql ignore
		 * @return ignore
		 */
		private int indexOfSqlStart(String sql) {
			String upperCaseSql = sql.toUpperCase();
			Set<Integer> set = new HashSet<>();
			set.add(upperCaseSql.indexOf("SELECT "));
			set.add(upperCaseSql.indexOf("UPDATE "));
			set.add(upperCaseSql.indexOf("INSERT "));
			set.add(upperCaseSql.indexOf("DELETE "));
			set.remove(-1);
			if (CollectionUtils.isEmpty(set)) {
				return -1;
			}
			List<Integer> list = new ArrayList<>(set);
			list.sort(Comparator.naturalOrder());
			return list.get(0);
		}

	}

}
