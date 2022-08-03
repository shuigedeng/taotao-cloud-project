package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成的 sql 信息
 */
@Data
public class SqlList {
    private String dbType;
    private List<String> sqls = new ArrayList<>();

    public SqlList() {
    }

    public SqlList(String dbType) {
        this.dbType = dbType;
    }

    public void addSql(String sql){
        sqls.add(sql);
    }
}
