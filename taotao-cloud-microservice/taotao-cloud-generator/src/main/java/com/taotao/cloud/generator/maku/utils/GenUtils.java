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

package com.taotao.cloud.generator.maku.utils;

import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.taotao.cloud.generator.maku.common.exception.ServerException;
import com.taotao.cloud.generator.maku.config.DbType;
import com.taotao.cloud.generator.maku.config.GenDataSource;
import com.taotao.cloud.generator.maku.config.query.AbstractQuery;
import com.taotao.cloud.generator.maku.entity.TableEntity;
import com.taotao.cloud.generator.maku.entity.TableFieldEntity;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码生成器 工具类
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Slf4j
public class GenUtils {

    /**
     * 根据数据源，获取全部数据表
     *
     * @param datasource 数据源
     */
    public static List<TableEntity> getTableList(GenDataSource datasource) {
        List<TableEntity> tableList = new ArrayList<>();
        try {
            AbstractQuery query = datasource.getDbQuery();

            // 查询数据
            PreparedStatement preparedStatement =
                    datasource.getConnection().prepareStatement(query.tableSql(null));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                TableEntity table = new TableEntity();
                table.setTableName(rs.getString(query.tableName()));
                table.setTableComment(rs.getString(query.tableComment()));
                table.setDatasourceId(datasource.getId());
                tableList.add(table);
            }

            datasource.getConnection().close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return tableList;
    }

    /**
     * 根据数据源，获取指定数据表
     *
     * @param datasource 数据源
     * @param tableName  表名
     */
    public static TableEntity getTable(GenDataSource datasource, String tableName) {
        try {
            AbstractQuery query = datasource.getDbQuery();

            // 查询数据
            PreparedStatement preparedStatement =
                    datasource.getConnection().prepareStatement(query.tableSql(tableName));
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                TableEntity table = new TableEntity();
                table.setTableName(rs.getString(query.tableName()));
                table.setTableComment(rs.getString(query.tableComment()));
                table.setDatasourceId(datasource.getId());
                return table;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        throw new ServerException("数据表不存在：" + tableName);
    }

    /**
     * 获取表字段列表
     *
     * @param datasource 数据源
     * @param tableId    表ID
     * @param tableName  表名
     */
    public static List<TableFieldEntity> getTableFieldList(
            GenDataSource datasource, Long tableId, String tableName) {
        List<TableFieldEntity> tableFieldList = new ArrayList<>();

        try {
            AbstractQuery query = datasource.getDbQuery();
            String tableFieldsSql = query.tableFieldsSql();
            if (datasource.getDbType() == DbType.Oracle) {
                DatabaseMetaData md = datasource.getConnection().getMetaData();
                tableFieldsSql =
                        String.format(
                                tableFieldsSql.replace("#schema", md.getUserName()), tableName);
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            }
            PreparedStatement preparedStatement =
                    datasource.getConnection().prepareStatement(tableFieldsSql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                TableFieldEntity field = new TableFieldEntity();
                field.setTableId(tableId);
                field.setFieldName(rs.getString(query.fieldName()));
                String fieldType = rs.getString(query.fieldType());
                if (fieldType.contains(" ")) {
                    fieldType = fieldType.substring(0, fieldType.indexOf(" "));
                }
                field.setFieldType(fieldType);
                field.setFieldComment(rs.getString(query.fieldComment()));
                String key = rs.getString(query.fieldKey());
                field.setPrimaryPk(StringUtils.isNotBlank(key) && "PRI".equalsIgnoreCase(key));

                tableFieldList.add(field);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return tableFieldList;
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        return StrUtil.subAfter(packageName, ".", true);
    }

    /**
     * 获取功能名，默认使用表名作为功能名
     *
     * @param tableName 表名
     * @return 功能名
     */
    public static String getFunctionName(String tableName) {
        return tableName;
    }

    /**
     * 表名转驼峰并移除前后缀
     *
     * @param upperFirst   首字母大写
     * @param tableName    表名
     * @param removePrefix 删除前缀
     * @param removeSuffix 删除后缀
     * @return java.lang.String
     */
    public static String camelCase(
            boolean upperFirst, String tableName, String removePrefix, String removeSuffix) {
        String className = tableName;
        // 移除前缀
        if (StrUtil.isNotBlank(removePrefix)) {
            className = StrUtil.removePrefix(tableName, removePrefix);
        }
        // 移除后缀
        if (StrUtil.isNotBlank(removeSuffix)) {
            className = StrUtil.removeSuffix(className, removeSuffix);
        }
        // 是否首字母大写
        if (upperFirst) {
            return NamingCase.toPascalCase(className);
        } else {
            return NamingCase.toCamelCase(className);
        }
    }
}
