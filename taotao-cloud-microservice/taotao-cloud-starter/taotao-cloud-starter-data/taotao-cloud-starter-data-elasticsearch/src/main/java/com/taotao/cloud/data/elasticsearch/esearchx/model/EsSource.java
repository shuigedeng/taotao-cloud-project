package com.taotao.cloud.data.elasticsearch.esearchx.model;

import java.util.Arrays;
import org.noear.snack.ONode;

/**
 * ElasticSearch 字段控制
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:08:27
 */
public class EsSource {

	private final ONode oNode;

	public EsSource(ONode oNode) {
		this.oNode = oNode;
	}

	public EsSource includes(String... includes) {
		oNode.getOrNew("includes").addAll(Arrays.asList(includes));
		return this;
	}

	public EsSource excludes(String... includes) {
		oNode.getOrNew("excludes").addAll(Arrays.asList(includes));
		return this;
	}
}
