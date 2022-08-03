package com.taotao.cloud.sys.biz.api.controller.tools.database.controller;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/db/metadata")
@Validated
public class MetadataController {
    @Autowired
    private JdbcMetaService jdbcMetaService;
    @Autowired
    private ConnectService connectService;
    @Autowired
    private MetaCompareService metaCompareService;
    @Autowired
    private TableSearchService tableSearchService;
    @Autowired
    private JdbcMetaRefreshService jdbcMetaRefreshService;
    @Autowired
    private MetaConvertService metaConvertService;

    /**
     * 客户端信息
     * @param connName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/clientInfo")
    public ClientInfo clientInfo(@NotNull String connName) throws IOException, SQLException {
        return jdbcMetaService.clientInfo(connName);
    }

    /**
     * 刷新连接获取所有的 catalogs
     * @param connName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/catalogs")
    public List<String> catalogs(@NotEmpty String connName) throws IOException, SQLException {
        return jdbcMetaService.catalogs(connName);
    }

    /**
     * 获取所有 schemas
     * @param connName
     * @return
     */
    @GetMapping("/schemas")
    public List<Schema> schemas(@NotNull String connName) throws IOException, SQLException {
        return jdbcMetaService.schemas(connName);
    }

    /**
     * 查询根据 catalog 过滤的 schemas
     * @param connName
     * @param catalog
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/filterSchemas")
    public List<Schema> filterSchemas(@NotNull String connName,String catalog) throws IOException, SQLException {
        return jdbcMetaService.filterSchemas(connName,catalog);
    }

    /**
     * 查询所有数据表, 如果之前查询过将取缓存数据
     * @param namespace 名称空间
     * @param connName 连接名称
     */
    @GetMapping("/tables")
    public Collection<TableMeta> tables(@NotNull String connName, Namespace namespace) throws SQLException, IOException {
        Collection<TableMeta> tables = jdbcMetaService.tables(connName, namespace);
        return tables;
    }

    /**
     * 数据表列表刷新
     * @param connName 连接名称
     * @param namespace 名称空间
     */
    @GetMapping("/refreshTables")
    public Collection<TableMeta> refreshTables(@NotNull String connName, Namespace namespace) throws SQLException, IOException {
        Collection<TableMeta> tables = jdbcMetaService.tables(connName, namespace);
        return tables;
    }

    /**
     * 获取单张表的元数据信息
     * @param connName
     * @param actualTableName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/table")
    public TableMeta table(@NotNull String connName,ActualTableName actualTableName) throws IOException, SQLException {
        return jdbcMetaService.tableInfo(connName,actualTableName);
    }

    /**
     * 获取数据表索引信息
     * @param connName
     * @param actualTableName
     * @return
     */
    @GetMapping("/indices")
    public List<Index> indices(@NotNull String connName,ActualTableName actualTableName) throws IOException, SQLException {
        return jdbcMetaService.indices(connName,actualTableName);
    }

    /**
     * 获取数据表主键信息
     * @param connName
     * @param actualTableName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/primaryKeys")
    public List<PrimaryKey> primaryKeys(@NotNull String connName,ActualTableName actualTableName) throws IOException, SQLException {
        return jdbcMetaService.primaryKeys(connName, actualTableName);
    }

    /**
     * 刷新数据表
     * @param connName
     * @param actualTableName
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/refreshTable")
    public TableMeta refreshTable(@NotNull String connName,ActualTableName actualTableName) throws IOException, SQLException {
        return jdbcMetaRefreshService.refreshTable(connName,actualTableName);
    }

    /**
     * 搜索表 , keyword 可以写成表达式的形式, 目前支持
     * table: column: tag:
     * 后面可以继续扩展操作符 , 像 everything 一样
     * @param connName 连接名称
     * @param searchParam 搜索参数
     * @return
     * @throws Exception
     */
    @GetMapping("/searchTables")
    public List<TableMeta> searchTables(@NotNull String connName, SearchParam searchParam) throws Exception {
        return tableSearchService.searchTables(connName, searchParam);
    }

    /**
     *
     * @param dbType 需要输出的 ddl 类型
     * @param dataSourceConfig 数据源配置
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws TemplateException
     */
    @PostMapping("/output/{dbType}/ddl")
    public List<String> metaDDL(@PathVariable("dbType") String dbType, @RequestBody ProjectGenerateConfig.DataSourceConfig dataSourceConfig) throws SQLException, IOException, TemplateException {
        return metaConvertService.batchConvertTableDDL(dataSourceConfig, dbType);
    }

    /**
     * 元数据对比, 变更 sql 记录
     * @param compareParam 对比范围限制
     * @return
     */
    @PostMapping("/compare/changeSqls")
    public List<String> metaCompareChangeSqls(@RequestBody @Validated CompareParam compareParam) throws SQLException, IOException, TemplateException {
        return metaCompareService.changeSqls(compareParam);
    }

    /**
     * 元数据对比
     * @param compareParam 对比范围限制
     * @return
     */
    @PostMapping("/compare")
    public List<TableModify> metaCompare(@RequestBody @Validated CompareParam compareParam) throws IOException, SQLException {
        List<TableModify> tableModifies = new ArrayList<>();

        final ModifyInfo modifyInfo = metaCompareService.compare(compareParam);

        // 增删数据表
        final List<ModifyTable> modifyTables = modifyInfo.getModifyTables();
        for (ModifyTable modifyTable : modifyTables) {
            tableModifies.add(new TableModify(modifyTable.getTableName(),modifyTable.getDiffType(),modifyTable.getTableMetaData()));
        }

        // 表变更 tableName => TableModify
        Map<String,TableModify> tableModifyMap = new HashMap<>();

        // 表变更列信息 tableName => List<ModifyColumn>
        MultiValueMap<String, ModifyColumn> modifyColumnMultiValueMap = new LinkedMultiValueMap<>();
        for (ModifyColumn modifyColumn : modifyInfo.getModifyColumns()) {
            modifyColumnMultiValueMap.add(modifyColumn.getTableName(),modifyColumn);
        }
        final Iterator<Map.Entry<String, List<ModifyColumn>>> iterator = modifyColumnMultiValueMap.entrySet().iterator();
        while (iterator.hasNext()){
            final Map.Entry<String, List<ModifyColumn>> next = iterator.next();
            final String tableName = next.getKey();
            final List<ModifyColumn> modifyColumns = next.getValue();
            final TableModify tableModify = new TableModify(tableName, DiffType.MODIFY);
            tableModify.setModifyColumns(modifyColumns);
            tableModifyMap.put(tableName,tableModify);

        }

        // 表变更索引信息 tableName => List<ModifyIndex>
        MultiValueMap<String, ModifyIndex> modifyIndexLinkedMultiValueMap = new LinkedMultiValueMap<>();
        for (ModifyIndex modifyIndex : modifyInfo.getModifyIndices()) {
            modifyIndexLinkedMultiValueMap.add(modifyIndex.getTableName(),modifyIndex);
        }
        final Iterator<Map.Entry<String, List<ModifyIndex>>> entryIterator = modifyIndexLinkedMultiValueMap.entrySet().iterator();
        while (entryIterator.hasNext()){
            final Map.Entry<String, List<ModifyIndex>> next = entryIterator.next();
            final String tableName = next.getKey();
            final List<ModifyIndex> modifyIndices = next.getValue();
            if (tableModifyMap.containsKey(tableName)){
                final TableModify tableModify = tableModifyMap.get(tableName);
                tableModify.setModifyIndices(modifyIndices);
            }else{
                final TableModify tableModify = new TableModify(tableName, DiffType.MODIFY);
                tableModify.setModifyIndices(modifyIndices);
                tableModifyMap.put(tableName,tableModify);
            }
        }

        tableModifies.addAll(tableModifyMap.values());

        return tableModifies;
    }
}
