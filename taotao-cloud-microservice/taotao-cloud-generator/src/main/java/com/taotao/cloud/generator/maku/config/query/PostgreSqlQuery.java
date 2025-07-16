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

package com.taotao.cloud.generator.maku.config.query;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.generator.maku.config.DbType;

/**
 * PostgreSql查询
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public class PostgreSqlQuery implements AbstractQuery {

    @Override
    public DbType dbType() {
        return DbType.PostgreSQL;
    }

    @Override
    public String tableSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select t1.tablename, obj_description(relfilenode, 'pg_class') as comments from pg_tables t1, pg_class t2 ");
        sql.append(
                "where t1.tablename not like 'pg%' and t1.tablename not like 'sql_%' and t1.tablename = t2.relname ");
        // 表名查询
        if (StrUtil.isNotBlank(tableName)) {
            sql.append("and t1.tablename = '").append(tableName).append("' ");
        }

        return sql.toString();
    }

    @Override
    public String tableFieldsSql() {
        return "select t2.attname as columnName, pg_type.typname as dataType, col_description(t2.attrelid,t2.attnum) as columnComment,"
                + "(CASE t3.contype WHEN 'p' THEN 'PRI' ELSE '' END) as columnKey "
                + "from pg_class as t1, pg_attribute as t2 inner join pg_type on pg_type.oid = t2.atttypid "
                + "left join pg_constraint t3 on t2.attnum = t3.conkey[1] and t2.attrelid = t3.conrelid "
                + "where t1.relname = '%s' and t2.attrelid = t1.oid and t2.attnum>0";
    }

    @Override
    public String tableName() {
        return "tablename";
    }

    @Override
    public String tableComment() {
        return "comments";
    }

    @Override
    public String fieldName() {
        return "columnName";
    }

    @Override
    public String fieldType() {
        return "dataType";
    }

    @Override
    public String fieldComment() {
        return "columnComment";
    }

    @Override
    public String fieldKey() {
        return "columnKey";
    }
}
