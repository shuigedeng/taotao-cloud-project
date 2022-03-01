package com.taotao.cloud.sys.biz.tools.database.service;

import com.taotao.cloud.sys.biz.tools.core.exception.ToolException;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Column;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Index;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 表结构差异对比
 */
@Service
public class MetaCompareService {
    @Autowired
    private JdbcService jdbcService;
    @Autowired
    private Configuration configuration;

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
    public List<String> changeSqls(String baseConnName,String compareConnName,String baseCatalog,String compareCatalog) throws IOException, SQLException, TemplateException {
        List<String> sqls = new ArrayList<>();
        final ModifyInfo modifyInfo = this.compare(baseConnName, compareConnName, baseCatalog, compareCatalog);
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
                    dataModel.put("actualTableName",new ActualTableName(compareCatalog,null,modifyTable.getTableName()));
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
    public ModifyInfo compare(String baseConnName,String compareConnName,String baseCatalog,String compareCatalog) throws IOException, SQLException {
        final String baseDbType = jdbcService.dbType(baseConnName);
        final String compareDbType = jdbcService.dbType(compareConnName);
        if (!baseDbType.equals(compareDbType)){
            throw new ToolException("仅支持同类型数据库比较");
        }

        final Collection<TableMetaData> baseTables = jdbcService.tables(baseConnName, baseCatalog,null);
        final Collection<TableMetaData> compareTables = jdbcService.tables(compareConnName, compareCatalog,null);

        final Map<String, TableMetaData> baseTableMap = baseTables.stream().collect(Collectors.toMap(tableMetaData -> tableMetaData.getActualTableName().getTableName(), Function.identity()));
        final Map<String, TableMetaData> compareTableMap = compareTables.stream().collect(Collectors.toMap(tableMetaData -> tableMetaData.getActualTableName().getTableName(), Function.identity()));

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
            List<ModifyColumn> modifyColumns = compareColumns(tableName,baseTableMetaData.getColumns(),compareTableMetaData.getColumns());
            modifyInfo.getModifyColumns().addAll(modifyColumns);

            // 索引信息比较
            List<ModifyIndex> modifyIndices = compareIndices(tableName,baseTableMetaData.getIndexs(),compareTableMetaData.getIndexs());
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
    private List<ModifyColumn> compareColumns(String tableName, List<Column> baseColumns, List<Column> compareColumns) {
        List<ModifyColumn> modifyColumns = new ArrayList<>();

        final Map<String, Column> baseColumnMap = baseColumns.stream().collect(Collectors.toMap(Column::getColumnName, Function.identity()));
        final Map<String, Column> compareColumnMap = compareColumns.stream().collect(Collectors.toMap(Column::getColumnName, Function.identity()));

        final Iterator<String> iterator = baseColumnMap.keySet().iterator();
        while (iterator.hasNext()){
            final String columnName = iterator.next();
            final Column baseColumn = baseColumnMap.get(columnName);
            final Column compareColumn = compareColumnMap.remove(columnName);
            if (compareColumn == null){
                modifyColumns.add(new ModifyColumn(tableName,DiffType.DELETE,baseColumn,null));
                continue;
            }

            if (!baseColumn.equals(compareColumn)){
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
    private List<ModifyIndex> compareIndices(String tableName, List<Index> baseIndices, List<Index> compareIndices){
        List<ModifyIndex> modifyIndices = new ArrayList<>();

        final Map<String, Index> baseIndexMap = baseIndices.stream().collect(Collectors.toMap(index -> index.getIndexName()+"_"+index.getOrdinalPosition(), Function.identity()));
        final Map<String, Index> compareIndexMap = compareIndices.stream().collect(Collectors.toMap(index -> index.getIndexName()+"_"+index.getOrdinalPosition(), Function.identity()));

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

    public static final class ModifyInfo{
        private List<ModifyTable> modifyTables = new ArrayList<>();
        private List<ModifyColumn> modifyColumns = new ArrayList<>();
        private List<ModifyIndex> modifyIndices = new ArrayList<>();
        private String dbType;

        public void addModifyTable(ModifyTable modifyTable){
            modifyTables.add(modifyTable);
        }
        public void addModifyColumn(ModifyColumn modifyColumn){
            modifyColumns.add(modifyColumn);
        }
        public void addModifyIndex(ModifyIndex modifyIndex){

            modifyIndices.add(modifyIndex);
        }

	    public List<ModifyTable> getModifyTables() {
		    return modifyTables;
	    }

	    public void setModifyTables(
		    List<ModifyTable> modifyTables) {
		    this.modifyTables = modifyTables;
	    }

	    public List<ModifyColumn> getModifyColumns() {
		    return modifyColumns;
	    }

	    public void setModifyColumns(
		    List<ModifyColumn> modifyColumns) {
		    this.modifyColumns = modifyColumns;
	    }

	    public List<ModifyIndex> getModifyIndices() {
		    return modifyIndices;
	    }

	    public void setModifyIndices(
		    List<ModifyIndex> modifyIndices) {
		    this.modifyIndices = modifyIndices;
	    }

	    public String getDbType() {
		    return dbType;
	    }

	    public void setDbType(String dbType) {
		    this.dbType = dbType;
	    }
    }

    /**
     * 列修改
     */
    public static final class ModifyColumn{
        private String tableName;
        private DiffType diffType;
        private Column baseColumn;
        private Column newColumn;

        public ModifyColumn(String tableName, DiffType diffType, Column baseColumn, Column newColumn) {
            this.tableName = tableName;
            this.diffType = diffType;
            this.baseColumn = baseColumn;
            this.newColumn = newColumn;
        }

	    public String getTableName() {
		    return tableName;
	    }

	    public void setTableName(String tableName) {
		    this.tableName = tableName;
	    }

	    public DiffType getDiffType() {
		    return diffType;
	    }

	    public void setDiffType(
		    DiffType diffType) {
		    this.diffType = diffType;
	    }

	    public Column getBaseColumn() {
		    return baseColumn;
	    }

	    public void setBaseColumn(Column baseColumn) {
		    this.baseColumn = baseColumn;
	    }

	    public Column getNewColumn() {
		    return newColumn;
	    }

	    public void setNewColumn(Column newColumn) {
		    this.newColumn = newColumn;
	    }
    }

    /**
     * 索引修改
     */
    public static final class ModifyIndex{
        private String tableName;
        private DiffType diffType;
        private Index baseIndex;
        private Index newIndex;

        public ModifyIndex(String tableName, DiffType diffType, Index baseIndex, Index newIndex) {
            this.tableName = tableName;
            this.diffType = diffType;
            this.baseIndex = baseIndex;
            this.newIndex = newIndex;
        }

	    public String getTableName() {
		    return tableName;
	    }

	    public void setTableName(String tableName) {
		    this.tableName = tableName;
	    }

	    public DiffType getDiffType() {
		    return diffType;
	    }

	    public void setDiffType(
		    DiffType diffType) {
		    this.diffType = diffType;
	    }

	    public Index getBaseIndex() {
		    return baseIndex;
	    }

	    public void setBaseIndex(Index baseIndex) {
		    this.baseIndex = baseIndex;
	    }

	    public Index getNewIndex() {
		    return newIndex;
	    }

	    public void setNewIndex(Index newIndex) {
		    this.newIndex = newIndex;
	    }
    }

    /**
     * 数据表增删
     */
    public static final class ModifyTable{
        private String tableName;
        private DiffType diffType;
        private TableMetaData tableMetaData;

        public ModifyTable(String tableName, DiffType diffType) {
            this.tableName = tableName;
            this.diffType = diffType;
        }

        public ModifyTable(String tableName, DiffType diffType, TableMetaData tableMetaData) {
            this.tableName = tableName;
            this.diffType = diffType;
            this.tableMetaData = tableMetaData;
        }

	    public String getTableName() {
		    return tableName;
	    }

	    public void setTableName(String tableName) {
		    this.tableName = tableName;
	    }

	    public DiffType getDiffType() {
		    return diffType;
	    }

	    public void setDiffType(
		    DiffType diffType) {
		    this.diffType = diffType;
	    }

	    public TableMetaData getTableMetaData() {
		    return tableMetaData;
	    }

	    public void setTableMetaData(TableMetaData tableMetaData) {
		    this.tableMetaData = tableMetaData;
	    }
    }

    /**
     * 更改类型
     */
    public enum DiffType{
        ADD,MODIFY,DELETE
    }
}
