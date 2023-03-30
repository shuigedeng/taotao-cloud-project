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

/** 类功能 */
@Data
public class DataTypeModel {

    /** 数据类型名 */
    private String dbFieldType;

    /** 页面类型模板 */
    private String viewDataType;

    /** 当前数据长度（宽度） */
    private Integer currentLength;

    /** 当前数据精度 */
    private Integer currentPrecision;

    /** 默认长度 */
    private Integer defaultLength;

    /** 最大长度 */
    private Integer lengthMax;

    /** true：可修改。false：不可修改 */
    private Boolean lengthModifyFlag;

    /** 默认精度 */
    private Integer defaultPrecision;

    /** 最大精度 */
    private Integer precisionMax;

    public DataTypeModel(
            String dbFieldType,
            String viewDataType,
            Integer defaultLength,
            Integer lengthMax,
            Boolean lengthModifyFlag,
            Integer defaultPrecision,
            Integer precisionMax) {
        this.dbFieldType = dbFieldType;
        this.viewDataType = viewDataType;
        this.defaultLength = defaultLength;
        this.lengthMax = lengthMax;
        this.lengthModifyFlag = lengthModifyFlag;
        this.defaultPrecision = defaultPrecision;
        this.precisionMax = precisionMax;
    }
}
