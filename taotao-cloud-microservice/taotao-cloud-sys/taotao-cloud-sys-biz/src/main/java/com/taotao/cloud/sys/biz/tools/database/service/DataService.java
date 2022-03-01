package com.taotao.cloud.sys.biz.tools.database.service;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.database.dtos.DataQueryParam;
import com.taotao.cloud.sys.biz.tools.database.dtos.DynamicQueryDto;
import com.taotao.cloud.sys.biz.tools.database.dtos.ExportProcessDto;
import com.taotao.cloud.sys.biz.tools.database.dtos.RelationDataQueryResult;
import com.taotao.cloud.sys.biz.tools.database.dtos.TableRelationDto;
import com.taotao.cloud.sys.biz.tools.database.dtos.TableRelationTree;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.util.SelectUtils;
import net.sf.jsqlparser.util.TablesNamesFinder;

@Service
public class DataService {
    @Autowired
    private JdbcService jdbcService;

    // jsqlparser 解析
    CCJSqlParserManager parserManager = new CCJSqlParserManager();

    @Autowired
    private FileManager fileManager;

    @Autowired
    private TableRelationService tableRelationService;


    /**
     * sql 关联数据查询,返回需要查询的所有 sql 语句
     * @param connName
     * @param catalog
     * @param sql
     * @return
     */
    public RelationDataQueryResult relationDataQuery(String connName, String catalog, String sql) throws JSQLParserException {
        RelationDataQueryResult relationDataQueryResult = new RelationDataQueryResult(sql);
        // 分析当前 sql , 找到当前 sql 要查的主表
        Select select = (Select) parserManager.parse(new StringReader(sql));
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(select);
        if (CollectionUtils.isNotEmpty(tableList)){
            String tableName = tableList.get(0);
            String[] split = tableName.split("\\.");
            String schema = null;
            if (split.length == 2) {
                schema = split[0];
                tableName = split[1];
            }
            PlainSelect selectBody = (PlainSelect) select.getSelectBody();
            Expression where = selectBody.getWhere();

            ActualTableName actualTableName = new ActualTableName(catalog, schema, tableName);
            TableRelationTree tableRelationTree = tableRelationService.hierarchy(connName, actualTableName);
            TableRelationTree parents = tableRelationService.superTypes(connName, actualTableName);

            List<String> sqls = new ArrayList<>();
            List<TableRelationTree> children = tableRelationTree.getChildren();
            for (TableRelationTree child : children) {
                String generateSql = childRelationsSql(tableRelationTree.getTableName(), child, selectBody);
                sqls.add(generateSql);
            }

//            List<String> parentSqls = relationSqls(actualTableName,childs);
//            relationDataQueryResult.setParents(parentSqls);
        }

        return relationDataQueryResult;
    }

    /**
     *
     * @param main
     * @param subTableRelation
     * @param selectBody
     * @return
     */
    private String childRelationsSql(ActualTableName main,TableRelationTree subTableRelation, SelectBody selectBody) {
        Table mainTable = new Table(main.getSchema(), main.getTableName());
        mainTable.setAlias(new Alias(DigestUtils.md5Hex(main.getFullName())));
        ActualTableName sub = subTableRelation.getTableName();
        Table subTable = new Table(sub.getSchema(), sub.getTableName());
        subTable.setAlias(new Alias(DigestUtils.md5Hex(sub.getFullName())));

        // 一对一 和一对多都是单字段关联 , 多对多可能需要一张表来关联
        TableRelationDto.Relation relation = subTableRelation.getRelation();
        switch (relation){
            case MANY_MANY:
                break;
            case ONE_MANY:
            case ONE_ONE:
                // 构建一个连接查询
                Select select = SelectUtils.buildSelectFromTableAndExpressions(mainTable, new Column("b.*"));

                EqualsTo equalsTo = new EqualsTo();
                equalsTo.setLeftExpression(new Column("a.uuid"));
                equalsTo.setRightExpression(new Column("b.event_record_id"));
                SelectUtils.addJoin(select,subTable,equalsTo);
                break;
            default:
        }

        return null;
    }

    /**
     * 数据导出预览
     * @param dataQueryParam
     * @return
     * @throws JSQLParserException
     * @throws IOException
     * @throws SQLException
     */
    public DynamicQueryDto exportPreview(DataQueryParam dataQueryParam) throws JSQLParserException, IOException, SQLException {
        String connName = dataQueryParam.getConnName();
        String sql = dataQueryParam.getFirstSql();
        // sql 解析,加上 limit 限制条数
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        Limit limit = new Limit();
        limit.setOffset(0);
        limit.setRowCount(15);
        plainSelect.setLimit(limit);

        List<DynamicQueryDto> dynamicQueryDtos = jdbcService.executeDynamicQuery(connName, Collections.singletonList(select.toString()));
        return dynamicQueryDtos.get(0);
    }

    /**
     * 单线程导出数据
     * @param dataQueryParam
     * @return
     */
    public ExportProcessDto exportSingleProcessor(DataQueryParam dataQueryParam) throws IOException, SQLException {
        String connName = dataQueryParam.getConnName();
        String sql = dataQueryParam.getFirstSql();

        File exportDir = fileManager.mkTmpDir("database/data/export/" + dataQueryParam.getTraceId());

        List<DynamicQueryDto> dynamicQueryDtos = jdbcService.executeDynamicQuery(connName,Collections.singletonList(sql));
        DynamicQueryDto dynamicQueryDto = dynamicQueryDtos.get(0);
        File excelPartFile = new File(exportDir, dataQueryParam.getTraceId()+ ".xlsx");
        LogUtil.info("Excel 文件 :{}",excelPartFile.getName());

        Workbook workbook = new SXSSFWorkbook(1000);
        Sheet sheet = workbook.createSheet(connName);
        FileOutputStream fileOutputStream = new FileOutputStream(excelPartFile);
        fillExcelSheet(dynamicQueryDto,sheet);
        workbook.write(fileOutputStream);

        Path path = fileManager.relativePath(exportDir.toPath());

        return new ExportProcessDto(path.toString(),1,1);
    }

    // 单线程导出最大数据量
    static final int exportPerLimit = 10000;

    // 数据导出线程池 私用
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,10,0, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(100),new NamedThreadFactory("exportExcel"));
    /**
     * 多线程导出数据
     * @param dataQueryParam
     * @throws IOException
     * @throws SQLException
     * @return
     */
    public ExportProcessDto exportLowMemoryMutiProcessor(DataQueryParam dataQueryParam) throws IOException, SQLException, JSQLParserException {
        String connName = dataQueryParam.getConnName();
        String sql = dataQueryParam.getFirstSql();

        // 查询数据总数
        String countSql = "select count(*) from (" + sql + ") b";
        Long dataCount = jdbcService.executeQuery(connName, countSql, new ScalarHandler<Long>(1));

        if(dataCount < exportPerLimit){
            return exportSingleProcessor(dataQueryParam);
        }

        //计算线程数
        final int threadCount = (int) ((dataCount - 1) / exportPerLimit + 1);

	    LogUtil.info("启用多线程进度导出:{}",dataQueryParam.getTraceId());

        //创建临时目录
        File exportDir = fileManager.mkTmpDir("database/data/export/"+dataQueryParam.getTraceId());
	    LogUtil.info("临时文件将输出到此目录:{}",exportDir);

        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

        //多线程导出; 分每批 10 万 生成多个 Excel,每个 Excel 生成后;  释放到临时文件中,释放内存,最后将整个目录使用 zip 打包
        for (int i = 0; i < threadCount; i++) {
            int currentBatch = i;
            final long begin = currentBatch * exportPerLimit;
            long end = (currentBatch + 1) * exportPerLimit;
            if(end > dataCount){
                end = dataCount;
            }
            final long  finalEnd = end;
            Limit limit = new Limit();
            limit.setOffset(begin);
            limit.setRowCount(end);
            plainSelect.setLimit(limit);
            final String currentSql = select.toString();

            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    FileOutputStream fileOutputStream = null;
                    try {
                        List<DynamicQueryDto> dynamicQueryDtos = jdbcService.executeDynamicQuery(connName,Collections.singletonList(currentSql));
                        DynamicQueryDto dynamicQueryDto = dynamicQueryDtos.get(0);
                        File excelPartFile = new File(exportDir, dataQueryParam.getTraceId()+"_" + begin + "~" + finalEnd + ".xlsx");
                        LogUtil.info("Excel 部分文件 :{}",excelPartFile.getName());

                        Workbook workbook = new SXSSFWorkbook(1000);
                        Sheet sheet = workbook.createSheet(connName +  "_" + begin + "~" + finalEnd);
                        fileOutputStream = new FileOutputStream(excelPartFile);
                        fillExcelSheet(dynamicQueryDto,sheet);
                        workbook.write(fileOutputStream);
                    } catch (Exception e) {
	                    LogUtil.error("exportLowMemoryMutiProcessor() error : {}",e.getMessage(),e);
                    } finally {
                        IOUtils.closeQuietly(fileOutputStream);
                    }
                }
            });

        }

        Path path = fileManager.relativePath(exportDir.toPath());
        ExportProcessDto exportProcessDto = new ExportProcessDto(path.toString(), 0, dataCount);
        return exportProcessDto;
    }

    /**
     * 填充 excel sheet 页
     * @param session
     * @param sqlExecuteResult
     * @param sheet
     */
    public static final float BASE_HEIGHT_1_PX = 15.625f;
    private void fillExcelSheet( DynamicQueryDto sqlExecuteResult, Sheet sheet) {
        Row headRow = sheet.createRow(0);
        headRow.setHeight((short)(30 * BASE_HEIGHT_1_PX));
        //创建标题列
        List<DynamicQueryDto.Header> headers = sqlExecuteResult.getHeaders();

        for (int i = 0; i < headers.size(); i++) {
            DynamicQueryDto.Header header = headers.get(i);
            Cell headCell = headRow.createCell(i);
            headCell.setCellValue(header.getColumnName());
            headCell.setCellType(CellType.STRING);
        }
        //创建数据列
        List<Map<String,Object>> rows = sqlExecuteResult.getRows();

        for (int i = 0; i < rows.size(); i++) {
            //设置进度
            Map<String,Object> objects = rows.get(i);
            Row dataRow = sheet.createRow(i + 1);
            for (int j = 0; j < objects.size(); j++) {
                DynamicQueryDto.Header colTypeHeader = headers.get(j);
                String colType = colTypeHeader.getTypeName();
                String columnName = colTypeHeader.getColumnName();

                Cell cell = dataRow.createCell(j);
                Object value = objects.get(columnName);

                if(value == null){
                    // 空值
                    cell.setCellType(CellType.STRING);
                    continue;
                }
                if("char".equalsIgnoreCase(colType) || "varchar".equalsIgnoreCase(colType)) {
                    cell.setCellValue(String.valueOf(value));
                    cell.setCellType(CellType.STRING);
                }else if ("datetime".equalsIgnoreCase(colType)){
                    cell.setCellType(CellType.STRING);
                    Timestamp timestamp = (Timestamp) value;
                    long time = timestamp.getTime();
                    String format = DateFormatUtils.ISO_DATE_FORMAT.format(time);
                    cell.setCellValue(format);
                }else if("int".equalsIgnoreCase(colType) || "decimal".equalsIgnoreCase(colType) || "bigint".equalsIgnoreCase(colType)){
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(NumberUtils.toLong(String.valueOf(value)));
                }else if ("date".equalsIgnoreCase(colType) || "TIMESTAMP".equalsIgnoreCase(colType)){
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(String.valueOf(value));
                }else if("TINYINT".equalsIgnoreCase(colType)){
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(String.valueOf(value));
                }else {
	                LogUtil.error("不支持的数据库类型,需要添加类型支持:{},value:{}",colType,value);
                }
            }
        }

        //设置列宽; 自动列宽
//        for (int i = 0; i < headers.size(); i++) {
//            sheet.autoSizeColumn(i);
//        }
    }

//    @PostConstruct
//    public void register(){
//        pluginManager.register(PluginDto.builder()
//                .module(MODULE).name("dataExport").author("sanri").envs("default")
//                .logo("mysql.jpg")
//                .desc("数据导出功能")
//                .build());
//    }
}
