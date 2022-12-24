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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
//basePackages 这里是mapper所在包路径， 根据自己项目调整
@MapperScan(basePackages = "com.mo.xue.doristest.mapper.mysql", sqlSessionFactoryRef = "mysqlSqlSessionFactory")
public class MysqlConfig {

	//这里是mapper.xml路径， 根据自己的项目调整
	private static final String MAPPER_LOCATION = "classpath*:mapper/mysql/*.xml";
	//这里是数据库表对应的entity实体类所在包路径， 根据自己的项目调整
	private static final String TYPE_ALIASES_PACKAGE = "com.mo.xue.doristest.bean.mysql.*";

	@Primary //这个注解的意思是默认使用当前数据源
	@Bean(name = "mysqlDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.mysql")
	public DataSource mysqlDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean("mysqlSqlSessionFactory")
	public SqlSessionFactory mysqlSqlSessionFactory(
		@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		// mapper的xml形式文件位置必须要配置，不然将报错：no statement （这种错误也可能是mapper的xml中，namespace与项目的路径不一致导致）
		bean.setMapperLocations(
			new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
		bean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
		return bean.getObject();
	}

	@Primary
	@Bean("mysqlSqlSessionTemplate")
	public SqlSessionTemplate mysqlSqlSessionTemplate(
		@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
