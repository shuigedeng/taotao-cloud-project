/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.api.dto;

import com.taotao.cloud.system.api.annotation.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public class JobQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    private Boolean enabled;

    @Query
    private Long deptId;

    @Query(propName = "deptId", type = Query.Type.IN)
    private Set<Long> deptIds;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

	public JobQueryCriteria(){}

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

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Set<Long> getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(Set<Long> deptIds) {
		this.deptIds = deptIds;
	}

	public List<Timestamp> getCreateTime() {
		return createTime;
	}

	public void setCreateTime(List<Timestamp> createTime) {
		this.createTime = createTime;
	}
}
