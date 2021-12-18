/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;

import java.io.Serializable;
import java.sql.Timestamp;

@TableName("local_storage")
public class LocalStorage extends SuperEntity<LocalStorage, Long> implements Serializable{

    @TableId
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "id")
    private Long id;


    /** 文件真实的名称 */
    // @Column(name = "real_name")
    private String realName;


    /** 文件名 */
    // @Column(name = "name")
    private String name;


    /** 后缀 */
    // @Column(name = "suffix")
    private String suffix;


    /** 路径 */
    // @Column(name = "path")
    private String path;


    /** 类型 */
    // @Column(name = "type")
    private String type;


    /** 大小 */
    // @Column(name = "size")
    private String size;


    /** 操作人 */
    // @Column(name = "operate")
    private String operate;


    /** 创建日期 */
    // @Column(name = "create_time")
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;


    public LocalStorage(String realName, String name, String suffix, String path, String type, String size, String operate) {
        this.realName = realName;
        this.name = name;
        this.suffix = suffix;
        this.path = path;
        this.type = type;
        this.size = size;
        this.operate = operate;
    }

    public void copy(LocalStorage source) {
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
