package com.taotao.cloud.elasticsearch.esearchx.model;

import org.noear.snack.ONode;

/**
 * es突出领域
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:07:49
 */
public class EsHighlightField {

	private final ONode oNode;

	public EsHighlightField(ONode oNode) {
		this.oNode = oNode;
	}


	public EsHighlightField preTags(String tags) {
		oNode.set("pre_tags", tags);
		return this;
	}

	public EsHighlightField postTags(String tags) {
		oNode.set("post_tags", tags);
		return this;
	}

	public EsHighlightField requireMatch(boolean requireMatch) {
		oNode.set("require_field_match", requireMatch);
		return this;
	}
}
