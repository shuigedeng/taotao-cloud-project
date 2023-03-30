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

package com.taotao.cloud.workflow.biz.common.model.visiual;

import java.util.List;
import lombok.Data;

/** */
@Data
public class TableModel {

    /** 类型：1-主表、0-子表 */
    private String typeId;
    /** 表名 */
    private String table;
    /** 说明 */
    private String tableName;
    /** 主键 */
    private String tableKey;
    /** 外键字段 */
    private String tableField;
    /** 关联主表 */
    private String relationTable;
    /** 关联主键 */
    private String relationField;

    private List<TableFields> fields;
}
