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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import lombok.Data;

/** 类功能 */
@Data
public class DbModel {

    public DbModel(Connection connection) {
        try {
            // 从conn中获取数据库的元数据
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            /*============库信息===========*/
            // 连接的库名目录
            this.catalog = connection.getCatalog();
            // 数据库类型 MYSQL ORACLE
            this.jdbcDbType = databaseMetaData.getDatabaseProductName();
            // 数据库版本号 8.0.15
            this.version = databaseMetaData.getDatabaseProductVersion();
            // 数据库大版本 8
            this.majorVersion = databaseMetaData.getDatabaseMajorVersion();
            // jdbc连接的url
            this.url = databaseMetaData.getURL();
        } catch (Exception e) {
            LogUtils.error(e);
        }
    }

    /** 数据库目录 */
    private String catalog;

    /** jdbc数据库类型 */
    private String jdbcDbType;

    /** 数据库版本号 例：8.0.15 */
    private String version;

    /** 数据库大版本 例：8 */
    private Integer majorVersion;

    /** 数据库连接 */
    private String url;
}
