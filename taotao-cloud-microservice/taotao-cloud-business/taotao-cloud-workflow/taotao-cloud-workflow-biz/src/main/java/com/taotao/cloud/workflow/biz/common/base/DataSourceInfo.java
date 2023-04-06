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

package com.taotao.cloud.workflow.biz.common.base;

/** */
public class DataSourceInfo {

    // TODO 这个类准备删除掉

    public static final String mysqlUrl =
            "jdbc:mysql://{host}:{port}/{dbName}?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true&serverTimezone=GMT%2B8";

    public static final String mysqlDriver = "com.mysql.cj.jdbc.Driver";

    public static final String oracleUrl = "jdbc:oracle:thin:@{host}:{port}:{dbName}";

    public static final String oracleDriver = "oracle.jdbc.OracleDriver";

    public static final String sqlserverUrl = "jdbc:sqlserver://{host}:{port};Databasename={dbName}";

    public static final String sqlserverDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static final String dmUrl = "jdbc:dm://{host}:{port}/{dbName}?zeroDateTimeBehavior=convertToNull&useUnicode"
            + "=true&characterEncoding=utf-8";

    public static final String dmDriver = "dm.jdbc.driver.DmDriver";

    public static final String kingBaseUrl = "jdbc:kingbase8://{host}:{port}/{dbName}";

    public static final String kingBaseDriver = "com.kingbase8.Driver";

    public static final String postgreUrl = "jdbc:postgresql://{host}:{port}/{dbName}";

    public static final String postgreDriver = "org.postgresql.Driver";
}
