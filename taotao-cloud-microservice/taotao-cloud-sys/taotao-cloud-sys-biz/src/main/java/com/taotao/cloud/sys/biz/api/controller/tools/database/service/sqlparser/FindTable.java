package com.taotao.cloud.sys.biz.api.controller.tools.database.service.sqlparser;

import lombok.Data;

import java.util.List;

@Data
public class FindTable {
    private String name;
    private String alias;

    public FindTable() {
    }

    public FindTable(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }
}
