package com.taotao.cloud.health.base;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author: chejiangyi
 * @version: 2019-08-12 11:27
 **/
public class CoreProperties {

	/**
	 * 枚举:dev,test,prd
	 */
	@Value("${bsf.env:dev}")
	private Environment env;


	public static String Project = "Core";
	public static String SpringApplicationName = "spring.application.name";
	public static String BsfEnv = "bsf.env";
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
	public static String BsfLoggingFileTotalSize = "bsf.logging.file.total-size";
	public static String BsfContextRestartText = "bsf.context.restart.text";
	public static String BsfContextRestartEnabled = "bsf.context.restart.enabled";
	public static String BsfContextRestartTimeSpan = "bsf.context.restart.timespan";
	public static String BsfEnabled = "bsf.enabled";
	public static String BsfCollectHookEnabled = "bsf.collect.hook.enabled";
	public static String BsfIsPrintSqlTimeWatch = "bsf.db.printSql.enabled";
	public static String BsfIsPrintSqlError = "bsf.db.printSqlError.enabled";

	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public static String getProject() {
		return Project;
	}

	public static void setProject(String project) {
		Project = project;
	}

	public static String getSpringApplicationName() {
		return SpringApplicationName;
	}

	public static void setSpringApplicationName(String springApplicationName) {
		SpringApplicationName = springApplicationName;
	}

	public static String getBsfEnv() {
		return BsfEnv;
	}

	public static void setBsfEnv(String bsfEnv) {
		BsfEnv = bsfEnv;
	}

	public static String getSpringJacksonDateFormat() {
		return SpringJacksonDateFormat;
	}

	public static void setSpringJacksonDateFormat(String springJacksonDateFormat) {
		SpringJacksonDateFormat = springJacksonDateFormat;
	}

	public static String getSpringJacksonTimeZone() {
		return SpringJacksonTimeZone;
	}

	public static void setSpringJacksonTimeZone(String springJacksonTimeZone) {
		SpringJacksonTimeZone = springJacksonTimeZone;
	}

	public static String getServerTomcatMaxThreads() {
		return ServerTomcatMaxThreads;
	}

	public static void setServerTomcatMaxThreads(String serverTomcatMaxThreads) {
		ServerTomcatMaxThreads = serverTomcatMaxThreads;
	}

	public static String getServerTomcatMaxConnections() {
		return ServerTomcatMaxConnections;
	}

	public static void setServerTomcatMaxConnections(String serverTomcatMaxConnections) {
		ServerTomcatMaxConnections = serverTomcatMaxConnections;
	}

	public static String getServerTomcatMinSpaceThreads() {
		return ServerTomcatMinSpaceThreads;
	}

	public static void setServerTomcatMinSpaceThreads(String serverTomcatMinSpaceThreads) {
		ServerTomcatMinSpaceThreads = serverTomcatMinSpaceThreads;
	}

	public static String getServeCompressionEnabled() {
		return ServeCompressionEnabled;
	}

	public static void setServeCompressionEnabled(String serveCompressionEnabled) {
		ServeCompressionEnabled = serveCompressionEnabled;
	}

	public static String getServeCompressionMimeTypes() {
		return ServeCompressionMimeTypes;
	}

	public static void setServeCompressionMimeTypes(String serveCompressionMimeTypes) {
		ServeCompressionMimeTypes = serveCompressionMimeTypes;
	}

	public static String getLoggingFile() {
		return LoggingFile;
	}

	public static void setLoggingFile(String loggingFile) {
		LoggingFile = loggingFile;
	}

	public static String getLoggingFileMaxHistory() {
		return LoggingFileMaxHistory;
	}

	public static void setLoggingFileMaxHistory(String loggingFileMaxHistory) {
		LoggingFileMaxHistory = loggingFileMaxHistory;
	}

	public static String getLoggingFileMaxSize() {
		return LoggingFileMaxSize;
	}

	public static void setLoggingFileMaxSize(String loggingFileMaxSize) {
		LoggingFileMaxSize = loggingFileMaxSize;
	}

	public static String getBsfLoggingFileTotalSize() {
		return BsfLoggingFileTotalSize;
	}

	public static void setBsfLoggingFileTotalSize(String bsfLoggingFileTotalSize) {
		BsfLoggingFileTotalSize = bsfLoggingFileTotalSize;
	}

	public static String getBsfContextRestartText() {
		return BsfContextRestartText;
	}

	public static void setBsfContextRestartText(String bsfContextRestartText) {
		BsfContextRestartText = bsfContextRestartText;
	}

	public static String getBsfContextRestartEnabled() {
		return BsfContextRestartEnabled;
	}

	public static void setBsfContextRestartEnabled(String bsfContextRestartEnabled) {
		BsfContextRestartEnabled = bsfContextRestartEnabled;
	}

	public static String getBsfContextRestartTimeSpan() {
		return BsfContextRestartTimeSpan;
	}

	public static void setBsfContextRestartTimeSpan(String bsfContextRestartTimeSpan) {
		BsfContextRestartTimeSpan = bsfContextRestartTimeSpan;
	}

	public static String getBsfEnabled() {
		return BsfEnabled;
	}

	public static void setBsfEnabled(String bsfEnabled) {
		BsfEnabled = bsfEnabled;
	}

	public static String getBsfCollectHookEnabled() {
		return BsfCollectHookEnabled;
	}

	public static void setBsfCollectHookEnabled(String bsfCollectHookEnabled) {
		BsfCollectHookEnabled = bsfCollectHookEnabled;
	}

	public static String getBsfIsPrintSqlTimeWatch() {
		return BsfIsPrintSqlTimeWatch;
	}

	public static void setBsfIsPrintSqlTimeWatch(String bsfIsPrintSqlTimeWatch) {
		BsfIsPrintSqlTimeWatch = bsfIsPrintSqlTimeWatch;
	}

	public static String getBsfIsPrintSqlError() {
		return BsfIsPrintSqlError;
	}

	public static void setBsfIsPrintSqlError(String bsfIsPrintSqlError) {
		BsfIsPrintSqlError = bsfIsPrintSqlError;
	}
}
