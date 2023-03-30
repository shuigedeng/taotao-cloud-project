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

package com.taotao.cloud.workflow.biz.common.database.enums.datatype;

import com.taotao.cloud.workflow.biz.common.database.constant.ViewDataTypeConst;

/** 类功能 */
public enum DtDM implements DtInterface {

    /** */
    VARCHAR("VARCHAR", ViewDataTypeConst.VARCHAR, 50, 8189),
    /*============================时间格式▽===========================*/
    /** 默认长度：36，默认精度：6 Mysql datetime转达梦 TIMESTAMP,手动改成DATETIME数据会报错 利用 PostGreSQL进行迁移 */
    DATA_TIME("DATETIME", ViewDataTypeConst.DATE_TIME, false),
    /** 默认长度：36，默认精度：6 默认工具mysql（Timestamp）--> Dm（DateTime） */
    TIME_STAMP("TIMESTAMP", ViewDataTypeConst.DATE_TIME, false),
    /** 默认长度：22，默认精度：6 */
    TIME("TIME", ViewDataTypeConst.DATE_TIME, false),
    /** 默认长度：13 */
    DATE("DATE", ViewDataTypeConst.DATE_TIME, false),
    /*============================时间格式△===========================*/
    /*============================浮点▽===========================*/
    DECIMAL("DECIMAL", ViewDataTypeConst.DECIMAL, 22, 38, 6, 38),
    DEC("DEC", ViewDataTypeConst.DECIMAL, 22, 38, 6, 38),
    /*============================浮点△===========================*/
    /** 默认长度：2147483647 */
    TEXT("TEXT", ViewDataTypeConst.TEXT, false),
    /** 默认长度：2147483647 */
    CLOB("CLOB", ViewDataTypeConst.TEXT, false),
    /*============================整型▽===========================*/
    /** 默认长度：10 */
    INT("INT", ViewDataTypeConst.INT, false),
    /** 默认长度：19 */
    BIGINT("BIGINT", ViewDataTypeConst.BIGINT, false);
    /*============================整型△===========================*/

    public final DbBase dbBase = new DbDM();

    /** 数据库字段类型 */
    private final String dbFieldType;

    /** 1：可修改。0：不可修改 */
    private final Boolean lengthModifyFlag;

    /** 前端显示数据类型 */
    private final String viewDataType;

    /** 默认长度 */
    private final Integer defaultLength;

    /** 长度范围 */
    private final Integer lengthMax;

    /** 默认精度 */
    private final Integer defaultPrecision;

    /** 精度范围 */
    private final Integer precisionMax;

    DtDM(
            String dbFieldType,
            String viewDataType,
            Integer defaultLength,
            Integer lengthMax,
            Integer defaultPrecision,
            Integer precisionMax) {
        this.dbFieldType = dbFieldType;
        this.lengthModifyFlag = true;
        this.viewDataType = viewDataType;
        this.defaultLength = defaultLength;
        this.lengthMax = lengthMax;
        this.defaultPrecision = defaultPrecision;
        this.precisionMax = precisionMax;
    }

    DtDM(String dbFieldType, String viewDataType, Integer defaultLength, Integer lengthMax) {
        this.dbFieldType = dbFieldType;
        this.lengthModifyFlag = true;
        this.viewDataType = viewDataType;
        this.defaultLength = defaultLength;
        this.lengthMax = lengthMax;
        this.defaultPrecision = null;
        this.precisionMax = null;
    }

    DtDM(String dbFieldType, String viewDataType, Boolean lengthModifyFlag) {
        this.dbFieldType = dbFieldType;
        this.lengthModifyFlag = lengthModifyFlag;
        this.viewDataType = viewDataType;
        this.defaultLength = null;
        this.lengthMax = null;
        this.defaultPrecision = null;
        this.precisionMax = null;
    }

    @Override
    public DataTypeModel getDataTypeModel() {
        return new DataTypeModel(
                this.dbFieldType,
                this.viewDataType,
                this.defaultLength,
                this.lengthMax,
                this.lengthModifyFlag,
                this.defaultPrecision,
                this.precisionMax);
    }

    @Override
    public String getDbFieldType() {
        return dbFieldType;
    }
}
