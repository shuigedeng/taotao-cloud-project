/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.io.Serializable;

@TableName("column_config")
public class ColumnConfig extends SuperEntity<ColumnConfig, Long> implements Serializable {

    @TableId
    private Long id;

    private String tableName;

    /** 数据库字段名称 */
    private String columnName;

    /** 数据库字段类型 */
    private String columnType;

    /**  数据库字段键类型 */
    private String keyType;

    /** 字段额外的参数 */
    private String extra;

    /** 数据库字段描述 */
    private String remark;

    /** 必填 */
    private Boolean notNull;

    /** 是否在列表显示 */
    private Boolean listShow;

    /** 是否表单显示 */
    private Boolean formShow;

    /** 表单类型 */
    private String formType;

    /** 查询 1:模糊 2：精确 */
    private String queryType;

    /** 字典名称 */
    private String dictName;

    /** 日期注解 */
    private String dateAnnotation;

    public ColumnConfig(String tableName, String columnName, Boolean notNull, String columnType, String remark, String keyType, String extra) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.columnType = columnType;
        this.keyType = keyType;
        this.extra = extra;
        this.notNull = notNull;
        //if (GenUtil.PK.equalsIgnoreCase(keyType) && GenUtil.EXTRA.equalsIgnoreCase(extra)) {
        //    this.notNull = false;
        //}
        this.remark = remark;
        this.listShow = true;
        this.formShow = true;
    }

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getNotNull() {
		return notNull;
	}

	public void setNotNull(Boolean notNull) {
		this.notNull = notNull;
	}

	public Boolean getListShow() {
		return listShow;
	}

	public void setListShow(Boolean listShow) {
		this.listShow = listShow;
	}

	public Boolean getFormShow() {
		return formShow;
	}

	public void setFormShow(Boolean formShow) {
		this.formShow = formShow;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDateAnnotation() {
		return dateAnnotation;
	}

	public void setDateAnnotation(String dateAnnotation) {
		this.dateAnnotation = dateAnnotation;
	}
}
