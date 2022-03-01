package com.taotao.cloud.sys.biz.tools.database.service.meta.dtos;


import java.util.ArrayList;
import java.util.List;

public class TableMetaData {

	protected ActualTableName actualTableName;
	protected Table table;
	protected List<Column> columns = new ArrayList<>();
	protected List<Index> indexs = new ArrayList<>();
	protected List<PrimaryKey> primaryKeys = new ArrayList<>();

	public TableMetaData() {
	}

	public TableMetaData(ActualTableName actualTableName, Table table, List<Column> columns,
		List<Index> indexs, List<PrimaryKey> primaryKeys) {
		this.actualTableName = actualTableName;
		this.table = table;
		if (columns != null) {
			this.columns.addAll(columns);
		}
		if (indexs != null) {
			this.indexs.addAll(indexs);
		}
		if (primaryKeys != null) {
			this.primaryKeys.addAll(primaryKeys);
		}
	}

	public ActualTableName getActualTableName() {
		return actualTableName;
	}

	public void setActualTableName(
		ActualTableName actualTableName) {
		this.actualTableName = actualTableName;
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

	public List<Index> getIndexs() {
		return indexs;
	}

	public void setIndexs(
		List<Index> indexs) {
		this.indexs = indexs;
	}

	public List<PrimaryKey> getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(
		List<PrimaryKey> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
}
