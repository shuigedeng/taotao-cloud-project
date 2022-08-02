package com.taotao.cloud.sys.biz.modules.database.service;

import com.sanri.tools.modules.core.exception.ToolException;
import com.sanri.tools.modules.database.service.connect.ConnDatasourceAdapter;
import com.sanri.tools.modules.database.service.dtos.compare.*;
import com.sanri.tools.modules.database.service.dtos.meta.TableMetaData;
import com.sanri.tools.modules.database.service.meta.dtos.ActualTableName;
import com.sanri.tools.modules.database.service.meta.dtos.Column;
import com.sanri.tools.modules.database.service.meta.dtos.Index;
import com.sanri.tools.modules.database.service.meta.dtos.Namespace;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MetaCompareService {
    @Autowired
    private JdbcMetaService jdbcMetaService;
    @Autowired
    private TableSearchService tableSearchService;

    @Autowired
    private Configuration configuration;

    @Autowired
    private ConnDatasourceAdapter connDatasourceAdapter;

    /**
     * 变更 sql 语句
     * @param baseConnName
     * @param compareConnName
     * @param baseCatalog
     * @param compareCatalog
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public List<String> changeSqls(CompareParam compareParam) throws IOException, SQLException, TemplateException {
        List<String> sqls = new ArrayList<>();
        final ModifyInfo modifyInfo = this.compare(compareParam);
        final String dbType = modifyInfo.getDbType();

        // sql 语句模板
        Template alterTableTemplate = configuration.getTemplate("sqls/altertable."+dbType+".ftl");
        Template alterTableColumnTemplate = configuration.getTemplate("sqls/altertablecolumn."+dbType+".ftl");
        Template alterTableIndexTemplate = configuration.getTemplate("sqls/altertableindex."+dbType+".ftl");

        // 增删表语句
        final List<ModifyTable> modifyTables = modifyInfo.getModifyTables();
        for (ModifyTable modifyTable : modifyTables) {
            final DiffType diffType = modifyTable.getDiffType();
            Map<String,Object> dataModel = new HashMap<>();
            dataModel.put("diffType",diffType);
            final StringWriter stringWriter = new StringWriter();
            switch (diffType){
                case DELETE:
                    dataModel.put("meta",modifyTable.getTableMetaData());
                    dataModel.put("actualTableName",new ActualTableName(compareParam.getCompareNamespace(),modifyTable.getTableName()));
                    alterTableTemplate.process(dataModel,stringWriter);
                    sqls.add(stringWriter.toString());
                    break;
                case ADD:
                    dataModel.put("meta",modifyTable.getTableMetaData());
                    alterTableTemplate.process(dataModel,stringWriter);
                    sqls.add(stringWriter.toString());
                    break;
                default:

            }
        }

        // 修改列
        final List<ModifyColumn> modifyColumns = modifyInfo.getModifyColumns();
        for (ModifyColumn modifyColumn : modifyColumns) {
            Map<String,Object> dataModel = new HashMap<>();
            dataModel.put("modifyColumn",modifyColumn);
            final StringWriter stringWriter = new StringWriter();
            alterTableColumnTemplate.process(dataModel,stringWriter);
            sqls.add(stringWriter.toString());
        }

        // 修改索引
        final List<ModifyIndex> modifyIndices = modifyInfo.getModifyIndices();
        for (ModifyIndex modifyIndex : modifyIndices) {
            Map<String,Object> dataModel = new HashMap<>();
            dataModel.put("modifyIndex",modifyIndex);
            final StringWriter stringWriter = new StringWriter();
            alterTableIndexTemplate.process(dataModel,stringWriter);
            sqls.add(stringWriter.toString());
        }
        return sqls;
    }

    /**
     * 对比两个连接指定 catalog 的数据表差异
     * @param baseConnName
     * @param compareConnName
     * @param catalog
     * @throws IOException
     * @throws SQLException
     */
    public ModifyInfo compare(CompareParam compareParam) throws IOException, SQLException {
        final String baseConnName = compareParam.getBaseConnName();
        final String compareConnName = compareParam.getCompareConnName();

        final String baseDbType = connDatasourceAdapter.dbType(baseConnName);
        final String compareDbType = connDatasourceAdapter.dbType(compareConnName);
        if (!baseDbType.equals(compareDbType)){
            log.warn("不同的数据库({} <=> {})比较,可能比较结果会有问题",baseDbType,compareDbType);
//            throw new ToolException("仅支持同类型数据库比较");
        }

        final Collection<TableMetaData> baseTables = jdbcMetaService.tableInfos(baseConnName, compareParam.getBaseNamespace());
        final Collection<TableMetaData> compareTables = jdbcMetaService.tableInfos(compareConnName, compareParam.getCompareNamespace());

        Map<String, TableMetaData> baseTableMap = baseTables.stream().collect(Collectors.toMap(tableMetaData -> tableMetaData.getActualTableName().getTableName(), Function.identity()));
        Map<String, TableMetaData> compareTableMap = compareTables.stream().collect(Collectors.toMap(tableMetaData -> tableMetaData.getActualTableName().getTableName(), Function.identity()));

        // 是否忽略大小写
        if (compareParam.isIgnoreCase()){
            baseTableMap = new CaseInsensitiveMap<>(baseTableMap);
            compareTableMap = new CaseInsensitiveMap<>(compareTableMap);
        }

        ModifyInfo modifyInfo = new ModifyInfo();
        modifyInfo.setDbType(baseDbType);

        final Iterator<String> iterator = baseTableMap.keySet().iterator();
        while (iterator.hasNext()){
            final String tableName = iterator.next();
            final TableMetaData compareTableMetaData = compareTableMap.remove(tableName);
            if (compareTableMetaData == null){
                modifyInfo.addModifyTable(new ModifyTable(tableName,DiffType.DELETE));
                continue;
            }

            // 比较基础表元数据和比较表元数据
            final TableMetaData baseTableMetaData = baseTableMap.get(tableName);

            // 列比较
            List<ModifyColumn> modifyColumns = compareColumns(tableName,compareParam.isIgnoreCase(),baseTableMetaData.getColumns(),compareTableMetaData.getColumns());
            modifyInfo.getModifyColumns().addAll(modifyColumns);

            // 索引信息比较
            List<ModifyIndex> modifyIndices = compareIndices(tableName,compareParam.isIgnoreCase(),baseTableMetaData.getIndices(),compareTableMetaData.getIndices());
            modifyInfo.getModifyIndices().addAll(modifyIndices);
        }

        // 前面全部删除完成后, 比较类的数据中还有, 则为新加的数据表信息
        final Iterator<String> addTables = compareTableMap.keySet().iterator();
        while (addTables.hasNext()){
            final String tableName = addTables.next();
            final TableMetaData tableMetaData = compareTableMap.get(tableName);
            modifyInfo.addModifyTable(new ModifyTable(tableName,DiffType.ADD,tableMetaData));
        }

        return modifyInfo;
    }

    /**
     * 列信息比较
     * @param baseColumns
     * @param compareColumns
     * @return
     */
    private List<ModifyColumn> compareColumns(String tableName,boolean ignoreCase, List<Column> baseColumns, List<Column> compareColumns) {
        List<ModifyColumn> modifyColumns = new ArrayList<>();

        Map<String, Column> baseColumnMap = baseColumns.stream().collect(Collectors.toMap(Column::getColumnName, Function.identity()));
        Map<String, Column> compareColumnMap = compareColumns.stream().collect(Collectors.toMap(Column::getColumnName, Function.identity()));

        if (ignoreCase){
            baseColumnMap = new CaseInsensitiveMap<>(baseColumnMap);
            compareColumnMap = new CaseInsensitiveMap<>(compareColumnMap);
        }

        final Iterator<String> iterator = baseColumnMap.keySet().iterator();
        while (iterator.hasNext()){
            final String columnName = iterator.next();
            final Column baseColumn = baseColumnMap.get(columnName);
            final Column compareColumn = compareColumnMap.remove(columnName);
            if (compareColumn == null){
                modifyColumns.add(new ModifyColumn(tableName,DiffType.DELETE,baseColumn,null));
                continue;
            }

            if (!baseColumn.equalsValues(compareColumn,ignoreCase)){
                modifyColumns.add(new ModifyColumn(tableName,DiffType.MODIFY,baseColumn,compareColumn));
            }
        }

        // 如果移除之后还有的, 那么则是新加的列
        final Iterator<Column> addColumns = compareColumnMap.values().iterator();
        while (addColumns.hasNext()){
            final Column column = addColumns.next();
            modifyColumns.add(new ModifyColumn(tableName,DiffType.ADD,null,column));
        }

        return modifyColumns;
    }

    /**
     * 索引信息比较
     * @param baseIndices
     * @param compareIndices
     * @return
     */
    private List<ModifyIndex> compareIndices(String tableName,boolean ignoreCase, List<Index> baseIndices, List<Index> compareIndices){
        List<ModifyIndex> modifyIndices = new ArrayList<>();

        Map<String, Index> baseIndexMap = baseIndices.stream().collect(Collectors.toMap(index -> index.getIndexName()+"_"+index.getOrdinalPosition(), Function.identity()));
        Map<String, Index> compareIndexMap = compareIndices.stream().collect(Collectors.toMap(index -> index.getIndexName()+"_"+index.getOrdinalPosition(), Function.identity()));

        if (ignoreCase){
            baseIndexMap = new CaseInsensitiveMap<>(baseIndexMap);
            compareIndexMap = new CaseInsensitiveMap<>(compareIndexMap);
        }

        final Iterator<String> iterator = baseIndexMap.keySet().iterator();
        while (iterator.hasNext()){
            final String indexName = iterator.next();
            final Index baseIndex = baseIndexMap.get(indexName);
            final Index compareIndex = compareIndexMap.remove(indexName);
            if (compareIndex == null){
                modifyIndices.add(new ModifyIndex(tableName,DiffType.DELETE,baseIndex,null));
                continue;
            }

            if (!baseIndex.equals(compareIndex)){
                modifyIndices.add(new ModifyIndex(tableName,DiffType.MODIFY,baseIndex,compareIndex));
            }
        }

        // 如果移除之后还有的, 那么则是新加的索引
        final Iterator<Index> addIndices = compareIndexMap.values().iterator();
        while (addIndices.hasNext()){
            final Index index = addIndices.next();
            modifyIndices.add(new ModifyIndex(tableName,DiffType.ADD,null,index));
        }

        return modifyIndices;
    }
}
