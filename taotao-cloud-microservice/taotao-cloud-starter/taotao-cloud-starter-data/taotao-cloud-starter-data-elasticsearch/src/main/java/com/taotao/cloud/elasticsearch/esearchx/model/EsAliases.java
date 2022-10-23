package com.taotao.cloud.elasticsearch.esearchx.model;

import org.noear.snack.ONode;

/**
 * es别名
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:06:34
 */
public class EsAliases {

	private final ONode oNode;

	public EsAliases(ONode oNode) {
		this.oNode = oNode;
	}

	public EsAliases add(String indiceName, String alias) {
		oNode.addNew().getOrNew("add").set("index", indiceName).set("alias", alias);
		return this;
	}

	public EsAliases remove(String indiceName, String alias) {
		oNode.addNew().getOrNew("remove").set("index", indiceName).set("alias", alias);
		return this;
	}
}
