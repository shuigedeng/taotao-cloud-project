package com.taotao.cloud.workflow.api.common.database.source.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.taotao.cloud.workflow.api.common.database.data.DataSourceContextHolder;
import com.taotao.cloud.workflow.api.common.database.source.DbBase;
import com.taotao.cloud.workflow.api.common.database.sql.impl.SqlSQLServer;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQLServer模型
 *
 */
public class DbSQLServer extends DbBase {

    @Override
    protected void init() {
        setInstance(
                DbBase.SQL_SERVER,
                DbType.SQL_SERVER,
                "1433",
                "sqlserver",
                "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "jdbc:sqlserver://{host}:{port};databaseName={dbname}",
                new SqlSQLServer(this));
    }


    @Override
    protected String getDynamicTableName(String tableName) {
        return DataSourceContextHolder.getDatasourceName()+".dbo." + tableName;
    }

    @Override
    public DbTableFieldModel getPartFieldModel(ResultSet result) throws SQLException, DataException {
        DbTableFieldModel model = new DbTableFieldModel();
        /*  Text 和 Image 是可能被 SQServer 以后的版本淘汰的数据类型
            varchar(max)-------text;
            nvarchar(max)-----ntext;
            varbinary(max)----p_w_picpath.
            查询出来只能显示nvarchar，长度-1的时候代表nvarchar(max)，项目中转换成text */
        // 字段类型
        String dataType = result.getString(DbAliasEnum.getAsByDb(this, DbAliasConst.DATA_TYPE));
        // 字段长度
        int dataLength = result.getInt(DbAliasEnum.getAsByDb(this, DbAliasConst.DATA_LENGTH));
        // nvarchar会显示-1
        if("nvarchar".equals(dataType) && dataLength == -1){
            model.setDataType(DataTypeEnum.TEXT.getViewFieldType());
            model.setDefaults("默认");
        }
        return model;
    }

}
