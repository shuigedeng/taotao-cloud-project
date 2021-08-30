package com.taotao.cloud.core.properties;

import com.taotao.cloud.core.enums.EnvironmentEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author: chejiangyi
 * @version: 2019-08-12 11:27
 **/
@RefreshScope
@ConfigurationProperties(prefix = CoreProperties.PREFIX)
public class CoreProperties {

	public static final String PREFIX = "taotao.cloud.core";

	public static String SpringApplicationName = "spring.application.name";

	public static String ServerTomcatMaxThreads = "server.tomcat.max-threads";
	public static String ServerTomcatMaxConnections = "server.tomcat.max-connections";
	public static String ServerTomcatMinSpaceThreads = "server.tomcat.min-spare-threads";
	public static String ServeCompressionEnabled = "server.compression.enabled";
	public static String ServeCompressionMimeTypes = "server.compression.mime-types";
	public static String LoggingFile = "logging.file";
	public static String LoggingFileMaxHistory = "logging.file.max-history";
	public static String LoggingFileMaxSize = "logging.file.max-size";
	public static String LoggingFileTotalSize = "logging.file.total-size";
	public static String ContextRestartText = "taotao.cloud.core.context.restart.text";
	public static String IsPrintSqlError = "taotao.cloud.core.dbPrintSqlErrorEnabled";


	private EnvironmentEnum env;
	private boolean enabled = true;
	private boolean dbPrintSqlEnabled = true;
	private boolean collectHookEnabled = true;
	private boolean contextRestartEnabled = false;
	private boolean isPrintSqlError = true;
	private int contextRestartTimespan = 10;


	public EnvironmentEnum getEnv() {
		return env;
	}

	public void setEnv(EnvironmentEnum env) {
		this.env = env;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isDbPrintSqlEnabled() {
		return dbPrintSqlEnabled;
	}

	public void setDbPrintSqlEnabled(boolean dbPrintSqlEnabled) {
		this.dbPrintSqlEnabled = dbPrintSqlEnabled;
	}

	public boolean isCollectHookEnabled() {
		return collectHookEnabled;
	}

	public void setCollectHookEnabled(boolean collectHookEnabled) {
		this.collectHookEnabled = collectHookEnabled;
	}

	public boolean isContextRestartEnabled() {
		return contextRestartEnabled;
	}

	public void setContextRestartEnabled(boolean contextRestartEnabled) {
		this.contextRestartEnabled = contextRestartEnabled;
	}

	public int getContextRestartTimespan() {
		return contextRestartTimespan;
	}

	public void setContextRestartTimespan(int contextRestartTimespan) {
		this.contextRestartTimespan = contextRestartTimespan;
	}

	public boolean isPrintSqlError() {
		return isPrintSqlError;
	}

	public void setPrintSqlError(boolean printSqlError) {
		isPrintSqlError = printSqlError;
	}
}
