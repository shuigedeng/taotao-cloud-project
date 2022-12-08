package com.taotao.cloud.data.mybatisplus.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.taotao.cloud.common.model.PageParam;
import java.util.List;

public class MpPage<T> extends PageDTO<T> {

	public MpPage() {
	}

	public MpPage(long current, long size) {
		super(current, size);
	}

	public MpPage(long current, long size, long total) {
		super(current, size, total);
	}

	public MpPage(long current, long size, boolean searchCount) {
		super(current, size, searchCount);
	}

	public MpPage(long current, long size, long total, boolean searchCount) {
		super(current, size, total, searchCount);
	}

	public static <T> Page<T> of(long current, long size) {
		return of(current, size, 0L);
	}

	public static <T> Page<T> of(long current, long size, long total) {
		return of(current, size, total, true);
	}

	public static <T> Page<T> of(long current, long size, boolean searchCount) {
		return of(current, size, 0L, searchCount);
	}

	public static <T> Page<T> of(long current, long size, long total, boolean searchCount) {
		return new MpPage<T>(current, size, total, searchCount);
	}

	public static <T> Page<T> of(PageParam pageParam, List<T> records) {
		Page<T> page = of(pageParam.getCurrentPage(), pageParam.getCurrentPage(),
			records.size());
		page.setRecords(records);
		return page;
	}
}
