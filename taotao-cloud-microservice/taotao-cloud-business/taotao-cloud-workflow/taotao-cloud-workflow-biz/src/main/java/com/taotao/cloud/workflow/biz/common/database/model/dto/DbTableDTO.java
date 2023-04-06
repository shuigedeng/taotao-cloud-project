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

package com.taotao.cloud.workflow.biz.common.database.model.dto;

import java.sql.Connection;
import java.util.List;
import lombok.Data;

/** 表数据传输对象 */
@Data
public class DbTableDTO {

    public DbTableDTO(
            Connection conn, DbTableModel dbTableModel, List<DbTableFieldModel> dbTableFieldList, String tableSpace) {
        this.conn = conn;
        this.dbTableFieldList = dbTableFieldList;
        this.tableComment = dbTableModel.getTableComment();
        this.tableSpace = tableSpace;
    }

    /** 主键改变标识 */
    private Boolean priChangFlag = false;

    /** 数据源 */
    private Connection conn;

    /** ==============数据库信息==============* */
    private String dbName;

    private String tableSpace;

    /** ===============表信息=================* */

    /** 查询时被使用表名 */
    private String originTable;

    /** 表说明 */
    private String tableComment;

    /** ===============字段信息=================* */

    /** 字段信息集合 */
    private List<DbTableFieldModel> dbTableFieldList;
}
