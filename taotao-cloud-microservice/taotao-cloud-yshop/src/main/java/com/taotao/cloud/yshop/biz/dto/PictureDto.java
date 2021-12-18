/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.api.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class PictureDto implements Serializable {

    /** ID */
    private Long id;

    /** 上传日期 */
    private Timestamp createTime;

    /** 删除的URL */
    private String deleteUrl;

    /** 图片名称 */
    private String filename;

    /** 图片高度 */
    private String height;

    /** 图片大小 */
    private String size;

    /** 图片地址 */
    private String url;

    /** 用户名称 */
    private String username;

    /** 图片宽度 */
    private String width;

    /** 文件的MD5值 */
    private String md5code;

	public Long getId() {
		return id;
	}

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
