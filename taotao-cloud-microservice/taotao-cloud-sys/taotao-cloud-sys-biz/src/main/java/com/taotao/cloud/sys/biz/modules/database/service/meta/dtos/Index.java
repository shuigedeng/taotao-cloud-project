package com.taotao.cloud.sys.biz.modules.database.service.meta.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = "actualTableName")
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
}
