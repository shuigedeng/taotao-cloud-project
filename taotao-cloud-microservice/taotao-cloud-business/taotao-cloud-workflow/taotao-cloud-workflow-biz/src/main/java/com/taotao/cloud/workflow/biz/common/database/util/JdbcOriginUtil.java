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

package com.taotao.cloud.workflow.biz.common.database.util;

import com.taotao.cloud.workflow.biz.common.database.model.DbTableFieldModel;
import com.taotao.cloud.workflow.biz.common.database.model.DbTableInfoModel;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Cleanup;

/** 类功能 */
public class JdbcOriginUtil {

    /** 获取单张表信息模板对象 */
    public static DbTableInfoModel getTableInfo(Connection conn, String table) {
        return getTableInfoAll(conn).stream()
                .filter(t -> t.getTable().equals(table))
                .findFirst()
                .orElse(null);
    }

    /** 获取连接底下所有的表模板对象 */
    public static List<DbTableInfoModel> getTableInfoAll(Connection conn) {
        List<DbTableInfoModel> dbTableInfoModelList = new ArrayList<>();
        setTableInfo(conn, dbTableInfoModelList, null);
        return dbTableInfoModelList;
    }

    /** 获取表里所有字段模板 */
    public static List<DbTableFieldModel> getFields(Connection conn, String table) {
        String primaryField = getPrimaryField(conn, table);
        List<DbFieldMod> DbFieldModList = getFieldsMeta(conn, table);
        List<DbTableFieldModel> dbTableFieldModelList = new ArrayList<>();
        DbFieldModList.forEach(x -> {
            DbTableFieldModel dbTableFieldModel = new DbTableFieldModel();
            dbTableFieldModel.setField(x.getColumnName());
            dbTableFieldModel.setDefaults(x.getColumnDefault());
            dbTableFieldModel.setIdentity(x.getIsAutoIncrement());
            dbTableFieldModel.setFieldName(x.getColumnComment());
            dbTableFieldModel.setDataType(x.getColumnTypeName());
            dbTableFieldModel.setPrimaryKey(x.getColumnName().equals(primaryField) ? 1 : 0);
            dbTableFieldModel.setAllowNull(x.getIsNull());
            dbTableFieldModel.setDataLength(x.getColumnSize());
            dbTableFieldModelList.add(dbTableFieldModel);
        });
        return dbTableFieldModelList;
    }

    /** 获取表里所有字段模板 */
    public static List<DbFieldMod> getFieldsMeta(Connection conn, String table) {
        List<DbFieldMod> dbFieldModList = new ArrayList<>();
        setFieldInfo(conn, dbFieldModList, null, table);
        return dbFieldModList;
    }

    /** 获取表里所有字段元数据对象 */
    public static List<Map<String, String>> getFieldsMap(Connection conn, String table) {
        List<Map<String, String>> mapList = new ArrayList<>();
        setFieldInfo(conn, null, mapList, table);
        return mapList;
    }

    /*============================= 内部方法 ===============================*/

    /** 获取连接底下所有的表元数据对象 */
    private static List<Map<String, String>> getTableMetaDataAll(Connection conn) {
        List<Map<String, String>> mapList = new ArrayList<>();
        setTableInfo(conn, null, mapList);
        return mapList;
    }

    private static void setTableInfo(
            Connection conn, List<DbTableInfoModel> dbTableInfoModelList, List<Map<String, String>> mapList) {
        try {
            // 从conn中获取数据库的表元数据
            @Cleanup ResultSet rs = conn.getMetaData().getTables(conn.getCatalog(), null, null, new String[] {"TABLE"});
            while (rs.next()) {
                if (dbTableInfoModelList != null) {
                    DbTableInfoModel dbTableInfoModel = new DbTableInfoModel();
                    dbTableInfoModel.setTable(rs.getString("TABLE_NAME"));
                    dbTableInfoModel.setTableType(rs.getString("TABLE_TYPE"));
                    dbTableInfoModel.setComment(rs.getString("REMARKS"));
                    dbTableInfoModelList.add(dbTableInfoModel);
                } else {
                    // 表元数据
                    ResultSetMetaData resultSetMetaData = rs.getMetaData();
                    // 遍历表所有元数据信息
                    Map<String, String> map = new HashMap<>(16);
                    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                        map.put(resultSetMetaData.getColumnName(i), rs.getString(i));
                    }
                    mapList.add(map);
                }
            }
        } catch (Exception e) {
            LogUtils.error(e);
        }
    }

    private static String getPrimaryField(Connection conn, String table) {
        try {
            // 获取表主键
            @Cleanup ResultSet rs2 = conn.getMetaData().getPrimaryKeys(conn.getCatalog(), null, table);
            while (rs2.next()) {
                return rs2.getString("COLUMN_NAME");
            }
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return "";
    }

    private static void setFieldInfo(
            Connection conn, List<DbFieldMod> dbFieldModList, List<Map<String, String>> mapList, String table) {
        try {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            // 字段信息
            @Cleanup
            ResultSet resultSet = dbMetaData.getColumns(conn.getCatalog(), dbMetaData.getUserName(), table, null);
            while (resultSet.next()) {
                if (dbFieldModList != null) {
                    DbFieldMod dbFieldMod = new DbFieldMod();
                    dbFieldMod.setTableName(resultSet.getString("TABLE_NAME"));
                    dbFieldMod.setColumnName(resultSet.getString("COLUMN_NAME"));
                    dbFieldMod.setTableName(resultSet.getString("TYPE_NAME"));
                    dbFieldMod.setColumnTypeName(resultSet.getString("TYPE_NAME"));
                    dbFieldMod.setColumnSize(resultSet.getString("COLUMN_SIZE"));
                    dbFieldMod.setDecimalDigits(resultSet.getString("DECIMAL_DIGITS"));
                    dbFieldMod.setColumnDefault(resultSet.getString("COLUMN_DEF"));
                    dbFieldMod.setColumnComment(resultSet.getString("REMARKS"));
                    dbFieldMod.setOrdinalPosition(resultSet.getString("ORDINAL_POSITION"));
                    dbFieldMod.setIsAutoIncrement(resultSet.getString("IS_AUTOINCREMENT"));
                    String isNull = resultSet.getString("IS_NULLABLE");
                    dbFieldMod.setIsNull(isNull.equals("YES") ? 1 : 0);
                    dbFieldModList.add(dbFieldMod);
                } else {
                    setMetadataInfo(mapList, resultSet);
                }
            }
        } catch (Exception e) {
            LogUtils.error(e);
        }
    }

    private static void setMetadataInfo(List<Map<String, String>> mapList, ResultSet resultSet) {
        try {
            /*===================遍历表字段所有元数据=====================*/ ;
            Map<String, String> map = new HashMap<>(16);
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                map.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
            }
            mapList.add(map);
        } catch (Exception e) {
            LogUtils.error(e);
        }
    }
}
