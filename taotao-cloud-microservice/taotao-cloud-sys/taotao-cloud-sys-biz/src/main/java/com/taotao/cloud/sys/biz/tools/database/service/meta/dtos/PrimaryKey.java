package com.taotao.cloud.sys.biz.tools.database.service.meta.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
public class PrimaryKey {
    @JsonIgnore
    private ActualTableName actualTableName;
    private String columnName;
    private int keySeq;
    private String pkName;

    public PrimaryKey() {
    }

    public PrimaryKey(ActualTableName actualTableName, String columnName, int keySeq, String pkName) {
        this.actualTableName = actualTableName;
        this.columnName = columnName;
        this.keySeq = keySeq;
        this.pkName = pkName;
    }

	public ActualTableName getActualTableName() {
		return actualTableName;
	}

	public void setActualTableName(
		ActualTableName actualTableName) {
		this.actualTableName = actualTableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getKeySeq() {
		return keySeq;
	}

	public void setKeySeq(int keySeq) {
		this.keySeq = keySeq;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
}
