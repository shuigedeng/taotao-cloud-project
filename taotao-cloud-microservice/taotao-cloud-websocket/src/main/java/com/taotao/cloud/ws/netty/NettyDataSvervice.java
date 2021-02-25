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

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * NettyDataSvervice
 *
 * @author dengtao
 * @since 2020/12/30 下午4:37
 * @version 1.0.0
 */
@Slf4j
public class NettyDataSvervice {

	private static final ReentrantLock LOCK = new ReentrantLock();

	/**
	 * 将传送过来的数据进行解析，包括异或运算 （第一次服务器端给客户端发）
	 *
	 * @param receiveData
	 * @return
	 */
	@SuppressWarnings({"checkstyle:Indentation", "checkstyle:FileTabCharacter"})
	public static String sendData(String receiveData) {
		final ReentrantLock putLock = LOCK;
		log.info("接收数据" + receiveData);
		putLock.lock();
		try {
			//此处需要读数据进行校验以及分包黏包处理，本文主要提供思路所以省略
			/*
			 *   处理分包黏包、拆分、解析等
			 */
			//进入数据解析
			parseData(receiveData);
			try {
				//数据帧WebSocket推送
				NettyWebSocketServer.broadCastInfo(receiveData);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return receiveData;
		} finally {
			putLock.unlock();
		}
	}

	/**
	 * 数据解析入库处理
	 */
	public static void parseData(String receiveData) {
		//此处省略真实入库操作
		System.out.println("执行入库操作");
	}
}
