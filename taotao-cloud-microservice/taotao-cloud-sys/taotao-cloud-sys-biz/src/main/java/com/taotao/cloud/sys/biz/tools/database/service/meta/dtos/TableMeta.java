package com.taotao.cloud.sys.biz.tools.database.service.meta.dtos;


import java.util.ArrayList;
import java.util.List;

public class TableMeta {
    private Table table;
    private List<Column> columns = new ArrayList<>();

    public TableMeta() {
    }

    public TableMeta(Table table, List<Column> columns) {
        this.table = table;
        this.columns = columns;
    }

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(
		List<Column> columns) {
		this.columns = columns;
	}
}
