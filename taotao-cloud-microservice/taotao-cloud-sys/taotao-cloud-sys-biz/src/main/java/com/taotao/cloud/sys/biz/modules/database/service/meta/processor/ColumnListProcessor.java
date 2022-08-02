package com.taotao.cloud.sys.biz.modules.database.service.meta.processor;

import com.sanri.tools.modules.database.service.meta.dtos.ActualTableName;
import com.sanri.tools.modules.database.service.meta.dtos.Column;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColumnListProcessor implements ResultSetHandler<List<Column>> {

    @Override
    public List<Column> handle(ResultSet rs) throws SQLException {
        List<Column> columns = new ArrayList<>();
        while (rs.next()) {
            String catalog = rs.getString("TABLE_CAT");
            String schema = rs.getString("TABLE_SCHEM");
            String tableName = rs.getString("TABLE_NAME");
            ActualTableName actualTableName = new ActualTableName(catalog, schema, tableName);

            String columnName = rs.getString("COLUMN_NAME");
            int dataType = rs.getInt("DATA_TYPE");
            String typeName = rs.getString("TYPE_NAME");
            int columnSize = rs.getInt("COLUMN_SIZE");
            int decimalDigits = rs.getInt("DECIMAL_DIGITS");
            int nullableInt = rs.getInt("NULLABLE");
            String remarks = rs.getString("REMARKS");

            // 对于日期类型, 这里会返回字符串长度, 是不准备的, 手动修改 datetime 为 6
            if ("datetime".equalsIgnoreCase(typeName)) {
                columnSize = 6;
            }
            String autoIncrement = null;

            boolean nullable = nullableInt == 1 ? true : false;
            boolean isAutoIncrement = "YES".equals(autoIncrement) ? true : false;
            final String columnDef = rs.getString("COLUMN_DEF");
            Column column = new Column(actualTableName, columnName, dataType, typeName, columnSize, decimalDigits, nullable, remarks, isAutoIncrement, columnDef);
            columns.add(column);
        }
        return columns;
    }
}
