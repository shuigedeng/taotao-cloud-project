package com.taotao.cloud.data.analysis.datasource;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = DataSourceCommonProperties.DS, ignoreUnknownFields = false)
@Data
public class DataSourceCommonProperties {
    final static String DS = "spring.datasource.commonconfig";
    private int initialSize = 10;
    private int minIdle;
    private int maxIdle;
    private int maxActive;
    private int maxWait;
    private int timeBetweenEvictionRunsMillis;
    private int minEvictableIdleTimeMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean poolPreparedStatements;
    private int maxOpenPreparedStatements;
    private String filters;
    private String mapperLocations;
    private String typeAliasPackage;
}
