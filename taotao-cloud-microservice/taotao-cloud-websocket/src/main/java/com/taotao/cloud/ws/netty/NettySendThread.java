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
package com.taotao.cloud.ws.netty;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.Objects;

/**
 * NettySendThread
 *
 * @author dengtao
 * @date 2020/12/30 下午4:46
 * @since v1.0
 */
@Slf4j
public class NettySendThread implements Runnable {
	private final ChannelHandlerContext ctx;
	private final String data;

	public NettySendThread(ChannelHandlerContext ctx, String data) {
		this.ctx = ctx;
		this.data = data;
	}

	@Override
	public void run() {
		String bcc = NettyDataSvervice.sendData(data);
		log.info("接收需要返回的消息：" + bcc);
		if (Objects.isNull(bcc)) {
			return;
		}
		//数据的发送与接收都是16进制
		byte[] sendData = NettyByteAndStringUtils.hexToByte(bcc);
		log.info("发送数据时间: " + LocalTime.now());
		ctx.writeAndFlush(sendData);
	}
}
