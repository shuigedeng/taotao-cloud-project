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

public class PermissionDto implements Serializable {

    private Long id;

    private String name;

    private Long pid;

    private String alias;

    private Timestamp createTime;

    private List<PermissionDto> children;

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pid=" + pid +
                ", alias='" + alias + '\'' +
                ", createTime=" + createTime +
                '}';
    }

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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<PermissionDto> getChildren() {
		return children;
	}

	public void setChildren(List<PermissionDto> children) {
		this.children = children;
	}
}
