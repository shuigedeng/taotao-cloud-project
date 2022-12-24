package com.taotao.cloud.data.analysis.doris;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = "com.mo.xue.doristest.mapper.doris", sqlSessionFactoryRef = "dorisSqlSessionFactory")
public class DorisConfig {

	private static final String MAPPER_LOCATION = "classpath*:mapper/doris/*.xml";
	private static final String TYPE_ALIASES_PACKAGE = "com.mo.xue.doristest.bean.doris.*";

	@Bean("dorisDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.doris")
	public DataSource getDb1DataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean("dorisSqlSessionFactory")
	public SqlSessionFactory dorisSqlSessionFactory(
		@Qualifier("dorisDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(
			new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
		bean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
		return bean.getObject();
	}

	@Bean("dorisSqlSessionTemplate")
	public SqlSessionTemplate dorisSqlSessionTemplate(
		@Qualifier("dorisSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
