/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.api.dto;


import com.taotao.cloud.system.api.annotation.Query;
import java.sql.Timestamp;
import java.util.List;

public class LocalStorageQueryCriteria {

    @Query(blurry = "name,suffix,type,operate,size")
    private String blurry;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

	public String getBlurry() {
		return blurry;
	}

	public void setBlurry(String blurry) {
		this.blurry = blurry;
	}

	public List<Timestamp> getCreateTime() {
		return createTime;
	}

	public void setCreateTime(List<Timestamp> createTime) {
		this.createTime = createTime;
	}
}
