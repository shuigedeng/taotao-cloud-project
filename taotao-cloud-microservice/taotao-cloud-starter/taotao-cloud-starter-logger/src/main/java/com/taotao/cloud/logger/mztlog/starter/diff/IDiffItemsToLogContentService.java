package com.taotao.cloud.logger.mztlog.starter.diff;

import de.danielbechler.diff.node.DiffNode;

public interface IDiffItemsToLogContentService {

    String toLogContent(DiffNode diffNode, final Object o1, final Object o2);
}
