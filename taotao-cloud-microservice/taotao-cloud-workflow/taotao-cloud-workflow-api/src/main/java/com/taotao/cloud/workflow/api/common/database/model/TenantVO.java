package com.taotao.cloud.workflow.api.common.database.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class TenantVO implements Serializable {

    private String dbName;

    /**
     * 配置连接
     */
    private List<TenantLinkModel> linkList;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<TenantLinkModel> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<TenantLinkModel> linkList) {
        this.linkList = linkList;
    }
}
