package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class Column {
    @JsonIgnore
    private ActualTableName actualTableName;
    private String columnName;
    /**
     * {@link javax.sql.Types}
     */
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

    /**
     * 值比较方法
     * @param other
     * @return
     */
    public boolean equalsValues(Column other,boolean ignoreCase){
        if (other == null) {
            return false;
        }

        if (ignoreCase) {
            return StringUtils.compareIgnoreCase(other.getColumnName(), columnName) == 0
                    && dataType == other.getDataType()
                    && StringUtils.compareIgnoreCase(other.getTypeName(), typeName) == 0
                    && columnSize == other.getColumnSize()
                    && decimalDigits == other.getDecimalDigits()
                    && nullable == other.isNullable()
                    && StringUtils.compareIgnoreCase(remark, other.getRemark()) == 0
                    && autoIncrement == other.isAutoIncrement()
                    && StringUtils.compareIgnoreCase(defaultValue, other.getDefaultValue()) == 0;
        }
        return StringUtils.compare(other.getColumnName(), columnName) == 0
                && dataType == other.getDataType()
                && StringUtils.compare(other.getTypeName(), typeName) == 0
                && columnSize == other.getColumnSize()
                && decimalDigits == other.getDecimalDigits()
                && nullable == other.isNullable()
                && StringUtils.compare(remark, other.getRemark()) == 0
                && autoIncrement == other.isAutoIncrement()
                && StringUtils.compare(defaultValue, other.getDefaultValue()) == 0;
    }
}
