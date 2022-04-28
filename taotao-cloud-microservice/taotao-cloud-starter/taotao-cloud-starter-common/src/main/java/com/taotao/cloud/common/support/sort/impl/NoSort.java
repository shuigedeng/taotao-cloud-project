package com.taotao.cloud.common.support.sort.impl;

import com.taotao.cloud.common.support.sort.ISort;

import java.util.List;

/**
 * 不进行任何排序
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:11:47
 */
public class NoSort<T> implements ISort<T> {

	@Override
	public List<T> sort(List<T> list) {
		return list;
	}

}
