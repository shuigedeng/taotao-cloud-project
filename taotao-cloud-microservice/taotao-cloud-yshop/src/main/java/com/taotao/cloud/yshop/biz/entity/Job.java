/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@TableName("job")
public class Job extends SuperEntity<Job, Long> implements Serializable {

    /** 岗位ID */
    @TableId
    private Long id;

    /** 岗位名称 */
    @NotBlank(message = "岗位名称不能为空")
    private String name;


    /** 岗位状态 */
    private Boolean enabled;

    @TableField(exist = false)
    private Dept dept;

    /** 岗位排序 */
    private Long sort;


    /** 部门ID */
    private Long deptId;



    public void copy(Job source) {

        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
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

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
}
