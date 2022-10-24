package com.taotao.cloud.data.elasticsearch.esearchx.model;

import org.noear.snack.ONode;

/**
 * es范围
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:08:01
 */
public class EsRange {

	private final ONode oNode;

	public EsRange(ONode oNode) {
		this.oNode = oNode;
	}

	public EsRange gt(Object value) {
		oNode.set("gt", value);
		return this;
	}

	public EsRange gte(Object value) {
		oNode.set("gte", value);
		return this;
	}

	public EsRange lt(Object value) {
		oNode.set("lt", value);
		return this;
	}

	public EsRange lte(Object value) {
		oNode.set("lte", value);
		return this;
	}
}
