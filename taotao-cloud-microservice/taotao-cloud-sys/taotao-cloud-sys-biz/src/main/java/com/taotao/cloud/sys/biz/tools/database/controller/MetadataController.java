package com.taotao.cloud.sys.biz.tools.database.controller;

import com.taotao.cloud.sys.biz.tools.core.service.connect.ConnectService;
import com.taotao.cloud.sys.biz.tools.core.service.connect.dtos.ConnectInput;
import com.taotao.cloud.sys.biz.tools.core.service.connect.dtos.ConnectOutput;
import com.taotao.cloud.sys.biz.tools.database.controller.dtos.TableModify;
import com.taotao.cloud.sys.biz.tools.database.dtos.ExtendTableMetaData;
import com.taotao.cloud.sys.biz.tools.database.service.JdbcService;
import com.taotao.cloud.sys.biz.tools.database.service.MetaCompareService;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Catalog;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import com.taotao.cloud.sys.biz.tools.database.service.search.TableSearchServiceCodeImpl;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/db/metadata")
@Validated
public class MetadataController {
    @Autowired
    private JdbcService jdbcService;
    @Autowired
    private ConnectService connectService;
    @Autowired
    private TableSearchServiceCodeImpl tableSearchService;
    @Autowired
    private MetaCompareService metaCompareService;
    /**
     *
     * 查询所有的连接
     */
    @GetMapping("/connections")
    public List<String> connections(){
        final List<ConnectOutput> connectOutputs = connectService.moduleConnects(JdbcService.MODULE);
        return connectOutputs.stream().map(ConnectOutput::getConnectInput).map(ConnectInput::getBaseName).collect(Collectors.toList());
    }

    /**
     * 刷新连接获取所有的 catalogs 和 schema
     * @param connName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/catalogs")
    public List<Catalog> catalogs(@NotNull String connName) throws IOException, SQLException {
        return jdbcService.refreshConnection(connName);
    }

    /**
     * 首次查询连接所有的表,如果已经存在将会在缓存中获取
     * @param catalog  数据库 catalog
     * @param connName 连接名称
     */
    @GetMapping("/tables")
    public Collection<TableMetaData> tables(@NotNull String connName, String catalog, String schema) throws SQLException, IOException {
        Collection<TableMetaData> tables = jdbcService.tables(connName, catalog, schema);
        return tables;
    }

    /**
     * 刷新 catalog 或者刷新 schema
     * @param connName 连接名称
     * @param catalog 数据库 catalog
     * @param schema 数据库 scheam
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/refreshCatalogOrSchema")
    public Collection<TableMetaData> refreshCatalogOrSchema(@NotNull String connName,String catalog,String schema) throws IOException, SQLException {
        return jdbcService.refreshCatalogOrSchema(connName,catalog,schema);
    }

    /**
     * 刷新 table 元数据
     * @param connName 连接名
     * @param catalog 数据库 catalog
     * @param schema 数据库 schema
     * @param tableName 表名
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/refreshTable")
    public TableMetaData refreshTable(@NotNull String connName, String catalog, String schema, @NotNull String tableName) throws IOException, SQLException {
        ActualTableName actualTableName = new ActualTableName(catalog,schema,tableName);
        return jdbcService.refreshTable(connName,actualTableName);
    }

    /**
     * 搜索表 , keyword 可以写成表达式的形式, 目前支持
     * table: column: tag:
     * 后面可以继续扩展操作符 , 像 everything 一样
     * @param connName 连接名称
     * @param catalog 数据库 catalog
     * @param schemas 数据库 schema 列表
     * @param keyword 关键字
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/searchTables")
    public List<ExtendTableMetaData> searchTables(@NotNull String connName, String catalog, String[] schemas, String keyword) throws Exception {
        return tableSearchService.searchTablesEx(connName, catalog, schemas, keyword);
    }

    /**
     * 元数据对比, 变更 sql 记录
     * @param baseConnName
     * @param compareConnName
     * @param baseCatalog
     * @param compareCatalog
     * @return
     */
    @GetMapping("/compare/changeSqls")
    public List<String> metaCompareChangeSqls(@NotNull String baseConnName, @NotNull String compareConnName, @NotNull String baseCatalog, @NotNull String compareCatalog) throws SQLException, IOException, TemplateException {
        return metaCompareService.changeSqls(baseConnName, compareConnName, baseCatalog, compareCatalog);
    }

    /**
     * 元数据对比
     * @return
     */
    @GetMapping("/compare")
    public List<TableModify> metaCompare(@NotNull String baseConnName, @NotNull String compareConnName, @NotNull String baseCatalog, @NotNull String compareCatalog) throws IOException, SQLException {
        List<TableModify> tableModifies = new ArrayList<>();

        final MetaCompareService.ModifyInfo modifyInfo = metaCompareService.compare(baseConnName, compareConnName, baseCatalog, compareCatalog);

        // 增删数据表
        final List<MetaCompareService.ModifyTable> modifyTables = modifyInfo.getModifyTables();
        for (MetaCompareService.ModifyTable modifyTable : modifyTables) {
            tableModifies.add(new TableModify(modifyTable.getTableName(),modifyTable.getDiffType(),modifyTable.getTableMetaData()));
        }

        // 表变更 tableName => TableModify
        Map<String,TableModify> tableModifyMap = new HashMap<>();

        // 表变更列信息 tableName => List<ModifyColumn>
        MultiValueMap<String, MetaCompareService.ModifyColumn> modifyColumnMultiValueMap = new LinkedMultiValueMap<>();
        for (MetaCompareService.ModifyColumn modifyColumn : modifyInfo.getModifyColumns()) {
            modifyColumnMultiValueMap.add(modifyColumn.getTableName(),modifyColumn);
        }
        final Iterator<Map.Entry<String, List<MetaCompareService.ModifyColumn>>> iterator = modifyColumnMultiValueMap.entrySet().iterator();
        while (iterator.hasNext()){
            final Map.Entry<String, List<MetaCompareService.ModifyColumn>> next = iterator.next();
            final String tableName = next.getKey();
            final List<MetaCompareService.ModifyColumn> modifyColumns = next.getValue();
            final TableModify tableModify = new TableModify(tableName, MetaCompareService.DiffType.MODIFY);
            tableModify.setModifyColumns(modifyColumns);
            tableModifyMap.put(tableName,tableModify);

        }

        // 表变更索引信息 tableName => List<ModifyIndex>
        MultiValueMap<String, MetaCompareService.ModifyIndex> modifyIndexLinkedMultiValueMap = new LinkedMultiValueMap<>();
        for (MetaCompareService.ModifyIndex modifyIndex : modifyInfo.getModifyIndices()) {
            modifyIndexLinkedMultiValueMap.add(modifyIndex.getTableName(),modifyIndex);
        }
        final Iterator<Map.Entry<String, List<MetaCompareService.ModifyIndex>>> entryIterator = modifyIndexLinkedMultiValueMap.entrySet().iterator();
        while (entryIterator.hasNext()){
            final Map.Entry<String, List<MetaCompareService.ModifyIndex>> next = entryIterator.next();
            final String tableName = next.getKey();
            final List<MetaCompareService.ModifyIndex> modifyIndices = next.getValue();
            if (tableModifyMap.containsKey(tableName)){
                final TableModify tableModify = tableModifyMap.get(tableName);
                tableModify.setModifyIndices(modifyIndices);
            }else{
                final TableModify tableModify = new TableModify(tableName, MetaCompareService.DiffType.MODIFY);
                tableModify.setModifyIndices(modifyIndices);
                tableModifyMap.put(tableName,tableModify);
            }
        }

        tableModifies.addAll(tableModifyMap.values());

        return tableModifies;
    }
}
