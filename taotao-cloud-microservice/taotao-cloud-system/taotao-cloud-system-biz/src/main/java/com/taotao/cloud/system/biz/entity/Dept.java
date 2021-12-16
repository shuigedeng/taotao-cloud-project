/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.*;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@TableName("dept")
public class Dept extends SuperEntity<Dept, Long> implements Serializable {

    /** ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /** 名称 */
    @NotBlank(message = "部门名称不能为空")
    private String name;


    /** 上级部门 */
    @NotNull(message = "上级部门不能为空")
    private Long pid;


    /** 状态 */
    private Boolean enabled;



    public void copy(Dept source) {

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
}
