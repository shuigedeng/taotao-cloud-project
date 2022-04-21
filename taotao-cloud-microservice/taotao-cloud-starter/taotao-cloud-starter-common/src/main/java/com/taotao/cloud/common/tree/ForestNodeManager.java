/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 森林管理类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:20:48
 */
public class ForestNodeManager<T extends INode> {

	/**
	 * 森林的所有节点
	 */
	private List<T> list;

	/**
	 * 森林的父节点ID
	 */
	private List<Long> parentIds = new ArrayList<>();

	public ForestNodeManager() {
	}

	public ForestNodeManager(List<T> items) {
		list = items;
	}

	public ForestNodeManager(List<T> list, List<Long> parentIds) {
		this.list = list;
		this.parentIds = parentIds;
	}

	/**
	 * 根据节点ID获取一个节点
	 *
	 * @param id 节点ID
	 * @return INode对象
	 * @since 2021-09-02 22:20:56
	 */
	public INode getTreeNodeAT(Long id) {
		for (INode forestNode : list) {
			if (forestNode.getId().longValue() == id.longValue()) {
				return forestNode;
			}
		}
		return null;
	}

	/**
	 * 增加父节点ID
	 *
	 * @param parentId 父节点
	 * @since 2021-09-02 22:21:06
	 */
	public void addParentId(Long parentId) {
		parentIds.add(parentId);
	}

	/**
	 * 获取树的根节点(一个森林对应多颗树)
	 *
	 * @return 根节点数据
	 * @since 2021-09-02 22:21:16
	 */
	public List<T> getRoot() {
		List<T> roots = new ArrayList<>();
		for (T forestNode : list) {
			if (forestNode.getParentId() == 0 || parentIds.contains(forestNode.getId())) {
				roots.add(forestNode);
			}
		}
		return roots;
	}

	@Override
	public String toString() {
		return "ForestNodeManager{" +
			"list=" + list +
			", parentIds=" + parentIds +
			'}';
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ForestNodeManager<?> that = (ForestNodeManager<?>) o;
		return Objects.equals(list, that.list) && Objects.equals(parentIds,
			that.parentIds);
	}

	@Override
	public int hashCode() {
		return Objects.hash(list, parentIds);
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public List<Long> getParentIds() {
		return parentIds;
	}

	public void setParentIds(List<Long> parentIds) {
		this.parentIds = parentIds;
	}
}
