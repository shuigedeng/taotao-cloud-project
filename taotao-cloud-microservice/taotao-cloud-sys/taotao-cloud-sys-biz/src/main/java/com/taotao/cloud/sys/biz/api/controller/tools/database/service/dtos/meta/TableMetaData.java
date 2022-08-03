package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TableMetaData {
    protected ActualTableName actualTableName;
    protected Table table;
    protected List<Column> columns = new ArrayList<>();
    protected List<Index> indices = new ArrayList<>();
    protected List<PrimaryKey> primaryKeys = new ArrayList<>();

    public TableMetaData() {
    }

    public TableMetaData(ActualTableName actualTableName, Table table, List<Column> columns, List<Index> indices, List<PrimaryKey> primaryKeys) {
        this.actualTableName = actualTableName;
        this.table = table;
        this.columns = columns;
        this.indices = indices;
        this.primaryKeys = primaryKeys;
    }
}
