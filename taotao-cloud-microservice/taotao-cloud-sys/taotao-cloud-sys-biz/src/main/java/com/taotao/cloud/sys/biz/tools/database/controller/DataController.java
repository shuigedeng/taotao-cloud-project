package com.taotao.cloud.sys.biz.tools.database.controller;

import com.taotao.cloud.sys.biz.tools.database.dtos.DataQueryParam;
import com.taotao.cloud.sys.biz.tools.database.dtos.DynamicQueryDto;
import com.taotao.cloud.sys.biz.tools.database.dtos.ExcelImportParam;
import com.taotao.cloud.sys.biz.tools.database.dtos.ExportPreviewDto;
import com.taotao.cloud.sys.biz.tools.database.dtos.ExportProcessDto;
import com.taotao.cloud.sys.biz.tools.database.dtos.TableDataParam;
import com.taotao.cloud.sys.biz.tools.database.service.DataService;
import com.taotao.cloud.sys.biz.tools.database.service.JdbcService;
import com.taotao.cloud.sys.biz.tools.database.service.TableDataService;
import com.taotao.cloud.sys.biz.tools.database.service.TableMarkService;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据库数据管理
 */
@RestController
@RequestMapping("/db/data")
@Validated
public class DataController {
    @Autowired
    private TableMarkService tableMarkService;
    @Autowired
    private TableDataService tableDataService;
    @Autowired
    private JdbcService jdbcService;
    @Autowired
    private DataService dataService;

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
     * 获取清除所有业务表数据 sql
     * @return
     */
    @GetMapping("/cleanTagTables")
    public List<String> cleanTagTables(@NotNull String connName,String catalog,String[] schemas,@NotNull String tag) throws SQLException, IOException {
        Set<String> schemasSet = Arrays.stream(schemas).collect(Collectors.toSet());
        List<TableMetaData> tagTables = tableMarkService.searchTables(connName,catalog, schemasSet,tag);
        List<String> sqls = new ArrayList<>();
        for (TableMetaData tableMetaData : tagTables) {
            ActualTableName actualTableName = tableMetaData.getActualTableName();
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
        tableDataService.singleTableWriteRandomData(tableDataParam);
    }

    /**
     * 导入数据
     * @param file
     * @throws IOException
     */
    @PostMapping("/import/excel")
    public void importDataFromExcel(@RequestPart("config") @Valid ExcelImportParam excelImportParam,@RequestPart("excel") MultipartFile multipartFile) throws IOException, SQLException {
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
        DynamicQueryDto dynamicQueryDto = dataService.exportPreview(dataQueryParam);
        String connName = dataQueryParam.getConnName();
        String sql = dataQueryParam.getFirstSql();

        // 数据总数查询
        String countSql = "select count(*) from (" + sql + ") b";
        Long executeQuery = jdbcService.executeQuery(connName, countSql, new ScalarHandler<Long>(1));
        ExportPreviewDto exportPreviewDto = new ExportPreviewDto(dynamicQueryDto, executeQuery);
        return exportPreviewDto;
    }

    /**
     * 导出数据为 csv 格式,导出进度会写入指定 key , 可查询导出进度
     * 当数据量过大时使用多线程导出
     * @param connName
     * @param sql
     * @return
     */
    @PostMapping("/exportData")
    public ExportProcessDto exportData(@RequestBody @Valid DataQueryParam dataQueryParam) throws JSQLParserException, SQLException, IOException {
        ExportProcessDto fileRelativePath = dataService.exportLowMemoryMutiProcessor(dataQueryParam);
        return fileRelativePath;
    }

    /**
     * 执行查询 sql
     * @param dataQueryParam
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @PostMapping("/executeQuery")
    public List<DynamicQueryDto> executeQuery(@RequestBody @Valid DataQueryParam dataQueryParam) throws IOException, SQLException {
        List<DynamicQueryDto> dynamicQueryDtos = jdbcService.executeDynamicQuery(dataQueryParam.getConnName(), dataQueryParam.getSqls());
        return dynamicQueryDtos;
    }

    /**
     * 执行更新操作, 包含 ddl
     * @param dataQueryParam
     * @return
     * @throws SQLException
     */
    @PostMapping("/executeUpdate")
    public List<Integer> executeUpdate(@RequestBody @Valid DataQueryParam dataQueryParam) throws SQLException, IOException {
        List<Integer> updates = jdbcService.executeUpdate(dataQueryParam.getConnName(), dataQueryParam.getSqls());
        return updates;
    }
}
