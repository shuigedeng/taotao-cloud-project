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

package com.taotao.cloud.goods.adapter.model.co.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ColumnInfo
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:05:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnInfo {

    /** 数据库字段名称 */
    private Object columnName;

    /** 允许空值 */
    private Object isNullable;

    /** 数据库字段类型 */
    private Object columnType;

    /** 数据库字段注释 */
    private Object columnComment;

    /** 数据库字段键类型 */
    private Object columnKey;

    /** 额外的参数 */
    private Object extra;

    /** 查询 1:模糊 2：精确 */
    private String columnQuery;

    /** 是否在列表显示 */
    private String columnShow;
}
