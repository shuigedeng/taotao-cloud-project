package com.taotao.cloud.data.sync.batch.mybatis;

/**
 * @Author : JCccc
 * @CreateTime : 2020/3/17
 * @Description :
 **/
public class BlogInfo {

	private Integer id;
	private String blogAuthor;
	private String blogUrl;
	private String blogTitle;
	private String blogItem;

	@Override
	public String toString() {
		return "BlogInfo{" +
			"id=" + id +
			", blogAuthor='" + blogAuthor + '\'' +
			", blogUrl='" + blogUrl + '\'' +
			", blogTitle='" + blogTitle + '\'' +
			", blogItem='" + blogItem + '\'' +
			'}';
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBlogAuthor() {
		return blogAuthor;
	}

	public void setBlogAuthor(String blogAuthor) {
		this.blogAuthor = blogAuthor;
	}

	public String getBlogUrl() {
		return blogUrl;
	}

	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}

	public String getBlogTitle() {
		return blogTitle;
	}

	public void setBlogTitle(String blogTitle) {
		this.blogTitle = blogTitle;
	}

	public String getBlogItem() {
		return blogItem;
	}

	public void setBlogItem(String blogItem) {
		this.blogItem = blogItem;
	}
}
