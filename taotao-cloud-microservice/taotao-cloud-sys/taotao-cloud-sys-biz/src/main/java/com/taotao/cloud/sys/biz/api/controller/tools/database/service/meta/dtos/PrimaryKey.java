package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
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
}
