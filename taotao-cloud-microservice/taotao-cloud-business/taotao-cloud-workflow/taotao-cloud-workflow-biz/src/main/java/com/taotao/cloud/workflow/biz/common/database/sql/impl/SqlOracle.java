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
import java.sql.Connection;
import java.util.List;
import lombok.Data;
import org.springframework.web.util.HtmlUtils;

/** MySQL SQL语句模板 */
@Data
public class SqlOracle extends SqlBase {

    private final String dbTimeSql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') as TIME from dual";

    protected String deleteSql = "DROP TABLE ?;";

    /** 构造初始化 */
    public SqlOracle(DbBase dbBase) {
        super(dbBase);
    }

    @Override
    protected void init() {
        // TODO BINARY_FLOAT类型查询不出来，这个语句有隐患
        String fieldListSql = "SELECT * FROM \n"
                + "\n"
                + "(\n"
                + "SELECT DISTINCT\n"
                + "\tA.column_name AS "
                + DbAliasConst.FIELD_NAME
                + ",\n"
                + "\tA.data_type AS "
                + DbAliasConst.DATA_TYPE
                + ",\n"
                + "\tA.CHAR_COL_DECL_LENGTH AS "
                + DbAliasConst.DATA_LENGTH
                + ",\n"
                + "CASE\n"
                + "\t\tA.nullable \n"
                + "\t\tWHEN 'N' THEN\n"
                + "\t\t'0' ELSE '1' \n"
                + "\tEND AS "
                + DbAliasConst.ALLOW_NULL
                + ",\n"
                + "CASE\n"
                + "\tA.nullable \n"
                + "\tWHEN 'N' THEN\n"
                + "\t'1' ELSE '0' \n"
                + "\tEND AS "
                + DbAliasConst.PRIMARY_KEY
                + ",\n"
                + "\tB.comments AS "
                + DbAliasConst.FIELD_COMMENT
                + "\nFROM\n"
                + "\tuser_tab_columns A,\n"
                + "\tuser_col_comments B,\n"
                + "\tall_cons_columns C,\n"
                + "\tUSER_TAB_COMMENTS D \n"
                + "WHERE\n"
                + "\ta.COLUMN_NAME = b.column_name \n"
                + "\tAND A.Table_Name = B.Table_Name \n"
                + "\tAND A.Table_Name = D.Table_Name \n"
                + "\tAND ( A.TABLE_NAME = c.table_name ) \n"
                + "\tAND A.Table_Name = "
                + ParamEnum.TABLE.getParamSign()
                + "\n"
                + ") A,\n"
                + "(\n"
                + "select a.column_name name,case when a.column_name=t.column_name then 1"
                + " else 0 end "
                + DbAliasConst.PRIMARY_KEY
                + "\n"
                + "from user_tab_columns a\n"
                + "left join (select b.table_name,b.column_name from user_cons_columns b\n"
                + "join user_constraints c on c.CONSTRAINT_NAME=b.CONSTRAINT_NAME\n"
                + "where c.constraint_type   ='P') t\n"
                + "on a.table_name=t.table_name\n"
                + "where a.table_name= "
                + ParamEnum.TABLE.getParamSign()
                + "\n"
                + ") B WHERE A."
                + DbAliasConst.FIELD_NAME
                + " = b.NAME";
        String tableListSql = "SELECT "
                        + "a.TABLE_NAME "
                        + DbAliasConst.TABLE_NAME
                        + ", "
                        + "b.COMMENTS "
                        + DbAliasConst.TABLE_COMMENT
                        + ", "
                        + "a.num_rows "
                        + DbAliasConst.TABLE_SUM
                        + "\nFROM user_tables a, user_tab_comments b "
                        + "WHERE a.TABLE_NAME = b.TABLE_NAME "
                /*+ "and a.TABLESPACE_NAME='"+ DbSttEnum.TABLE_SPACE.getTarget()+"'"*/ ;

        String existsTableSql = "SELECT "
                + "a.TABLE_NAME "
                + DbAliasConst.TABLE_NAME
                + " FROM user_tables a "
                + "WHERE a.TABLE_NAME = "
                + ParamEnum.TABLE.getParamSign();
        setInstance(fieldListSql, tableListSql, existsTableSql, "{table}:{table}", "", "{table}");
    }

    @Override
    public String batchInsertSql(List<List<DbFieldMod>> dataList, String table) {
        InsertSqlDTO iInsertSqlDTO = new InsertSqlDTO(this.dbBase, table, dataList, ";");
        return InsertSql.batch(iInsertSqlDTO);
    }

    @Override
    public String[] getPageSql(String sql, String sortType, Integer currentPage, Integer pageSize) {
        /*mysql可以用 SELECT SQL_CALC_FOUND_ROWS * FROM table
        LIMIT index;SELECT FOUND_ROWS();方法获得两个结果集*/
        // 获取dataListSql
        String sortSql = StringUtil.isEmpty(sortType) ? "" : " ORDER BY " + sortType;
        String dataListSql = "SELECT *FROM ("
                + "SELECT workflow_tt.*, ROWNUM AS rowno FROM ("
                + sql
                + ") workflow_tt "
                + sortSql
                + ") jnfp_tab WHERE jnfp_tab.rowno between "
                + (currentPage - 1) * pageSize
                + " and "
                + currentPage * pageSize;
        // 获取totalSql
        String totalSql = "SELECT COUNT(*) totalRecord FROM (" + sql + ") workflow_tab";
        return new String[] {dataListSql, totalSql};
    }

    @Override
    public PreparedStatementDTO getDeleteSqlPSD(Connection conn, String deleteTable) {
        // 表名无法用?占位符的方式防止SQL注入，使用过滤的方法了
        deleteTable = HtmlUtils.htmlEscape(String.valueOf(deleteTable), CharsetKit.UTF_8);
        return new PreparedStatementDTO(conn, "DROP TABLE " + deleteTable);
    }

    /*==================特有的一些方法======================*/

    /**
     * oracle 时间格式转换
     *
     * @param dataType
     * @param value
     * @return
     */
    public static String getOracleDataTime(String dataType, String value) {
        if ("date".equals(dataType.toLowerCase()) || dataType.toLowerCase().contains("time")) {
            value = "TO_DATE('" + value + "','YYYY-MM-DD HH24:MI:SS')";
        } else {
            value = "'" + value + "'";
        }
        return value;
    }

    /**
     * 添加修改sql
     *
     * @param sql
     * @return
     */
    public String jdbcCreUpSql(String sql) {
        String jdbcSql = "";
        // 添加数据Sql处理
        if (sql.toLowerCase().contains("insert") && sql.replaceAll(" ", "").contains("),(")) {
            String[] splitSql = sql.split("\\),\\(");
            // centerSql取出INTO TEST_DETAILS ( F_ID, F_RECEIVABLEID)
            String centerSql = splitSql[0].split("VALUES")[0].split("INSERT")[1];
            // for循环尾部
            String lastSql = splitSql[splitSql.length - 1];
            splitSql[splitSql.length - 1] = lastSql.substring(0, lastSql.length() - 1);
            for (int i = 0; i < splitSql.length; i++) {
                // 第一个语句INSERT INTO TEST_DETAILS ( F_ID, F_RECEIVABLEID) VALUES ( '71',
                // '28bf3436e5d1'
                // 需要拼接成 INSERT INTO TEST_DETAILS ( F_ID, F_RECEIVABLEID) VALUES ( '71',
                // '28bf3436e5d1'）
                String sqlFlagm;
                if (i == 0) {
                    sqlFlagm = splitSql[i] + ")";
                } else {
                    sqlFlagm = "INSERT " + centerSql + "VALUES (" + splitSql[i] + ")";
                }
                jdbcSql = jdbcSql + sqlFlagm;
            }
        } else {
            jdbcSql = sql;
        }
        return jdbcSql;
    }
}
