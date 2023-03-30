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

package com.taotao.cloud.workflow.biz.common.database.model;

import lombok.Data;

/** 数据模板接口 */
@Data
public class DbFieldMod {

    /** 基础配置创建对象 */
    public DbFieldMod(
            String tableName,
            String columnLabel,
            String columnName,
            String columnTypeName,
            Integer columnTypeEncode,
            String columnValue,
            Boolean lowercaseFlag) {
        this.tableName = tableName;
        this.columnLabel = columnLabel;
        this.columnName = lowercaseFlag ? columnName.toLowerCase() : columnName;
        this.columnTypeName = columnTypeName;
        this.columnTypeEncode = columnTypeEncode;
        this.columnValue = columnValue;
    }

    public DbFieldMod() {}

    /** 字段名 */
    private String columnName;

    /** 字段别名 */
    private String columnLabel;

    /** 字段类型 */
    private String columnTypeName;

    /** 字段类型jdbc编码 */
    private Integer columnTypeEncode;

    /** 字段值 */
    private String columnValue;

    /** 所属表名 */
    private String tableName;

    /*================扩展属性=================*/

    /** 字段长度 */
    private String columnSize;

    /** 字段精度 */
    private String decimalDigits;

    /** 字段默认值 */
    private String columnDefault;

    /** 字段注解 */
    private String columnComment;

    /** 字段位置 */
    private String ordinalPosition;

    /** 自增标识 */
    private String isAutoIncrement;

    /** 主键标识 1：是主键 0：非主键 */
    private String primaryKeyMark;

    /** 允空 1:可以为空 0：不为空 */
    private Integer isNull;
}
