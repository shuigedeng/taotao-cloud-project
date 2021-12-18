/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.io.Serializable;

@TableName("gen_config")
public class GenConfig extends SuperEntity<GenConfig, Long> implements Serializable {

    public GenConfig(String tableName) {
        this.cover = false;
        this.moduleName = "yshop-admin";
        this.tableName = tableName;
    }

    @TableId
    private Long id;

    /**表明**/
    private String tableName;

    /** 接口名称 **/
    private String apiAlias;

    /** 包路径 */
    private String pack;

    /** 模块名 */
    private String moduleName;

    /** 前端文件路径 */
    private String path;

    /** 前端文件路径 */
    private String apiPath;

    /** 作者 */
    private String author;

    /** 表前缀 */
    private String prefix;

    /** 是否覆盖 */
    private Boolean cover;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getApiAlias() {
		return apiAlias;
	}

	public void setApiAlias(String apiAlias) {
		this.apiAlias = apiAlias;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getApiPath() {
		return apiPath;
	}

	public void setApiPath(String apiPath) {
		this.apiPath = apiPath;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Boolean getCover() {
		return cover;
	}

	public void setCover(Boolean cover) {
		this.cover = cover;
	}
}
