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
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.util.HtmlUtils;

/** MySQL SQL语句模板 */
public class SqlMySQL extends SqlBase {

    private final String dbTimeSql = "SELECT DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i:%s') as TIME";

    /** 构造初始化 */
    public SqlMySQL(DbBase dbBase) {
        super(dbBase);
    }

    @Override
    protected void init() {
        String fieldListSql = "SELECT COLUMN_NAME "
                + DbAliasConst.FIELD_NAME
                + ",data_type "
                + DbAliasConst.DATA_TYPE
                + ",CHARACTER_MAXIMUM_LENGTH "
                + DbAliasConst.DATA_LENGTH
                + ", "
                + "NUMERIC_PRECISION AS "
                + DbAliasConst.PRECISION
                + ",NUMERIC_SCALE AS "
                + DbAliasConst.DECIMALS
                + ", "
                + "IF ( IS_NULLABLE = 'YES', '1', '0' ) "
                + DbAliasConst.ALLOW_NULL
                + ", COLUMN_COMMENT "
                + DbAliasConst.FIELD_COMMENT
                + ","
                + "IF ( COLUMN_KEY = 'PRI', '1', '0' ) "
                + DbAliasConst.PRIMARY_KEY
                + ", "
                + "column_default "
                + DbAliasConst.DEFAULTS
                + ","
                + "CONCAT(upper(COLUMN_NAME),'(',COLUMN_COMMENT,')') as 'F_DESCRIPTION' "
                + "FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_NAME = "
                + ParamEnum.TABLE.getParamSign()
                + " AND TABLE_SCHEMA= "
                + ParamEnum.DB_SCHEMA.getParamSign()
                + ";";
        String tableListSql = "SELECT table_name "
                + DbAliasConst.TABLE_NAME
                + ",table_rows "
                + DbAliasConst.TABLE_SUM
                + ","
                + " data_length "
                + DbAliasConst.TABLE_SIZE
                + ", table_comment "
                + DbAliasConst.TABLE_COMMENT
                + ", CONCAT(table_name,'(',table_comment,')') as 'F_DESCRIPTION' FROM"
                + " information_schema.TABLES WHERE TABLE_SCHEMA = "
                + ParamEnum.DB_NAME.getParamSign()
                + ";";
        String existsTableSql = "SELECT table_name "
                + DbAliasConst.TABLE_NAME
                + " FROM information_schema.TABLES WHERE "
                + "TABLE_SCHEMA = "
                + ParamEnum.DB_NAME.getParamSign()
                + " and table_name = "
                + ParamEnum.TABLE.getParamSign()
                + ";";
        setInstance(fieldListSql, tableListSql, existsTableSql, "{table}:{dbName}", "{dbName}:", "{dbName}:{table}");
    }

    @Override
    public String batchInsertSql(List<List<DbFieldMod>> dataList, String table) {
        InsertSqlDTO iInsertSqlDTO = new InsertSqlDTO(this.dbBase, table, dataList, ",");
        String sql = InsertSql.batch(iInsertSqlDTO);
        // 去除最后一个逗号
        return sql.substring(0, sql.length() - 1) + ";";
    }

    @Override
    public String[] getPageSql(String sql, String sortType, Integer currentPage, Integer pageSize) {
        /*mysql可以用 SELECT SQL_CALC_FOUND_ROWS * FROM table
        LIMIT index;SELECT FOUND_ROWS();方法获得两个结果集*/
        int startIndex = currentPage - 1;
        String sortSql = StringUtil.isEmpty(sortType) ? "" : " ORDER BY " + sortType;
        // 获取dataListSql
        String dataListSql = sql + sortSql + " LIMIT " + startIndex * pageSize + "," + pageSize + ";";
        // 获取totalSql
        String totalSql =
                "SELECT COUNT(*) " + DbAliasEnum.TOTAL_RECORD.asByDb(this.dbBase) + " FROM (" + sql + ") workflow_tab;";
        return new String[] {dataListSql, totalSql};
    }

    private String getTotalSql(String sql) {
        // SELECT COUNT(*) FROM  (SELECT * FROM base_dblink) jnfp_tab
        // 第一SELECT一定要大写，第一个FROM与SELECT之间，能插入COUNT(*),不报错
        int selectStar = sql.indexOf("SELECT");
        int fromEnd = sql.indexOf("FROM");
        return sql.substring(0, selectStar + 6) + " COUNT(*) AS totalRecord " + sql.substring(fromEnd);
    }

    /** 设置表注释 */
    @Override
    public PreparedStatementDTO getTableCommentPSD(CreateSqlDTO createSqlDTO) {
        // 模板：ALTER TABLE table_name COMMENT='这是表的注释';
        String table = HtmlUtils.htmlEscape(String.valueOf(createSqlDTO.getNewTable()), CharsetKit.UTF_8);
        String preparedSql = "ALTER TABLE " + table + " COMMENT= ?;";
        return new PreparedStatementDTO(null, preparedSql, createSqlDTO.getTableComment());
    }

    /** 设置字段注释 */
    @Override
    public List<PreparedStatementDTO> getFieldCommentPSD(CreateSqlDTO createSqlDTO) {
        // 模板：ALTER table table_name MODIFY `column_name` datetime DEFAULT NULL COMMENT '这是字段的注释'
        String table = HtmlUtils.htmlEscape(String.valueOf(createSqlDTO.getNewTable()), CharsetKit.UTF_8);
        List<PreparedStatementDTO> listPSD = new ArrayList<>();
        String dataTypeFormat = "";
        for (DbTableFieldModel fieldModel : createSqlDTO.getFieldModels()) {
            try {
                dataTypeFormat = CreateSql.dataTypeFormat(
                        fieldModel.getDataType(),
                        fieldModel.getDataLength(),
                        fieldModel.getPrimaryKey(),
                        new DbMySQL());
            } catch (DataException e) {
                LogUtils.error(e);
            }
            String column = HtmlUtils.htmlEscape(String.valueOf(fieldModel.getField()), CharsetKit.UTF_8);
            String preparedSql =
                    "ALTER TABLE " + table + " MODIFY COLUMN `" + column + "` " + dataTypeFormat + " COMMENT ?;";
            listPSD.add(new PreparedStatementDTO(null, preparedSql, fieldModel.getFieldName()));
        }
        return listPSD;
    }

    /*==================特有的一些方法======================*/

    /**
     * 获取添加表时的字段注释
     *
     * @param tableComment 字段注释
     * @return SQL片段
     */
    public static String getCreFieldComment(String tableComment) {
        return "COMMENT " + tableComment;
    }

    /**
     * 获取注释 mysql独有注释方式
     *
     * @param tableComment 表注释
     * @return SQL片段
     */
    public static List<PreparedStatementDTO> getTabComment(String tableComment) {
        String sql = "\tCOMMENT\t\'" + tableComment + "\';";
        return new ArrayList<>();
    }
}
