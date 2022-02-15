/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.api.vo.generator;

public class TableInfo {

    /** 表名称 */
    private Object tableName;

    /** 创建日期 */
    private Object createTime;

    /** 数据库引擎 */
    private Object engine;

    /** 编码集 */
    private Object coding;

    /** 备注 */
    private Object remark;

	public Object getTableName() {
		return tableName;
	}

	public void setTableName(Object tableName) {
		this.tableName = tableName;
	}

	public Object getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Object createTime) {
		this.createTime = createTime;
	}

	public Object getEngine() {
		return engine;
	}

	public void setEngine(Object engine) {
		this.engine = engine;
	}

	public Object getCoding() {
		return coding;
	}

	public void setCoding(Object coding) {
		this.coding = coding;
	}

	public Object getRemark() {
		return remark;
	}

	public void setRemark(Object remark) {
		this.remark = remark;
	}
}
