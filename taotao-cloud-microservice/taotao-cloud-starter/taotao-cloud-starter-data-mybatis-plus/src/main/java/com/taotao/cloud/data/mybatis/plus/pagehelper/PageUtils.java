package com.taotao.cloud.data.mybatis.plus.pagehelper;

import com.github.pagehelper.PageInfo;
import com.taotao.cloud.common.utils.lang.StringUtil;
import java.io.Serializable;

public class PageUtils implements Serializable {

	private static final long serialVersionUID = 377943433889798799L;

	public static <T> PagedList<T> exportPagedList(PageParam<T> pageParam) {
		PagedList<T> pl = new PagedList<T>();
		// pagesize
		int pageSize = pageParam.getPageSize();
		if (pageSize <= 0) {
			pageSize = 10;
		} else {
			pl.setPageSize(pageSize);
		}
		int pageNum = pageParam.getPageNum();
		pl.setPageNum(pageNum);
		String orderBy = pageParam.getOrderBy();
		if (StringUtil.isNotEmpty(
			orderBy)) {
			//防止sql注入
			String orderBySql = SQLFilter.sqlInject(orderBy);
			pl.setOrderBy(orderBySql);
		}
		return pl;
	}

	public static <T> PagedList<T> toPageList(PageInfo<T> spage) {
		PagedList<T> pagedList = new PagedList<T>();
		pagedList.setPageSize((int) spage.getPageSize());
		pagedList.setPageNum((int) spage.getPageNum());
		pagedList.setRecordCount((int) spage.getTotal());
		pagedList.setData(spage.getList());
		pagedList.setPageCount((int) spage.getPages());
		return pagedList;
	}
}
