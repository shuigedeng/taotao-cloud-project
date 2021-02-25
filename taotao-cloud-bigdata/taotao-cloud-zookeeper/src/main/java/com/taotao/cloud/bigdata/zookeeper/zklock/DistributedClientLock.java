package com.taotao.cloud.bigdata.zookeeper.zklock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DistributedClientLock {

	// 会话超时
	private static final int SESSION_TIMEOUT = 2000;

	// zookeeper集群地址
	private String hosts = "192.168.56.100:2181,192.168.56.101:2181,192.168.56.102:2181";
	private String groupNode = "locks";
	private String subNode = "sub";
	private boolean haveLock = false;

	private ZooKeeper zk;
	// 记录自己创建的子节点路径
	private volatile String thisPath;

	/**
	 * 连接zookeeper
	 */
	public void connectZookeeper() throws Exception {
		zk = new ZooKeeper(hosts, SESSION_TIMEOUT, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				try {
					// 判断事件类型，此处只处理子节点变化事件
					if (event.getType() == EventType.NodeChildrenChanged && event.getPath().equals("/" + groupNode)) {
						//获取子节点，并对父节点进行监听
						List<String> childrenNodes = zk.getChildren("/" + groupNode, true);
						String thisNode = thisPath.substring(("/" + groupNode + "/").length());
						// 去比较是否自己是最小id
						Collections.sort(childrenNodes);
						if (childrenNodes.indexOf(thisNode) == 0) {
							//访问共享资源处理业务，并且在处理完成之后删除锁
							doSomething();

							//重新注册一把新的锁
							thisPath = zk.create("/" + groupNode + "/" + subNode, null, Ids.OPEN_ACL_UNSAFE,
								CreateMode.EPHEMERAL_SEQUENTIAL);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// 1、程序一进来就先注册一把锁到zk上
		thisPath = zk.create("/" + groupNode + "/" + subNode, null, Ids.OPEN_ACL_UNSAFE,
			CreateMode.EPHEMERAL_SEQUENTIAL);

		// wait一小会，便于观察
		Thread.sleep(new Random().nextInt(1000));

		// 从zk的锁父目录下，获取所有子节点，并且注册对父节点的监听
		List<String> childrenNodes = zk.getChildren("/" + groupNode, true);

		//如果争抢资源的程序就只有自己，则可以直接去访问共享资源
		if (childrenNodes.size() == 1) {
			doSomething();
			thisPath = zk.create("/" + groupNode + "/" + subNode, null, Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL_SEQUENTIAL);
		}
	}

	/**
	 * 处理业务逻辑，并且在最后释放锁
	 */
	private void doSomething() throws Exception {
		try {
			System.out.println("gain lock: " + thisPath);
			Thread.sleep(2000);
		} finally {
			System.out.println("finished: " + thisPath);
			// 锟斤拷thisPath删锟斤拷, 锟斤拷锟斤拷thisPath锟斤拷client锟斤拷锟斤拷锟酵ㄖ�
			// 锟洁当锟斤拷锟酵凤拷锟斤拷
			zk.delete(this.thisPath, -1);
		}
	}

	public static void main(String[] args) throws Exception {
		DistributedClientLock dl = new DistributedClientLock();
		dl.connectZookeeper();
		Thread.sleep(Long.MAX_VALUE);
	}
}

