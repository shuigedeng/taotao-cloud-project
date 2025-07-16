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
 * Oracle查询
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public class OracleQuery implements AbstractQuery {

    @Override
    public DbType dbType() {
        return DbType.Oracle;
    }

    @Override
    public String tableSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select dt.table_name, dtc.comments from user_tables dt,user_tab_comments dtc ");
        sql.append("where dt.table_name = dtc.table_name ");
        // 表名查询
        if (StrUtil.isNotBlank(tableName)) {
            sql.append("and dt.table_name = '").append(tableName).append("' ");
        }
        sql.append("order by dt.table_name asc");

        return sql.toString();
    }

    @Override
    public String tableName() {
        return "table_name";
    }

    @Override
    public String tableComment() {
        return "comments";
    }

    @Override
    public String tableFieldsSql() {
        return "SELECT A.COLUMN_NAME, A.DATA_TYPE, B.COMMENTS,DECODE(C.POSITION, '1', 'PRI') KEY FROM ALL_TAB_COLUMNS A "
                + " INNER JOIN ALL_COL_COMMENTS B ON A.TABLE_NAME = B.TABLE_NAME AND A.COLUMN_NAME = B.COLUMN_NAME AND B.OWNER = '#schema'"
                + " LEFT JOIN ALL_CONSTRAINTS D ON D.TABLE_NAME = A.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P' AND D.OWNER = '#schema'"
                + " LEFT JOIN ALL_CONS_COLUMNS C ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME AND C.COLUMN_NAME=A.COLUMN_NAME AND C.OWNER = '#schema'"
                + "WHERE A.OWNER = '#schema' AND A.TABLE_NAME = '%s' ORDER BY A.COLUMN_ID ";
    }

    @Override
    public String fieldName() {
        return "COLUMN_NAME";
    }

    @Override
    public String fieldType() {
        return "DATA_TYPE";
    }

    @Override
    public String fieldComment() {
        return "COMMENTS";
    }

    @Override
    public String fieldKey() {
        return "KEY";
    }
}
