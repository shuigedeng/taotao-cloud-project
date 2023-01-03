package com.taotao.cloud.data.analysis.datasource.trino;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.data.analysis.datasource.DataSourceCommonProperties;
import com.taotao.cloud.data.analysis.datasource.DataSourceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.TimeZone;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
@ConditionalOnProperty(name = "spring.datasource.trino.enabled", havingValue = "true")
public class TrinoDataSourceConfiguration {

    private static Logger logger = LoggerFactory.getLogger(TrinoDataSourceConfiguration.class);

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private DataSourceCommonProperties dataSourceCommonProperties;

    @Bean("trinoDruidDataSource") //新建bean实例
    @Qualifier("trinoDruidDataSource")//标识
    public DataSource dataSource(){
        TimeZone.setDefault(TimeZone.getTimeZone("+08:00"));
        DruidDataSource datasource = new DruidDataSource();

        //配置数据源属性
        datasource.setUrl(dataSourceProperties.getTrino().getUrl());
        datasource.setUsername(dataSourceProperties.getTrino().getUsername());
        datasource.setPassword(dataSourceProperties.getTrino().getPassword());
        datasource.setDriverClassName(dataSourceProperties.getTrino().getDriverClassName());

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

	@Bean(name = "trinoTemplate")
	public JdbcTemplate prestoJdbcTemplate(@Qualifier("trinoDruidDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}


	//@Bean("dorisSqlSessionFactory")
	//public SqlSessionFactory dorisSqlSessionFactory(
	//	@Qualifier("dorisDataSource") DataSource dataSource) throws Exception {
	//	SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
	//	bean.setDataSource(dataSource);
	//	bean.setMapperLocations(
	//		new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
	//	bean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
	//	return bean.getObject();
	//}
	//
	//@Bean("dorisSqlSessionTemplate")
	//public SqlSessionTemplate dorisSqlSessionTemplate(
	//	@Qualifier("dorisSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
	//	return new SqlSessionTemplate(sqlSessionFactory);
	//}
}
