package com.taotao.cloud.sys.biz.elasticsearch.pagemodel;

public class BucketResult {
	String visittime;
	
	Long docnumber;
	
	public BucketResult(String visittime, Long docnumber) {
		super();
		this.visittime = visittime;
		this.docnumber = docnumber;
	}

	public String getVisittime() {
		return visittime;
	}

	public void setVisittime(String visittime) {
		this.visittime = visittime;
	}

	public Long getDocnumber() {
		return docnumber;
	}

	public void setDocnumber(Long docnumber) {
		this.docnumber = docnumber;
	}
	
}
