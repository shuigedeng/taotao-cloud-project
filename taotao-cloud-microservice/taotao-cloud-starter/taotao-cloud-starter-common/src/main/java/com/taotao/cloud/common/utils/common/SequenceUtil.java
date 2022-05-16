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
package com.taotao.cloud.common.utils.common;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 分布式高效有序ID生成器 优化开源项目：http://git.oschina.net/yu120/sequence
 * <p>
 * Twitter_Snowflake<br> SnowFlake的结构如下(每部分用-分开):<br> 0 - 0000000000 0000000000 0000000000
 * 0000000000 0 - 00000 - 00000 - 000000000000 <br> 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截) 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T
 * = (1L {@code <<} 41) / (1000L * 60 * 60 * 24 * 365) = 69<br> 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br> 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 * </p>
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 14:53:15
 */
public final class SequenceUtil {

	/**
	 * 时间起始标记点，作为基准，一般取系统的最近时间（一旦确定不能变动）
	 */
	private final long epoch = 1288834974657L;
	/**
	 * 机器标识位数
	 */
	private final long workerIdBits = 5L;
	/**
	 * datacenterIdBits
	 */
	private final long datacenterIdBits = 5L;
	/**
	 * maxWorkerId
	 */
	private final long maxWorkerId = ~(-1L << workerIdBits);
	/**
	 * maxDatacenterId
	 */
	private final long maxDatacenterId = ~(-1L << datacenterIdBits);
	/**
	 * 毫秒内自增位
	 */
	private final long sequenceBits = 12L;
	/**
	 * workerIdShift
	 */
	private final long workerIdShift = sequenceBits;
	/**
	 * datacenterIdShift
	 */
	private final long datacenterIdShift = sequenceBits + workerIdBits;
	/**
	 * 时间戳左移动位
	 */
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	/**
	 * sequenceMask
	 */
	private final long sequenceMask = ~(-1L << sequenceBits);
	/**
	 * workerId
	 */
	private long workerId = 0L;

	/**
	 * 数据标识 ID 部分
	 */
	private long datacenterId = 0L;
	/**
	 * 并发控制
	 */
	private long sequence = 0L;
	/**
	 * 上次生产 ID 时间戳
	 */
	private long lastTimestamp = -1L;

	/**
	 * 时间回拨最长时间(ms)，超过这个时间就抛出异常
	 */
	private long timestampOffset = 5L;

	public SequenceUtil() {
		this.datacenterId = getDatacenterId(maxDatacenterId);
		this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
	}

	/**
	 * 有参构造器
	 *
	 * @param workerId     工作机器 ID
	 * @param datacenterId 序列号
	 * @since 2021-09-02 14:54:22
	 */
	public SequenceUtil(long workerId, long datacenterId) {
		Assert.isFalse(workerId > maxWorkerId || workerId < 0,
			String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		Assert.isFalse(datacenterId > maxDatacenterId || datacenterId < 0,
			String
				.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	/**
	 * maxWorkerId
	 *
	 * @param datacenterId datacenterId
	 * @param maxWorkerId  maxWorkerId
	 * @return long
	 * @since 2021-09-02 14:54:50
	 */
	protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
		StringBuilder mpid = new StringBuilder();
		mpid.append(datacenterId);
		String name = ManagementFactory.getRuntimeMXBean().getName();
		if (StrUtil.isNotEmpty(name)) {
			/*
			 * GET jvmPid
			 */
			mpid.append(name.split("@")[0]);
		}
		/*
		 * MAC + PID 的 hashcode 获取16个低位
		 */
		return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
	}

	/**
	 * 数据标识id部分
	 *
	 * @param maxDatacenterId maxDatacenterId
	 * @return long
	 * @since 2021-09-02 14:54:57
	 */
	protected static long getDatacenterId(long maxDatacenterId) {
		long id = 0L;
		try {
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			if (network == null) {
				id = 1L;
			} else {
				byte[] mac = network.getHardwareAddress();
				if (null != mac) {
					id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (
						((long) mac[mac.length - 2]) << 8))) >> 6;
					id = id % (maxDatacenterId + 1);
				}
			}
		} catch (Exception e) {
			LogUtil.warn(" getDatacenterId: " + e.getMessage());
		}
		return id;
	}

	/**
	 * 获取下一个ID
	 *
	 * @return long
	 * @since 2021-09-02 14:55:02
	 */
	public synchronized long nextId() {
		long timestamp = timeGen();
		//闰秒
		if (timestamp < lastTimestamp) {
			long offset = lastTimestamp - timestamp;
			if (offset <= timestampOffset) {
				try {
					wait(offset << 1);
					timestamp = timeGen();
					if (timestamp < lastTimestamp) {
						throw new RuntimeException(String.format(
							"Clock moved backwards.  Refusing to generate id for %d milliseconds",
							offset));
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				throw new RuntimeException(String
					.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
						offset));
			}
		}

		if (lastTimestamp == timestamp) {
			// 相同毫秒内，序列号自增
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				// 同一毫秒的序列数已经达到最大
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			// 不同毫秒内，序列号置为 1 - 3 随机数
			sequence = ThreadLocalRandom.current().nextLong(1, 3);
		}

		lastTimestamp = timestamp;

		// 时间戳部分 | 数据中心部分 | 机器标识部分 | 序列号部分
		return ((timestamp - epoch) << timestampLeftShift)
			| (datacenterId << datacenterIdShift)
			| (workerId << workerIdShift)
			| sequence;
	}

	/**
	 * tilNextMillis
	 *
	 * @param lastTimestamp lastTimestamp
	 * @return long
	 * @since 2021-09-02 14:55:10
	 */
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	/**
	 * timeGen
	 *
	 * @return long
	 * @since 2021-09-02 14:55:20
	 */
	protected long timeGen() {
		return SystemClock.now();
	}
}
