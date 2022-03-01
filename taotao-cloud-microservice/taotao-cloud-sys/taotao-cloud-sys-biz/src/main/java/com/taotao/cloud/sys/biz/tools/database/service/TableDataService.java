package com.taotao.cloud.sys.biz.tools.database.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sys.biz.tools.core.exception.ToolException;
import com.taotao.cloud.sys.biz.tools.database.dtos.ExcelImportParam;
import com.taotao.cloud.sys.biz.tools.database.dtos.TableDataParam;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Column;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableDataService {
    @Autowired
    private JdbcService jdbcService;
    @Autowired
    private DataService dataService;

    private ExpressionParser expressionParser = new SpelExpressionParser();

    // jsqlparser 解析
    private CCJSqlParserManager parserManager = new CCJSqlParserManager();

    /**
     * 清空数据表
     * @param connName
     * @param actualTableName
     * @return
     */
    public int emptyTable(String connName, ActualTableName actualTableName) throws IOException, SQLException {
        String sql = "truncate "+actualTableName.getSchema()+"."+actualTableName.getTableName();
        if (StringUtils.isBlank(actualTableName.getSchema())){
            sql = "truncate "+actualTableName.getCatalog()+"."+actualTableName.getTableName();
        }
        List<Integer> integers = jdbcService.executeUpdate(connName, Collections.singletonList(sql));
        if (CollectionUtils.isNotEmpty(integers)){
            return integers.get(0);
        }
        return 0 ;
    }

    /**
     * 单表数据添加,随机数据
     * @param tableDataParam
     */
    public void singleTableWriteRandomData(TableDataParam tableDataParam) throws IOException, SQLException, JSQLParserException {
        // 获取表元数据信息
        String connName = tableDataParam.getConnName();
        ActualTableName actualTableName = tableDataParam.getActualTableName();
        TableMetaData tableMetaData = jdbcService.findTable(connName,actualTableName);
        if (tableMetaData == null){
            LogUtil.error("未找到数据表: [{}]",actualTableName);
            return ;
        }

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

            List<Object> executeQuery = jdbcService.executeQuery(connName, sql, new ColumnListHandler<Object>(1));
            datas.put(sqlMd5,executeQuery);
        }

        List<String> columns = columnMappers.stream().map(TableDataParam.ColumnMapper::getColumnName).collect(Collectors.toList());

        // column 映射成 map
        Map<String, Column> columnMap = tableMetaData.getColumns().stream().collect(Collectors.toMap(Column::getColumnName, column -> column));

        // 将数据分段插入
        int SEGMENT_SIZE = 1000;
        int segments = (tableDataParam.getSize() - 1) / SEGMENT_SIZE + 1;
	    LogUtil.info("数据将分段插入,总共分成 {} 段",segments);
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
	        LogUtil.info("数据表 {} 数据插入进度 {}/{} , 百分比: {} %",actualTableName.getTableName(),(k+1),segments,percent);
        }

    }

    /**
     * 从 excel 导入数据到某张表
     * @param excelImportParam
     * @param multipartFile
     */
    public void importDataFromExcel(ExcelImportParam excelImportParam, MultipartFile excel) throws IOException, SQLException {
        String connName = excelImportParam.getConnName();
        TableMetaData tableMetaData = jdbcService.findTable(connName, excelImportParam.getActualTableName());
        if (tableMetaData == null){
            throw new ToolException(excelImportParam.getActualTableName().getTableName()+" 数据表不存在,导入失败");
        }
        // 读取 Excel 数据
        ImportDataToTableListener syncReadListener = new ImportDataToTableListener(tableMetaData,excelImportParam);
        ReadSheet readSheet = EasyExcel.readSheet(0).headRowNumber(excelImportParam.getStartRow()).build();
        ExcelReader excelReader = EasyExcel.read(excel.getInputStream())
                .autoTrim(true).ignoreEmptyRow(true).registerReadListener(syncReadListener).build();
        excelReader.read(readSheet);
    }

    /**
     * 直接用map接收数据
     */
    private class ImportDataToTableListener extends AnalysisEventListener<Map<Integer, String>> {
        private TableMetaData tableMetaData;
        private ExcelImportParam excelImportParam;
        private static final int BATCH_SIZE = 1000;

        private List<Map<Integer,String>> cacheDatas = new ArrayList<>();

        String insertSql = "insert into ${tableName}(${columns}) values ${values}";

        public ImportDataToTableListener(TableMetaData tableMetaData, ExcelImportParam excelImportParam) {
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
            List<Integer> executeUpdate = jdbcService.executeUpdate(connName, Arrays.asList(finalSql));
	        LogUtil.info("影响行数 {}",executeUpdate);
        } catch (SQLException | IOException e) {
	        LogUtil.error("当前 sql 执行错误{}, 当前sql  {}",e.getMessage(),finalSql);
        }

    }

}
