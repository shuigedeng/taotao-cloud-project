package com.taotao.cloud.workflow.api.common.base;

/**
 */
public class DataSourceInfo {

    //TODO 这个类准备删除掉

    public static final String mysqlUrl="jdbc:mysql://{host}:{port}/{dbName}?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true&serverTimezone=GMT%2B8";

    public static final String mysqlDriver="com.mysql.cj.jdbc.Driver";


    public static final String oracleUrl="jdbc:oracle:thin:@{host}:{port}:{dbName}";

    public static final String oracleDriver="oracle.jdbc.OracleDriver";


    public static final String sqlserverUrl="jdbc:sqlserver://{host}:{port};Databasename={dbName}";

    public static final String sqlserverDriver="com.microsoft.sqlserver.jdbc.SQLServerDriver";


    public static final String dmUrl="jdbc:dm://{host}:{port}/{dbName}?zeroDateTimeBehavior=convertToNull&useUnicode" +
            "=true&characterEncoding=utf-8";

    public static final String dmDriver="dm.jdbc.driver.DmDriver";


    public static final String kingBaseUrl = "jdbc:kingbase8://{host}:{port}/{dbName}";

    public static final String kingBaseDriver = "com.kingbase8.Driver";


    public static final String postgreUrl = "jdbc:postgresql://{host}:{port}/{dbName}" ;

    public static final String postgreDriver = "org.postgresql.Driver";

}
