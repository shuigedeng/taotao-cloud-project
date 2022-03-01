package com.taotao.cloud.sys.biz.elasticsearch.pagemodel;

import java.util.Date;

/**
 * Description:检索参数
 */
public class QueryCommand {
	// 表名
	String indexname;
	//关键词
	private String keyWords;
	//搜索域
	private String search_field;
	//逻辑连接词
	private String operator;
	// 排序
	String sort;
	//起始位置
	private int start;
	//返回条数
	private int rows;
	//返回字段
	private String return_filed;
	
	String startdate;

	String enddate;
	// 聚集字段
	String aggsField;
	// 步长
	Integer step;
	// 滚动分页id
	String scrollid;
	
	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getSearch_field() {
		return search_field;
	}

	public void setSearch_field(String search_field) {
		this.search_field = search_field;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getReturn_filed() {
		return return_filed;
	}

	public void setReturn_filed(String return_filed) {
		this.return_filed = return_filed;
	}


	public String getIndexname() {
		return indexname;
	}

	public void setIndexname(String indexname) {
		this.indexname = indexname;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

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

	public String getAggsField() {
		return aggsField;
	}

	public void setAggsField(String aggsField) {
		this.aggsField = aggsField;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getScrollid() {
		return scrollid;
	}

	public void setScrollid(String scrollid) {
		this.scrollid = scrollid;
	}
	
}
