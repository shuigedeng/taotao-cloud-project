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
package com.taotao.cloud.demo.http;


import com.taotao.cloud.common.utils.log.LogUtils;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理设置
 *
 */
public class MicaProxySelector extends ProxySelector {

	@Override
	public List<Proxy> select(URI uri) {
		// 注意代理都不可用
		List<Proxy> proxyList = new ArrayList<>();
		proxyList.add(getProxy("127.0.0.1", 8080));
		proxyList.add(getProxy("127.0.0.1", 8081));
		proxyList.add(getProxy("127.0.0.1", 8082));
		proxyList.add(getProxy("127.0.0.1", 3128));
		return proxyList;
	}

	@Override
	public void connectFailed(URI uri, SocketAddress address, IOException ioe) {
		// 注意：经过测试，此处不会触发
		LogUtils.error("ConnectFailed uri:{}, address:{}, ioe:{}", uri, address, ioe);
	}

	/**
	 * 构造 Proxy
	 *
	 * @param host host
	 * @param port 端口
	 * @return Proxy 对象
	 */
	public static Proxy getProxy(String host, int port) {
		return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port));
	}
}
