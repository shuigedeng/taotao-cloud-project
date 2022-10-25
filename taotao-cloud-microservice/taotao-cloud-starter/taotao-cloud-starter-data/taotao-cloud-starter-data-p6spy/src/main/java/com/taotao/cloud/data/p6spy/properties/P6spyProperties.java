package com.taotao.cloud.data.p6spy.properties;

import cn.hutool.core.util.StrUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

@RefreshScope
@ConfigurationProperties(prefix = P6spyProperties.PREFIX)
public class P6spyProperties implements EnvironmentAware {

	public static final String PREFIX = "taotao.cloud.p6spy";
	private Environment environment;
	private boolean enabled = true;

	private String realdatasourceproperties;
	private String exclude;
	private String jndicontextfactory;
	private String useNanoTime;
	private String append = "true";
	private String stacktraceclass;
	private String databaseDialectTimestampFormat = "yyyy-MM-dd HH:mm:ss";
	private String dateformat = "yyyy-MM-dd HH:mm:ss";
	private String jmxPrefix;
	private String driverlist = "com.mysql.cj.jdbc.Driver";
	private String appender = "com.taotao.cloud.data.p6spy.logger.P6spyLogger";
	private String excludecategories;
	private String sqlexpression;
	private String modulelist = "com.p6spy.engine.spy.P6SpyFactory,com.p6spy.engine.logging.P6LogFactory,com.p6spy.engine.outage.P6OutageFactory";
	private String include;
	private String executionThreshold;
	private String reloadpropertiesinterval;
	private String outagedetectioninterval = "2";
	private String jmx;
	private String stacktrace;
	private String autoflush = "false";
	private String reloadproperties;
	private String logMessageFormat = "com.taotao.cloud.data.p6spy.logger.format.P6spyLogFormat";
	private String databaseDialectBinaryFormat;
	private String realdatasource;
	private String jndicontextcustom;
	private String customLogMessageFormat = "%(currentTime)|%(executionTime)|%(category)|connection%(connectionId)|%(sqlSingleLine)";
	private String filter;
	private String jndicontextproviderurl;
	private String outagedetection = "true";
	private String logfile;
	//private String logfile = "${user.home}/logs/${spring.application.name}/p6spy/spy.log";
	private String databaseDialectDateFormat = "yyyy-MM-dd HH:mm:ss";
	private String excludebinary;
	private String realdatasourceclass;

	public String getRealdatasourceproperties() {
		return realdatasourceproperties;
	}

	public void setRealdatasourceproperties(String realdatasourceproperties) {
		this.realdatasourceproperties = realdatasourceproperties;
	}

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public String getJndicontextfactory() {
		return jndicontextfactory;
	}

	public void setJndicontextfactory(String jndicontextfactory) {
		this.jndicontextfactory = jndicontextfactory;
	}

	public String getUseNanoTime() {
		return useNanoTime;
	}

	public void setUseNanoTime(String useNanoTime) {
		this.useNanoTime = useNanoTime;
	}

	public String getAppend() {
		return append;
	}

	public void setAppend(String append) {
		this.append = append;
	}

	public String getStacktraceclass() {
		return stacktraceclass;
	}

	public void setStacktraceclass(String stacktraceclass) {
		this.stacktraceclass = stacktraceclass;
	}

	public String getDatabaseDialectTimestampFormat() {
		return databaseDialectTimestampFormat;
	}

	public void setDatabaseDialectTimestampFormat(String databaseDialectTimestampFormat) {
		this.databaseDialectTimestampFormat = databaseDialectTimestampFormat;
	}

	public String getDateformat() {
		return dateformat;
	}

	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}

	public String getJmxPrefix() {
		return jmxPrefix;
	}

	public void setJmxPrefix(String jmxPrefix) {
		this.jmxPrefix = jmxPrefix;
	}

	public String getDriverlist() {
		return driverlist;
	}

	public void setDriverlist(String driverlist) {
		this.driverlist = driverlist;
	}

	public String getAppender() {
		return appender;
	}

	public void setAppender(String appender) {
		this.appender = appender;
	}

	public String getExcludecategories() {
		return excludecategories;
	}

	public void setExcludecategories(String excludecategories) {
		this.excludecategories = excludecategories;
	}

	public String getSqlexpression() {
		return sqlexpression;
	}

	public void setSqlexpression(String sqlexpression) {
		this.sqlexpression = sqlexpression;
	}

	public String getModulelist() {
		return modulelist;
	}

	public void setModulelist(String modulelist) {
		this.modulelist = modulelist;
	}

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	public String getExecutionThreshold() {
		return executionThreshold;
	}

	public void setExecutionThreshold(String executionThreshold) {
		this.executionThreshold = executionThreshold;
	}

	public String getReloadpropertiesinterval() {
		return reloadpropertiesinterval;
	}

	public void setReloadpropertiesinterval(String reloadpropertiesinterval) {
		this.reloadpropertiesinterval = reloadpropertiesinterval;
	}

	public String getOutagedetectioninterval() {
		return outagedetectioninterval;
	}

	public void setOutagedetectioninterval(String outagedetectioninterval) {
		this.outagedetectioninterval = outagedetectioninterval;
	}

	public String getJmx() {
		return jmx;
	}

	public void setJmx(String jmx) {
		this.jmx = jmx;
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	public String getAutoflush() {
		return autoflush;
	}

	public void setAutoflush(String autoflush) {
		this.autoflush = autoflush;
	}

	public String getReloadproperties() {
		return reloadproperties;
	}

	public void setReloadproperties(String reloadproperties) {
		this.reloadproperties = reloadproperties;
	}

	public String getLogMessageFormat() {
		return logMessageFormat;
	}

	public void setLogMessageFormat(String logMessageFormat) {
		this.logMessageFormat = logMessageFormat;
	}

	public String getDatabaseDialectBinaryFormat() {
		return databaseDialectBinaryFormat;
	}

	public void setDatabaseDialectBinaryFormat(String databaseDialectBinaryFormat) {
		this.databaseDialectBinaryFormat = databaseDialectBinaryFormat;
	}

	public String getRealdatasource() {
		return realdatasource;
	}

	public void setRealdatasource(String realdatasource) {
		this.realdatasource = realdatasource;
	}

	public String getJndicontextcustom() {
		return jndicontextcustom;
	}

	public void setJndicontextcustom(String jndicontextcustom) {
		this.jndicontextcustom = jndicontextcustom;
	}

	public String getCustomLogMessageFormat() {
		return customLogMessageFormat;
	}

	public void setCustomLogMessageFormat(String customLogMessageFormat) {
		this.customLogMessageFormat = customLogMessageFormat;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getJndicontextproviderurl() {
		return jndicontextproviderurl;
	}

	public void setJndicontextproviderurl(String jndicontextproviderurl) {
		this.jndicontextproviderurl = jndicontextproviderurl;
	}

	public String getOutagedetection() {
		return outagedetection;
	}

	public void setOutagedetection(String outagedetection) {
		this.outagedetection = outagedetection;
	}

	public String getLogfile() {
		return logfile;
	}

	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}

	public String getDatabaseDialectDateFormat() {
		return databaseDialectDateFormat;
	}

	public void setDatabaseDialectDateFormat(String databaseDialectDateFormat) {
		this.databaseDialectDateFormat = databaseDialectDateFormat;
	}

	public String getExcludebinary() {
		return excludebinary;
	}

	public void setExcludebinary(String excludebinary) {
		this.excludebinary = excludebinary;
	}

	public String getRealdatasourceclass() {
		return realdatasourceclass;
	}

	public void setRealdatasourceclass(String realdatasourceclass) {
		this.realdatasourceclass = realdatasourceclass;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;

		if (StrUtil.isBlank(this.logfile)) {
			String userHome = environment.getProperty("user.home");
			String springApplicationName = environment.getProperty("spring.application.name");

			this.logfile = userHome + "/logs/" + springApplicationName + "/p6spy/spy.log";
		}
	}
}
