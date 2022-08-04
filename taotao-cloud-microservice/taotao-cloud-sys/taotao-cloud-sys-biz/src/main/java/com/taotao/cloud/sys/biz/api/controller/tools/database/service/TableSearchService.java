package com.taotao.cloud.sys.biz.api.controller.tools.database.service;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableMeta;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Column;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Namespace;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Table;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.alibaba.druid.util.JdbcConstants;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.search.SearchParam;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.connect.ConnDatasourceAdapter;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.TableMarkMetaData;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.aspect.JdbcConnection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TableSearchService {
    @Autowired
    private JdbcMetaService jdbcMetaService;
    @Autowired
    private ConnDatasourceAdapter connDatasourceAdapter;
    @Autowired
    private TableMarkMetaData tableMarkMetaData;

    /**
     * 精确查询某一张表
     * @param connName
     * @param actualTableName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public Optional<TableMeta> getTable(String connName, ActualTableName actualTableName) throws IOException, SQLException {
        final List<TableMeta> tables = jdbcMetaService.tables(connName, actualTableName.getNamespace());
        return tables.stream().filter(tableMeta -> tableMeta.getTable().getActualTableName().equals(actualTableName)).findFirst();
    }

    /**
     * 精确查询表列表
     * @param connName
     * @param namespace
     * @param tableNames
     * @return
     */
    @JdbcConnection
    public List<TableMeta> getTables(String connName, Namespace namespace, List<String> tableNames) throws IOException, SQLException {
        final List<TableMeta> tableMetas = jdbcMetaService.tables(connName, namespace);
        List<TableMeta> findTables = new ArrayList<>();

        for (TableMeta tableMeta : tableMetas) {
            final String tableName = tableMeta.getTable().getActualTableName().getTableName();
            if (tableNames.contains(tableName)){
                findTables.add(tableMeta);
            }
        }
        return findTables;
    }

    /**
     * 表搜索
     * @param connName
     * @param searchParam
     */
    @JdbcConnection
    public List<TableMeta> searchTables(String connName, SearchParam searchParam) throws IOException, SQLException {
        final String catalog = searchParam.getCatalog();
        final List<String> schemas = searchParam.getSchemas();

        // 先查询到所有的数据表信息
        List<TableMeta> firstFilterTables = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(schemas)) {
            for (String schema : schemas) {
                final List<TableMeta> tables = jdbcMetaService.tables(connName, new Namespace(catalog, schema));
                firstFilterTables.addAll(tables);
            }
        }else {
            firstFilterTables = jdbcMetaService.tables(connName,new Namespace(catalog,null));
        }

        // 使用关键字过滤数据表
        String keyword = searchParam.getKeyword();
        // 空搜索返回所有表
        if(StringUtils.isBlank(keyword)){
            return firstFilterTables;
        }

        // oracle 的特殊处理关键字大写
        final String dbType = connDatasourceAdapter.dbType(connName);
        if (JdbcConstants.ORACLE.equalsIgnoreCase(dbType)){
            keyword = keyword.toUpperCase();
            searchParam.setKeyword(keyword);
        }

        // 根据 , 拆分搜索块
        final String[] keywordParts = StringUtils.split(keyword, ",");

        // 搜索前缀
        final String searchSchema = searchParam.getSearchSchema();

        // 如果是标签搜索, 则使用标签管理来搜索表
        if ("tag".equalsIgnoreCase(searchSchema)){
            return tableMarkMetaData.searchTables(firstFilterTables,connName,searchParam);
        }

        List<TableMeta> findTables = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(firstFilterTables)){
            A: for (TableMeta tableMetaData : firstFilterTables) {
                ActualTableName actualTableName = tableMetaData.getTable().getActualTableName();
                String tableName = actualTableName.getTableName();
                Table table = tableMetaData.getTable();
                String tableComments = table.getRemark();
                if(StringUtils.isBlank(searchSchema) || "table".equalsIgnoreCase(searchSchema)) {
                    for (String keywordPart : keywordParts) {
                        if (tableName.contains(keywordPart) || (StringUtils.isNotBlank(tableComments) && tableComments.contains(keywordPart))) {
                            findTables.add(tableMetaData);
                            continue A;
                        }
                    }

                }

                //再看是否有列是匹配的
                List<Column> columns = tableMetaData.getColumns();
                if(CollectionUtils.isNotEmpty(columns)){
                    B: for (Column column : columns) {
                        String columnName = column.getColumnName();
                        String columnComments = column.getRemark();

                        if(StringUtils.isBlank(searchSchema) || "column".equalsIgnoreCase(searchSchema)) {
                            for (String keywordPart : keywordParts) {
                                if (columnName.contains(keywordPart) || (StringUtils.isNotBlank(columnComments) && columnComments.contains(keywordPart))) {
                                    findTables.add(tableMetaData);
                                    break B;
                                }
                            }

                        }
                    }
                }
            }
        }

        return findTables;
    }
}
