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

package com.taotao.cloud.generator.maku.controller;

import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.utils.Result;
import com.taotao.cloud.generator.maku.entity.TableEntity;
import com.taotao.cloud.generator.maku.entity.TableFieldEntity;
import com.taotao.cloud.generator.maku.service.TableFieldService;
import com.taotao.cloud.generator.maku.service.TableService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 数据表管理
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("maku-generator/gen/table")
@AllArgsConstructor
public class TableController {
    private final TableService tableService;
    private final TableFieldService tableFieldService;

    /**
     * 分页
     *
     * @param query 查询参数
     */
    @GetMapping("page")
    public Result<PageResult<TableEntity>> page(Query query) {
        PageResult<TableEntity> page = tableService.page(query);

        return Result.ok(page);
    }

    /**
     * 获取表信息
     *
     * @param id 表ID
     */
    @GetMapping("{id}")
    public Result<TableEntity> get(@PathVariable("id") Long id) {
        TableEntity table = tableService.getById(id);

        // 获取表的字段
        List<TableFieldEntity> fieldList = tableFieldService.getByTableId(table.getId());
        table.setFieldList(fieldList);

        return Result.ok(table);
    }

    /**
     * 修改
     *
     * @param table 表信息
     */
    @PutMapping
    public Result<String> update(@RequestBody TableEntity table) {
        tableService.updateById(table);

        return Result.ok();
    }

    /**
     * 删除
     *
     * @param ids 表id数组
     */
    @DeleteMapping
    public Result<String> delete(@RequestBody Long[] ids) {
        tableService.deleteBatchIds(ids);

        return Result.ok();
    }

    /**
     * 同步表结构
     *
     * @param id 表ID
     */
    @PostMapping("sync/{id}")
    public Result<String> sync(@PathVariable("id") Long id) {
        tableService.sync(id);

        return Result.ok();
    }

    /**
     * 导入数据源中的表
     *
     * @param datasourceId  数据源ID
     * @param tableNameList 表名列表
     */
    @PostMapping("import/{datasourceId}")
    public Result<String> tableImport(
            @PathVariable("datasourceId") Long datasourceId,
            @RequestBody List<String> tableNameList) {
        for (String tableName : tableNameList) {
            tableService.tableImport(datasourceId, tableName);
        }

        return Result.ok();
    }

    /**
     * 修改表字段数据
     *
     * @param tableId        表ID
     * @param tableFieldList 字段列表
     */
    @PutMapping("field/{tableId}")
    public Result<String> updateTableField(
            @PathVariable("tableId") Long tableId,
            @RequestBody List<TableFieldEntity> tableFieldList) {
        tableFieldService.updateTableField(tableId, tableFieldList);

        return Result.ok();
    }
}
