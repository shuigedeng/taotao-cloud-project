package com.taotao.cloud.generator.maku.service.impl;

import cn.hutool.core.text.NamingCase;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.maku.generator.common.exception.ServerException;
import net.maku.generator.common.page.PageResult;
import net.maku.generator.common.query.Query;
import net.maku.generator.common.service.impl.BaseServiceImpl;
import net.maku.generator.config.GenDataSource;
import net.maku.generator.config.template.GeneratorConfig;
import net.maku.generator.config.template.GeneratorInfo;
import net.maku.generator.dao.TableDao;
import net.maku.generator.entity.TableEntity;
import net.maku.generator.entity.TableFieldEntity;
import net.maku.generator.enums.FormLayoutEnum;
import net.maku.generator.enums.GeneratorTypeEnum;
import net.maku.generator.service.DataSourceService;
import net.maku.generator.service.TableFieldService;
import net.maku.generator.service.TableService;
import net.maku.generator.utils.GenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 数据表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
@AllArgsConstructor
public class TableServiceImpl extends BaseServiceImpl<TableDao, TableEntity> implements TableService {
    private final TableFieldService tableFieldService;
    private final DataSourceService dataSourceService;
    private final GeneratorConfig generatorConfig;

    @Override
    public PageResult<TableEntity> page(Query query) {
        IPage<TableEntity> page = baseMapper.selectPage(
                getPage(query),
                getWrapper(query)
        );
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
        List<TableFieldEntity> tableFieldList = GenUtils.getTableFieldList(dataSource, table.getId(), table.getTableName());
        // 初始化字段数据
        tableFieldService.initFieldList(tableFieldList);

        // 保存列数据
        tableFieldList.forEach(tableFieldService::save);

        try {
            //释放数据源
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
        List<TableFieldEntity> dbTableFieldList = GenUtils.getTableFieldList(datasource, table.getId(), table.getTableName());
        if (dbTableFieldList.size() == 0) {
            throw new ServerException("同步失败，请检查数据库表：" + table.getTableName());
        }

        List<String> dbTableFieldNameList = dbTableFieldList.stream().map(TableFieldEntity::getFieldName).collect(Collectors.toList());

        // 表字段列表
        List<TableFieldEntity> tableFieldList = tableFieldService.getByTableId(id);

        Map<String, TableFieldEntity> tableFieldMap = tableFieldList.stream().collect(Collectors.toMap(TableFieldEntity::getFieldName, Function.identity()));

        // 初始化字段数据
        tableFieldService.initFieldList(dbTableFieldList);

        // 同步表结构字段
        dbTableFieldList.forEach(field -> {
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
        List<TableFieldEntity> delFieldList = tableFieldList.stream().filter(field -> !dbTableFieldNameList.contains(field.getFieldName())).collect(Collectors.toList());
        if (delFieldList.size() > 0) {
            List<Long> fieldIds = delFieldList.stream().map(TableFieldEntity::getId).collect(Collectors.toList());
            tableFieldService.removeBatchByIds(fieldIds);
        }
    }
}
