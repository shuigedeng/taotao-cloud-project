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
package com.taotao.cloud.zookeeper.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import java.util.List;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.Assert;

/**
 * ZookeeperTemplate
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:39:37
 */
public class ZookeeperTemplate {

	private final CuratorFramework client;

	public ZookeeperTemplate(CuratorFramework client) {
		this.client = client;
	}

	/**
	 * 创建空节点，默认持久节点
	 *
	 * @param path 节点路径
	 * @param node 节点名称
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-07 20:39:47
	 */
	public String createNode(String path, String node) throws Exception {
		return createNode(path, node, CreateMode.PERSISTENT);
	}

	/**
	 * 创建带类型的空节点
	 *
	 * @param path       节点路径
	 * @param node       节点名称
	 * @param createMode 类型
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-07 20:39:57
	 */
	public String createNode(String path, String node, CreateMode createMode) throws Exception {
		path = buildPath(path, node);
		client.create()
			.orSetData()
			.creatingParentsIfNeeded()
			.withMode(createMode)
			.forPath(path);
		return path;
	}


	/**
	 * 创建节点，默认持久节点
	 *
	 * @param path  节点路径
	 * @param node  节点名称
	 * @param value 节点值
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-07 20:40:07
	 */
	public String createNode(String path, String node, String value) throws Exception {
		return createNode(path, node, value, CreateMode.PERSISTENT);
	}

	/**
	 * 创建节点，默认持久节点
	 *
	 * @param path       节点路径
	 * @param node       节点名称
	 * @param value      节点值
	 * @param createMode 节点类型
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-07 20:40:15
	 */
	public String createNode(String path, String node, String value, CreateMode createMode)
		throws Exception {
		Assert.isTrue(StrUtil.isNotEmpty(value), "zookeeper节点值不能为空!");

		path = buildPath(path, node);
		client.create()
			.orSetData()
			.creatingParentsIfNeeded()
			.withMode(createMode)
			.forPath(path, value.getBytes());
		return path;
	}

	/**
	 * 获取节点数据
	 *
	 * @param path 路径
	 * @param node 节点名称
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-07 20:40:25
	 */
	public String get(String path, String node) throws Exception {
		path = buildPath(path, node);
		byte[] bytes = client.getData().forPath(path);
		if (bytes.length > 0) {
			return new String(bytes);
		}
		return null;
	}

	/**
	 * 更新节点数据
	 *
	 * @param path  节点路径
	 * @param node  节点名称
	 * @param value 更新值
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-07 20:40:33
	 */
	public String update(String path, String node, String value) throws Exception {
		Assert.isTrue(StrUtil.isNotEmpty(value), "zookeeper节点值不能为空!");

		path = buildPath(path, node);
		client.setData().forPath(path, value.getBytes());
		return path;
	}

	/**
	 * 删除节点，并且递归删除子节点
	 *
	 * @param path 路径
	 * @param node 节点名称
	 * @author shuigedeng
	 * @since 2021-09-07 20:40:41
	 */
	public void delete(String path, String node) throws Exception {
		path = buildPath(path, node);
		client.delete().quietly().deletingChildrenIfNeeded().forPath(path);
	}

	/**
	 * 获取子节点
	 *
	 * @param path 节点路径
	 * @return {@link java.util.List }
	 * @author shuigedeng
	 * @since 2021-09-07 20:40:48
	 */
	public List<String> getChildren(String path) throws Exception {
		if (StrUtil.isEmpty(path)) {
			return null;
		}

		if (!path.startsWith(CommonConstant.PATH_SPLIT)) {
			path = CommonConstant.PATH_SPLIT + path;
		}
		return client.getChildren().forPath(path);
	}

	/**
	 * 判断节点是否存在
	 *
	 * @param path 路径
	 * @param node 节点名称
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-07 20:40:56
	 */
	public boolean exists(String path, String node) throws Exception {
		List<String> list = getChildren(path);
		return CollUtil.isNotEmpty(list) && list.contains(node);
	}

	/**
	 * 对一个节点进行监听，监听事件包括指定的路径节点的增、删、改的操作
	 *
	 * @param path     节点路径
	 * @param listener 回调方法
	 * @author shuigedeng
	 * @since 2021-09-07 20:41:03
	 */
	public void watchNode(String path, NodeCacheListener listener) {
		CuratorCacheListener curatorCacheListener = CuratorCacheListener.builder()
			.forNodeCache(listener)
			.build();
		CuratorCache curatorCache = CuratorCache.builder(client, path).build();
		curatorCache.listenable().addListener(curatorCacheListener);
		curatorCache.start();
	}


	/**
	 * 对指定的路径节点的一级子目录进行监听，不对该节点的操作进行监听，对其子目录的节点进行增、删、改的操作监听
	 *
	 * @param path     节点路径
	 * @param listener 回调方法
	 * @author shuigedeng
	 * @since 2021-09-07 20:41:10
	 */
	public void watchChildren(String path, PathChildrenCacheListener listener) {
		CuratorCacheListener curatorCacheListener = CuratorCacheListener.builder()
			.forPathChildrenCache(path, client, listener)
			.build();
		CuratorCache curatorCache = CuratorCache.builder(client, path).build();
		curatorCache.listenable().addListener(curatorCacheListener);
		curatorCache.start();
	}

	/**
	 * 将指定的路径节点作为根节点（祖先节点），对其所有的子节点操作进行监听，呈现树形目录的监听
	 *
	 * @param path     节点路径
	 * @param maxDepth 回调方法
	 * @param listener 监听
	 * @author shuigedeng
	 * @since 2021-09-07 20:41:17
	 */
	public void watchTree(String path, int maxDepth, TreeCacheListener listener) {
		CuratorCacheListener curatorCacheListener = CuratorCacheListener.builder()
			.forTreeCache(client, listener)
			.build();
		CuratorCache curatorCache = CuratorCache.builder(client, path).build();
		curatorCache.listenable().addListener(curatorCacheListener);
		curatorCache.start();
	}

	/**
	 * 转换路径
	 *
	 * @param path 路径
	 * @param node 节点名
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-07 20:41:26
	 */
	private String buildPath(String path, String node) {
		Assert.isTrue(StrUtil.isNotEmpty(path) && StrUtil.isNotEmpty(node)
			, "zookeeper路径或者节点名称不能为空！");

		if (!path.startsWith(CommonConstant.PATH_SPLIT)) {
			path = CommonConstant.PATH_SPLIT + path;
		}

		if (CommonConstant.PATH_SPLIT.equals(path)) {
			return path + node;
		} else {
			return path + CommonConstant.PATH_SPLIT + node;
		}
	}
}
