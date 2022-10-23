package com.taotao.cloud.elasticsearch.esearchx.model;

import org.noear.snack.ONode;

/**
 * es设置
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:08:14
 */
public class EsSetting {

	private final ONode oNode;

	public EsSetting(ONode oNode) {
		this.oNode = oNode;
	}


	public EsSetting set(String name, Object value) {
		oNode.getOrNew("settings").set(name, value);
		return this;
	}

	/**
	 * 设置副本数
	 */
	public EsSetting setNumberOfReplicas(int value) {
		return set("index.number_of_replicas", value);
	}

	/**
	 * 设置刷新时间
	 *
	 * @param value 例：5000,"5s"
	 */
	public EsSetting setRefreshInterval(Object value) {
		return set("index.refresh_interval", value);
	}
}
