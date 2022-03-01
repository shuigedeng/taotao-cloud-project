package com.taotao.cloud.sys.biz.tools.database.service.meta;

import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Column;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Index;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Schema;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Table;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMeta;
import com.taotao.cloud.sys.biz.tools.database.service.meta.processor.ColumnListProcessor;
import com.taotao.cloud.sys.biz.tools.database.service.meta.processor.IndexListProcessor;
import com.taotao.cloud.sys.biz.tools.database.service.meta.processor.PrimaryKeyListProcessor;
import com.taotao.cloud.sys.biz.tools.database.service.meta.processor.SchemaListProcessor;
import com.taotao.cloud.sys.biz.tools.database.service.meta.processor.TableListProcessor;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface DatabaseMetaDataLoad {

    /**
     * 获取 catalog 列表
     * @param dataSource
     * @return
     */
	default List<String> catalogs(ConnectionPoolDataSource dataSource) throws SQLException {
        final PooledConnection pooledConnection = dataSource.getPooledConnection();
        final Connection connection = pooledConnection.getConnection();
        final DatabaseMetaData metaData = connection.getMetaData();
        final ResultSet resultSet = metaData.getCatalogs();
        ResultSetHandler<List<String>> resultSetHandler = new ColumnListHandler<String>();
        return resultSetHandler.handle(resultSet);
	}

    /**
     * 获取模式列表
     * @param dataSource
     * @return
     */
    default List<Schema> schemas(ConnectionPoolDataSource dataSource) throws SQLException {
        final PooledConnection pooledConnection = dataSource.getPooledConnection();
        final Connection connection = pooledConnection.getConnection();
        final ResultSet resultSet = connection.getMetaData().getSchemas();
        return schemaListProcessor.handle(resultSet);
    }

    /**
     * 获取所有数据表
     * @param dataSource
     * @return
     */
    default List<TableMeta> tables(ConnectionPoolDataSource dataSource, String catalog, String schema) throws SQLException {
        List<TableMeta> tableMetas = new ArrayList<>();

        final Connection connection = dataSource.getPooledConnection().getConnection();
        final ResultSet resultSet = connection.getMetaData().getTables(catalog, schema, "%", new String[]{"table"});
        final List<Table> tables = tableListProcessor.handle(resultSet);
        final ResultSet columnResultSet = connection.getMetaData().getColumns(catalog, schema, "%", "%");
        final List<Column> columns = columnListProcessor.handle(columnResultSet);
        Map<ActualTableName, List<Column>> columnMap = columns.stream().collect(Collectors.groupingBy(Column::getActualTableName));
        for (Table table : tables) {
            final ActualTableName actualTableName = table.getActualTableName();
            final List<Column> tableColumns = columnMap.get(actualTableName);
            tableMetas.add(new TableMeta(table,tableColumns));
        }
        return tableMetas;
    }

    /**
     * 获取列数据
     * @param dataSource
     * @param actualTableName
     * @return
     * @throws SQLException
     */
    default List<Column> columns(ConnectionPoolDataSource dataSource, ActualTableName actualTableName) throws SQLException {
        final Connection connection = dataSource.getPooledConnection().getConnection();
        final ResultSet resultSet = connection.getMetaData().getColumns(actualTableName.getCatalog(), actualTableName.getSchema(), actualTableName.getTableName(), "%");
        return columnListProcessor.handle(resultSet);
    }

    /**
     * 获取所有视图
     * @param dataSource
     * @param catalog
     * @param schema
     */
    void views(ConnectionPoolDataSource dataSource,String catalog,String schema);

    /**
     * 获取所有索引数据
     * @param dataSource
     * @return
     */
    default List<Index> indices(ConnectionPoolDataSource dataSource, ActualTableName actualTableName) throws SQLException {
        final Connection connection = dataSource.getPooledConnection().getConnection();
        final ResultSet resultSet = connection.getMetaData().getIndexInfo(actualTableName.getCatalog(), actualTableName.getSchema(), actualTableName.getTableName(), false, true);
        return indexListProcessor.handle(resultSet);
    }

    /**
     * 存储过程
     * @param dataSource
     * @param catalog
     * @param schema
     */
    default void procedures(ConnectionPoolDataSource dataSource, String catalog, String schema) throws SQLException {
        final Connection connection = dataSource.getPooledConnection().getConnection();
        final ResultSet resultSet = connection.getMetaData().getProcedures(catalog, schema, "%");

    }

    /**
     * 函数列表
     * @param dataSource
     * @param catalog
     * @param schema
     */
    default void functions(ConnectionPoolDataSource dataSource, String catalog, String schema) throws SQLException {
        final Connection connection = dataSource.getPooledConnection().getConnection();
        final ResultSet functions = connection.getMetaData().getFunctions(catalog, schema, "%");
        final ResultSet functionColumns = connection.getMetaData().getFunctionColumns(catalog, schema, "%", "%");

    }

    /**
     * jdbc 标准数据处理器
     */
    public static final SchemaListProcessor schemaListProcessor = new SchemaListProcessor();
    public static final TableListProcessor tableListProcessor = new TableListProcessor();
    public static final ColumnListProcessor columnListProcessor = new ColumnListProcessor();
    public static final IndexListProcessor indexListProcessor = new IndexListProcessor();
    public static final PrimaryKeyListProcessor primaryKeyListProcessor = new PrimaryKeyListProcessor();
}
