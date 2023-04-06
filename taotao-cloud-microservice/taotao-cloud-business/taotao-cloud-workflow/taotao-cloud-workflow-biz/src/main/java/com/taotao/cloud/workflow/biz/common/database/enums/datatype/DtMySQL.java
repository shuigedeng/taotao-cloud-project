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

/** MySQL字段数据类型枚举 */
public enum DtMySQL implements DtInterface {

    /** 字符串 默认最大长度16170（65535 bytes） */
    VARCHAR("varchar", ViewDataTypeConst.VARCHAR, 50, 16170, DtDM.VARCHAR),
    /** 字符串 标注:当varchar当主键的时候，最长是768（3072 bytes），当长度过长时推荐用text */
    VARCHAR_KEY("varchar", ViewDataTypeConst.VARCHAR, 50, 768, DtDM.VARCHAR),
    /** 时间 默认长度：0 */
    DATA_TIME("datetime", ViewDataTypeConst.DATE_TIME, false, DtDM.DATA_TIME),
    /** 文本 默认长度：0 */
    TEXT("text", ViewDataTypeConst.TEXT, false, DtDM.TEXT),
    /** 时间 默认长度：0 */
    TINY_TEXT("tinytext", ViewDataTypeConst.TEXT, false, DtDM.TEXT),
    /** 长文本 默认长度：0 */
    LONG_TEXT("longtext", ViewDataTypeConst.TEXT, false, DtDM.TEXT),
    /** 整型 */
    INT("int", ViewDataTypeConst.INT, 11, 255, DtDM.INT),
    /** 长整型 */
    BIGINT("bigint", ViewDataTypeConst.BIGINT, 20, 255, DtDM.BIGINT),
    CHAR("char", ViewDataTypeConst.VARCHAR, 50, 255, DtDM.VARCHAR),
    DATE("date", ViewDataTypeConst.DATE_TIME, false, DtDM.DATE),
    /*=======================精度类型=======================*/
    /** 浮点型 */
    DECIMAL("decimal", ViewDataTypeConst.DECIMAL, 10, 65, 0, 30, DtDM.DECIMAL);

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

    /** 达梦数据类型 */
    private DtDM dtDmEnum;

    DtMySQL(
            String dbFieldType,
            String viewDataType,
            Integer defaultLength,
            Integer lengthMax,
            Integer defaultPrecision,
            Integer precisionMax,
            DtDM dtDmEnum) {
        this.dbFieldType = dbFieldType;
        this.lengthModifyFlag = true;
        this.viewDataType = viewDataType;
        this.defaultLength = defaultLength;
        this.lengthMax = lengthMax;
        this.defaultPrecision = defaultPrecision;
        this.precisionMax = precisionMax;
    }

    DtMySQL(String dbFieldType, String viewDataType, Integer defaultLength, Integer lengthMax, DtDM dtDmEnum) {
        this.dbFieldType = dbFieldType;
        this.lengthModifyFlag = true;
        this.viewDataType = viewDataType;
        this.defaultLength = defaultLength;
        this.lengthMax = lengthMax;
        this.defaultPrecision = null;
        this.precisionMax = null;
    }

    DtMySQL(String dbFieldType, String viewDataType, Boolean lengthModifyFlag, DtDM dtDmEnum) {
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
