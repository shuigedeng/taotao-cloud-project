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
package com.taotao.cloud.web.tree;

import java.util.List;

/**
 * 节点归并类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @see https://blog.csdn.net/u014424628/article/details/51765394?utm_source=blogxgwz2
 * @since 2021/8/24 23:06
 */
public class ForestNodeMerger {

	/**
	 * 将节点数组归并为一个森林（多棵树）（填充节点的children域） 时间复杂度为O(n^2)
	 *
	 * @param items 节点域
	 * @return java.util.List<T> 多棵树的根节点集合
	 * @author shuigedeng
	 * @since 2021/8/24 23:06
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
