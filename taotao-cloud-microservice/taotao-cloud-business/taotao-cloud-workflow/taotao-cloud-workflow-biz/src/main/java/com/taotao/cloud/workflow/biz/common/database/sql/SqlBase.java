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

package com.taotao.cloud.workflow.biz.common.database.sql;

import com.taotao.cloud.workflow.biz.common.database.source.DbBase;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.web.util.HtmlUtils;

/** SQL语句模板基类 */
@Data
public abstract class SqlBase {

    /** 数据基类 */
    protected DbBase dbBase;

    /** 表结构字段相关SQL与参数 */
    protected String fieldListSql;

    protected String fieldListParams;
    protected String tableListSql;
    protected String tableListParams;
    protected String existsTableSql;
    protected String existsTableParams;

    //    /**
    //     * 获取表数据SQL
    //     */
    //    protected final String dataSql = "SELECT * FROM ?;";

    /** 删除表SQL */
    protected String deleteSql = "DROP TABLE IF EXISTS ?;";

    /** 获取表数据SQL */
    public String getDataSql(String table) {
        return "SELECT * FROM " + table;
    }

    /** 重命名SQL */
    //    protected String renameSql = "ALTER TABLE ? RENAME TO ?;";
    public String getRenameSql(String oldTableName, String newTableName) {
        return "ALTER TABLE " + oldTableName + " RENAME TO " + newTableName + ";";
    }

    /** 增填权限 */
    public static final String INSERT_AUTHORIZE =
            "INSERT INTO base_authorize (F_ID, F_ITEMTYPE, F_ITEMID, F_OBJECTTYPE, F_OBJECTID,"
                    + " F_SORTCODE, F_CREATORTIME, F_CREATORUSERID ) VALUES  (?,?,?,?,?,?,?,?)";

    public static final String INSERT_AUTHORIZE2 =
            "INSERT INTO base_authorize (F_ID, F_ITEMTYPE, F_ITEMID, F_OBJECTTYPE, F_OBJECTID,"
                    + " F_SORTCODE, F_CREATORTIME, F_CREATORUSERID ) VALUES "
                    + " (?,?,?,?,?,?,TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'),?)";

    public static String getAuthorDelSql(List<String> objectIdAll) {
        return "DELETE FROM base_authorize WHERE (F_OBJECTID in( '"
                + String.join(",", objectIdAll)
                + "') AND F_ITEMTYPE <> 'portal')";
    }

    /** 构造初始化 */
    public SqlBase(DbBase dbBase) {
        this.dbBase = dbBase;
        init();
    }

    protected void setInstance(
            String fieldListSql,
            String tableListSql,
            String existsTableSql,
            String fieldListParams,
            String tableListParams,
            String existsTableParams) {
        this.fieldListSql = fieldListSql;
        this.tableListSql = tableListSql;
        this.existsTableSql = existsTableSql;
        this.fieldListParams = fieldListParams;
        this.tableListParams = tableListParams;
        this.existsTableParams = existsTableParams;
    }

    /** 初始设置参数 */
    protected abstract void init();

    /** 查询字段SQL */
    public PreparedStatementDTO getFieldListPSD(Connection conn, String table, DataSourceMod dbSourceOrDbLink) {
        DataSourceDTO dto = dbSourceOrDbLink.convertDTO();
        return new PreparedStatementDTO(conn, fieldListSql, this.dbBase.getStructParams(fieldListParams, table, dto));
    }

    /** 查询表SQL */
    public PreparedStatementDTO getTableListPSD(Connection conn, DataSourceMod dbSourceOrDbLink) {
        DataSourceDTO dto = dbSourceOrDbLink.convertDTO();
        return new PreparedStatementDTO(conn, tableListSql, this.dbBase.getStructParams(tableListParams, "", dto));
    }

    /** 查询表是否存在SQL */
    public PreparedStatementDTO getExistsTablePSD(Connection conn, String table, DataSourceMod dbSourceOrDbLink) {
        DataSourceDTO dto = dbSourceOrDbLink.convertDTO();
        return new PreparedStatementDTO(
                conn, existsTableSql, this.dbBase.getStructParams(existsTableParams, table, dto));
    }

    /** 添加表SQL */
    public List<PreparedStatementDTO> getCreateTablePSD(
            String table, String tableComment, List<DbTableFieldModel> dbTableFieldModelList) throws Exception {
        return CreateSql.getCreTabSql(new CreateSqlDTO(this.dbBase, table, tableComment, dbTableFieldModelList));
    }

    public PreparedStatementDTO getDeleteSqlPSD(Connection conn, String deleteTable) {
        // 表名无法用?占位符的方式防止SQL注入，使用过滤的方法了
        deleteTable = HtmlUtils.htmlEscape(String.valueOf(deleteTable), CharsetKit.UTF_8);
        return new PreparedStatementDTO(conn, "DROP TABLE IF EXISTS " + deleteTable + ";");
    }

    /** 设置表注释 */
    public PreparedStatementDTO getTableCommentPSD(CreateSqlDTO createSqlDTO) {
        // 模板：COMMENT ON TABLE {table} is {comment}
        String table = HtmlUtils.htmlEscape(String.valueOf(createSqlDTO.getNewTable()), CharsetKit.UTF_8);
        //        String preparedSql = "COMMENT ON TABLE " + table + " is ?;";
        String preparedSql = "COMMENT ON TABLE " + table + " is '" + createSqlDTO.getTableComment() + "'";
        //        return new PreparedStatementDTO(null, preparedSql, createSqlDTO.getNewTable(),
        // createSqlDTO.getTableComment());
        return new PreparedStatementDTO(null, preparedSql);
    }

    /** 设置字段注释 */
    public List<PreparedStatementDTO> getFieldCommentPSD(CreateSqlDTO createSqlDTO) {
        // 模板：COMMENT ON TABLE {table}.{column} is {comment}
        String table = HtmlUtils.htmlEscape(String.valueOf(createSqlDTO.getNewTable()), CharsetKit.UTF_8);
        String prepareSql = "COMMENT ON COLUMN " + table + ".{column} is '{comment}'";
        List<PreparedStatementDTO> listPSD = new ArrayList<>();
        for (DbTableFieldModel fieldModel : createSqlDTO.getFieldModels()) {
            String preparedSql = prepareSql
                    .replace("{column}", fieldModel.getField())
                    .replace("{comment}", fieldModel.getFieldName());
            listPSD.add(new PreparedStatementDTO(null, preparedSql));
        }
        return listPSD;
    }

    /** 批量添加数据 */
    // TODO 其余几个数据还没有添加方法
    public String batchInsertSql(List<List<DbFieldMod>> dataList, String table) {
        return "";
    }
    ;

    public abstract String[] getPageSql(String sql, String sortType, Integer currentPage, Integer pageSize);
}
