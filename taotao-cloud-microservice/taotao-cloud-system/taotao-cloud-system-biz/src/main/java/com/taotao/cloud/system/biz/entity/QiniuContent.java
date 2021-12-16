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

@TableName("qiniu_content")
public class QiniuContent extends SuperEntity<QiniuContent, Long> implements Serializable{

    /** ID */
    @TableId
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "id")
    private Long id;


    /** Bucket 识别符 */
    // @Column(name = "bucket")
    private String bucket;


    /** 文件名称 */
    @TableField("name")
    private String name;


    /** 文件大小 */
    // @Column(name = "size")
    private String size;


    /** 文件类型：私有或公开 */
    // @Column(name = "type")
    private String type;


    /** 上传或同步的时间 */
    // @Column(name = "update_time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;


    /** 文件url */
    // @Column(name = "url")
    private String url;


    // @Column(name = "suffix")
    private String suffix;


    public void copy(QiniuContent source) {
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

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
