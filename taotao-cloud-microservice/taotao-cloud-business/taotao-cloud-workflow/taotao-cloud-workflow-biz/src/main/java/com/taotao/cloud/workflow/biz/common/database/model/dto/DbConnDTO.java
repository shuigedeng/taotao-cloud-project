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
import lombok.Data;

/** 数据连接相关数据传输对象 */
@Data
public class DbConnDTO {

    public DbConnDTO(DbBase dbBase, DataSourceUtil dbSource, Connection conn) {
        this.dbBase = dbBase;
        this.dbSource = dbSource;
        this.conn = conn;
    }

    /** 数据库基类 */
    private DbBase dbBase;

    /** 数据源信息 */
    private DataSourceUtil dbSource;

    /** 数据连接 */
    private Connection conn;
}
