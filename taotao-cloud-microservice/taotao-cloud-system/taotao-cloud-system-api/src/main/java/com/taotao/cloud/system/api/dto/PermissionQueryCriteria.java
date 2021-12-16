/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.api.dto;


import com.taotao.cloud.system.api.annotation.Query;

public class PermissionQueryCriteria {

    // 多字段模糊
    @Query(blurry = "name,alias")
    private String blurry;

	public String getBlurry() {
		return blurry;
	}

	public void setBlurry(String blurry) {
		this.blurry = blurry;
	}
}
