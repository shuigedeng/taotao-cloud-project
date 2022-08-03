package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.processor;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SchemaListProcessor implements ResultSetHandler<List<Schema>> {
	@Override
	public List<Schema> handle(ResultSet rs) throws SQLException {
		List<Schema> schemaList = new ArrayList<>();
		final int columnCount = rs.getMetaData().getColumnCount();
		while (rs.next()) {
			String schema = rs.getString("TABLE_SCHEM");
			String catalog = null;
			if (columnCount > 1) {
				catalog = rs.getString("TABLE_CATALOG");
			}
			schemaList.add(new Schema(schema, catalog));
		}
		return schemaList;
	}
}
