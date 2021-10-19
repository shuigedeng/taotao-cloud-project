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
package com.taotao.cloud.uc.biz.utils;

import com.taotao.cloud.uc.api.bo.resource.ResourceBO;
import com.taotao.cloud.uc.api.vo.resource.ResourceTreeVO;
import com.taotao.cloud.uc.api.vo.resource.TreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * TreeUtil
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/21 11:20
 */
public class TreeUtil {

	/**
	 * 两层循环实现建树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @param parentId  父节点
	 * @return java.util.List<T>
	 * @author shuigedeng
	 * @since 2020/10/21 11:21
	 */
	public static <T extends TreeNode> List<T> build(List<T> treeNodes, Long parentId) {
		List<T> trees = new ArrayList<>();
		for (T treeNode : treeNodes) {
			if (parentId.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}
			for (T it : treeNodes) {
				if (it.getParentId().equals(treeNode.getId())) {
					if (treeNode.getChildren().size() != 0) {
						treeNode.setHasChildren(true);
					}
					treeNode.add(it);
				}
			}
		}
		return trees;
	}


	/**
	 * 使用递归方法建树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @param parentId  父节点
	 * @return java.util.List<T>
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/10/21 11:22
	 */
	public static <T extends TreeNode> List<T> recursiveBuild(List<T> treeNodes, Long parentId) {
		List<T> trees = new ArrayList<T>();
		for (T treeNode : treeNodes) {
			if (parentId.equals(treeNode.getParentId())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点
	 *
	 * @param treeNode  节点
	 * @param treeNodes 子节点列表
	 * @return T
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/10/21 11:23
	 */
	public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId().equals(it.getParentId())) {
				if (treeNode.getChildren().size() != 0) {
					treeNode.setHasChildren(true);
				}
				treeNode.add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}

	/**
	 * 通过SysResource创建树形节点
	 *
	 * @param resources 资源列表
	 * @param parentId  父id
	 * @return java.util.List<ResourceTree>
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/10/21 11:23
	 */
	public static List<ResourceTreeVO> buildTree(List<ResourceBO> resources, Long parentId) {
		List<ResourceTreeVO> trees = new ArrayList<>();
		ResourceTreeVO node;
		for (ResourceBO resource : resources) {
			node = new ResourceTreeVO();
			node.setId(resource.getId());
			node.setParentId(resource.getParentId());
			node.setName(resource.getName());
			node.setPath(resource.getPath());
			node.setPerms(resource.getPerms());
			node.setLabel(resource.getName());
			node.setIcon(resource.getIcon());
			node.setType(resource.getType());
			node.setSort(resource.getSortNum());
			node.setHasChildren(false);
			node.setChildren(new ArrayList<>());
			node.setKeepAlive(resource.getKeepAlive());
			trees.add(node);
		}
		return TreeUtil.build(trees, parentId);
	}

}
