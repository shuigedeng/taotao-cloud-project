package com.taotao.cloud.sys.biz.modules.database.service.dtos.meta;

import com.sanri.tools.modules.database.service.meta.dtos.Column;
import com.sanri.tools.modules.database.service.meta.dtos.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TableMeta {
    private Table table;
    private List<Column> columns = new ArrayList<>();

    public TableMeta() {
    }

    public TableMeta(Table table, List<Column> columns) {
        this.table = table;
        this.columns = columns;
    }
}
