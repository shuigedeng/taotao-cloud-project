package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.processor;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Index;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IndexListProcessor implements ResultSetHandler<List<Index>> {

    @Override
    public List<Index> handle(ResultSet rs) throws SQLException {
        List<Index> indices = new ArrayList<>();
        while (rs.next()) {
            String catalog = rs.getString("TABLE_CAT");
            String schema = rs.getString("TABLE_SCHEM");
            String tableName = rs.getString("TABLE_NAME");
            ActualTableName actualTableName = new ActualTableName(catalog, schema, tableName);

            boolean nonUnique = rs.getBoolean("NON_UNIQUE");
            String indexName = rs.getString("INDEX_NAME");
            short type = rs.getShort("TYPE");
            short ordinalPosition = rs.getShort("ORDINAL_POSITION");
            String columnName = rs.getString("COLUMN_NAME");
            Index index = new Index(actualTableName, !nonUnique, indexName, type, ordinalPosition, columnName);
            indices.add(index);
        }
        return indices;
    }
}
