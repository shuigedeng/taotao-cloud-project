package com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel;

import java.util.List;

public class DataTable<T> {
	// 当前记录总数
	Long recordsFiltered;
	// 页面大小
	Integer length;
	// guid
	String draw;
	// 总记录数
	Long recordsTotal;
	// 记录数据
	List<T> data;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public String getDraw() {
		return draw;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}

	public Long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public Long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}
	
}
