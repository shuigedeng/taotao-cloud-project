package com.taotao.cloud.sys.biz.tools.database.service.meta.processor;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Table;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class TableListProcessor implements ResultSetHandler<List<Table>> {
    @Override
    public List<Table> handle(ResultSet rs) throws SQLException {
        List<Table> tables = new ArrayList<>();
        while (rs.next()) {
            String catalog = rs.getString("TABLE_CAT");
            String schema = rs.getString("TABLE_SCHEM");
            String tableName = rs.getString("TABLE_NAME");
            String remarks = rs.getString("REMARKS");
//                String remarks = null;
//                try {
//                    // 解决注释字段乱码问题
//                    byte[] remarksBytes = rs.getBytes("REMARKS");
//                    if (remarksBytes != null) {
//                        remarks = new String(remarksBytes, "UTF-8");
//                    }
//                } catch (UnsupportedEncodingException e) {}
            ActualTableName actualTableName = new ActualTableName(catalog, schema, tableName);
            Table table = new Table(actualTableName, remarks);
            tables.add(table);
        }
        return tables;
    }
}
