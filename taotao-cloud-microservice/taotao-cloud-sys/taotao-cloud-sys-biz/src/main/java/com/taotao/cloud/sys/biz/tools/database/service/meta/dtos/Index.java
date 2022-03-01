package com.taotao.cloud.sys.biz.tools.database.service.meta.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Index {
    @JsonIgnore
    private ActualTableName actualTableName;
    private boolean unique;
    private String indexName;
    private short indexType;
    private short ordinalPosition;
    private String columnName;

    public Index() {
    }

    public Index(ActualTableName actualTableName, boolean unique, String indexName, short indexType, short ordinalPosition, String columnName) {
        this.actualTableName = actualTableName;
        this.unique = unique;
        this.indexName = indexName;
        this.indexType = indexType;
        this.ordinalPosition = ordinalPosition;
        this.columnName = columnName;
    }

	public ActualTableName getActualTableName() {
		return actualTableName;
	}

	public void setActualTableName(
		ActualTableName actualTableName) {
		this.actualTableName = actualTableName;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public short getIndexType() {
		return indexType;
	}

	public void setIndexType(short indexType) {
		this.indexType = indexType;
	}

	public short getOrdinalPosition() {
		return ordinalPosition;
	}

	public void setOrdinalPosition(short ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
