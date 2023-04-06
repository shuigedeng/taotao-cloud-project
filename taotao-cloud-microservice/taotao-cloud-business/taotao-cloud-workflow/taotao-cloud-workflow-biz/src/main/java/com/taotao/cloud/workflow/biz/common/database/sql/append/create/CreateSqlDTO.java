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

package com.taotao.cloud.workflow.biz.common.database.sql.append.create;

import java.util.List;
import lombok.Data;

/** 类功能 */
@Data
public class CreateSqlDTO {

    public CreateSqlDTO(DbBase dbBase, String newTable, String tableComment, List<DbTableFieldModel> fieldModels) {
        this.dbBase = dbBase;
        this.newTable = newTable;
        this.tableComment = tableComment;
        this.fieldModels = fieldModels;
    }

    /** 数据库基类 */
    private DbBase dbBase;

    /** 新建表名 */
    private String newTable;

    /** 新建表注释 */
    private String tableComment;

    /** 新建表字段 */
    private List<DbTableFieldModel> fieldModels;
}
