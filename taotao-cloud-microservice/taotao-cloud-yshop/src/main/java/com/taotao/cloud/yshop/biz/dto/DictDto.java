/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.api.dto;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class DictDto implements Serializable {

    /** 字典ID */
    private Long id;

    /** 字典名称 */
    private String name;

    private List<DictDetailDto> dictDetails;

    /** 描述 */
    private String remark;

    /** 创建日期 */
    private Timestamp createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DictDetailDto> getDictDetails() {
		return dictDetails;
	}

	public void setDictDetails(List<DictDetailDto> dictDetails) {
		this.dictDetails = dictDetails;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
