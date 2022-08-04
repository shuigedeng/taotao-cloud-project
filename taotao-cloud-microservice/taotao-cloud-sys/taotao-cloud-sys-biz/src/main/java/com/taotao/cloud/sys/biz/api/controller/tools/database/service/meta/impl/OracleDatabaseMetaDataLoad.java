package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.sys.biz.api.controller.tools.core.exception.ToolException;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableMeta;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.DatabaseMetaDataLoad;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.aspect.JdbcConnectionManagerAspect;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Column;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Namespace;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Table;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OracleDatabaseMetaDataLoad implements DatabaseMetaDataLoad {
    @Override
    public List<TableMeta> tables(DruidDataSource dataSource, Namespace namespace) throws SQLException, IOException {
        List<TableMeta> tableMetas = new ArrayList<>();
        final String catalog = namespace.getCatalog();
        final String schema = namespace.getSchema();

//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);

        final ResultSet resultSet = connection.getMetaData().getTables(catalog, schema, "%", null);
        final List<Table> tables = tableListProcessor.handle(resultSet);
        final ResultSet columnResultSet = connection.getMetaData().getColumns(catalog, schema, "%", null);
        final List<Column> columns = columnListProcessor.handle(columnResultSet);
        Map<ActualTableName, List<Column>> columnMap = columns.stream().collect(Collectors.groupingBy(Column::getActualTableName));
        for (Table table : tables) {
            final ActualTableName actualTableName = table.getActualTableName();
            final List<Column> tableColumns = columnMap.get(actualTableName);
            tableMetas.add(new TableMeta(table,tableColumns));
        }
        return tableMetas;
    }

    @Override
    public Table table(DruidDataSource dataSource, ActualTableName actualTableName) throws SQLException, IOException {
//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        final ResultSet resultSet = connection.getMetaData().getTables(actualTableName.getCatalog(), actualTableName.getSchema(), actualTableName.getTableName(), null);
        final List<Table> tables = tableListProcessor.handle(resultSet);
        if (CollectionUtils.isEmpty(tables)){
            throw new ToolException("数据表不存在:"+actualTableName);
        }
        return tables.get(0);
    }

    @Override
    public List<Column> columns(DruidDataSource dataSource, ActualTableName actualTableName) throws SQLException, IOException {
//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        final Namespace namespace = actualTableName.getNamespace();
        final ResultSet resultSet = connection.getMetaData().getColumns(namespace.getCatalog(), namespace.getSchema(), actualTableName.getTableName(), null);
        return columnListProcessor.handle(resultSet);
    }
}
