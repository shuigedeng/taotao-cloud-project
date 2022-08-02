package com.taotao.cloud.sys.biz.modules.database.service.sqlparser;

import com.sanri.tools.modules.database.service.meta.dtos.Column;
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
