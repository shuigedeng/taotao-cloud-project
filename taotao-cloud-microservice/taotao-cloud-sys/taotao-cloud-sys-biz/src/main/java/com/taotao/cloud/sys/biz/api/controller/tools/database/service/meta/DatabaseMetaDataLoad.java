package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.aspect.JdbcConnectionManagerAspect;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.ClientInfo;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.processor.CloseableResultSetHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
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
     * 客户端属性列表
     * @param dataSource
     * @return
     * @throws IOException
     * @throws SQLException
     */
    default ClientInfo clientInfo(DruidDataSource dataSource) throws IOException, SQLException {
        ClientInfo clientInfo = new ClientInfo();

        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        final DatabaseMetaData metaData = connection.getMetaData();

        // 客户端属性列表
        final ResultSet clientInfoProperties = metaData.getClientInfoProperties();
        final List<ClientInfo.Property> properties = clientInfoPropertiesProcessor.handle(clientInfoProperties);
        clientInfo.setProperties(properties);

        // 隔离级别
        final int defaultTransactionIsolation = metaData.getDefaultTransactionIsolation();
        clientInfo.setDefaultTransactionIsolation(defaultTransactionIsolation);

        // 驱动名称, 版本
        final String driverName = metaData.getDriverName();
        final int driverMajorVersion = metaData.getDriverMajorVersion();
        final int driverMinorVersion = metaData.getDriverMinorVersion();
        final Version driverVersion = new Version(driverMajorVersion, driverMinorVersion, 0);
        clientInfo.setDriverName(driverName);
        clientInfo.setDriverVersion(driverVersion);

        // jdbc 版本
        final int jdbcMajorVersion = metaData.getJDBCMajorVersion();
        final int jdbcMinorVersion = metaData.getJDBCMinorVersion();
        final Version jdbcVersion = new Version(jdbcMajorVersion, jdbcMinorVersion, 0);
        clientInfo.setJdbcVersion(jdbcVersion);

        // 数据库版本
        final int databaseMajorVersion = metaData.getDatabaseMajorVersion();
        final int databaseMinorVersion = metaData.getDatabaseMinorVersion();
        final Version databaseVersion = new Version(databaseMajorVersion, databaseMinorVersion, 0);
        clientInfo.setDatabaseVersion(databaseVersion);

        // 数据库产品名称, 版本
        final String databaseProductName = metaData.getDatabaseProductName();
        final String databaseProductVersion = metaData.getDatabaseProductVersion();
        clientInfo.setDatabaseProductName(databaseProductName);
        clientInfo.setDatabaseProductVersion(databaseProductVersion);

        // 关系字信息
        final String sqlKeywords = metaData.getSQLKeywords();
        clientInfo.setKeywords(sqlKeywords);

        return clientInfo;
    }

    /**
     * 获取 catalog 列表
     * @param dataSource
     * @return
     */
    default List<String> catalogs(DruidDataSource dataSource) throws SQLException, IOException {
//        final PooledConnection pooledConnection = dataSource.getPooledConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);

        final DatabaseMetaData metaData = connection.getMetaData();
        final ResultSet resultSet = metaData.getCatalogs();
        ResultSetHandler<List<String>> resultSetHandler = new CloseableResultSetHandler(new ColumnListHandler<String>());
        return resultSetHandler.handle(resultSet);
    }

    /**
     * 获取模式列表
     * @param dataSource
     * @return
     */
    default List<Schema> schemas(DruidDataSource dataSource) throws SQLException, IOException {
//        final PooledConnection pooledConnection = dataSource.getPooledConnection();
//        final Connection connection = pooledConnection.getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);

        final ResultSet resultSet = connection.getMetaData().getSchemas();
        return schemaListProcessor.handle(resultSet);
    }

    /**
     * 使用 catalog 过滤 schema 列表
     * @param dataSource
     * @param catalog
     * @return
     * @throws SQLException
     */
    default List<Schema> filterSchemas(DruidDataSource dataSource,String catalog) throws SQLException, IOException {
        final List<Schema> schemas = this.schemas(dataSource);
        if (StringUtils.isBlank(catalog)){
            return schemas;
        }

        List<Schema> filterSchemas = new ArrayList<>();
        for (Schema schema : schemas) {
            if (catalog.equals(schema.getCatalog())){
                filterSchemas.add(schema);
            }
        }
        return filterSchemas;
    }

    /**
     * 获取所有数据表
     * @param dataSource
     * @return
     */
    default List<TableMeta> tables(DruidDataSource dataSource, Namespace namespace) throws SQLException, IOException {
        List<TableMeta> tableMetas = new ArrayList<>();
        final String catalog = namespace.getCatalog();
        final String schema = namespace.getSchema();

//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);

        final ResultSet resultSet = connection.getMetaData().getTables(catalog, schema, "%", new String[]{"TABLE"});
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
     * 获取单个数据表的详细信息
     * @param dataSource
     * @param actualTableName
     * @return
     * @throws SQLException
     */
    default Table table(DruidDataSource dataSource,ActualTableName actualTableName) throws SQLException, IOException {
//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        final ResultSet resultSet = connection.getMetaData().getTables(actualTableName.getCatalog(), actualTableName.getSchema(), actualTableName.getTableName(), new String[]{"table"});
        final List<Table> tables = tableListProcessor.handle(resultSet);
        if (CollectionUtils.isEmpty(tables)){
            throw new ToolException("数据表不存在:"+actualTableName);
        }
        return tables.get(0);
    }

    /**
     * 获取数据库某张表的 ddl
     * @param dataSource
     * @param actualTableName
     * @return
     */
	default String ddl(DruidDataSource dataSource, ActualTableName actualTableName) throws IOException, SQLException {
	    throw new ToolException("功能未实现 DDL 功能");
	}

    /**
     * 获取列数据
     * @param dataSource
     * @param actualTableName
     * @return
     * @throws SQLException
     */
    default List<Column> columns(DruidDataSource dataSource, ActualTableName actualTableName) throws SQLException, IOException {
//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        final Namespace namespace = actualTableName.getNamespace();
        final ResultSet resultSet = connection.getMetaData().getColumns(namespace.getCatalog(), namespace.getSchema(), actualTableName.getTableName(), "%");
        return columnListProcessor.handle(resultSet);
    }

    /**
     * 获取主键列表
     * @param dataSource
     * @param actualTableName
     * @return
     * @throws SQLException
     */
    default List<PrimaryKey> primaryKeys(DruidDataSource dataSource, ActualTableName actualTableName) throws SQLException, IOException {
//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        final ResultSet resultSet = connection.getMetaData().getPrimaryKeys(actualTableName.getCatalog(), actualTableName.getSchema(), actualTableName.getTableName());
        return primaryKeyListProcessor.handle(resultSet);
    }

    /**
     * 获取所有视图
     * @param dataSource
     * @param catalog
     * @param schema
     * @return
     */
    default List<TableMeta> views(DruidDataSource dataSource, String catalog, String schema) throws SQLException, IOException {
        List<TableMeta> tableMetas = new ArrayList<>();

//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        final ResultSet resultSet = connection.getMetaData().getTables(catalog, schema, "%", new String[]{"VIEW"});
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
     * 获取所有索引数据
     * @param dataSource
     * @return
     */
    default List<Index> indices(DruidDataSource dataSource, ActualTableName actualTableName) throws SQLException, IOException {
//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        final ResultSet resultSet = connection.getMetaData().getIndexInfo(actualTableName.getCatalog(), actualTableName.getSchema(), actualTableName.getTableName(), false, true);
        return indexListProcessor.handle(resultSet);
    }

    /**
     * 存储过程
     * @param dataSource
     * @param catalog
     * @param schema
     */
    default void procedures(DruidDataSource dataSource, String catalog, String schema) throws SQLException, IOException {
//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        final ResultSet resultSet = connection.getMetaData().getProcedures(catalog, schema, "%");
    }

    /**
     * 函数列表
     * @param dataSource
     * @param catalog
     * @param schema
     */
    default void functions(DruidDataSource dataSource, String catalog, String schema) throws SQLException, IOException {
//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        final ResultSet resultSet = connection.getMetaData().getFunctions(catalog, schema, "%");
        final List<Function> functions = functionListProcessor.handle(resultSet);

        final ResultSet functionColumnsResultSet = connection.getMetaData().getFunctionColumns(catalog, schema, "%", "%");
        final List<Function.FunctionColumn> functionColumns = functionColumnListProcessor.handle(functionColumnsResultSet);
    }

    /**
     * 数据库支持的类型列表
     * @param dataSource
     * @throws SQLException
     */
    default void typeInfo(DruidDataSource dataSource) throws SQLException, IOException {
//        final Connection connection = dataSource.getPooledConnection().getConnection();
        final Connection connection = JdbcConnectionManagerAspect.threadBoundConnection(dataSource);
        final ResultSet typeInfo = connection.getMetaData().getTypeInfo();
    }

    /**
     * jdbc 标准数据处理器
     */
    final ResultSetHandler<List<Schema>> schemaListProcessor = new CloseableResultSetHandler(new SchemaListProcessor());
    final ResultSetHandler<List<Table>> tableListProcessor = new CloseableResultSetHandler(new TableListProcessor());
    final ResultSetHandler<List<Column>> columnListProcessor = new CloseableResultSetHandler(new ColumnListProcessor());
    final ResultSetHandler<List<Index>> indexListProcessor = new CloseableResultSetHandler(new IndexListProcessor());
    final ResultSetHandler<List<PrimaryKey>> primaryKeyListProcessor = new CloseableResultSetHandler(new PrimaryKeyListProcessor());
    final ResultSetHandler<List<Function>> functionListProcessor = new CloseableResultSetHandler(new FunctionListProcessor());
    final ResultSetHandler<List<Function.FunctionColumn>> functionColumnListProcessor = new CloseableResultSetHandler(new FunctionColumnListProcessor());
    final ResultSetHandler<List<ClientInfo.Property>> clientInfoPropertiesProcessor = new CloseableResultSetHandler(new ClientInfoPropertiesProcessor());
}
