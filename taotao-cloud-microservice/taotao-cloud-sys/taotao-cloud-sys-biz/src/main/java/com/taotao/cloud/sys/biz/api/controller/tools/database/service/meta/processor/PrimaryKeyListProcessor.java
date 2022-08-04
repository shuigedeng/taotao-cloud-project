package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.processor;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.PrimaryKey;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class PrimaryKeyListProcessor implements ResultSetHandler<List<PrimaryKey>> {

    @Override
    public List<PrimaryKey> handle(ResultSet rs) throws SQLException {
        List<PrimaryKey> primaryKeys = new ArrayList<>();
        while (rs.next()) {
            String catalog = rs.getString("TABLE_CAT");
            String schema = rs.getString("TABLE_SCHEM");
            String tableName = rs.getString("TABLE_NAME");
            ActualTableName actualTableName = new ActualTableName(catalog, schema, tableName);

            String columnName = rs.getString("COLUMN_NAME");
            short keySeq = rs.getShort("KEY_SEQ");
            String pkName = rs.getString("PK_NAME");

            PrimaryKey primaryKey = new PrimaryKey(actualTableName, columnName, keySeq, pkName);
            primaryKeys.add(primaryKey);
        }
        return primaryKeys;
    }
}
