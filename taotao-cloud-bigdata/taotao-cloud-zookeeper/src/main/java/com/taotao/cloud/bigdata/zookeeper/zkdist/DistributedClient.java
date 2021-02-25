package com.taotao.cloud.bigdata.zookeeper.zkdist;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;

public class DistributedClient {

	private static final String connectString = "192.168.56.100:2181,192.168.56.101:2181,192.168.56.102:2181";
	private static final int sessionTimeout = 2000;
	private static final String parentNode = "/servers";

	// 注意:加volatile的意义何在？
	private volatile List<String> serverList;
	private ZooKeeper zk = null;

	/**
	 * 创建到zk的客户端连接
	 *
	 * @throws Exception
	 */
	public void getConnect() throws Exception {
		zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				// 收到事件通知后的回调函数（应该是我们自己的事件处理逻辑）
				try {
					//重新更新服务器列表，并且注册了监听
					getServerList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取服务器信息列表
	 *
	 * @throws Exception
	 */
	public void getServerList() throws Exception {
		// 获取服务器子节点信息，并且对父节点进行监听
		List<String> children = zk.getChildren(parentNode, true);

		// 先创建一个局部的list来存服务器信息
		List<String> servers = new ArrayList<>();

		for (String child : children) {
			// child只是子节点的节点名
			byte[] data = zk.getData(parentNode + "/" + child, false, null);
			servers.add(new String(data));
		}
		// 把servers赋值给成员变量serverList，已提供给各业务线程使用
		serverList = servers;

		//打印服务器列表
		System.out.println(serverList);

	}

	/**
	 * 业务功能
	 *
	 * @throws InterruptedException
	 */
	public void handleBussiness() throws InterruptedException {
		System.out.println("client start working.....");
		Thread.sleep(Long.MAX_VALUE);
	}


	public static void main(String[] args) throws Exception {
		// 获取zk连接
		DistributedClient client = new DistributedClient();
		client.getConnect();

		// 获取servers的子节点信息（并监听），从中获取服务器信息列表
		client.getServerList();

		// 业务线程启动
		client.handleBussiness();
	}

}
