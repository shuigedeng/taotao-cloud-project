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
import lombok.Data;

/** MySQL SQL语句模板 */
@Data
public class SqlSQLServer extends SqlBase {

    private final String dbTimeSql = "Select CONVERT(varchar(100), GETDATE(), 120) as TIME";

    protected String deleteSql = "DROP TABLE ?;";

    protected String renameSql = "EXEC sp_rename '?', '?';";

    /** 构造初始化 */
    public SqlSQLServer(DbBase dbBase) {
        super(dbBase);
    }

    @Override
    protected void init() {
        String fieldListSql = "SELECT cast(a.name as varchar(50)) "
                + DbAliasConst.FIELD_NAME
                + " , cast(case when exists(SELECT 1 FROM sysobjects where xtype='PK' and"
                + " name in (  SELECT name FROM sysindexes WHERE indid in(  SELECT indid"
                + " FROM sysindexkeys WHERE id = a.id AND colid=a.colid )))  then '1' else"
                + " '0' end as varchar(50)) "
                + DbAliasConst.PRIMARY_KEY
                + ", "
                + " cast(b.name as varchar(50)) "
                + DbAliasConst.DATA_TYPE
                + ", "
                + " cast(COLUMNPROPERTY(a.id,a.name,'PRECISION') as varchar(50)) "
                + DbAliasConst.DATA_LENGTH
                + ", "
                + " cast(case when a.isnullable=0 then '0'else '1' end as varchar(50)) "
                + DbAliasConst.ALLOW_NULL
                + ", "
                + " cast(isnull(e.text,'') as varchar(50)) "
                + DbAliasConst.DEFAULTS
                + ", "
                + " cast(isnull(g.[value],'') as varchar(50)) "
                + DbAliasConst.FIELD_COMMENT
                + "\n"
                + "FROM syscolumns a left join systypes b on a.xusertype=b.xusertype inner"
                + " join sysobjects d on a.id=d.id and d.xtype='U' and"
                + " d.name<>'dtproperties' left join syscomments e on a.cdefault=e.id left"
                + " join sys.extended_properties g on a.id=g.major_id and"
                + " a.colid=g.minor_id left join sys.extended_properties f on"
                + " d.id=f.major_id and f.minor_id=0 where d.name = "
                + ParamEnum.DB_NAME.getParamSign()
                + "\norder by a.id,a.colorder";
        String tableListSql = "SET NOCOUNT ON DECLARE @TABLEINFO TABLE ( NAME VARCHAR(50) , SUMROWS VARCHAR(11) ,"
                + " RESERVED VARCHAR(50) , DATA VARCHAR(50) , INDEX_SIZE VARCHAR(50) , UNUSED"
                + " VARCHAR(50) , PK VARCHAR(50) ) DECLARE @TABLENAME TABLE ( NAME VARCHAR(50)"
                + " ) DECLARE @NAME VARCHAR(50) DECLARE @PK VARCHAR(50) INSERT INTO @TABLENAME"
                + " ( NAME ) SELECT O.NAME FROM SYSOBJECTS O , SYSINDEXES I WHERE O.ID = I.ID"
                + " AND O.XTYPE = 'U' AND I.INDID < 2 ORDER BY I.ROWS DESC , O.NAME WHILE"
                + " EXISTS ( SELECT 1 FROM @TABLENAME ) BEGIN SELECT TOP 1 @NAME = NAME FROM"
                + " @TABLENAME DELETE @TABLENAME WHERE NAME = @NAME DECLARE @OBJECTID INT SET"
                + " @OBJECTID = OBJECT_ID(@NAME) SELECT @PK = COL_NAME(@OBJECTID, COLID) FROM"
                + " SYSOBJECTS AS O INNER JOIN SYSINDEXES AS I ON I.NAME = O.NAME INNER JOIN"
                + " SYSINDEXKEYS AS K ON K.INDID = I.INDID WHERE O.XTYPE = 'PK' AND PARENT_OBJ"
                + " = @OBJECTID AND K.ID = @OBJECTID INSERT INTO @TABLEINFO ( NAME , SUMROWS ,"
                + " RESERVED , DATA , INDEX_SIZE , UNUSED ) EXEC SYS.SP_SPACEUSED @NAME UPDATE"
                + " @TABLEINFO SET PK = @PK WHERE NAME = @NAME END SELECT cast(F.NAME AS"
                + " varchar(50))"
                + DbAliasConst.TABLE_NAME
                + ",cast(ISNULL( P.TDESCRIPTION, F.NAME )  AS varchar(50)) "
                + DbAliasConst.TABLE_COMMENT
                + ",cast(F.RESERVED AS varchar(50))"
                + " F_SIZE,cast(RTRIM( F.SUMROWS ) AS varchar(50)) "
                + DbAliasConst.TABLE_SUM
                + ",cast(F.PK AS varchar(50)) F_PRIMARYKEY FROM @TABLEINFO F LEFT JOIN ("
                + " SELECT NAME = CASE WHEN A.COLORDER = 1 THEN D.NAME ELSE '' END ,"
                + " TDESCRIPTION = CASE WHEN A.COLORDER = 1 THEN ISNULL(F.VALUE, '') ELSE"
                + " '' END FROM SYSCOLUMNS A LEFT JOIN SYSTYPES B ON A.XUSERTYPE ="
                + " B.XUSERTYPE INNER JOIN SYSOBJECTS D ON A.ID = D.ID AND D.XTYPE = 'U'"
                + " AND D.NAME <> 'DTPROPERTIES' LEFT JOIN SYS.EXTENDED_PROPERTIES F ON"
                + " D.ID = F.MAJOR_ID WHERE A.COLORDER = 1 AND F.MINOR_ID = 0 ) P ON F.NAME"
                + " = P.NAME WHERE 1 = 1 ORDER BY "
                + DbAliasConst.TABLE_NAME;
        String existsTableSql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES where table_type = 'BASE TABLE'"
                + " and TABLE_NAME = "
                + ParamEnum.TABLE.getParamSign()
                + ";";
        setInstance(fieldListSql, tableListSql, existsTableSql, "{table}:", "", "{table}");
    }

    @Override
    public String[] getPageSql(String sql, String sortType, Integer currentPage, Integer pageSize) {
        /*第二种方式：offset fetch next方式（SQL2012以上的版本才支持：推荐使用 ）
        select * from ArtistModels order by ArtistId offset 4 rows fetch next 5 rows only
        --order by ArtistId offset 页数 rows fetch next 条数 rows only ----*/
        String sortSql = StringUtil.isEmpty(sortType) ? "" : " ORDER BY " + sortType;
        int startIndex = currentPage - 1;
        // 获取dataListSql
        String dataListSql =
                sql + sortSql + " offset " + startIndex * pageSize + " rows fetch next " + pageSize + " rows only;";
        // 获取totalSql
        String totalSql = "SELECT COUNT(*) totalRecord FROM (" + sql + ") workflow_tab;";
        return new String[] {dataListSql, totalSql};
    }

    private String getTotalSql(String sql) {
        // 第一SELECT一定要大写，第一个FROM与SELECT之间，能插入COUNT(*),不报错
        int selectStar = sql.indexOf("SELECT");
        int fromEnd = sql.indexOf("FROM");
        return sql.substring(0, selectStar + 6) + " COUNT(*) AS totalRecord " + sql.substring(fromEnd);
    }

    /*==================特有的一些方法======================*/

    /**
     * 添加注解
     *
     * @param tableComment
     * @param newTableName
     * @param models
     * @return
     */
    public static List<PreparedStatementDTO> getTableComment(
            String tableComment, String newTableName, List<DbTableFieldModel> models) {
        List<PreparedStatementDTO> PSDs = new ArrayList<>();
        // 添加表注释
        PSDs.add(insertTableCommentPSD(tableComment, "dbo", newTableName));
        // 添加字段注释
        for (DbTableFieldModel model : models) {
            PSDs.add(insertFieldCommentPSD(model.getFieldName(), "dbo", newTableName, model.getField()));
        }
        return PSDs;
    }

    /** 添加表注释 */
    private static PreparedStatementDTO insertTableCommentPSD(String comment, String schema, String table) {
        // 模板：EXEC sp_addextendedproperty
        // 'MS_Description',N'{value}','SCHEMA',N'{value}','TABLE',N'{value}';
        String preparedSql = "EXEC sp_addextendedproperty 'MS_Description',N'"
                + comment
                + "','SCHEMA',N'"
                + schema
                + "','TABLE',N'"
                + table
                + "';";
        return new PreparedStatementDTO(null, preparedSql);
    }

    /** 添加字段注释 */
    private static PreparedStatementDTO insertFieldCommentPSD(
            String comment, String schema, String table, String column) {
        // 模板：EXEC sp_addextendedproperty
        // 'MS_Description',N'{value}','SCHEMA',N'{value}','TABLE',N'{value}','COLUMN',N'{column}';
        String preparedSql = "EXEC sp_addextendedproperty 'MS_Description',N'"
                + comment
                + "','SCHEMA',N'"
                + schema
                + "','TABLE',N'"
                + table
                + "','COLUMN',N'"
                + column
                + "';";
        return new PreparedStatementDTO(null, preparedSql);
    }

    //    /*==================复用======================*/
    //
    //    private static String insertCommentCommon(String value,Integer type){
    //        String valueFrame = ",N'" + value;
    //        switch (type){
    //            case 1:
    //                // 'MS_Description',N'{value}'
    //                value = "'MS_Description'" + valueFrame;
    //                break;
    //            case 2:
    //                // 'SCHEMA',N'{value}'
    //                value = "'SCHEMA'" + valueFrame;
    //                break;
    //            case 3:
    //                // 'TABLE',N'{value}'
    //                value = "'TABLE'" + valueFrame;
    //                break;
    //            case 4:
    //                // 'COLUMN',N'{value}'
    //                value = "'COLUMN'" + valueFrame;
    //                break;
    //            default:
    //        }
    //        return value;
    //    }

}
