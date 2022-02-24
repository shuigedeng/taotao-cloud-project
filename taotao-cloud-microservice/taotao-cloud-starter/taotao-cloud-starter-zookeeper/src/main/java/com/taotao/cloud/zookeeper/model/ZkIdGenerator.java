package com.taotao.cloud.zookeeper.model;

import com.taotao.cloud.common.utils.LogUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * ZkIdGenerator
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 11:41:13
 */
public class ZkIdGenerator {

	private static final String path = "/zk-id";

	private final AtomicInteger atomicInteger = new AtomicInteger();

	private final AtomicReference<String> machinePrefix = new AtomicReference<>("");

	private static final String[] AUX_ARRAY = {"", "0", "00", "000", "0000", "00000"};

	private final CuratorFramework curatorFramework;

	public ZkIdGenerator(CuratorFramework curatorFramework) {
		this.curatorFramework = curatorFramework;
	}

	/**
	 * 通过zk获取不一样的机器号，机器号取有序节点最后三位 id格式： 机器号 + 日期 + 小时 + 分钟 + 秒 + 5位递增号码 一秒可分近10w个id 需要对齐可以在每一位补零
	 */
	public Optional<String> genId() {
		if ("".equals(machinePrefix.get())) {
			acquireMachinePrefix();
		}
		if (machinePrefix.get().length() == 0) {
			// get id failed
			return Optional.empty();
		}
		final LocalDateTime now = LocalDateTime.now();
		int aux = atomicInteger.getAndAccumulate(1, ((left, right) -> {
			int val = left + right;
			return val > 99999 ? 1 : val;
		}));

		String time =
			conv2Str(now.getDayOfYear(), 3) + conv2Str(now.getHour(), 2) + conv2Str(now.getMinute(),
				2) + conv2Str(now.getSecond(), 2);
		String suffix = conv2Str(aux, 5);
		return Optional.of(machinePrefix.get() + time + suffix);
	}

	private synchronized void acquireMachinePrefix() {
		if (machinePrefix.get().length() > 0) {
			return;
		}

		try {
			//ZooKeeper zooKeeper = new ZooKeeper(ZooKeeperConstant.SERVERS, 30_000, null);

			final String s = curatorFramework.getZookeeperClient()
				.getZooKeeper().create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT_SEQUENTIAL);
			if (s.length() > 3) {
				machinePrefix.compareAndSet("", s.substring(s.length() - 3));
			}
		} catch (Exception e) {
			LogUtil.error("connect to zookeeper failed, exception is ", e);
		}
	}

	private static String conv2Str(int value, int length) {
		if (length > 5) {
			throw new IllegalArgumentException("length should be less than 5");
		}
		String str = String.valueOf(value);
		return AUX_ARRAY[length - str.length()] + str;
	}
}
