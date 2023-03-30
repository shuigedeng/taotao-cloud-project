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

package com.taotao.cloud.generator.biz.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.generator.biz.entity.GenTable;
import com.taotao.cloud.generator.biz.entity.GenTableColumn;
import com.taotao.cloud.generator.biz.page.PageQuery;
import com.taotao.cloud.generator.biz.page.TableDataInfo;
import com.taotao.cloud.generator.biz.service.IGenTableService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代码生成 操作处理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RequestMapping("/gen")
@RestController
public class GenController {

    private final IGenTableService genTableService;

    /** 查询代码生成列表 */
    // @SaCheckPermission("tool:gen:list")
    @GetMapping("/list")
    public TableDataInfo<GenTable> genList(GenTable genTable, PageQuery pageQuery) {
        return genTableService.selectPageGenTableList(genTable, pageQuery);
    }

    /**
     * 修改代码生成业务
     *
     * @param tableId 表主键
     */
    // @SaCheckPermission("tool:gen:query")
    @GetMapping(value = "/{tableId}")
    public Result<Map<String, Object>> getInfo(@PathVariable Long tableId) {
        GenTable table = genTableService.selectGenTableById(tableId);
        List<GenTable> tables = genTableService.selectGenTableAll();
        List<GenTableColumn> list = genTableService.selectGenTableColumnListByTableId(tableId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("info", table);
        map.put("rows", list);
        map.put("tables", tables);
        return Result.success(map);
    }

    /** 查询数据库列表 */
    // @SaCheckPermission("tool:gen:list")
    @GetMapping("/db/list")
    public TableDataInfo<GenTable> dataList(GenTable genTable, PageQuery pageQuery) {
        return genTableService.selectPageDbTableList(genTable, pageQuery);
    }

    /**
     * 查询数据表字段列表
     *
     * @param tableId 表主键
     */
    @GetMapping(value = "/column/{tableId}")
    public TableDataInfo<GenTableColumn> columnList(Long tableId) {
        TableDataInfo<GenTableColumn> dataInfo = new TableDataInfo<>();
        List<GenTableColumn> list = genTableService.selectGenTableColumnListByTableId(tableId);
        dataInfo.setRows(list);
        dataInfo.setTotal(list.size());
        return dataInfo;
    }

    /**
     * 导入表结构（保存）
     *
     * @param tables 表名串
     */
    // @SaCheckPermission("tool:gen:import")
    // @Log(title = "代码生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importTable")
    public Result<Boolean> importTableSave(String tables) {
        String[] tableNames = Convert.toStrArray(tables);
        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList);
        return Result.success(true);
    }

    /** 修改保存代码生成业务 */
    // @SaCheckPermission("tool:gen:edit")
    // @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Boolean> editSave(@Validated @RequestBody GenTable genTable) {
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        return Result.success(true);
    }

    /**
     * 删除代码生成
     *
     * @param tableIds 表主键串
     */
    // @SaCheckPermission("tool:gen:remove")
    // @Log(title = "代码生成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableIds}")
    public Result<Boolean> remove(@PathVariable Long[] tableIds) {
        genTableService.deleteGenTableByIds(tableIds);
        return Result.success(true);
    }

    /**
     * 预览代码
     *
     * @param tableId 表主键
     */
    // @SaCheckPermission("tool:gen:preview")
    @GetMapping("/preview/{tableId}")
    public Result<Map<String, String>> preview(@PathVariable("tableId") Long tableId)
            throws IOException {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return Result.success(dataMap);
    }

    /**
     * 生成代码（下载方式）
     *
     * @param tableName 表名
     */
    // @SaCheckPermission("tool:gen:code")
    // @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName)
            throws IOException {
        byte[] data = genTableService.downloadCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     *
     * @param tableName 表名
     */
    // @SaCheckPermission("tool:gen:code")
    // @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/genCode/{tableName}")
    public Result<Boolean> genCode(@PathVariable("tableName") String tableName) {
        genTableService.generatorCode(tableName);
        return Result.success(true);
    }

    /**
     * 同步数据库
     *
     * @param tableName 表名
     */
    // @SaCheckPermission("tool:gen:edit")
    // @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @GetMapping("/synchDb/{tableName}")
    public Result<Boolean> synchDb(@PathVariable("tableName") String tableName) {
        genTableService.synchDb(tableName);
        return Result.success(true);
    }

    /**
     * 批量生成代码
     *
     * @param tables 表名串
     */
    // @SaCheckPermission("tool:gen:code")
    // @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
    }

    /** 生成zip文件 */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), false, data);
    }
}
