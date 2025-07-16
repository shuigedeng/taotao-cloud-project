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

package com.taotao.cloud.generator.maku.config;

import cn.hutool.core.util.StrUtil;

/**
 * 数据库类型 枚举
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public enum DbType {
    MySQL("com.mysql.cj.jdbc.Driver"),
    Oracle("oracle.jdbc.driver.OracleDriver"),
    PostgreSQL("org.postgresql.Driver"),
    SQLServer("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    DM("dm.jdbc.driver.DmDriver"),
    Clickhouse("com.clickhouse.jdbc.ClickHouseDriver"),
    KingBase("com.kingbase8.Driver");

    private final String driverClass;

    DbType(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public static DbType getValue(String dbType) {
        if (StrUtil.equalsAny(dbType, "MySQL")) {
            return MySQL;
        }

        if (StrUtil.equalsAny(dbType, "Oracle")) {
            return Oracle;
        }

        if (StrUtil.equalsAny(dbType, "PostgreSQL")) {
            return PostgreSQL;
        }

        if (StrUtil.equalsAny(dbType, "SQLServer", "Microsoft SQL Server")) {
            return SQLServer;
        }

        if (StrUtil.equalsAny(dbType, "DM", "DM DBMS")) {
            return DM;
        }

        if (StrUtil.equalsAny(dbType, "Clickhouse")) {
            return Clickhouse;
        }

        if (StrUtil.equalsAny(dbType, "KingBase")) {
            return KingBase;
        }

        return null;
    }
}
