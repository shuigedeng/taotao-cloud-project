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

package com.taotao.cloud.workflow.biz.common.database.sql.impl;

import com.taotao.cloud.workflow.biz.common.database.source.DbBase;
import com.taotao.cloud.workflow.biz.common.database.sql.SqlBase;

/** 达梦 SQL语句模板 */
public class SqlDM extends SqlBase {

    private final String dbTimeSql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') as TIME ";

    /** 构造初始化 */
    public SqlDM(DbBase dbBase) {
        super(dbBase);
    }

    @Override
    protected void init() {
        String fieldListSql = "select distinct A.column_name AS "
                + DbAliasConst.FIELD_NAME
                + ","
                + " A.data_type AS "
                + DbAliasConst.DATA_TYPE
                + ", "
                + "A.data_length AS "
                + DbAliasConst.DATA_LENGTH
                + ","
                + "case A.nullable when 'N' then '1' else '0' end AS "
                + DbAliasConst.PRIMARY_KEY
                + ","
                + "case A.nullable when 'N' then '0' else '1' end AS "
                + DbAliasConst.ALLOW_NULL
                + ","
                + "B.comments AS "
                + DbAliasConst.FIELD_COMMENT
                + " from all_tab_columns A left join user_col_comments B on"
                + " A.table_name=B.table_name AND A.column_name=B.column_name AND A.OWNER ="
                + " B.OWNER where A.table_name = "
                + ParamEnum.TABLE.getParamSign()
                + " AND A.OWNER = "
                + ParamEnum.DB_SCHEMA.getParamSign();
        String tableListSql = "dbms_stats.GATHER_SCHEMA_stats ("
                + ParamEnum.DB_NAME.getParamSign()
                + ");\n"
                + "SELECT\n"
                + "ut.TABLE_NAME "
                + DbAliasConst.TABLE_NAME
                + ",utc.COMMENTS "
                + DbAliasConst.TABLE_COMMENT
                + "\n"
                + ",ut.num_rows "
                + DbAliasConst.TABLE_SUM
                + "\n"
                + "FROM ALL_TABLES AS ut\n"
                + "LEFT JOIN\n"
                + "all_tab_comments AS utc\n"
                + "ON\n"
                + "ut.TABLE_NAME = utc.TABLE_NAME AND ut.OWNER = utc.OWNER\n"
                + "WHERE ut.OWNER = "
                + ParamEnum.DB_SCHEMA.getParamSign()
                + "\n"
                + "ORDER BY F_TABLE_NAME;";
        String existsTableSql = "SELECT\n"
                + "ut.TABLE_NAME "
                + DbAliasConst.TABLE_NAME
                + " FROM ALL_TABLES AS ut\n"
                + "WHERE ut.OWNER = "
                + ParamEnum.DB_SCHEMA.getParamSign()
                + " and ut.TABLE_NAME = "
                + ParamEnum.TABLE.getParamSign()
                + ";";
        setInstance(
                fieldListSql,
                tableListSql,
                existsTableSql,
                "{table}:{dbSchema}",
                "{dbName}:{dbSchema}",
                "{dbSchema}:{table}");
    }

    @Override
    public String[] getPageSql(String sql, String sortType, Integer currentPage, Integer pageSize) {
        return new DbMySQL().getSqlBase().getPageSql(sql, sortType, currentPage, pageSize);
    }
}
