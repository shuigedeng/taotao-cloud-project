package com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel;


public class FilterCommand {
	String startdate;
	
	String enddate;
	
	String field;

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
}
