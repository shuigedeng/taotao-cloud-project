package com.taotao.cloud.sys.biz.tools.database.service.meta.dtos;


public class Table {
    private ActualTableName actualTableName;
    private String remark;

    public Table() {
    }

    public Table(ActualTableName actualTableName, String remark) {
        this.actualTableName = actualTableName;
        this.remark = remark;
    }

	public ActualTableName getActualTableName() {
		return actualTableName;
	}

	public void setActualTableName(
		ActualTableName actualTableName) {
		this.actualTableName = actualTableName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
