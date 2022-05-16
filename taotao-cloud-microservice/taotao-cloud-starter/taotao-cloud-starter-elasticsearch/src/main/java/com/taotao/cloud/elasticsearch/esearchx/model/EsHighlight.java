package com.taotao.cloud.elasticsearch.esearchx.model;

import java.util.function.Consumer;
import org.noear.snack.ONode;

/**
 * es突出
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:07:42
 */
public class EsHighlight {

	private final ONode oNode;

	public EsHighlight(ONode oNode) {
		this.oNode = oNode;
	}

	public EsHighlight addField(String field, Consumer<EsHighlightField> consumer) {
		ONode oNode1 = oNode.getOrNew("fields").addNew().getOrNew(field);
		consumer.accept(new EsHighlightField(oNode1));
		return this;
	}
}
