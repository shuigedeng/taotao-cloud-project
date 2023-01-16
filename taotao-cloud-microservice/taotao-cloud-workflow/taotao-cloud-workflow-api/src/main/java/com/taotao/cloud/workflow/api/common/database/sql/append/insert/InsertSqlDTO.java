package com.taotao.cloud.workflow.api.common.database.sql.append.insert;

import com.taotao.cloud.workflow.api.common.database.source.DbBase;
import java.util.List;
import lombok.Data;


/**
 * 类功能
 *
 */
@Data
public class InsertSqlDTO {

    public InsertSqlDTO(DbBase dbBase, String table, List<List<DbFieldMod>> dataList, String batchInsertSeparator){
        this.dbBase = dbBase;
        this.table = table;
        this.dataList = dataList;
        this.batchInsertSeparator = batchInsertSeparator;
    }

    private DbBase dbBase;

    private String table;

    private List<List<DbFieldMod>> dataList;

    private String batchInsertSeparator;


    /*============独有方法=============*/
    /*
     * 为了便于快速定位，个别数据库独有方式，抽取成独有变量来解耦
     */

    public String getFieldValue(String fieldTypeName, String fieldValue){
        if(this.dbBase.getClass() == DbOracle.class){
            return SqlOracle.getOracleDataTime(fieldTypeName, fieldValue);
        }else {
            return  "'" + fieldValue + "',";
        }
    }

    public String getOracleInsertBasicSql(){
        if(this.dbBase.getClass() == DbOracle.class){
            return "INSERT INTO " + table + " VALUES";
        }
        return "";
    }

    public String getMysqlInsertBasicSql(){
        if(this.dbBase.getClass() == DbMySQL.class){
            return "INSERT INTO " + table + " VALUES";
        }
        return "";
    }

}
