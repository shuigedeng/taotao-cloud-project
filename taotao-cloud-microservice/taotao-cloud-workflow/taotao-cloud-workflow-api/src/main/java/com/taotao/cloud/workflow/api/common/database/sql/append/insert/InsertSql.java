package com.taotao.cloud.workflow.api.common.database.sql.append.insert;

import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 插入数据SQL模板
 */
@Data
public class InsertSql {

    @Autowired
    DataSourceUtil dataSourceUtil;

    /*==================框架======================**/
    /*
     * 框架 - 基础
     * MySQL基本框架：INSERT INTO + {表名} + VALUES ({字段集合}),... + {注释}
     * Oracle基本框架：INSERT INTO + {表名} + VALUES ({字段集合}) + {注释}; ...
     * @return SQL
     */
    /*=================================================================**/

    /**
     * 批量插入数据
     * 使用不指定字段名SQL语句
     */
    public static String batch(InsertSqlDTO insertSqlDTO){
        StringBuilder sql = new StringBuilder();
        //遍历游标
        for(List<DbFieldMod> data : insertSqlDTO.getDataList()){
            StringBuilder values = new StringBuilder();
            //遍历字段
            for(DbFieldMod field : data){
                String fieldValue = insertSqlDTO.getFieldValue(field.getColumnTypeName(), field.getColumnValue());
                values.append(fieldValue);
            }
            String sqlFragment = insertSqlDTO.getOracleInsertBasicSql() + "(" + values.substring(0,values.length()-1)
                    + ")" + insertSqlDTO.getBatchInsertSeparator();
            sql.append(sqlFragment);
        }
        return  insertSqlDTO.getMysqlInsertBasicSql() + sql;
    }

}
