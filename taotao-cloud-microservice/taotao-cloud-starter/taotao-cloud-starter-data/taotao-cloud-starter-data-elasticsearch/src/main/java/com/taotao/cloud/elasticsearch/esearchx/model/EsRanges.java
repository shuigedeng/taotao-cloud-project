package com.taotao.cloud.elasticsearch.esearchx.model;

import org.noear.snack.ONode;

/**
 * es范围
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:08:08
 */
public class EsRanges {

	private final ONode oNode;

	public EsRanges(ONode oNode) {
		this.oNode = oNode;
	}

	public EsRanges add(Object from, Object to) {
		ONode oNode1 = oNode.addNew();

		if (from != null) {
			oNode1.set("from", from);
		}

		if (to != null) {
			oNode1.set("to", to);
		}

		return this;
	}
}
