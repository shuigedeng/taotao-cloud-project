package com.taotao.cloud.workflow.biz.common.database.source.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.taotao.cloud.workflow.biz.common.database.source.DbBase;
import com.taotao.cloud.workflow.biz.common.database.sql.impl.SqlKingbase;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * 金仓模型
 *
 */
public class DbKingbase extends DbBase {



    @Override
    protected void init() {
        setInstance(
                DbBase.KINGBASE_ES,
                DbType.KINGBASE_ES,
                "54321",
                "kingbase8",
                "com.kingbase8.Driver",
                "jdbc:kingbase8://{host}:{port}/{dbname}?currentSchema={schema}",
                new SqlKingbase(this));
    }

    @Override
    public String getConnUrl(String prepareUrl, String host, Integer port, String dbName, String schema) {
        return super.getConnUrl(prepareUrl,host,port,dbName,getCheckSchema(schema));
    }

    @Override
    public DbTableFieldModel getPartFieldModel(ResultSet result) throws SQLException, DataException {
        DbTableFieldModel model = new DbTableFieldModel();
        //主键
        if(result.getString(DbAliasEnum.PRIMARY_KEY.AS()) != null){
            model.setPrimaryKey(DbAliasEnum.PRIMARY_KEY.isTrue());
        }else {
            model.setPrimaryKey(DbAliasEnum.PRIMARY_KEY.isFalse());
        }
        //允空
        if(result.getBoolean(DbAliasEnum.ALLOW_NULL.AS())){
            model.setAllowNull(DbAliasEnum.ALLOW_NULL.isFalse());
        }else {
            model.setAllowNull(DbAliasEnum.ALLOW_NULL.isTrue());
        }
        return model;
    }

    @Override
    public LinkedList<Object> getStructParams(String structParams,String table, DataSourceMod dbSourceOrDbLink) {
        DataSourceDTO dataSourceDTO = dbSourceOrDbLink.convertDTO();
        dataSourceDTO.setDbName(dataSourceDTO.getUserName());
        dataSourceDTO.setDbSchema(getCheckSchema(dataSourceDTO.getDbSchema()));
        return super.getStructParams(structParams, table, dataSourceDTO);
    }

    private String getCheckSchema(String schema){
        if(StringUtil.isEmpty(schema)){
            // 默认public模式
            schema = "public";
        }
        return schema;
    }

}
