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
package com.taotao.cloud.common.base;

import lombok.Data;

/**
 * CoreProperties
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/6/22 17:08
 **/
@Data
public class CoreProperties {
//    /**
//     * 枚举:dev,test,prd
//     */
//    @Value("${taotao.cloud.env:dev}")
//    private Environment env;


	public static String Project = "Core";
	public static String SpringApplicationName = "spring.application.name";
	public static String SpringJacksonDateFormat = "spring.jackson.date-format";
	public static String SpringJacksonTimeZone = "spring.jackson.time-zone";
	public static String ServerTomcatMaxThreads = "server.tomcat.max-threads";
	public static String ServerTomcatMaxConnections = "server.tomcat.max-connections";
	public static String ServerTomcatMinSpaceThreads = "server.tomcat.min-spare-threads";
	public static String ServeCompressionEnabled = "server.compression.enabled";
	public static String ServeCompressionMimeTypes = "server.compression.mime-types";
	public static String LoggingFile = "logging.file";
	public static String LoggingFileMaxHistory = "logging.file.max-history";
	public static String LoggingFileMaxSize = "logging.file.max-size";

	public static String TaoTaoCloudEnv = "taotao.cloud.core.env";
	public static String TaoTaoCloudLoggingFileTotalSize = "taotao.cloud.core.logging.file.total-size";
	public static String TaoTaoCloudContextRestartText = "taotao.cloud.core.context.restart.text";
	public static String TaoTaoCloudContextRestartEnabled = "taotao.cloud.core.context.restart.enabled";
	public static String TaoTaoCloudContextRestartTimeSpan = "taotao.cloud.core.context.restart.timespan";
	public static String TaoTaoCloudEnabled = "taotao.cloud.core.enabled";
	public static String TaoTaoCloudCollectHookEnabled = "taotao.cloud.core.collect.hook.enabled";
	public static String TaoTaoCloudIsPrintSqlTimeWatch = "taotao.cloud.core.db.printSql.enabled";
	public static String TaoTaoCloudIsPrintSqlError = "taotao.cloud.core.db.printSqlError.enabled";
}
