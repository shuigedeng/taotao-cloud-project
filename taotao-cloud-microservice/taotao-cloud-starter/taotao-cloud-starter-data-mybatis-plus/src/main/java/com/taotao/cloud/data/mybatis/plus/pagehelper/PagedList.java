package com.taotao.cloud.data.mybatis.plus.pagehelper;

import com.taotao.cloud.common.utils.lang.ObjectUtil;
import java.io.Serializable;
import java.util.List;

public class PagedList<T> implements Serializable {

	private static final long serialVersionUID = -1253790062865437768L;
	private int pageNum = 1;
	private List<T> data = null;
	private int pageCount = 0;
	private int recordCount = -1;
	private int pagingType = 0;
	private int pageSize;
	private String orderBy;

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		if (pageSize <= 0) {
			return;
		}
		this.pageSize = pageSize;
	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount *            the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		if (pageCount <= 0) {
			return;
		}
		this.pageCount = pageCount;
	}

	/**
	 * @return the recordCount
	 */
	public int getRecordCount() {
		return recordCount;
	}

	/**
	 * @param recordCount *            the recordCount to set
	 */
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
		calcPageCount();
	}

	private void calcPageCount() {
		if (this.recordCount < 0) {
			return;
		}
		int tmp = this.recordCount % getPageSize();
		this.pageCount = (tmp == 0 ? (this.recordCount / getPageSize()) : (this.recordCount / getPageSize() + 1));
		if (this.pageNum > this.pageCount && this.pageCount != 0) {
			this.pageNum = this.pageCount;
		}
		this.pageNum = this.pageCount;
	}

	public void setData(List<T> data) {
		this.data = data;
		if (ObjectUtil.isNotEmpty(data) && this.recordCount == -1) {
			this.recordCount = data.size();
		}
	}

	public List<T> getData() {
		return data;
	}

	/**
	 * @return the pagingType
	 */
	public int getPagingType() {
		return pagingType;
	}

	/**
	 * @param pagingType *            the pagingType to set
	 */
	public void setPagingType(int pagingType) {
		this.pagingType = pagingType;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public String getOrderBy() {
		return orderBy;
	}
}
