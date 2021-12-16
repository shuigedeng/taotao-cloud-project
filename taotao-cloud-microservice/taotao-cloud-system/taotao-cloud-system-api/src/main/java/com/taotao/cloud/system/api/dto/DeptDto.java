/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.api.dto;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class DeptDto implements Serializable {

    /** ID */
    private Long id;

    /** 名称 */
    private String name;

    /** 上级部门 */
    private Long pid;

    /** 状态 */
    private Boolean enabled;

    private List<DeptDto> children;

    /** 创建日期 */
    private Timestamp createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<DeptDto> getChildren() {
		return children;
	}

	public void setChildren(List<DeptDto> children) {
		this.children = children;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getLabel() {
        return name;
    }
}
