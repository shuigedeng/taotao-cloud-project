package com.taotao.cloud.zookeeper.model;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taotao.cloud.common.utils.LogUtil;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 *
 * 使用ZooKeeper做主备选举，商用代码和demo的主要差距就在能否正确处理SessionTimeout，如果不能正确处理SessionTimeout，主备选举的代码难以自愈。LeaderElectionService接收三个输入参数：
 *
 * scene为场景，用来防止不同场景下主备选举zkPath冲突
 * serverId serverId用来区分主备不同实例，通常使用ip地址或hostname
 * LeaderLatchListener 主备回调函数
 */
public class LeaderElectionService {

	private final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(
		"zookeeper-init").build();

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,
		threadFactory);

	private final CuratorFramework framework;

	private final LeaderLatch leaderLatch;

	private final String zkPath;

	public LeaderElectionService(String scene, String serverId, LeaderLatchListener listener) {
		this.framework = CuratorFrameworkFactory.newClient(ZooKeeperConstant.SERVERS,
			new ExponentialBackoffRetry(1000, 3));
		this.zkPath = String.format("/election/%s", scene);
		this.leaderLatch = new LeaderLatch(framework, zkPath, serverId);
		leaderLatch.addListener(listener);
		executorService.execute(this::init);
	}

	private void init() {
		initStep1();
		initStep2();
		initStep3();
		executorService.shutdown();
	}

	private void initStep1() {
		while (true) {
			try {
				this.framework.create().creatingParentsIfNeeded()
					.withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(zkPath);
				break;
			} catch (Exception e) {
				LogUtil.error("create parent path exception is ", e);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	private void initStep2() {
		while (true) {
			try {
				this.framework.start();
				break;
			} catch (Exception e) {
				LogUtil.error("create parent path exception is ", e);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	private void initStep3() {
		while (true) {
			try {
				this.leaderLatch.start();
				break;
			} catch (Exception e) {
				LogUtil.error("create parent path exception is ", e);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	public void close() {
		if (leaderLatch != null) {
			try {
				leaderLatch.close();
			} catch (Exception e) {
				LogUtil.error("leader latch close exception ", e);
			}
		}
		if (framework != null) {
			try {
				framework.close();
			} catch (Exception e) {
				LogUtil.error("frame close exception ", e);
			}
		}
	}

	public static class ConnListener implements ConnectionStateListener {

		private final String path;

		private final String serverId;

		public ConnListener(String path, String serverId) {
			this.path = path;
			this.serverId = serverId;
		}


		@Override
		public void stateChanged(CuratorFramework client, ConnectionState newState) {
			if (newState != ConnectionState.LOST) {
				return;
			}
			while (true) {
				try {
					client.getZookeeperClient().blockUntilConnectedOrTimedOut();
					client.create().creatingParentsIfNeeded()
						.withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
						.forPath(path, serverId.getBytes(StandardCharsets.UTF_8));
					break;
				} catch (Exception e) {
					LogUtil.error("rebuild exception ", e);
				}
			}
		}
	}

}
