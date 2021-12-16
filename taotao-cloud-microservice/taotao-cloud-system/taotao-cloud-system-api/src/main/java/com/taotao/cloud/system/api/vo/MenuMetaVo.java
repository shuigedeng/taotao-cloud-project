/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.api.vo;

import java.io.Serializable;

public class MenuMetaVo implements Serializable {

    private String title;

    private String icon;

    private Boolean noCache;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Boolean getNoCache() {
		return noCache;
	}

	public void setNoCache(Boolean noCache) {
		this.noCache = noCache;
	}
}
