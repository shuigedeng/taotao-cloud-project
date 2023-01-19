package com.taotao.cloud.workflow.biz.common.database.source.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import java.sql.ResultSet;

/**
 * MySQL模型
 *
 */
public class DbMySQL extends DbBase {

    @Override
    protected void init(){
        setInstance(
                DbBase.MYSQL,
                DbType.MYSQL,
                "3306",
                "mysql",
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://{host}:{port}/{dbname}?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8&useSSL=false",
                //connUrl = "jdbc:mysql://{host}:{port}/{dbname}?useUnicode=true&autoReconnect=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
                new SqlMySQL(this));
    }

    @Override
    public DbTableFieldModel getPartFieldModel(ResultSet result) throws Exception {
        DbTableFieldModel model = new DbTableFieldModel();
        // 精度
        String precision = result.getString(DbAliasConst.PRECISION);
        DataTypeModel dataTypeModel = getDataTypeModel(result.getString(DbAliasEnum.getAsByDb(this, DbAliasConst.DATA_TYPE)));
        if (dataTypeModel != null) {
            if (!StringUtil.isEmpty(precision) || dataTypeModel.getPrecisionMax() != null) {
                model.setDataLength(precision + "," + result.getString(DbAliasConst.DECIMALS));
            }
        }
        return model;
    }

}
