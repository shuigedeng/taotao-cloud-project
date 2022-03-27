package com.taotao.cloud.web.schedule.common.utils.proxy;

import java.util.List;

/**
 * Chain 
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 15:17:23
 */
public class Chain {

	private List<Point> list;
	private int index = -1;

	public List<Point> getList() {
		return list;
	}

	public void setList(List<Point> list) {
		this.list = list;
	}

	public int getIndex() {
		return index;
	}

	/**
	 * 索引自增1
	 */
	public int incIndex() {
		return ++index;
	}

	/**
	 * 索引还原
	 */
	public void resetIndex() {
		this.index = -1;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Chain() {
	}

	public Chain(List<Point> list) {
		this.list = list;
	}
}
