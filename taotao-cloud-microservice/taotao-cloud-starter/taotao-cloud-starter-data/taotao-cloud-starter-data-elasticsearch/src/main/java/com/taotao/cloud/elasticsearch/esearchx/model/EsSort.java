package com.taotao.cloud.elasticsearch.esearchx.model;

import org.noear.snack.ONode;

/**
 * es排序
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:08:22
 */
public class EsSort {

	private final ONode oNode;

	public EsSort(ONode oNode) {
		this.oNode = oNode;
	}

	public EsSort addByAes(String field) {
		oNode.addNew().getOrNew(field).set("order", "asc");

		return this;
	}

	public EsSort addByDesc(String field) {
		oNode.addNew().getOrNew(field).set("order", "desc");

		return this;
	}
}
