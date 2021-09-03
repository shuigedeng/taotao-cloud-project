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
package com.taotao.cloud.core.properties;

import com.taotao.cloud.core.enums.EnvironmentEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * CoreProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:43:31
 */
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

	/**
	 * env
	 */
	private EnvironmentEnum env;
	/**
	 * enabled
	 */
	private boolean enabled = true;
	/**
	 * dbPrintSqlEnabled
	 */
	private boolean dbPrintSqlEnabled = true;
	/**
	 * collectHookEnabled
	 */
	private boolean collectHookEnabled = true;
	/**
	 * contextRestartEnabled
	 */
	private boolean contextRestartEnabled = false;
	/**
	 * isPrintSqlError
	 */
	private boolean isPrintSqlError = true;
	/**
	 * contextRestartTimespan
	 */
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
