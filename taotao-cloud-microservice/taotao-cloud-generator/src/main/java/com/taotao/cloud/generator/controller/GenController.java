/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.generator.controller;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.fastjson.JSON;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.generator.entity.GenTable;
import com.taotao.cloud.generator.entity.GenTableColumn;
import com.taotao.cloud.generator.page.TableDataInfo;
import com.taotao.cloud.generator.service.IGenTableColumnService;
import com.taotao.cloud.generator.service.IGenTableService;
import com.taotao.cloud.generator.util.CxSelect;
import com.taotao.cloud.generator.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.dromara.hutool.core.convert.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 代码生成 操作处理
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/tool/gen")
public class GenController {

    private String prefix = "tool/gen";

    @Autowired private IGenTableService genTableService;

    @Autowired private IGenTableColumnService genTableColumnService;

    @GetMapping()
    public String gen() {
        return prefix + "/gen";
    }

    /**
     * 查询代码生成列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo genList(GenTable genTable) {
        //		startPage();
        //		List<GenTable> list = genTableService.selectGenTableList(genTable);
        //		return getDataTable(list);
        return null;
    }

    /**
     * 查询数据库列表
     */
    @PostMapping("/db/list")
    @ResponseBody
    public TableDataInfo dataList(GenTable genTable) {
        //		startPage();
        //		List<GenTable> list = genTableService.selectDbTableList(genTable);
        //		return getDataTable(list);
        return null;
    }

    /**
     * 查询数据表字段列表
     */
    @PostMapping("/column/list")
    @ResponseBody
    public TableDataInfo columnList(GenTableColumn genTableColumn) {
        TableDataInfo dataInfo = new TableDataInfo();
        List<GenTableColumn> list =
                genTableColumnService.selectGenTableColumnListByTableId(genTableColumn);
        dataInfo.setRows(list);
        dataInfo.setTotal(list.size());
        return dataInfo;
    }

    /**
     * 导入表结构
     */
    @GetMapping("/importTable")
    public String importTable() {
        return prefix + "/importTable";
    }

    /**
     * 创建表结构
     */
    @GetMapping("/createTable")
    public String createTable() {
        return prefix + "/createTable";
    }

    /**
     * 导入表结构（保存）
     */
    @PostMapping("/importTable")
    @ResponseBody
    public Result<Boolean> importTableSave(String tables) {
        String[] tableNames = ConvertUtil.toStrArray(tables);
        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        //		String operName = Convert.toStr(PermissionUtils.getPrincipalProperty("loginName"));
        //		genTableService.importGenTable(tableList, operName);
        return Result.success();
    }

    /**
     * 修改代码生成业务
     */
    @GetMapping("/edit/{tableId}")
    public String edit(@PathVariable("tableId") Long tableId, ModelMap mmap) {
        GenTable table = genTableService.selectGenTableById(tableId);
        List<GenTable> genTables = genTableService.selectGenTableAll();
        List<CxSelect> cxSelect = new ArrayList<CxSelect>();
        for (GenTable genTable : genTables) {
            if (!StringUtils.equals(table.getTableName(), genTable.getTableName())) {
                CxSelect cxTable =
                        new CxSelect(
                                genTable.getTableName(),
                                genTable.getTableName() + '：' + genTable.getTableComment());
                List<CxSelect> cxColumns = new ArrayList<CxSelect>();
                for (GenTableColumn tableColumn : genTable.getColumns()) {
                    cxColumns.add(
                            new CxSelect(
                                    tableColumn.getColumnName(),
                                    tableColumn.getColumnName()
                                            + '：'
                                            + tableColumn.getColumnComment()));
                }
                cxTable.setS(cxColumns);
                cxSelect.add(cxTable);
            }
        }
        mmap.put("table", table);
        mmap.put("data", JSON.toJSON(cxSelect));
        return prefix + "/edit";
    }

    /**
     * 修改保存代码生成业务
     */
    @PostMapping("/edit")
    @ResponseBody
    public Result editSave(@Validated GenTable genTable) {
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        return Result.success();
    }

    @PostMapping("/remove")
    @ResponseBody
    public Result remove(String ids) {
        genTableService.deleteGenTableByIds(ids);
        return Result.success();
    }

    @PostMapping("/createTable")
    @ResponseBody
    public Result create(String sql) {
        try {
            //			SqlUtil.filterKeyword(sql);
            List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
            List<String> tableNames = new ArrayList<>();
            for (SQLStatement sqlStatement : sqlStatements) {
                if (sqlStatement instanceof MySqlCreateTableStatement) {
                    MySqlCreateTableStatement createTableStatement =
                            (MySqlCreateTableStatement) sqlStatement;
                    if (genTableService.createTable(createTableStatement.toString())) {
                        String tableName = createTableStatement.getTableName().replaceAll("`", "");
                        tableNames.add(tableName);
                    }
                }
            }
            List<GenTable> tableList =
                    genTableService.selectDbTableListByNames(
                            tableNames.toArray(new String[tableNames.size()]));
            //			String operName = Convert.toStr(PermissionUtils.getPrincipalProperty("loginName"));
            //			genTableService.importGenTable(tableList, operName);
            return Result.success();
        } catch (Exception e) {
            LogUtils.error(e.getMessage(), e);
            return Result.fail("创建表结构异常");
        }
    }

    /**
     * 预览代码
     */
    @GetMapping("/preview/{tableId}")
    @ResponseBody
    public Result preview(@PathVariable("tableId") Long tableId) throws IOException {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return Result.success(dataMap);
    }

    /**
     * 生成代码（下载方式）
     */
    @GetMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName)
            throws IOException {
        byte[] data = genTableService.downloadCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     */
    @GetMapping("/genCode/{tableName}")
    @ResponseBody
    public Result genCode(@PathVariable("tableName") String tableName) {
        genTableService.generatorCode(tableName);
        return Result.success();
    }

    /**
     * 同步数据库
     */
    @GetMapping("/synchDb/{tableName}")
    @ResponseBody
    public Result synchDb(@PathVariable("tableName") String tableName) {
        genTableService.synchDb(tableName);
        return Result.success();
    }

    /**
     * 批量生成代码
     */
    @GetMapping("/batchGenCode")
    @ResponseBody
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = ConvertUtil.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
