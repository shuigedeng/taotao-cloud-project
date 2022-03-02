package com.taotao.cloud.sys.biz.elasticsearch.pagemodel;

import java.util.List;



public class DataGrid<T> {
	private Integer current;//当前页面号
	private Integer rowCount;//每页行数
	private Long total;//总行数
	private List<T> rows;
	
	public Integer getCurrent() {
		return current;
	}
	public void setCurrent(Integer current) {
		this.current = current;
	}
	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
}
