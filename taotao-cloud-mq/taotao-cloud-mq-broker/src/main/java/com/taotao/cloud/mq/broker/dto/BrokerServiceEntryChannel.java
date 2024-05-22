package com.taotao.cloud.mq.broker.dto;

import io.netty.channel.Channel;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class BrokerServiceEntryChannel extends ServiceEntry {

	private Channel channel;

	/**
	 * 最后访问时间
	 */
	private long lastAccessTime;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}
}
