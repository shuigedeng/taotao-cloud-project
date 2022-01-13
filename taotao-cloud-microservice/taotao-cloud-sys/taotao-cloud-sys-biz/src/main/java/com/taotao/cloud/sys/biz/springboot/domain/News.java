package com.taotao.cloud.sys.biz.springboot.domain;
/**
 * 
 * @author duhongming
 *
 */
public class News {
	
	private String docno;
	private String contenttitle;
	private String content;
	private String url;
	
	public News() {
		super();
	}
	
	public News(String docno, String contenttitle, String content, String url) {
		super();
		this.docno = docno;
		this.contenttitle = contenttitle;
		this.content = content;
		this.url = url;
	}

	public String getDocno() {
		return docno;
	}
	public void setDocno(String docno) {
		this.docno = docno;
	}
	public String getContenttitle() {
		return contenttitle;
	}
	public void setContenttitle(String contenttitle) {
		this.contenttitle = contenttitle;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
