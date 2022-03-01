package com.taotao.cloud.sys.biz.tools.swaggerdoc.service;

import io.swagger.models.Info;

import java.util.List;

/**
 * swagger 文档信息
 */
public class Doc {
    private Info info;
    private List<Table> tables;

    public Doc() {
    }

    public Doc(Info info, List<Table> tables) {
        this.info = info;
        this.tables = tables;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
}
