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

package com.taotao.cloud.jdbcpool.constant;

/**
 * 数据库加在类名称常量
 *
 * date 2017/7/31
 * @since 2024.06
 */
public final class DriverNameConst {

    private DriverNameConst() {}

    /**
     * SQL Server 数据库
     * com.microsoft.sqlserver.jdbc.
     */
    public static final String SQL_SERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    /**
     * MySQL 数据库
     */
    public static final String MYSQL = "com.mysql.jdbc.Driver";

    /**
     * MYSQL 8.0 及其以后的版本
     */
    // jdbc.url=jdbc:mysql://127.0.0.1:3306/k3c?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&serverTimezone=Hongkong
    public static final String MYSQL_8 = "com.mysql.cj.jdbc.Driver";

    /**
     * Oracle 数据库
     */
    public static final String ORACLE = "oracle.jdbc.OracleDriver";
}
