/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.tree;

import java.util.List;

/**
 * 节点归并类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:21:27
 */
public class ForestNodeMerger {

	/**
	 * 将节点数组归并为一个森林（多棵树）（填充节点的children域） 时间复杂度为O(n^2)
	 *
	 * @param items 节点域
	 * @return {@link java.util.List }
	 * @since 2021-09-02 22:21:41
	 */
	public static <T extends INode> List<T> merge(List<T> items) {
		ForestNodeManager<T> forestNodeManager = new ForestNodeManager<>(items);
		items.forEach(forestNode -> {
			if (forestNode.getParentId() != 0) {
				INode node = forestNodeManager.getTreeNodeAT(forestNode.getParentId());
				if (node != null) {
					node.getChildren().add(forestNode);
				} else {
					forestNodeManager.addParentId(forestNode.getId());
				}
			}
		});
		return forestNodeManager.getRoot();
	}
}
