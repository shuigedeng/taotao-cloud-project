package com.taotao.cloud.sys.biz.api.controller.tools.database.controller;

import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.DictDto;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.DataExportService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.JdbcDataService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.TableDataService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.TableSearchService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.data.DirtyDataInfo;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.data.TableDataParam;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.search.SearchParam;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据库数据管理
 */
@RestController
@RequestMapping("/db/data")
@Validated
public class DataController {
    @Autowired
    private JdbcDataService jdbcDataService;
    @Autowired
    private TableSearchService tableSearchService;
    @Autowired
    private TableDataService tableDataService;
    @Autowired
    private DataExportService dataExportService;

    /**
     * 清空表数据
     * @param connName
     * @param actualTableName
     * @return
     */
    @PostMapping("/emptyTable")
    public int emptyTable(@NotNull String connName, @Valid ActualTableName actualTableName) throws IOException, SQLException {
        int emptyTable = tableDataService.emptyTable(connName, actualTableName);
        return emptyTable;
    }

    /**
     * 根据表关系, 进行脏数据检查
     * @param connName
     * @param namespace
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/checkDirtyData")
    public List<DirtyDataInfo> checkDirtyData(@NotNull String connName, Namespace namespace) throws IOException, SQLException {
        return tableDataService.checkDirtyData(connName,namespace);
    }

    /**
     * 获取清除所有业务表数据 sql
     * @return
     */
    @GetMapping("/cleanTagTables")
    public List<String> cleanTagTables(@NotNull String connName, SearchParam searchParam) throws SQLException, IOException {
        List<TableMeta> tagTables = tableSearchService.searchTables(connName,searchParam);
        List<String> sqls = new ArrayList<>();
        for (TableMeta tableMetaData : tagTables) {
            ActualTableName actualTableName = tableMetaData.getTable().getActualTableName();
            String tableName = actualTableName.getSchema()+"."+actualTableName.getTableName();
            sqls.add("truncate "+tableName);
        }

        return sqls;
    }

    /**
     * 单表随机数据生成
     * @param tableDataParam
     */
    @PostMapping("/singleTableRandomData")
    public void singleTableRandomData(@RequestBody @Valid TableDataParam tableDataParam) throws IOException, SQLException, JSQLParserException {
        // 如果 mapper 中有 spel 简写, 替换成 spel
        final Map<String, DictDto<String>> collect = dictDtos.stream().collect(Collectors.toMap(DictDto::getKey, Function.identity()));
        final List<TableDataParam.ColumnMapper> columnMappers = tableDataParam.getColumnMappers();
        for (TableDataParam.ColumnMapper columnMapper : columnMappers) {
            final String random = columnMapper.getRandom();
            if (collect.containsKey(random)){
                columnMapper.setRandom(collect.get(random).getValue());
            }
        }
        tableDataService.singleTableWriteRandomData(tableDataParam);
    }

    /**
     * 导入数据
     * @param file
     * @throws IOException
     */
    @PostMapping("/import/excel")
    public void importDataFromExcel(@RequestPart("config") @Valid ExcelImportParam excelImportParam, @RequestPart("excel") MultipartFile multipartFile) throws IOException, SQLException {
        tableDataService.importDataFromExcel(excelImportParam,multipartFile);
    }

    /**
     *  数据预览 , 为避免 sql 语句被编码 , 使用 post 请求数据放在请求体
     * @param dataQueryParam
     * @return
     * @throws IOException
     * @throws SQLException
     * @throws JSQLParserException
     */
    @PostMapping("/exportPreview")
    public ExportPreviewDto exportPreview(@RequestBody @Valid DataQueryParam dataQueryParam) throws IOException, SQLException, JSQLParserException {
        DynamicQueryDto dynamicQueryDto = dataExportService.exportPreview(dataQueryParam);
        String connName = dataQueryParam.getConnName();
        String sql = dataQueryParam.getFirstSql();

        // 数据总数查询
        String countSql = "select count(*) from (" + sql + ") b";
        Long executeQuery = jdbcDataService.executeQuery(connName, countSql, new ScalarHandler<Long>(1), dataQueryParam.getNamespace());
        ExportPreviewDto exportPreviewDto = new ExportPreviewDto(dynamicQueryDto, executeQuery);
        return exportPreviewDto;
    }

    /**
     * 导出数据为 csv 格式,导出进度会写入指定 key , 可查询导出进度
     * 当数据量过大时使用多线程导出
     * @param connName connName
     * @param sql sql
     * @return
     */
    @PostMapping("/exportData")
    public ExportProcessDto exportData(@RequestBody @Valid DataQueryParam dataQueryParam) throws JSQLParserException, SQLException, IOException {
        ExportProcessDto fileRelativePath = dataExportService.exportLowMemoryMutiProcessor(dataQueryParam);
        return fileRelativePath;
    }

    /**
     * 执行查询 sql
     * @param dataQueryParam 数据查询参数
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @PostMapping("/executeQuery")
    public List<DynamicQueryDto> executeQuery(@RequestBody @Valid DataQueryParam dataQueryParam) throws IOException, SQLException {
        List<DynamicQueryDto> dynamicQueryDtos = jdbcDataService.executeDynamicQuery(dataQueryParam.getConnName(), dataQueryParam.getSqls(),dataQueryParam.getNamespace());
        return dynamicQueryDtos;
    }

    /**
     * 执行更新操作, 包含 ddl
     * @param dataQueryParam dataQueryParam
     * @return
     * @throws SQLException
     */
    @PostMapping("/executeUpdate")
    public List<Integer> executeUpdate(@RequestBody @Validated DataQueryParam dataQueryParam) throws SQLException, IOException {
        List<Integer> updates = jdbcDataService.executeUpdate(dataQueryParam.getConnName(), dataQueryParam.getSqls(),dataQueryParam.getNamespace());
        return updates;
    }

    /**
     * 生成数据变更可重复执行 sql
     * @param dataChangeParam
     * @return
     */
    @PostMapping("/dataChangeSqls")
    public List<SqlList> generateDataChangeSqls(@RequestBody @Validated DataChangeParam dataChangeParam) throws IOException, SQLException {
        return tableDataService.generateDataChangeSqls(dataChangeParam);
    }

    /**
     * 随机值方法列表
     * @return
     */
    @GetMapping("/loadRandomMethods")
    public List<DictDto<String>> loadRandomMethods(){
        return dictDtos;
    }

    /**
     * 中文说明 => spel 表达式
     */
    static final List<DictDto<String>> dictDtos = new ArrayList<>();
    static {
        final Method[] methods1 = ReflectionUtils.getAllDeclaredMethods(RandomUtil.class);
        final Method[] methods2 = ReflectionUtils.getAllDeclaredMethods(RandomStringUtils.class);
        final Method[] methods3 = ReflectionUtils.getAllDeclaredMethods(RandomUtils.class);
        Method[] allDeclaredMethods = new Method[]{};
        allDeclaredMethods = ArrayUtils.addAll(methods1, methods2);
        allDeclaredMethods =  ArrayUtils.addAll(allDeclaredMethods, methods3);

        for (Method method : allDeclaredMethods) {
            if (method.getDeclaringClass() == Object.class){
                // 不要取 Object 中的方法
                continue;
            }
            if (Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0){
                // 没有参数的静态方法, 可以直接被调用
                StringBuffer methodSpel = new StringBuffer();
                methodSpel.append("T").append("(").append(method.getDeclaringClass().getName()).append(")")
                        .append(".").append(method.getName()).append("()");
                dictDtos.add(new DictDto<>(method.getName(),methodSpel.toString()));
            }
//            else if (Modifier.isStatic(method.getModifiers()) && method.getName().contains("next") && method.getParameterCount() == 2){
//                // nextInt , nextFloat 之类的方法
//                StringBuffer methodSpel = new StringBuffer();
//                methodSpel.append("T").append("(").append(method.getDeclaringClass().getName()).append(")")
//                        .append(".").append(method.getName()).append("(2,10)");
//                dictDtos.add(new DictDto<>(method.getName()+"(2,10)",methodSpel.toString()));
//            }
        }
    }
}
