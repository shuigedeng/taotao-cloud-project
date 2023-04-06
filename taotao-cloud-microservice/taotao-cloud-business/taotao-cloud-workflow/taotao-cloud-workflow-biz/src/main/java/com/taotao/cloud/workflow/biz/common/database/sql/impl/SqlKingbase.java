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

import com.taotao.cloud.workflow.biz.common.database.constant.DbAliasConst;
import com.taotao.cloud.workflow.biz.common.database.source.DbBase;
import com.taotao.cloud.workflow.biz.common.database.sql.SqlBase;

/** 金仓 SQL语句模板 */
public class SqlKingbase extends SqlBase {

    public static final String DATA_SUM_SQL = "SELECT count(*) AS COUNT_SUM FROM " + ParamEnum.TABLE.getParamSign();

    public static final String FIELD_SQL = " \n"
            + "SELECT\n"
            + "\tpretty_sizes.table_name,\n"
            + "\tpretty_sizes.comment_info,\n"
            + "\tsys_size_pretty(table_size) AS table_size\n"
            + "\t/*,sys_size_pretty(total_size) AS total_size*/\n"
            + "FROM\n"
            + "\t(\n"
            + "\t\tSELECT\n"
            + "\t\t\ttable_name,\n"
            + "\t\t\tcomment_info,\n"
            + "\t\t\tsys_table_size(table_name) AS table_size,\n"
            + "\t\t\tsys_total_relation_size(table_name) AS total_size\n"
            + "\t\tFROM\n"
            + "\t\t\t(\n"
            + "\t\t\t\tSELECT \n"
            + "\t\t\t\t\tt.TABLE_NAME AS table_name,\n"
            + "\t\t\t\t\tc.COMMENTS AS comment_info\n"
            + "\t\t\t\tFROM\n"
            + "\t\t\t\t\tinformation_schema.TABLES AS t\n"
            + "\t\t\t\t\tLEFT JOIN\n"
            + "\t\t\t\t\t(SELECT TABLE_NAME,COMMENTS FROM DBA_TAB_COMMENTS)AS c\n"
            + "\t\t\t\tON\n"
            + "\t\t\t\t\tt.TABLE_NAME = c.TABLE_NAME\n"
            + "\t\t\t\tWHERE\n"
            +
            //            "\t\t\t\t \tTABLE_SCHEMA = 'YANYU'\n" +
            "\t\t\t) AS all_tables\n"
            + "\t\tORDER BY\n"
            + "\t\t\ttotal_size DESC\n"
            + "\t) AS pretty_sizes\n"
            + "\t";

    /** 构造初始化 */
    public SqlKingbase(DbBase dbBase) {
        super(dbBase);
    }

    @Override
    protected void init() {
        // 注意：若金仓设置大小写敏感的话，字段信息无法被查询出来的
        String fieldListSql = "SELECT DISTINCT\n"
                + "c.relname,\n"
                + "a.attnum,\n"
                + "a.attname AS "
                + DbAliasConst.FIELD_NAME
                + ",\n"
                + "t.typname AS "
                + DbAliasConst.DATA_TYPE
                + ",\n"
                + "a.attnotnull AS "
                + DbAliasConst.ALLOW_NULL
                + ",\n"
                + "b.character_maximum_length AS "
                + DbAliasConst.DATA_LENGTH
                + ",\n"
                + "d.COMMENTS AS "
                + DbAliasConst.FIELD_COMMENT
                + ",\n"
                + "pk.colname AS "
                + DbAliasConst.PRIMARY_KEY
                + "\n"
                + "FROM sys_class c\n"
                + "INNER JOIN sys_namespace n on c.relnamespace = n.oid\n"
                + "INNER JOIN sys_attribute a ON c.oid = a.attrelid\n"
                + "INNER JOIN information_schema.columns b ON c.relname = b.table_name\n"
                + "INNER JOIN DBA_COL_COMMENTS d on a.attname = d.column_name\n"
                + "INNER JOIN sys_type t ON a.atttypid = t.oid\n"
                + "LEFT JOIN\n"
                + "(SELECT\n"
                + "c.conname AS pk_name,\n"
                + "a.attname AS colname\n"
                + "FROM\n"
                + "sys_constraint AS c\n"
                + "INNER JOIN sys_class AS clz ON c.conrelid = clz.oid\n"
                + "INNER JOIN sys_attribute AS a ON a.attrelid = clz.oid\n"
                + "AND a.attnum = c.conkey [1]\n"
                + "WHERE\n"
                + "clz.relname = "
                + ParamEnum.TABLE.getParamSign()
                + "\n"
                + "AND c.contype = 'p') AS pk ON a.attname = pk.colname\n"
                + "WHERE attnum > 0\n"
                + "AND b.column_name = a.attname\n"
                + "AND c.relname = d.table_name\n"
                +
                //                        "AND nspname = '"+
                // DbSttEnum.DB_SCHEMA.getTarget()+"'\n"+
                //                        "AND nspname = 'workflow_init'\n"+
                "AND b.table_schema = "
                + ParamEnum.DB_SCHEMA.getParamSign()
                + "AND c.relname = "
                + ParamEnum.TABLE.getParamSign()
                + "\n"
                + "ORDER BY attnum;";
        String tableListSql = "SELECT t.TABLE_NAME AS "
                + DbAliasConst.TABLE_NAME
                + ",c.COMMENTS AS "
                + DbAliasConst.TABLE_COMMENT
                + ", 0 AS "
                + DbAliasConst.TABLE_SUM
                + " FROM\n"
                + "information_schema.TABLES AS t\n"
                + "LEFT JOIN\n"
                + "(SELECT TABLE_NAME,COMMENTS FROM DBA_TAB_COMMENTS)AS c\n"
                + "ON\n"
                + "t.TABLE_NAME = c.TABLE_NAME\n"
                + "WHERE\n"
                + " TABLE_SCHEMA = "
                + ParamEnum.DB_SCHEMA.getParamSign();
        String existsTableSql = "SELECT t.TABLE_NAME AS "
                + DbAliasConst.TABLE_NAME
                + " FROM\n"
                + "information_schema.TABLES AS t WHERE TABLE_SCHEMA = "
                + ParamEnum.DB_SCHEMA.getParamSign()
                + " and t.TABLE_NAME = "
                + ParamEnum.TABLE.getParamSign();
        setInstance(
                fieldListSql,
                tableListSql,
                existsTableSql,
                "{table}:{dbSchema}:{table}",
                "{dbSchema}:",
                "{dbSchema}:{table}");
    }

    @Override
    public String[] getPageSql(String sql, String sortType, Integer currentPage, Integer pageSize) {
        return new DbMySQL().getSqlBase().getPageSql(sql, sortType, currentPage, pageSize);
    }
}
