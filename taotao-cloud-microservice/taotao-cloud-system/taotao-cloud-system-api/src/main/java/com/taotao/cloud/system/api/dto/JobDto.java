/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.api.dto;


import java.io.Serializable;
import java.sql.Timestamp;

public class JobDto implements Serializable {

    private Long id;

    private Long sort;

    private String name;

    private Boolean enabled;

    private DeptDto dept;

    private String deptSuperiorName;

    private Timestamp createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public DeptDto getDept() {
		return dept;
	}

	public void setDept(DeptDto dept) {
		this.dept = dept;
	}

	public String getDeptSuperiorName() {
		return deptSuperiorName;
	}

	public void setDeptSuperiorName(String deptSuperiorName) {
		this.deptSuperiorName = deptSuperiorName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	//    public JobDto(String name, Boolean enabled) {
//        this.name = name;
//        this.enabled = enabled;
//    }
}
