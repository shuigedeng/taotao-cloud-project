package com.taotao.cloud.data.analysis.datasource;


import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = DataSourceProperties.DS, ignoreUnknownFields = false)
@Data
public class DataSourceProperties {
    final static String DS = "spring.datasource";

    private Mysql mysql;
    private ClickHouse clickhouse;
    private Doris doris;
    private Hive hive;
    private Tidb tidb;
    private Trino trino;

	@Data
	public static class Mysql{
		private boolean enabled = false;

		private String driverClassName;
		private String url;
		private String username;
		private String password;
	}
	@Data
	public static class Trino{
		private boolean enabled = false;

		private String driverClassName;
		private String url;
		private String username;
		private String password;
	}
	@Data
	public static class Tidb{
		private boolean enabled= false;

		private String driverClassName;
		private String url;
		private String username;
		private String password;
	}
	@Data
	public static class Hive{
		private boolean enabled= false;

		private String driverClassName;
		private String url;
		private String username;
		private String password;
	}
	@Data
	public static class Doris{
		private boolean enabled= false;

		private String driverClassName;
		private String url;
		private String username;
		private String password;
	}
	@Data
	public static class ClickHouse{
		private boolean enabled= false;

		private String driverClassName;
		private String url;
		private String username;
		private String password;
	}


}
