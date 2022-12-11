package com.taotao.cloud.data.mybatisplus.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.taotao.cloud.data.mybatisplus.datascope.DataScopeInterceptor;
import com.taotao.cloud.data.mybatisplus.interceptor.MpInterceptor;
import com.taotao.cloud.data.mybatisplus.interceptor.SqlPaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 插件容器
 */
@AutoConfiguration
public class MpInterceptorConfiguration {

	/**
	 * 单页分页条数限制(默认无限制,参见 插件#handlerLimit 方法)
	 */
	private static final Long MAX_LIMIT = 1000L;

	/**
	 * 分页插件
	 */
	@Bean
	public MpInterceptor paginationInnerInterceptor() {
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
		return new MpInterceptor(paginationInterceptor, 120);
	}

	/**
	 * 防止全表更新与删除插件
	 */
	@Bean
	public MpInterceptor blockAttackInnerInterceptor() {
		return new MpInterceptor(new BlockAttackInnerInterceptor(), 121);
	}

	/**
	 * sql规范插件
	 */
	@Bean
	public MpInterceptor illegalSqlInnerInterceptor() {
		return new MpInterceptor(new IllegalSQLInnerInterceptor(), 125);
	}

	/**
	 * 乐观锁插件
	 */
	@Bean
	public MpInterceptor optimisticLockerInnerInterceptor() {
		return new MpInterceptor(new OptimisticLockerInnerInterceptor(), 128);
	}

	/**
	 * 住户插件
	 */
	public MpInterceptor getTenantInnerInterceptor() {
		// LogUtils.info("检测到 lamp.database.multiTenantType={}，已启用 {} 模式", databaseProperties.getMultiTenantType().name(), databaseProperties.getMultiTenantType().getDescribe());
		// if (StrUtil.equalsAny(databaseProperties.getMultiTenantType().name(),
		// 	MultiTenantType.SCHEMA.name(), MultiTenantType.SCHEMA_COLUMN.name())) {
		// 	ArgumentAssert.notNull(databaseProperties.getDbType(), "SCHEMA 模式请在mysql.yml、oracle.yml、sqlserver.yml中配置: {}.dbType", DatabaseProperties.PREFIX);
		//
		// 	// SCHEMA 动态表名插件
		// 	SchemaInterceptor schemaInterceptor = new SchemaInterceptor(databaseProperties.getTenantDatabasePrefix(), databaseProperties.getOwner(), databaseProperties.getDbType());
		// 	interceptor.addInnerInterceptor(schemaInterceptor);
		// }
		//
		// if (StrUtil.equalsAny(databaseProperties.getMultiTenantType().name(),
		// 	MultiTenantType.COLUMN.name(), MultiTenantType.SCHEMA_COLUMN.name(), MultiTenantType.DATASOURCE_COLUMN.name())) {
		// 	// COLUMN 模式 多租户插件
		// 	TenantLineInnerInterceptor tli = new TenantLineInnerInterceptor();
		// 	tli.setTenantLineHandler(new TenantLineHandler() {
		// 		@Override
		// 		public String getTenantIdColumn() {
		// 			return databaseProperties.getTenantIdColumn();
		// 		}
		//
		// 		@Override
		// 		public boolean ignoreTable(String tableName) {
		// 			return databaseProperties.getIgnoreTables() != null && databaseProperties.getIgnoreTables().contains(tableName);
		// 		}
		//
		// 		@Override
		// 		public Expression getTenantId() {
		// 			return MultiTenantType.COLUMN.eq(databaseProperties.getMultiTenantType()) ?
		// 				new StringValue(ContextUtil.getTenant()) :
		// 				new StringValue(ContextUtil.getSubTenant());
		// 		}
		// 	});
		// 	interceptor.addInnerInterceptor(tli);
		// }
		return null;
	}

	/**
	 * 数据权限插件
	 */
	public MpInterceptor dataScopeInterceptor() {
		return new MpInterceptor(new DataScopeInterceptor());
	}

}
