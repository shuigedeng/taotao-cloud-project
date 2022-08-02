package com.taotao.cloud.sys.biz.modules.database.service.meta.dtos;

import lombok.Data;

@Data
public class Schema {
    private String schema;
    private String catalog;

    public Schema() {
    }

    public Schema(String schema, String catalog) {
        this.schema = schema;
        this.catalog = catalog;
    }
}
