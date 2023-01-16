package com.taotao.cloud.data.analysis.datasource.doris;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.data.analysis.datasource.DataSourceCommonProperties;
import com.taotao.cloud.data.analysis.datasource.DataSourceProperties;
import com.taotao.cloud.data.analysis.datasource.trino.TrinoDataSourceConfiguration;
import java.sql.SQLException;
import java.util.TimeZone;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.doris.enabled", havingValue = "true")
@MapperScan(basePackages = "com.taotao.cloud.data.analysis.doris.mapper", sqlSessionFactoryRef = "dorisSqlSessionFactory")
public class DorisDataSourceConfiguration {

	private static Logger logger = LoggerFactory.getLogger(TrinoDataSourceConfiguration.class);

	//这里是mapper.xml路径， 根据自己的项目调整
	private static final String MAPPER_LOCATION = "classpath*:mapper/doris/*.xml";
	//这里是数据库表对应的entity实体类所在包路径， 根据自己的项目调整
	private static final String TYPE_ALIASES_PACKAGE = "com.taotao.cloud.data.analysis.doris.*";

	@Autowired
	private DataSourceProperties dataSourceProperties;

	@Autowired
	private DataSourceCommonProperties dataSourceCommonProperties;

	@Bean("dorisDruidDataSource") //新建bean实例
	@Qualifier("dorisDruidDataSource")//标识
	public DataSource clickHouseDruidDataSource(){
		TimeZone.setDefault(TimeZone.getTimeZone("+08:00"));
		DruidDataSource datasource = new DruidDataSource();

		//配置数据源属性
		datasource.setUrl(dataSourceProperties.getDoris().getUrl());
		datasource.setUsername(dataSourceProperties.getDoris().getUsername());
        datasource.setPassword(dataSourceProperties.getDoris().getPassword());
		datasource.setDriverClassName(dataSourceProperties.getDoris().getDriverClassName());

		//配置统一属性
		datasource.setInitialSize(dataSourceCommonProperties.getInitialSize());
		datasource.setMinIdle(dataSourceCommonProperties.getMinIdle());
		datasource.setMaxActive(dataSourceCommonProperties.getMaxActive());
		datasource.setMaxWait(dataSourceCommonProperties.getMaxWait());
		datasource.setTimeBetweenEvictionRunsMillis(dataSourceCommonProperties.getTimeBetweenEvictionRunsMillis());
		datasource.setMinEvictableIdleTimeMillis(dataSourceCommonProperties.getMinEvictableIdleTimeMillis());
		datasource.setValidationQuery(dataSourceCommonProperties.getValidationQuery());
		datasource.setTestWhileIdle(dataSourceCommonProperties.isTestWhileIdle());
		datasource.setTestOnBorrow(dataSourceCommonProperties.isTestOnBorrow());
		datasource.setTestOnReturn(dataSourceCommonProperties.isTestOnReturn());
		datasource.setPoolPreparedStatements(dataSourceCommonProperties.isPoolPreparedStatements());
		try {
			datasource.setFilters(dataSourceCommonProperties.getFilters());
		} catch (SQLException e) {
			logger.error("Druid configuration initialization filter error.", e);
		}
		return datasource;
	}

	@Bean(name = "dorisTemplate")
	public JdbcTemplate prestoJdbcTemplate(@Qualifier("dorisDruidDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean("dorisSqlSessionFactory")
	public SqlSessionFactory mysqlSqlSessionFactory(
		@Qualifier("dorisDruidDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		// mapper的xml形式文件位置必须要配置，不然将报错：no statement （这种错误也可能是mapper的xml中，namespace与项目的路径不一致导致）
		bean.setMapperLocations(
			new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
		bean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
		return bean.getObject();
	}

	@Bean("dorisSqlSessionTemplate")
	public SqlSessionTemplate mysqlSqlSessionTemplate(
		@Qualifier("dorisSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}

