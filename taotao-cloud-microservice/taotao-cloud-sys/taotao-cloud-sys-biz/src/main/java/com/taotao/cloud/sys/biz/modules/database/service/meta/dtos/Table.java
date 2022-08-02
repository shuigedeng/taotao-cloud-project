package com.taotao.cloud.sys.biz.modules.database.service.meta.dtos;

import lombok.Data;

@Data
public class Table {
    private ActualTableName actualTableName;
    private String remark;

    public Table() {
    }

    public Table(ActualTableName actualTableName, String remark) {
        this.actualTableName = actualTableName;
        this.remark = remark;
    }

    /**
     * 获取表名
     * @return
     */
    public String getTableName(){
        return this.actualTableName.getTableName();
    }
}
