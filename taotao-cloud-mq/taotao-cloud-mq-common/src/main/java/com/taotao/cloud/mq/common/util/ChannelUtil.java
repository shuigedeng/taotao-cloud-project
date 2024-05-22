package com.taotao.cloud.mq.common.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * channel 工具类
 *
 * @author shuigedeng
 * @since 2024.05
 */
public class ChannelUtil {

	private ChannelUtil() {
	}

	/**
	 * 获取 channel 标识
	 *
	 * @param channel 管道
	 * @return 结果
	 * @since 2024.05
	 */
	public static String getChannelId(Channel channel) {
		return channel.id().asLongText();
	}

	/**
	 * 获取 channel 标识
	 *
	 * @param ctx 管道
	 * @return 结果
	 * @since 2024.05
	 */
	public static String getChannelId(ChannelHandlerContext ctx) {
		return getChannelId(ctx.channel());
	}

}
