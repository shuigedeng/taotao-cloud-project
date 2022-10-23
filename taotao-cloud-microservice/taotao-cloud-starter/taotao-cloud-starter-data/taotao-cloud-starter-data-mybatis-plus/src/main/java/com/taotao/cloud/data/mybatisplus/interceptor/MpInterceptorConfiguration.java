package com.taotao.cloud.data.mybatisplus.interceptor;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 插件容器
 *
 */
@Configuration
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


}
