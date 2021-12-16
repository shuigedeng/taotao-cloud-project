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

@TableName("picture")
public class Picture extends SuperEntity<Picture, Long> implements Serializable {

    /** ID */
    @TableId
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "id")
    private Long id;


    /** 上传日期 */
    // @Column(name = "create_time")
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;


    /** 删除的URL */
    // @Column(name = "delete_url")
    private String deleteUrl;


    /** 图片名称 */
    // @Column(name = "filename")
    private String filename;


    /** 图片高度 */
    // @Column(name = "height")
    private String height;


    /** 图片大小 */
    // @Column(name = "size")
    private String size;


    /** 图片地址 */
    // @Column(name = "url")
    private String url;


    /** 用户名称 */
    // @Column(name = "username")
    private String username;


    /** 图片宽度 */
    // @Column(name = "width")
    private String width;


    /** 文件的MD5值 */
    // @Column(name = "md5code")
    private String md5code;

    public void copy(Picture source) {
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getMd5code() {
		return md5code;
	}

	public void setMd5code(String md5code) {
		this.md5code = md5code;
	}
}
