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

package com.taotao.cloud.generator.maku.service.impl;

import cn.hutool.core.text.NamingCase;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.cloud.generator.maku.common.exception.ServerException;
import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.service.impl.BaseServiceImpl;
import com.taotao.cloud.generator.maku.config.GenDataSource;
import com.taotao.cloud.generator.maku.config.template.GeneratorConfig;
import com.taotao.cloud.generator.maku.config.template.GeneratorInfo;
import com.taotao.cloud.generator.maku.dao.TableDao;
import com.taotao.cloud.generator.maku.entity.TableEntity;
import com.taotao.cloud.generator.maku.entity.TableFieldEntity;
import com.taotao.cloud.generator.maku.enums.FormLayoutEnum;
import com.taotao.cloud.generator.maku.enums.GeneratorTypeEnum;
import com.taotao.cloud.generator.maku.service.DataSourceService;
import com.taotao.cloud.generator.maku.service.TableFieldService;
import com.taotao.cloud.generator.maku.service.TableService;
import com.taotao.cloud.generator.maku.utils.GenUtils;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
@AllArgsConstructor
public class TableServiceImpl extends BaseServiceImpl<TableDao, TableEntity>
        implements TableService {
    private final TableFieldService tableFieldService;
    private final DataSourceService dataSourceService;
    private final GeneratorConfig generatorConfig;

    @Override
    public PageResult<TableEntity> page(Query query) {
        IPage<TableEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public TableEntity getByTableName(String tableName) {
        LambdaQueryWrapper<TableEntity> queryWrapper = Wrappers.lambdaQuery();
        return baseMapper.selectOne(queryWrapper.eq(TableEntity::getTableName, tableName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchIds(Long[] ids) {
        // 删除表
        baseMapper.deleteBatchIds(Arrays.asList(ids));

        // 删除列
        tableFieldService.deleteBatchTableIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tableImport(Long datasourceId, String tableName) {
        // 初始化配置信息
        GenDataSource dataSource = dataSourceService.get(datasourceId);
        // 查询表是否存在
        TableEntity table = this.getByTableName(tableName);
        // 表存在
        if (table != null) {
            throw new ServerException(tableName + "已存在");
        }

        // 从数据库获取表信息
        table = GenUtils.getTable(dataSource, tableName);

        // 代码生成器信息
        GeneratorInfo generator = generatorConfig.getGeneratorConfig();

        // 保存表信息
        table.setPackageName(generator.getProject().getPackageName());
        table.setVersion(generator.getProject().getVersion());
        table.setBackendPath(generator.getProject().getBackendPath());
        table.setFrontendPath(generator.getProject().getFrontendPath());
        table.setAuthor(generator.getDeveloper().getAuthor());
        table.setEmail(generator.getDeveloper().getEmail());
        table.setFormLayout(FormLayoutEnum.ONE.getValue());
        table.setGeneratorType(GeneratorTypeEnum.ZIP_DOWNLOAD.ordinal());
        table.setClassName(NamingCase.toPascalCase(tableName));
        table.setModuleName(GenUtils.getModuleName(table.getPackageName()));
        table.setFunctionName(GenUtils.getFunctionName(tableName));
        table.setCreateTime(new Date());
        this.save(table);

        // 获取原生字段数据
        List<TableFieldEntity> tableFieldList =
                GenUtils.getTableFieldList(dataSource, table.getId(), table.getTableName());
        // 初始化字段数据
        tableFieldService.initFieldList(tableFieldList);

        // 保存列数据
        tableFieldList.forEach(tableFieldService::save);

        try {
            // 释放数据源
            dataSource.getConnection().close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sync(Long id) {
        TableEntity table = this.getById(id);

        // 初始化配置信息
        GenDataSource datasource = dataSourceService.get(table.getDatasourceId());

        // 从数据库获取表字段列表
        List<TableFieldEntity> dbTableFieldList =
                GenUtils.getTableFieldList(datasource, table.getId(), table.getTableName());
        if (dbTableFieldList.size() == 0) {
            throw new ServerException("同步失败，请检查数据库表：" + table.getTableName());
        }

        List<String> dbTableFieldNameList =
                dbTableFieldList.stream()
                        .map(TableFieldEntity::getFieldName)
                        .collect(Collectors.toList());

        // 表字段列表
        List<TableFieldEntity> tableFieldList = tableFieldService.getByTableId(id);

        Map<String, TableFieldEntity> tableFieldMap =
                tableFieldList.stream()
                        .collect(
                                Collectors.toMap(
                                        TableFieldEntity::getFieldName, Function.identity()));

        // 初始化字段数据
        tableFieldService.initFieldList(dbTableFieldList);

        // 同步表结构字段
        dbTableFieldList.forEach(
                field -> {
                    // 新增字段
                    if (!tableFieldMap.containsKey(field.getFieldName())) {
                        tableFieldService.save(field);
                        return;
                    }

                    // 修改字段
                    TableFieldEntity updateField = tableFieldMap.get(field.getFieldName());
                    updateField.setPrimaryPk(field.isPrimaryPk());
                    updateField.setFieldComment(field.getFieldComment());
                    updateField.setFieldType(field.getFieldType());
                    updateField.setAttrType(field.getAttrType());

                    tableFieldService.updateById(updateField);
                });

        // 删除数据库表中没有的字段
        List<TableFieldEntity> delFieldList =
                tableFieldList.stream()
                        .filter(field -> !dbTableFieldNameList.contains(field.getFieldName()))
                        .collect(Collectors.toList());
        if (delFieldList.size() > 0) {
            List<Long> fieldIds =
                    delFieldList.stream().map(TableFieldEntity::getId).collect(Collectors.toList());
            tableFieldService.removeBatchByIds(fieldIds);
        }
    }
}
