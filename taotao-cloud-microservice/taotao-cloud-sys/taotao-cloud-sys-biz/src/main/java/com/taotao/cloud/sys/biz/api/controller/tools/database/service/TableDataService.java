package com.taotao.cloud.sys.biz.api.controller.tools.database.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.SqlList;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.compare.DiffType;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.data.DataChangeParam;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.data.transfer.DataChange;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.processors.ListDataRowProcessor;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.connect.ConnDatasourceAdapter;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.TabeRelationMetaData;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.aspect.JdbcConnection;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.cglib.beans.BeanMap;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;

@Service
@Slf4j
public class TableDataService {
    @Autowired
    private JdbcDataService jdbcDataService;
    @Autowired
    private JdbcMetaService jdbcMetaService;
    @Autowired
    private TableSearchService tableSearchService;
    @Autowired
    private TabeRelationMetaData tabeRelationMetaData;

    /**
     * sepl 表达式解析器
     */
    private ExpressionParser expressionParser = new SpelExpressionParser();

    private CCJSqlParserManager parserManager = new CCJSqlParserManager();

    @Autowired
    private ConnDatasourceAdapter connDatasourceAdapter;

    @Autowired
    private Configuration configuration;

    /**
     * 生成数据变更 sql 语句, 可重复执行的
     * @param connName
     * @param actualTableName
     * @param selectItems
     * @param condition
     * @return
     */
    @JdbcConnection
    public List<SqlList> generateDataChangeSqls(DataChangeParam dataChangeParam) throws IOException, SQLException {
        // 生成 sql 语句
        final ActualTableName actualTableName = dataChangeParam.getActualTableName();
        final String condition = dataChangeParam.getCondition();

        // 如果是删除语句 , 则不需要后面的数据查询了
        if (dataChangeParam.getDiffType() == DiffType.DELETE){
            List<SqlList> sqlLists = new ArrayList<>();
            for (String dbType : dataChangeParam.getDbTypes()) {
                final SqlList sqlList = new SqlList(dbType);
                sqlLists.add(sqlList);
                sqlList.addSql("delete from "+actualTableName.getTableName()+" where "+ condition);
            }
            return sqlLists;
        }

        List<SqlList> sqlLists = new ArrayList<>();

        // 补充主键查询, 如果没有查询主键的话
        final List<String> selectItems = dataChangeParam.getSelectItems();
        final List<PrimaryKey> primaryKeys = jdbcMetaService.primaryKeys(dataChangeParam.getConnName(), actualTableName);
        final List<String> primaryKeysColumnNames = primaryKeys.stream().map(PrimaryKey::getColumnName).collect(Collectors.toList());
        final Collection<String> finallySelectItems = CollectionUtils.union(selectItems, primaryKeysColumnNames);

        String sql = "select " + StringUtils.join(finallySelectItems,',') + " from " + actualTableName.getTableName();;

        if (StringUtils.isNotBlank(condition)){
            sql += (" where " + condition);
        }

        // 查询到要更改的数据
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(dataChangeParam.getConnName(), dataChangeParam.getActualTableName().getNamespace());
        QueryRunner queryRunner = new QueryRunner(druidDataSource);
        final List<ListDataRowProcessor.Row> result = queryRunner.query(sql, ListDataRowProcessor.INSTANCE);
        log.info("SQL[{}]查询到本次需要变更的数据量为:[{}]",sql,result.size());

        List<DataChange> dataChanges = new ArrayList<>();
        for (ListDataRowProcessor.Row row : result) {
            final DataChange dataChange = new DataChange(dataChangeParam.getDiffType(), actualTableName.getTableName());
            dataChanges.add(dataChange);

            // 列数据映射 columnName => 列值
            final Map<String, DataChange.ColumnValue> columnValueMap = row.getFields().stream().collect(Collectors.toMap(field -> field.getColumn().getColumnName(), field -> new DataChange.ColumnValue(Objects.toString(field.getValue(),null),field.getColumnType())));

            switch (dataChangeParam.getDiffType()){
                case ADD:
                    final DataChange.Insert insert = new DataChange.Insert(row.getColumnNames());
                    dataChange.setInsert(insert);

                    for (TransferDataRow.TransferDataField field : row.getFields()) {
                        insert.addColumnValue(new DataChange.ColumnValue(Objects.toString(field.getValue(),null),field.getColumnType()));
                    }

                    if (CollectionUtils.isNotEmpty(primaryKeys)) {
                        insert.setUniqueKey(primaryKeys.get(0).getColumnName());
                    }

                    break;
                case MODIFY:
                    final DataChange.Update update = new DataChange.Update();
                    dataChange.setUpdate(update);

                    // 修改项
                    for (String selectItem : selectItems) {
                        final DataChange.ColumnValue columnValue = columnValueMap.get(selectItem);
                        update.putColumnSet(selectItem,columnValue);
                    }

                    // where 条件
                    if (CollectionUtils.isNotEmpty(primaryKeys)) {
                        final String columnName = primaryKeys.get(0).getColumnName();
                        final DataChange.ColumnValue columnValue = columnValueMap.get(columnName);
                        DataChange.Condition where = new DataChange.Condition(columnName, columnValue);
                        update.setWhere(where);
                    }
                    break;
                default:
            }

        }

        // 对于所有的变更生成 sql 语句, 其中 update 语句需要加上条件
        for (String dbType : dataChangeParam.getDbTypes()) {
            final SqlList sqlList = new SqlList(dbType);
            sqlLists.add(sqlList);

            Template createTableTemplate = configuration.getTemplate("sqls/datachange."+dbType+".sql.ftl");
            for (DataChange dataChange : dataChanges) {
                Map<String,Object> dataModel =  beanToMap(dataChange);
                StringWriter stringWriter = new StringWriter();
                try {
                    createTableTemplate.process(dataModel,stringWriter);

                    sqlList.addSql(stringWriter.toString());
                } catch (TemplateException e) {
                    log.error("数据表[{}]对于数据库类型[{}]数据变更SQL生成失败",dataChange.getTableName(),dbType);
                }
            }
        }

        return sqlLists;
    }

    /**
     * 清空数据表
     * @param connName
     * @param actualTableName
     * @return
     */
    public int emptyTable(String connName, ActualTableName actualTableName) throws IOException, SQLException {
        String sql = "truncate "+actualTableName.getTableName();
        List<Integer> integers = jdbcDataService.executeUpdate(connName, Collections.singletonList(sql),actualTableName.getNamespace());
        if (CollectionUtils.isNotEmpty(integers)){
            return integers.get(0);
        }
        return 0 ;
    }

    /**
     * 清空数据 一对一清空模板
     */
    public static final String sqlLeftJoinTemplate = "SELECT t1.${firstSourcePrimaryKey} FROM ${sourceTableName} t1 LEFT JOIN ${targetTableName} t2 ON t1.${sourceColumnName} = t2.${targetColumnName} WHERE t2.${firstTargetPrimaryKey} IS NULL";
    public static final String sqlRightJoinTemplate = "SELECT t2.${firstTargetPrimaryKey} FROM ${sourceTableName} t1 RIGHT JOIN ${targetTableName} t2 ON t1.${sourceColumnName} = t2.${targetColumnName} WHERE t1.${firstSourcePrimaryKey} IS NULL";
    public static final String deleteDataSqlLeftJoinTemplate = "DELETE FROM ${sourceTableName} WHERE ${firstSourcePrimaryKey} IN ( "+ sqlLeftJoinTemplate+" ) ";
    public static final String deleteDataSqlRightJoinTemplate = "DELETE FROM ${targetTableName} WHERE ${firstTargetPrimaryKey} IN ( " + sqlRightJoinTemplate+" ) ";

    /**
     * 不符合关系的数据检查
     * @param connName
     * @param namespace
     * @return
     */
    @JdbcConnection
    public List<DirtyDataInfo> checkDirtyData(String connName, Namespace namespace) throws IOException, SQLException {
        final List<TableMeta> tables = jdbcMetaService.tables(connName, namespace);

        // 获取所有表名, 关系有可能有错误的表名
        Map<String, String> tableNameMap = tables.stream().map(TableMeta::getTable).map(Table::getActualTableName).map(ActualTableName::getTableName).collect(Collectors.toMap(Function.identity(), Function.identity()));
        tableNameMap = new CaseInsensitiveMap<>(tableNameMap);

        List<DirtyDataInfo> dirtyDataInfos = new ArrayList<>();
        for (TableMeta table : tables) {
            final ActualTableName actualTableName = table.getTable().getActualTableName();
            final List<TableRelation> childs = tabeRelationMetaData.childs(connName, actualTableName);
            if (CollectionUtils.isEmpty(childs)){
                // 如果当前数据表是没有关系配置的, 则跳过
                continue;
            }

            // 获取数据表主键(第一个主键)
            final List<PrimaryKey> sourcePrimaryKeys = jdbcMetaService.primaryKeys(connName, table.getTable().getActualTableName());
            final String firstSourcePrimaryKey = sourcePrimaryKeys.get(0).getColumnName();

            for (TableRelation child : childs) {
                if (!tableNameMap.containsKey(child.getSourceTableName()) || !tableNameMap.containsKey(child.getTargetTableName())){
//                    throw new ToolException("数据表被删除"+child.getSourceTableName()+", "+child.getTargetTableName());
                    log.warn("数据表[{}]或者[{}]被删除",child.getSourceTableName(),child.getTargetTableName());
                }

                final DirtyDataInfo dirtyDataInfo = new DirtyDataInfo(child);
                dirtyDataInfos.add(dirtyDataInfo);

                final List<PrimaryKey> targetPrimaryKeys = jdbcMetaService.primaryKeys(connName, new ActualTableName(namespace, child.getTargetTableName()));
                final String firstTargetPrimaryKey = targetPrimaryKeys.get(0).getColumnName();
                final ExtendTableRelation extendTableRelation = new ExtendTableRelation(child, firstSourcePrimaryKey,firstTargetPrimaryKey);

                StringSubstitutor stringSubstitutor = new StringSubstitutor(beanToMap(extendTableRelation));
                dirtyDataInfo.addDeleteItem(new DirtyDataInfo.DeleteItem(stringSubstitutor.replace(sqlLeftJoinTemplate),stringSubstitutor.replace(deleteDataSqlLeftJoinTemplate)));
                dirtyDataInfo.addDeleteItem(new DirtyDataInfo.DeleteItem(stringSubstitutor.replace(sqlRightJoinTemplate),stringSubstitutor.replace(deleteDataSqlRightJoinTemplate)));
            }
        }

        // 查询每条语句的删除数据数量
        long startTime = System.currentTimeMillis();
        try {
            ResultSetHandler<Long> resultSetHandler = new ScalarHandler<>();
            for (DirtyDataInfo dirtyDataInfo : dirtyDataInfos) {
                final List<DirtyDataInfo.DeleteItem> deleteItems = dirtyDataInfo.getDeleteItems();
                for (DirtyDataInfo.DeleteItem deleteItem : deleteItems) {
                    final String querySql = deleteItem.getQuerySql();
                    final Select select = (Select) parserManager.parse(new StringReader(querySql));
                    final PlainSelect selectBody = (PlainSelect) select.getSelectBody();
                    List<SelectItem> selectItems = new ArrayList<>();
                    final SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
                    selectItems.add(selectExpressionItem);
                    final net.sf.jsqlparser.expression.Function function = new net.sf.jsqlparser.expression.Function();
                    function.setAllColumns(true);
                    function.setName("count");
                    selectExpressionItem.setExpression(function);
                    selectBody.setSelectItems(selectItems);
                    final Long executeQuery = jdbcDataService.executeQuery(connName, select.toString(), resultSetHandler, namespace);
                    deleteItem.setTotal(executeQuery);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            log.info("执行查询耗时: {} ms",(System.currentTimeMillis() - startTime));
        }

        return dirtyDataInfos;
    }

    /**
     * 将对象装换为map
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 单表数据添加,随机数据
     * @param tableDataParam
     */
    public void singleTableWriteRandomData(TableDataParam tableDataParam) throws IOException, SQLException, JSQLParserException {
        // 获取表元数据信息
        String connName = tableDataParam.getConnName();
        ActualTableName actualTableName = tableDataParam.getActualTableName();
        Optional<TableMeta> tableMeta = tableSearchService.getTable(connName,actualTableName);
        if (!tableMeta.isPresent()){
            log.error("未找到数据表: [{}]",actualTableName);
            return ;
        }

        final TableMeta tableMetaData = tableMeta.get();

        List<TableDataParam.ColumnMapper> columnMappers = tableDataParam.getColumnMappers();
        // 如果有列映射是使用 sql 语句的,先查询 sql 语句的数据,每次都去查的话性能会受影响
        List<String> sqls = columnMappers.stream().filter(columnMapper -> StringUtils.isNotBlank(columnMapper.getSql())).map(TableDataParam.ColumnMapper::getSql).collect(Collectors.toList());
        // sql 做 md5 ,映射 sql 对象的列表数据
        Map<String,List<Object>> datas = new HashMap<>();
        for (String sql : sqls) {
            String sqlMd5 = DigestUtils.md5Hex(sql);
            // sql 如果没有加 limit ,这里给其加上 limit 100
            Select select = (Select) parserManager.parse(new StringReader(sql));
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            Limit originLimit = plainSelect.getLimit();
            if (originLimit == null) {
                originLimit = new Limit();
                originLimit.setOffset(0);
                originLimit.setRowCount(100);
            }
            plainSelect.setLimit(originLimit);

            List<Object> executeQuery = jdbcDataService.executeQuery(connName, sql, new ColumnListHandler<Object>(1), actualTableName.getNamespace());
            datas.put(sqlMd5,executeQuery);
        }

        List<String> columns = columnMappers.stream().map(TableDataParam.ColumnMapper::getColumnName).collect(Collectors.toList());

        // column 映射成 map
        Map<String, Column> columnMap = tableMetaData.getColumns().stream().collect(Collectors.toMap(Column::getColumnName, column -> column));

        // 将数据分段插入
        int SEGMENT_SIZE = 1000;
        int segments = (tableDataParam.getSize() - 1) / SEGMENT_SIZE + 1;
        log.info("数据将分段插入,总共分成 {} 段",segments);
        for (int k = 0; k < segments; k++) {
            int start = k * SEGMENT_SIZE;
            int end = (k + 1) * SEGMENT_SIZE;
            if(end > tableDataParam.getSize()){
                end = tableDataParam.getSize();
            }

            List<String[]> rows = new ArrayList<>();
            for (int i = start; i < end; i++) {
                List<String> row = new ArrayList<>();
                for (TableDataParam.ColumnMapper columnMapper : columnMappers) {
                    String random = columnMapper.getRandom();
                    String columnName = columnMapper.getColumnName();
                    String sql = columnMapper.getSql();
                    final String fixed = columnMapper.getFixed();

                    Object value = null;
                    if (StringUtils.isNotBlank(random)) {
                        // 使用 spel 生成数据
                        Expression expression = expressionParser.parseExpression(random);
                        value =  expression.getValue(String.class);
                    }else if (StringUtils.isNotBlank(sql)){
                        String md5Hex = DigestUtils.md5Hex(sql);
                        List<Object> list = datas.get(md5Hex);
                        int position = RandomUtils.nextInt(0,list.size());
                        value = list.get(position);
                    }else if (StringUtils.isNotBlank(fixed)){
                        value = fixed;
                    }

                    // 需要判断数据库字段类型,数字型和字符型的添加不一样的
                    Column column = columnMap.get(columnName);
                    String dataType = column.getTypeName();

                    // 暂时都使用 String 类型
                    row.add(Objects.toString(value));
                }
                rows.add(row.toArray(new String []{}));
            }
            writeDataToTable(connName,actualTableName,columns.toArray(new String []{}),rows);
            String percent = new BigDecimal((k+1) * 100).divide(new BigDecimal(segments)).setScale(2, RoundingMode.HALF_UP).toString();
            log.info("数据表 {} 数据插入进度 {}/{} , 百分比: {} %",actualTableName.getTableName(),(k+1),segments,percent);
        }

    }

    /**
     * 从 excel 导入数据到某张表
     * @param excelImportParam
     * @param multipartFile
     */
    public void importDataFromExcel(ExcelImportParam excelImportParam, MultipartFile excel) throws IOException, SQLException {
        String connName = excelImportParam.getConnName();
        final ActualTableName actualTableName = excelImportParam.getActualTableName();
        Optional<TableMeta> tableMeta = tableSearchService.getTable(connName,actualTableName);
        if (!tableMeta.isPresent()){
            log.error("未找到数据表: [{}]",actualTableName);
            return ;
        }

        final TableMeta tableMetaData = tableMeta.get();

        // 读取 Excel 数据
        ImportDataToTableListener syncReadListener = new ImportDataToTableListener(tableMetaData,excelImportParam);
        ReadSheet readSheet = EasyExcel.readSheet(0).headRowNumber(excelImportParam.getStartRow()).build();
        ExcelReader excelReader = EasyExcel.read(excel.getInputStream())
                .autoTrim(true).ignoreEmptyRow(true).registerReadListener(syncReadListener).build();
        excelReader.read(readSheet);
    }

    /**
     * 直接用map接收数据
     *
     * @author Jiaju Zhuang
     */
    private class ImportDataToTableListener extends AnalysisEventListener<Map<Integer, String>> {
        private TableMeta tableMetaData;
        private ExcelImportParam excelImportParam;
        private static final int BATCH_SIZE = 1000;

        private List<Map<Integer,String>> cacheDatas = new ArrayList<>();

        String insertSql = "insert into ${tableName}(${columns}) values ${values}";

        public ImportDataToTableListener(TableMeta tableMetaData, ExcelImportParam excelImportParam) {
            this.tableMetaData = tableMetaData;
            this.excelImportParam = excelImportParam;
        }

        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {
            if (cacheDatas.size() >= BATCH_SIZE){
                saveDataToTable();
                cacheDatas.clear();
            }
            cacheDatas.add(data);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            saveDataToTable();
        }

        private void saveDataToTable(){
            List<ExcelImportParam.Mapping> mappings = excelImportParam.getMapping();

            String [] headers = new String[mappings.size()];
            for (int i = 0; i < mappings.size(); i++) {
                ExcelImportParam.Mapping mapping = mappings.get(i);
                headers[i] = mapping.getColumnName();
            }

            List<String []> rows = new ArrayList<>();

            for (Map<Integer, String> cacheData : cacheDatas) {
                String [] body = new String[mappings.size()];
                for (int i = 0; i < mappings.size(); i++) {
                    ExcelImportParam.Mapping mapping = mappings.get(i);
                    int index = mapping.getIndex();
                    String random = mapping.getRandom();
                    if (index != -1){
                        body[i] = cacheData.get(index);
                    }else if (StringUtils.isNotBlank(random)){
                        // 使用 spel 生成数据
                        Expression expression = expressionParser.parseExpression(random);
                        body[i] = expression.getValue(String.class);
                    }else{
                        body[i] = mapping.getConstant();
                    }
                }

                rows.add(body);
            }

            cacheDatas.clear();
            writeDataToTable(excelImportParam.getConnName(),excelImportParam.getActualTableName(),headers,rows);
        }
    }

    /**
     * 数据写入表格
     * @param connName
     * @param actualTableName
     * @param columns
     * @param rows
     */
    private void writeDataToTable(String connName,ActualTableName actualTableName,String [] columns, List<String[]> rows){
        String insertSql = "insert into ${tableName}(${columns}) values ${values}";

        HashMap<String, Object> paramMap = new HashMap<>();
        String tableName = actualTableName.getSchema()+"."+actualTableName.getTableName();
        if (StringUtils.isBlank(actualTableName.getSchema())){
            tableName = actualTableName.getCatalog()+"."+actualTableName.getTableName();
        }
        paramMap.put("tableName",tableName);
        paramMap.put("columns",StringUtils.join(columns,','));
        List<String> multiValues = new ArrayList<>();
        StringSubstitutor stringSubstitutor = new StringSubstitutor(paramMap, "${", "}");

        for (String[] row : rows) {
            String currentValues = StringUtils.join(row,"','");
            currentValues = "('" + currentValues + "')";
            multiValues.add(currentValues);
        }
        String columnValues = StringUtils.join(multiValues, ',');
        paramMap.put("values",columnValues);
        String finalSql = stringSubstitutor.replace(insertSql);

        try {
            List<Integer> executeUpdate = jdbcDataService.executeUpdate(connName, Arrays.asList(finalSql), actualTableName.getNamespace());
            log.info("影响行数 {}",executeUpdate);
        } catch (SQLException | IOException e) {
            log.error("当前 sql 执行错误{}, 当前sql  {}",e.getMessage(),finalSql);
        }

    }

}
