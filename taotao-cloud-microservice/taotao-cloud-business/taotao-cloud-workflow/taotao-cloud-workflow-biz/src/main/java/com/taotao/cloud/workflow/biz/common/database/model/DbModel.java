package com.taotao.cloud.workflow.biz.common.database.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import lombok.Data;

/**
 * 类功能
 *
 */
@Data
public class DbModel {

    public DbModel(Connection connection){
        try {
            //从conn中获取数据库的元数据
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            /*============库信息===========*/
            //连接的库名目录
            this.catalog = connection.getCatalog();
            //数据库类型 MYSQL ORACLE
            this.jdbcDbType = databaseMetaData.getDatabaseProductName();
            //数据库版本号 8.0.15
            this.version = databaseMetaData.getDatabaseProductVersion();
            //数据库大版本 8
            this.majorVersion = databaseMetaData.getDatabaseMajorVersion();
            //jdbc连接的url
            this.url = databaseMetaData.getURL();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 数据库目录
     */
    private String catalog;

    /**
     * jdbc数据库类型
     */
    private String jdbcDbType;

    /**
     * 数据库版本号 例：8.0.15
     */
    private String version;

    /**
     * 数据库大版本 例：8
     */
    private Integer majorVersion;

    /**
     * 数据库连接
     */
    private String url;

}
