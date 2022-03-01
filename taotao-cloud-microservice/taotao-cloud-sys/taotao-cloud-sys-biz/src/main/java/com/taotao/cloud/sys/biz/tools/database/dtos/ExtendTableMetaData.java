package com.taotao.cloud.sys.biz.tools.database.dtos;

import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;

public class ExtendTableMetaData extends TableMetaData {
    private TableMark tableMark;

    public ExtendTableMetaData(TableMark tableMark) {
        this.tableMark = tableMark;
    }

    public ExtendTableMetaData(TableMetaData tableMetaData,TableMark tableMark) {
        super(tableMetaData.getActualTableName(), tableMetaData.getTable(), tableMetaData.getColumns(), tableMetaData.getIndexs(), tableMetaData.getPrimaryKeys());
        this.tableMark = tableMark;
    }

	public TableMark getTableMark() {
		return tableMark;
	}

	public void setTableMark(TableMark tableMark) {
		this.tableMark = tableMark;
	}
}
