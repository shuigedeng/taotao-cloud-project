package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.search;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchParam {
    private String catalog;
    private List<String> schemas = new ArrayList<>();
    private String searchSchema;
    private String keyword;

    public SearchParam() {
    }

    public SearchParam(String catalog, List<String> schemas, String searchSchema, String keyword) {
        this.catalog = catalog;
        this.schemas = schemas;
        this.searchSchema = searchSchema;
        this.keyword = keyword;
    }
}
