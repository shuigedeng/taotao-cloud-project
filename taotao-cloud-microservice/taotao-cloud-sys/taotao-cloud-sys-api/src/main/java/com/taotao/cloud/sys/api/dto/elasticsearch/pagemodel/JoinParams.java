package com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel;

public class JoinParams {
	String name;
	
	Integer pagenum;
	
	Integer pagesize;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPagenum() {
		return pagenum;
	}

	public void setPagenum(Integer pagenum) {
		this.pagenum = pagenum;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}
	
}
