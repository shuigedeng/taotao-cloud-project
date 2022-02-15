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
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.constant.StrPool;
import com.taotao.cloud.common.utils.IdGeneratorUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.SecurityUtil;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.data.mybatis.plus.datascope.DataScopeInterceptor;
import com.taotao.cloud.data.mybatis.plus.entity.MpSuperEntity;
import com.taotao.cloud.data.mybatis.plus.injector.MateSqlInjector;
import com.taotao.cloud.data.mybatis.plus.interceptor.SqlLogInterceptor;
import com.taotao.cloud.data.mybatis.plus.interceptor.SqlMybatisInterceptor;
import com.taotao.cloud.data.mybatis.plus.properties.MybatisPlusAutoFillProperties;
import com.taotao.cloud.data.mybatis.plus.properties.MybatisPlusProperties;
import com.taotao.cloud.data.mybatis.plus.properties.TenantProperties;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.EnumTypeHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlusAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:02
 */
@Configuration
@AutoConfigureAfter(TenantAutoConfiguration.class)
@EnableConfigurationProperties({MybatisPlusAutoFillProperties.class, MybatisPlusProperties.class, TenantProperties.class})
@ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = "enabled", havingValue = "true")
public class MybatisPlusAutoConfiguration implements InitializingBean {

	private final TenantProperties tenantProperties;
	private final MybatisPlusAutoFillProperties autoFillProperties;

	@Autowired(required = false)
	private TenantLineInnerInterceptor tenantLineInnerInterceptor;

	/**
	 * 单页分页条数限制(默认无限制,参见 插件#handlerLimit 方法)
	 */
	private static final Long MAX_LIMIT = 1000L;

	public MybatisPlusAutoConfiguration(
		TenantProperties tenantProperties,
		MybatisPlusAutoFillProperties autoFillProperties) {
		this.tenantProperties = tenantProperties;
		this.autoFillProperties = autoFillProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(MybatisPlusAutoConfiguration.class, StarterName.MYBATIS_PLUS_STARTER);
	}

	/**
	 * sql 注入配置
	 */
	@Bean
	public ISqlInjector sqlInjector() {
		return new MateSqlInjector();
	}

	@Bean
	@ConditionalOnProperty(value = "mybatis-plus.sql-log.enable", matchIfMissing = true)
	public SqlLogInterceptor sqlLogInterceptor() {
		return new SqlLogInterceptor();
	}

	@Bean
	@ConditionalOnClass(name = "org.apache.ibatis.plugin.Interceptor")
	public SqlMybatisInterceptor sqlMybatisInterceptor(Collector collector) {
		return new SqlMybatisInterceptor(collector);
	}

	/**
	 * 新的分页插件,一缓和二缓遵循mybatis的规则, 需要设置 MybatisConfiguration#useDeprecatedExecutor = false
	 * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

		//分页插件
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		paginationInnerInterceptor.setMaxLimit(MAX_LIMIT);
		paginationInnerInterceptor.setDbType(DbType.MYSQL);
		paginationInnerInterceptor.setOverflow(true);
		interceptor.addInnerInterceptor(paginationInnerInterceptor);

		if (tenantProperties.getEnabled() && Objects.nonNull(tenantLineInnerInterceptor)) {
			// 多租户插件
			interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
		}

		if (tenantProperties.getDataScope()) {
			// 数据权限插件
			interceptor.addInnerInterceptor(new DataScopeInterceptor());
		}

		//防止全表更新与删除插件: BlockAttackInnerInterceptor
		BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
		interceptor.addInnerInterceptor(blockAttackInnerInterceptor);

		//乐观锁插件
		OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
		interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor);

		//sql规范插件
		//IllegalSQLInnerInterceptor illegalSQLInnerInterceptor = new IllegalSQLInnerInterceptor();
		//interceptor.addInnerInterceptor(illegalSQLInnerInterceptor);

		return interceptor;
	}

	/**
	 * 自动填充数据配置
	 */
	@Bean
	@ConditionalOnProperty(prefix = MybatisPlusAutoFillProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
	public MetaObjectHandler metaObjectHandler() {
		return new DateMetaObjectHandler(autoFillProperties);
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
			if (metaObject.getOriginalObject() instanceof MpSuperEntity) {
				Object oldId = ((MpSuperEntity) metaObject.getOriginalObject()).getId();
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

			// 2. 没有继承SuperEntity， 但主键的字段名为：id
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
				keyType.getName().equalsIgnoreCase(StrPool.STRING_TYPE_NAME)
					? String.valueOf(id)
					: id;
			this.setFieldValByName(keyProperty, idVal, metaObject);
		}

		private void fillCreated(MetaObject metaObject) {
			// 设置创建时间和创建人
			if (metaObject.getOriginalObject() instanceof MpSuperEntity) {
				created(metaObject);
				return;
			}

			if (metaObject.hasGetter(MpSuperEntity.CREATED_BY)) {
				Object oldVal = metaObject.getValue(MpSuperEntity.CREATED_BY);
				if (oldVal == null) {
					this.setFieldValByName(MpSuperEntity.CREATED_BY, SecurityUtil.getUserId(),
						metaObject);
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
			MpSuperEntity entity = (MpSuperEntity) metaObject.getOriginalObject();
			if (entity.getCreateTime() == null) {
				this.setFieldValByName(autoFillProperties.getCreateTimeField(), LocalDateTime.now(),
					metaObject);
			}

			if (entity.getCreatedBy() == null || entity.getCreatedBy().equals(0)) {
				Object userIdVal = StrPool.STRING_TYPE_NAME.equals(
					metaObject.getGetterType(MpSuperEntity.CREATED_BY).getName()) ? String.valueOf(
					SecurityUtil.getUserId()) : SecurityUtil.getUserId();
				this.setFieldValByName(MpSuperEntity.CREATED_BY, userIdVal, metaObject);
			}
		}

		private void fillUpdated(MetaObject metaObject) {
			// 修改人 修改时间
			if (metaObject.getOriginalObject() instanceof MpSuperEntity) {
				update(metaObject);
				return;
			}

			if (metaObject.hasGetter(MpSuperEntity.UPDATED_BY)) {
				Object oldVal = metaObject.getValue(MpSuperEntity.UPDATED_BY);
				if (oldVal == null) {
					this.setFieldValByName(MpSuperEntity.UPDATED_BY, SecurityUtil.getUserId(),
						metaObject);
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
			MpSuperEntity entity = (MpSuperEntity) metaObject.getOriginalObject();
			if (entity.getUpdatedBy() == null || entity.getUpdatedBy().equals(0)) {
				Object userIdVal = StrPool.STRING_TYPE_NAME.equals(
					metaObject.getGetterType(MpSuperEntity.UPDATED_BY).getName()) ? String.valueOf(
					SecurityUtil.getUserId()) : SecurityUtil.getUserId();
				this.setFieldValByName(MpSuperEntity.UPDATED_BY, userIdVal, metaObject);
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


}
