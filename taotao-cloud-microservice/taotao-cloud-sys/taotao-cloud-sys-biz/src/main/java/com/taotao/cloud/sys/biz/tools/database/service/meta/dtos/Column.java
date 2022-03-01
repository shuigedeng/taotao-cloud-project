package com.taotao.cloud.sys.biz.tools.database.service.meta.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Column {
    @JsonIgnore
    private ActualTableName actualTableName;
    private String columnName;
    // javax.sql.Types
    private int dataType;
    private String typeName;
    private int columnSize;
    private int decimalDigits;
    private boolean nullable;
    private String remark;
    private boolean autoIncrement;
    private String defaultValue;

    public Column() {
    }

    public Column(ActualTableName actualTableName, String columnName, int dataType, String typeName, int columnSize, int decimalDigits, boolean nullable, String remark, boolean autoIncrement,String defaultValue) {
        this.actualTableName = actualTableName;
        this.columnName = columnName;
        this.dataType = dataType;
        this.typeName = typeName;
        this.columnSize = columnSize;
        this.decimalDigits = decimalDigits;
        this.nullable = nullable;
        this.remark = remark;
        this.autoIncrement = autoIncrement;
        this.defaultValue = defaultValue;
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

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
