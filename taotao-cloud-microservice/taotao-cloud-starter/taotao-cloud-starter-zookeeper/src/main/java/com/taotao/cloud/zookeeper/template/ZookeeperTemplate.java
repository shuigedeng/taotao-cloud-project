/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
import com.taotao.cloud.common.utils.log.LogUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
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
	public String createNode(String path, String node) {
		return createNode(path, node, CreateMode.PERSISTENT);
	}

	public String createNode(String path, CreateMode mode) {
		try {
			return client
				.create()
				.withMode(mode)
				.forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public void setNodeData(String path, String nodeData) {
		try {
			// 设置节点数据
			client.setData().forPath(path, nodeData.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createNodeAndData(CreateMode mode, String path, String nodeData) {
		try {
			// 创建节点，关联数据
			client.create().creatingParentsIfNeeded().withMode(mode)
				.forPath(path, nodeData.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getNodeData(String path) {
		try {
			// 数据读取和转换
			byte[] dataByte = client.getData().forPath(path);
			String data = new String(dataByte, StandardCharsets.UTF_8);
			if (StrUtil.isNotEmpty(data)) {
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getNodeChild(String path) {
		List<String> nodeChildDataList = new ArrayList<>();
		try {
			// 节点下数据集
			nodeChildDataList = client.getChildren().forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nodeChildDataList;
	}

	public void deleteNode(String path, Boolean recursive) {
		try {
			if (recursive) {
				// 递归删除节点
				client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
			} else {
				// 删除单个节点
				client.delete().guaranteed().forPath(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听 给定节点的创建、更新（不包括删除） 以及 该节点下的子节点的创建、删除、更新动作。
	 */
	public void addWatcherWithTreeCache(String path) {
		if (null == client) {
			throw new RuntimeException("there is not connect to zkServer...");
		}
		CuratorCache treeCache = CuratorCache.build(client, path);
		CuratorCacheListener listener = (type, oldData,  data) -> {
			LogUtils.info("节点路径 --{} ,节点事件类型: {} , 节点值为: {}",
				Objects.nonNull(data.getData()) ? Arrays.toString(data.getData()) : "无数据", type);
		};
		treeCache.listenable().addListener(listener);
		try {
			treeCache.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听给定节点下的子节点的创建、删除、更新
	 *
	 * @author shuigedeng
	 * @since 2022-02-09 16:21:47
	 */
	public void addWatcherWithChildCache(String path) {
		if (null == client) {
			throw new RuntimeException("there is not connect to zkServer...");
		}
		//cacheData if true, node contents are cached in addition to the stat
		CuratorCache pathChildrenCache =CuratorCache.build(client, path);
		CuratorCacheListener listener = (type,  oldData,  data) -> {
			LogUtils.info("event path is --{} ,event type is {}", data.getData(), type);
		};
		pathChildrenCache.listenable().addListener(listener);
		// StartMode : NORMAL  BUILD_INITIAL_CACHE  POST_INITIALIZED_EVENT
		try {
			//pathChildrenCache.start(PathChildrenCache.StartMode.NORMAL);
			pathChildrenCache.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建 给定节点的监听事件  监听一个节点的更新和创建事件(不包括删除)
	 */
	public void addWatcherWithNodeCache(String path) {
		if (null == client) {
			throw new RuntimeException("there is not connect to zkServer...");
		}
		// dataIsCompressed if true, data in the path is compressed
		CuratorCache nodeCache = CuratorCache.build(client, path);
		CuratorCacheListener listener = (type,  oldData,  data) -> {
			Optional<ChildData> currentData = nodeCache.get(path);
			LogUtils.info("{} Znode data is chagnge,new data is ---  {}",
				currentData.get().getPath(),
				new String(currentData.get().getData()));
		};
		nodeCache.listenable().addListener(listener);
		try {
			nodeCache.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public String createNode(String path, String node, CreateMode createMode) {
		try {
			path = buildPath(path, node);
			client.create()
				.orSetData()
				.creatingParentsIfNeeded()
				.withMode(createMode)
				.forPath(path);
			return path;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	public String createNode(String path, String node, String value) {
		try {
			return createNode(path, node, value, CreateMode.PERSISTENT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	public String createNode(String path, String node, String value, CreateMode createMode) {
		Assert.isTrue(StrUtil.isNotEmpty(value), "zookeeper节点值不能为空!");

		try {
			path = buildPath(path, node);
			client.create()
				.orSetData()
				.creatingParentsIfNeeded()
				.withMode(createMode)
				.forPath(path, value.getBytes());
			return path;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	public String get(String path, String node){
		try {
			path = buildPath(path, node);
			byte[] bytes = client.getData().forPath(path);
			if (bytes.length > 0) {
				return new String(bytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	public String update(String path, String node, String value){
		Assert.isTrue(StrUtil.isNotEmpty(value), "zookeeper节点值不能为空!");

		try {
			path = buildPath(path, node);
			client.setData().forPath(path, value.getBytes());
			return path;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除节点，并且递归删除子节点
	 *
	 * @param path 路径
	 * @param node 节点名称
	 * @author shuigedeng
	 * @since 2021-09-07 20:40:41
	 */
	public void delete(String path, String node)  {
		path = buildPath(path, node);
		try {
			client.delete().quietly().deletingChildrenIfNeeded().forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取子节点
	 *
	 * @param path 节点路径
	 * @return {@link java.util.List }
	 * @author shuigedeng
	 * @since 2021-09-07 20:40:48
	 */
	public List<String> getChildren(String path) {
		if (StrUtil.isEmpty(path)) {
			return null;
		}

		if (!path.startsWith(CommonConstant.PATH_SPLIT)) {
			path = CommonConstant.PATH_SPLIT + path;
		}
		try {
			return client.getChildren().forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	public boolean exists(String path, String node) {
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
			,"zookeeper路径或者节点名称不能为空！");

		if (!path.startsWith(CommonConstant.PATH_SPLIT)) {
			path = CommonConstant.PATH_SPLIT + path;
		}

		if (CommonConstant.PATH_SPLIT.equals(path)) {
			return path + node;
		} else {
			return path + CommonConstant.PATH_SPLIT + node;
		}
	}

	public boolean exists(String path) {
		try {
			return client.checkExists().forPath(path) != null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
