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
package com.taotao.cloud.p6spy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * P6spyProperties
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/10/14 09:11
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "taotao.cloud.p6spy")
public class P6spyProperties {

	private boolean enabled;

	/**
	 * 是否自动刷新 默认 flase # for flushing per statement # (default is false)
	 */
	private String autoflush;

	/**
	 * 真实JDBC driver , 多个以 逗号 分割 默认为空 # A comma separated list of JDBC drivers to load and register.
	 * # (default is empty) # # Note: This is normally only needed when using P6Spy in an #
	 * application server environment with a JNDI data source or when # using a JDBC driver that
	 * does not implement the JDBC 4.0 API # (specifically automatic registration).
	 */
	private String driverlist;

	/**
	 * 指定 Log 的文件名 默认 spy.log # name of logfile to use, note Windows users should make sure to use
	 * forward slashes in their pathname (e:/test/spy.log) # (used for
	 * com.p6spy.engine.spy.appender.FileLogger only) # (default is spy.log)
	 */
	private String logfile;

	/**
	 * 指定日志输出样式  默认为com.p6spy.engine.spy.appender.SingleLineFormat , 单行输出 不格式化语句 # class to use for
	 * formatting log messages (default is: com.p6spy.engine.spy.appender.SingleLineFormat)
	 */
	private String logMessageFormat;

	/**
	 * # 也可以采用  com.p6spy.engine.spy.appender.CustomLineFormat 来自定义输出样式,
	 * 默认值是%(currentTime)|%(executionTime)|%(category)|connection%(connectionId)|%(sqlSingleLine) #
	 * Custom log message format used ONLY IF logMessageFormat is set to
	 * com.p6spy.engine.spy.appender.CustomLineFormat # default is %(currentTime)|%(executionTime)|%(category)|connection%(connectionId)|%(sqlSingleLine)
	 * # Available placeholders are: # 可用的变量为: #   %(connectionId)            connection id #
	 * %(currentTime)             当前时间 #   %(executionTime)           执行耗时 #   %(category)
	 *      执行分组 #   %(effectiveSql)            提交的SQL 换行 #   %(effectiveSqlSingleLine)  提交的SQL
	 * 不换行显示 #   %(sql)                     执行的真实SQL语句，已替换占位 #customLogMessageFormat=%(currentTime)|%(executionTime)|%(category)|connection%(connectionId)|%(sqlSingleLine)
	 */
	private String customLogMessageFormat;

	/**
	 * 指定是否每次是增加 Log，设置为 false 则每次都会先进行清空 默认true # append to the p6spy log file. if this is set to
	 * false the # log file is truncated every time. (file logger only) # (default is true)
	 */
	private String append;

	/**
	 * 配置SimpleDateFormat日期格式 默认为空 # sets the date format using Java's SimpleDateFormat routine. #
	 * In case property is not set, milliseconds since 1.1.1970 (unix time) is used (default
	 */
	private String dateformat;

	/**
	 * 指定 Log 的 appender，取值：
	 * <p>
	 * # specifies the appender to use for logging # Please note: reload means forgetting all the
	 * previously set # settings (even those set during runtime - via JMX) # and starting with the
	 * clean table # (only the properties read from the configuration file) # (default is
	 * com.p6spy.engine.spy.appender.FileLogger) #appender=com.p6spy.engine.spy.appender.Slf4JLogger
	 * #appender=com.p6spy.engine.spy.appender.StdoutLogger #appender=com.p6spy.engine.spy.appender.FileLogger
	 */
	private String appender;

	/**
	 * 指定应用的日志拦截模块,默认为com.p6spy.engine.spy.P6SpyFactory # Module list adapts the modular
	 * functionality of P6Spy. # Only modules listed are active. # (default is
	 * com.p6spy.engine.logging.P6LogFactory and # com.p6spy.engine.spy.P6SpyFactory) # Please note
	 * that the core module (P6SpyFactory) can't be # deactivated. # Unlike the other properties,
	 * activation of the changes on # this one requires reload.
	 */
	private String modulelist;

	/**
	 * 打印堆栈跟踪信息 默认flase
	 * <p>
	 * # prints a stack trace for every statement logged
	 */
	private String stacktrace;

	/**
	 * 如果 stacktrace=true，则可以指定具体的类名来进行过滤。
	 * <p>
	 * # if stacktrace=true, specifies the stack trace to print
	 */
	private String stacktraceclass;

	/**
	 * 监测属性配置文件是否进行重新加载
	 * <p>
	 * # determines if property file should be reloaded # Please note: reload means forgetting all
	 * the previously set # settings (even those set during runtime - via JMX) # and starting with
	 * the clean table # (default is false) #reloadproperties=false
	 */
	private String reloadproperties;

	/**
	 * 属性配置文件重新加载的时间间隔，单位:秒 默认60s
	 * <p>
	 * # determines how often should be reloaded in seconds # (default is 60)
	 * #reloadpropertiesinterval=60
	 */
	private String reloadpropertiesinterval;

	/**
	 * 设置 JNDI 数据源的 NamingContextFactory。
	 * <p>
	 * # JNDI DataSource lookup                                        # #
	 *                                     # # If you are using the DataSource support outside of an
	 * app     # # server, you will probably need to define the JNDI Context     # # environment.
	 *                                               # #
	 *                   # # If the P6Spy code will be executing inside an app server then # # do
	 * not use these properties, and the DataSource lookup will   # # use the naming context defined
	 * by the app server.             # #
	 *    # # The two standard elements of the naming environment are       # # jndicontextfactory
	 * and jndicontextproviderurl. If you need    # # additional elements, use the jndicontextcustom
	 * property.      # # You can define multiple properties in jndicontextcustom,      # # in name
	 * value pairs. Separate the name and value with a       # # semicolon, and separate the pairs
	 * with commas.                # #
	 * # # The example shown here is for a standalone program running on # # a machine that is also
	 * running JBoss, so the JNDI context     # # is configured for JBoss (3.0.4).
	 *            # #                                                               # # (by default
	 * all these are empty)                              # #################################################################
	 * #jndicontextfactory=org.jnp.interfaces.NamingContextFactory #jndicontextproviderurl=localhost:1099
	 * #jndicontextcustom=java.naming.factory.url.pkgs;org.jboss.naming:org.jnp.interfaces
	 */
	private String jndicontextfactory;

	/**
	 * 设置 JNDI 数据源的提供者的 URL。
	 * <p>
	 * # JNDI DataSource lookup                                        # #
	 *                                     # # If you are using the DataSource support outside of an
	 * app     # # server, you will probably need to define the JNDI Context     # # environment.
	 *                                               # #
	 *                   # # If the P6Spy code will be executing inside an app server then # # do
	 * not use these properties, and the DataSource lookup will   # # use the naming context defined
	 * by the app server.             # #
	 *    # # The two standard elements of the naming environment are       # # jndicontextfactory
	 * and jndicontextproviderurl. If you need    # # additional elements, use the jndicontextcustom
	 * property.      # # You can define multiple properties in jndicontextcustom,      # # in name
	 * value pairs. Separate the name and value with a       # # semicolon, and separate the pairs
	 * with commas.                # #
	 * # # The example shown here is for a standalone program running on # # a machine that is also
	 * running JBoss, so the JNDI context     # # is configured for JBoss (3.0.4).
	 *            # #                                                               # # (by default
	 * all these are empty)                              # #################################################################
	 * #jndicontextfactory=org.jnp.interfaces.NamingContextFactory #jndicontextproviderurl=localhost:1099
	 * #jndicontextcustom=java.naming.factory.url.pkgs;org.jboss.naming:org.jnp.interfaces
	 */
	private String jndicontextproviderurl;

	/**
	 * 设置 JNDI 数据源的一些定制信息，以分号分隔。
	 * <p>
	 * # JNDI DataSource lookup                                        # #
	 *                                     # # If you are using the DataSource support outside of an
	 * app     # # server, you will probably need to define the JNDI Context     # # environment.
	 *                                               # #
	 *                   # # If the P6Spy code will be executing inside an app server then # # do
	 * not use these properties, and the DataSource lookup will   # # use the naming context defined
	 * by the app server.             # #
	 *    # # The two standard elements of the naming environment are       # # jndicontextfactory
	 * and jndicontextproviderurl. If you need    # # additional elements, use the jndicontextcustom
	 * property.      # # You can define multiple properties in jndicontextcustom,      # # in name
	 * value pairs. Separate the name and value with a       # # semicolon, and separate the pairs
	 * with commas.                # #
	 * # # The example shown here is for a standalone program running on # # a machine that is also
	 * running JBoss, so the JNDI context     # # is configured for JBoss (3.0.4).
	 *            # #                                                               # # (by default
	 * all these are empty)                              # #################################################################
	 * #jndicontextfactory=org.jnp.interfaces.NamingContextFactory #jndicontextproviderurl=localhost:1099
	 * #jndicontextcustom=java.naming.factory.url.pkgs;org.jboss.naming:org.jnp.interfaces
	 */
	private String jndicontextcustom;

	/**
	 * 实际数据源 JNDI
	 * <p>
	 * # DataSource replacement                                        # #
	 *                                     # # Replace the real DataSource class in your application
	 * server  # # configuration with the name com.p6spy.engine.spy.P6DataSource # # (that provides
	 * also connection pooling and xa support).       # # then add the JNDI name and class name of
	 * the real             # # DataSource here                                               # #
	 *                                                            # # Values set in this item cannot
	 * be reloaded using the          # # reloadproperties variable. Once it is loaded, it remains
	 *    # # in memory until the application is restarted.                 # #
	 *                                          # #################################################################
	 * #realdatasource=/RealMySqlDS
	 */
	private String realdatasource;

	/**
	 * 实际数据源 datasource class
	 * <p>
	 * #realdatasourceclass=com.mysql.jdbc.jdbc2.optional.MysqlDataSource
	 */
	private String realdatasourceclass;

	/**
	 * 实际数据源所携带的配置参数 以 k=v 方式指定 以 分号 分割
	 * <p>
	 * # DataSource properties                                         # #
	 *                                     # # If you are using the DataSource support to intercept
	 * calls    # # to a DataSource that requires properties for proper setup,    # # define those
	 * properties here. Use name value pairs, separate  # # the name and value with a semicolon, and
	 * separate the         # # pairs with commas.                                            # #
	 *                                                            # # The example shown here is for
	 * mysql                           # #
	 *     # #################################################################
	 * #realdatasourceproperties=port;3306,serverName;myhost,databaseName;jbossdb,foo;bar
	 */
	private String realdatasourceproperties;

	/**
	 * # format that is used for logging of the java.util.Date implementations (has to be compatible
	 * with java.text.SimpleDateFormat) # (default is yyyy-MM-dd'T'HH:mm:ss.SSSZ)
	 * #databaseDialectDateFormat=yyyy-MM-dd'T'HH:mm:ss.SSSZ
	 */
	private String databaseDialectDateFormat;
	/**
	 * # format that is used for logging of the java.sql.Timestamp implementations (has to be
	 * compatible with java.text.SimpleDateFormat) # (default is yyyy-MM-dd'T'HH:mm:ss.SSSZ)
	 * #databaseDialectTimestampFormat=yyyy-MM-dd'T'HH:mm:ss.SSSZ
	 */
	private String databaseDialectTimestampFormat;
	/**
	 * # format that is used for logging booleans, possible values: boolean, numeric # (default is
	 * boolean) #databaseDialectBooleanFormat=boolean
	 */
	private String databaseDialectBooleanFormat;
	/**
	 * # whether to expose options via JMX or not # (default is true) #jmx=true
	 */
	private String jmx;
	/**
	 * # if exposing options via jmx (see option: jmx), what should be the prefix used? # jmx naming
	 * pattern constructed is: com.p6spy(.<jmxPrefix>)?:name=<optionsClassName> # please note, if
	 * there is already such a name in use it would be unregistered first (the last registered wins)
	 * # (default is none) #jmxPrefix=
	 */
	private String jmxPrefix;
}
