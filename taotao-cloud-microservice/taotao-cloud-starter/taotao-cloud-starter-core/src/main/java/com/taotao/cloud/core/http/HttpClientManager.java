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
package com.taotao.cloud.core.http;


import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.ProcessExitEvent;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HttpClientManager
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:20:42
 */
public class HttpClientManager {

	/**
	 * pool
	 */
	private ConcurrentHashMap<String, HttpClient> pool = new ConcurrentHashMap<>();
	/**
	 * lock
	 */
	private final Object lock = new Object();

	public HttpClientManager() {
		ProcessExitEvent.register(() -> {
			try {
				closeAll();
			} catch (Exception e) {
				LogUtil.error(e, "关闭httpclient时出错");
			}
		}, Integer.MAX_VALUE - 1, false);
	}

	/**
	 * register
	 *
	 * @param httpClientId httpClientId
	 * @param client       client
	 * @return {@link com.taotao.cloud.core.http.DefaultHttpClient }
	 * @author shuigedeng
	 * @since 2021-09-02 20:21:12
	 */
	public HttpClient register(String httpClientId, HttpClient client) {
		try {
			client.open();

			synchronized (lock) {
				if (!pool.containsKey(httpClientId)) {
					pool.put(httpClientId, client);
					return client;
				} else {
					throw new HttpException("已注册HttpClient:" + httpClientId);
				}
			}
		} catch (RuntimeException exp) {
			try {
				client.close();
			} catch (Exception e) {
				LogUtil.error(e);
			}
			throw exp;
		}
	}

	/**
	 * get
	 *
	 * @param httpClientId httpClientId
	 * @return {@link com.taotao.cloud.core.http.DefaultHttpClient }
	 * @author shuigedeng
	 * @since 2021-09-02 20:21:16
	 */
	public HttpClient get(String httpClientId) {
		return pool.get(httpClientId);
	}

	/**
	 * remove
	 *
	 * @param httpClientId httpClientId
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 20:21:20
	 */
	public boolean remove(String httpClientId) {
		HttpClient httpClient = pool.get(httpClientId);
		if (httpClient != null) {
			synchronized (lock) {
				pool.remove(httpClientId);
			}

			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	/**
	 * remove
	 *
	 * @param httpClient httpClient
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 20:21:23
	 */
	public boolean remove(HttpClient httpClient) {
		String httpClientId = null;
		for (Map.Entry<String, HttpClient> e : pool.entrySet()) {
			if (e.getValue() == httpClient) {
				httpClientId = e.getKey();
				break;
			}
		}

		if (httpClientId != null) {
			return remove(httpClientId);
		}
		return false;
	}

	/**
	 * closeAll
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:21:25
	 */
	public void closeAll() {
		ConcurrentHashMap<String, HttpClient> temp;

		synchronized (lock) {
			temp = new ConcurrentHashMap<>(pool);
			pool.clear();
		}

		for (Map.Entry<String, HttpClient> entry : temp.entrySet()) {
			try {
				entry.getValue().close();
			} catch (IOException e) {
				LogUtil.error(e);
			}
		}
	}

	public ConcurrentHashMap<String, HttpClient> getPool() {
		return pool;
	}

	public void setPool(ConcurrentHashMap<String, HttpClient> pool) {
		this.pool = pool;
	}

	public Object getLock() {
		return lock;
	}
}
