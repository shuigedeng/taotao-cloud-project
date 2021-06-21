package com.taotao.cloud.common.base;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: chejiangyi
 * @version: 2019-08-12 11:27
 **/
@ConfigurationProperties
@Data
public class CoreProperties {
//    /**
//     * 枚举:dev,test,prd
//     */
//    @Value("${bsf.env:dev}")
//    private Environment env;


    public static String Project="Core";
    public static String SpringApplicationName= "spring.application.name";
    public static String BsfEnv="bsf.env";
    public static String SpringJacksonDateFormat="spring.jackson.date-format";
    public static String SpringJacksonTimeZone="spring.jackson.time-zone";
    public static String ServerTomcatMaxThreads="server.tomcat.max-threads";
    public static String ServerTomcatMaxConnections="server.tomcat.max-connections";
    public static String ServerTomcatMinSpaceThreads="server.tomcat.min-spare-threads";
    public static String ServeCompressionEnabled="server.compression.enabled";
    public static String ServeCompressionMimeTypes="server.compression.mime-types";
    public static String LoggingFile="logging.file";
    public static String LoggingFileMaxHistory="logging.file.max-history";
    public static String LoggingFileMaxSize="logging.file.max-size";
    public static String BsfLoggingFileTotalSize="bsf.logging.file.total-size";
    public static String BsfContextRestartText="bsf.context.restart.text";
    public static String BsfContextRestartEnabled="bsf.context.restart.enabled";
    public static String BsfContextRestartTimeSpan="bsf.context.restart.timespan";
    public static String BsfEnabled = "bsf.enabled";
    public static String BsfCollectHookEnabled="bsf.collect.hook.enabled";
    public static String BsfIsPrintSqlTimeWatch="bsf.db.printSql.enabled";
    public static String BsfIsPrintSqlError="bsf.db.printSqlError.enabled";
}
