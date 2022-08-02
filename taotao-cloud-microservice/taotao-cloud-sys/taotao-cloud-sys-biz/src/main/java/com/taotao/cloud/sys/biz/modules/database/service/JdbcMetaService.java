package com.taotao.cloud.sys.biz.modules.database.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.sanri.tools.modules.database.service.connect.ConnDatasourceAdapter;
import com.sanri.tools.modules.database.service.dtos.meta.TableMeta;
import com.sanri.tools.modules.database.service.dtos.meta.TableMetaData;
import com.sanri.tools.modules.database.service.meta.DatabaseMetaDataLoad;
import com.sanri.tools.modules.database.service.meta.MetaDataLoadManager;
import com.sanri.tools.modules.database.service.meta.aspect.JdbcConnection;
import com.sanri.tools.modules.database.service.meta.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class JdbcMetaService {
    @Autowired
    private ConnDatasourceAdapter connDatasourceAdapter;
    @Autowired
    private MetaDataLoadManager metaDataLoadManager;

    /**
     * 客户端属性和版本信息
     * @param connName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @JdbcConnection
    public ClientInfo clientInfo(String connName) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);
        return databaseMetaDataLoad.clientInfo(druidDataSource);
    }

    /**
     * 获取 catalogs
     * @param connName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @JdbcConnection
    public List<String> catalogs(String connName) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);
        return databaseMetaDataLoad.catalogs(druidDataSource);
    }

    /**
     * 获取 schemas
     * @param connName
     * @param catalog
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @JdbcConnection
    public List<Schema> schemas(String connName) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);
        final List<Schema> schemas = databaseMetaDataLoad.schemas(druidDataSource);
        return schemas;
    }

    /**
     * 查询根据 catalog 过滤出的 schema 列表
     * @param connName
     * @param catalog
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @JdbcConnection
    public List<Schema> filterSchemas(String connName,String catalog) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);
        final List<Schema> schemas = databaseMetaDataLoad.filterSchemas(druidDataSource, catalog);
        return schemas;
    }

    /**
     * 数据表元数据(只有表和列信息)
     * @param connName
     * @param namespace
     * @return
     */
    @JdbcConnection
    public List<TableMeta> tables(String connName, Namespace namespace) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);
        return databaseMetaDataLoad.tables(druidDataSource, namespace);
    }

    /**
     * 获取某一张表的列和表信息
     * @param connName
     * @param actualTableName
     * @return
     */
    @JdbcConnection
    public TableMeta tableInfo(String connName,ActualTableName actualTableName) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);
        final Table table = databaseMetaDataLoad.table(druidDataSource, actualTableName);
        final List<Column> columns = databaseMetaDataLoad.columns(druidDataSource, actualTableName);
        return new TableMeta(table,columns);
    }

    /**
     * 元数据信息扩充
     * @param connName
     * @param tableMetas
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @JdbcConnection
    public List<TableMetaData> tablesExtend(String connName,List<TableMeta> tableMetas) throws IOException, SQLException {
        List<TableMetaData> tableMetaData = new ArrayList<>();

        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);

        long startTime = System.currentTimeMillis();
        for (TableMeta table : tableMetas) {
            final ActualTableName actualTableName = table.getTable().getActualTableName();
            final List<Index> indices = databaseMetaDataLoad.indices(druidDataSource, actualTableName);
            final List<PrimaryKey> primaryKeys = databaseMetaDataLoad.primaryKeys(druidDataSource, actualTableName);

            tableMetaData.add(new TableMetaData(actualTableName,table.getTable(),table.getColumns(),indices,primaryKeys));
        }
        log.info("数据表元数据扩展耗时:{} ms",(System.currentTimeMillis() - startTime));

        return tableMetaData;
    }

    /**
     * 数据表详细元数据(表,列,索引,主键)
     * @param connName
     * @param namespace
     * @throws IOException
     * @throws SQLException
     */
    @JdbcConnection
    public List<TableMetaData> tableInfos(String connName, Namespace namespace) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);

        final List<TableMeta> tables = databaseMetaDataLoad.tables(druidDataSource, namespace);
        return tablesExtend(connName,tables);
    }

    /**
     * 查询表的数据列
     * @param connName
     * @param actualTableName
     */
    @JdbcConnection
    public List<Column> columns(String connName, ActualTableName actualTableName) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);

        return databaseMetaDataLoad.columns(druidDataSource,actualTableName);
    }

    /**
     * 查询表的索引
     * @param connName
     * @param actualTableName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @JdbcConnection
    public List<Index> indices(String connName, ActualTableName actualTableName) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);

        return databaseMetaDataLoad.indices(druidDataSource,actualTableName);
    }

    /**
     * 获取表的主键信息
     * @param connName
     * @param actualTableName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @JdbcConnection
    public List<PrimaryKey> primaryKeys(String connName, ActualTableName actualTableName) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        final DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadManager.databaseMetaDataLoad(connName);

        return databaseMetaDataLoad.primaryKeys(druidDataSource,actualTableName);
    }
}
