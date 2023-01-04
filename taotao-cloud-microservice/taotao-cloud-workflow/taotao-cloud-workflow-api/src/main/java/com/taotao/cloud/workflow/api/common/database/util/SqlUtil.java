package com.taotao.cloud.workflow.api.common.database.util;

import com.taotao.cloud.workflow.api.common.database.model.dto.PreparedStatementDTO;
import java.util.List;

/**
 * 类功能
 *
 */
public class SqlUtil {


    /*============独有方法=============*/
    /*
     * 为了便于快速定位，个别数据库独有方式，抽取成独有变量来解耦
     */
    /**
     * MySQL、SQLServer独有注释方法
     */
    public static List<PreparedStatementDTO> getMysqlSqlServerComments(CreateSqlDTO createSqlDTO){
        //执行一些个别数据独有的方式
        if(createSqlDTO.getDbBase().getClass() == DbSQLServer.class){
            return SqlSQLServer.getTableComment(createSqlDTO.getTableComment(), createSqlDTO.getNewTable(), createSqlDTO.getFieldModels());
        }
        return null;
    }

    /**
     * MySQL 字段注解方式
     * @param fieldComment 字段注解
     * @return mysql字段注解
     */
    public static String getMysqlFieldComment(String fieldComment, DbBase dbBase){
        if(dbBase.getClass() == DbMySQL.class){
            return SqlMySQL.getCreFieldComment(fieldComment);
        }
        return "";
    }

    /**
     * MySQL独有,varchar作为主键的特殊处理
     * @return
     */
    public static DataTypeModel getMysqlDataTypeModel(DbBase dbBase, Integer primaryKey, String dataType, DataTypeModel dataTypeModel){
        if(dbBase.getClass() == DbMySQL.class){
            if(primaryKey == 1 && dataType.equals(ViewDataTypeConst.VARCHAR)){
                return DtMySQL.VARCHAR_KEY.getDataTypeModel();
            }
        }
        return dataTypeModel;
    }


}
