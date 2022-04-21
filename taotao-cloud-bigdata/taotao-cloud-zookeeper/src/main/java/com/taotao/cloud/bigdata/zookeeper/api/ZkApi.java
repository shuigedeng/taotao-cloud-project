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
package com.taotao.cloud.bigdata.zookeeper.api;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ZkApi
 *
 * @author shuigedeng
 * @since 2020/12/3 下午6:25
 * @version 1.0.0
 */
@Component
public class ZkApi {

	private static final Logger logger = LoggerFactory.getLogger(ZkApi.class);

	@Autowired
	private ZooKeeper zkClient;


	/**
	 * 判断指定节点是否存在
	 *
	 * @param path
	 * @param needWatch 指定是否复用zookeeper中默认的Watcher
	 * @return
	 */
	public Stat exists(String path, boolean needWatch) {
		try {
			return zkClient.exists(path, needWatch);
		} catch (Exception e) {
			logger.error("【断指定节点是否存在异常】{},{}", path, e);
			return null;
		}
	}

	/**
	 * 检测结点是否存在 并设置监听事件
	 * 三种监听类型： 创建，删除，更新
	 *
	 * @param path
	 * @param watcher 传入指定的监听类
	 * @return
	 */
	public Stat exists(String path, Watcher watcher) {
		try {
			return zkClient.exists(path, watcher);
		} catch (Exception e) {
			logger.error("【断指定节点是否存在异常】{},{}", path, e);
			return null;
		}
	}

	/**
	 * 创建持久化节点
	 *
	 * @param path
	 * @param data
	 */
	public boolean createNode(String path, String data) {
		try {
			zkClient.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			return true;
		} catch (Exception e) {
			logger.error("【创建持久化节点异常】{},{},{}", path, data, e);
			return false;
		}
	}


	/**
	 * 修改持久化节点
	 *
	 * @param path
	 * @param data
	 */
	public boolean updateNode(String path, String data) {
		try {
			//zk的数据版本是从0开始计数的。如果客户端传入的是-1，则表示zk服务器需要基于最新的数据进行更新。如果对zk的数据节点的更新操作没有原子性要求则可以使用-1.
			//version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
			zkClient.setData(path, data.getBytes(), -1);
			return true;
		} catch (Exception e) {
			logger.error("【修改持久化节点异常】{},{},{}", path, data, e);
			return false;
		}
	}

	/**
	 * 删除持久化节点
	 *
	 * @param path
	 */
	public boolean deleteNode(String path) {
		try {
			//version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
			zkClient.delete(path, -1);
			return true;
		} catch (Exception e) {
			logger.error("【删除持久化节点异常】{},{}", path, e);
			return false;
		}
	}

	/**
	 * 获取当前节点的子节点(不包含孙子节点)
	 *
	 * @param path 父节点path
	 */
	public List<String> getChildren(String path) throws KeeperException, InterruptedException {
		return zkClient.getChildren(path, false);
	}

	/**
	 * 获取指定节点的值
	 *
	 * @param path
	 * @return
	 */
	public String getData(String path, Watcher watcher) {
		try {
			Stat stat = new Stat();
			byte[] bytes = zkClient.getData(path, watcher, stat);
			return new String(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
