package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.connect.ConnDatasourceAdapter;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.aspect.JdbcConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class JdbcMetaRefreshService {
    @Autowired
    private ConnDatasourceAdapter connDatasourceAdapter;
    @Autowired
    private MetaDataLoadManager metaDataLoadManager;

    /**
     * 单表刷新
     * @param connName
     * @param namespace
     * @param actualTableName
     * @return
     */
    @JdbcConnection
    public TableMeta refreshTable(String connName,ActualTableName actualTableName) throws IOException, SQLException {
        // 设置不取缓存值
        MetaDataLoadManager.metaCache.set(false);

        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);

        final Table table = databaseMetaDataLoad.table(druidDataSource, actualTableName);
        final List<Column> columns = databaseMetaDataLoad.columns(druidDataSource, actualTableName);
        return new TableMeta(table,columns);
    }

    /**
     * 单表所有元数据刷新
     * @param connName
     * @param namespace
     * @param actualTableName
     * @return
     */
    @JdbcConnection
    public TableMetaData refreshTableAll(String connName, ActualTableName actualTableName) throws IOException, SQLException {
        MetaDataLoadManager.metaCache.set(false);

        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);

        final Table table = databaseMetaDataLoad.table(druidDataSource, actualTableName);
        final List<Column> columns = databaseMetaDataLoad.columns(druidDataSource, actualTableName);
        final List<Index> indices = databaseMetaDataLoad.indices(druidDataSource, actualTableName);
        final List<PrimaryKey> primaryKeys = databaseMetaDataLoad.primaryKeys(druidDataSource, actualTableName);
        return new TableMetaData(table.getActualTableName(),table,columns,indices,primaryKeys);
    }

    /**
     * 名称空间数据表元数据刷新
     * @param connName
     * @param namespace
     * @return
     */
    @JdbcConnection
    public List<TableMeta> refreshNamespaceTables(String connName, Namespace namespace) throws IOException, SQLException {
        MetaDataLoadManager.metaCache.set(false);

        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);

        return databaseMetaDataLoad.tables(druidDataSource,namespace);
    }
}
