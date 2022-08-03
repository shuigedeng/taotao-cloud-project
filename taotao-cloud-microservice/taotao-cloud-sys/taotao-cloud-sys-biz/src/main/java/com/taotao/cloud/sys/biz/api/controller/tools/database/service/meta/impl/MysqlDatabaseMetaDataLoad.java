package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.DatabaseMetaDataLoad;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.aspect.JdbcConnectionManagerAspect;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class MysqlDatabaseMetaDataLoad implements DatabaseMetaDataLoad {
    @Override
    public String ddl(DruidDataSource dataSource, ActualTableName actualTableName) throws IOException, SQLException {
        String ddlSql = "show create table " + actualTableName.getTableName();

        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        QueryRunner queryRunner = new QueryRunner();
        final ScalarHandler<String> scalarHandler = new ScalarHandler(1);

        return queryRunner.query(connection,ddlSql,scalarHandler);
    }
}