package com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel;

import java.util.Date;

public class ResultData {
	Long NumberFound;
	
	Integer start;
	
	Date qtime;
	
	Object data;
	
	String scrollid;

	public Long getNumberFound() {
		return NumberFound;
	}

	public void setNumberFound(Long numberFound) {
		NumberFound = numberFound;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}


	public Date getQtime() {
		return qtime;
	}

	public void setQtime(Date qtime) {
		this.qtime = qtime;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getScrollid() {
		return scrollid;
	}

	public void setScrollid(String scrollid) {
		this.scrollid = scrollid;
	}
    
}
