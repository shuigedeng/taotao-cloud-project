package com.taotao.cloud.core.http;


import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.ProcessExitEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 16:30 HttpClient使用管理器
 **/
public class HttpClientManager {

	/**
	 * 默认实例
	 */
	public static HttpClientManager DEFAULT = new HttpClientManager();

	private ConcurrentHashMap<String, DefaultHttpClient> pool = new ConcurrentHashMap<>();
	private Object lock = new Object();

	static {
		ProcessExitEvent.register(() -> {
			try {
				DEFAULT.closeAll();
			} catch (Exception e) {
				LogUtil.error(e,"关闭httpclient时出错");
			}
		}, Integer.MAX_VALUE - 1, false);
	}

	public DefaultHttpClient register(String httpClientId, DefaultHttpClient client) {
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
			}
			throw exp;
		}
	}

	public DefaultHttpClient get(String httpClientId) {
		return pool.get(httpClientId);
	}

	public boolean remove(String httpClientId) {
		DefaultHttpClient httpClient = pool.get(httpClientId);
		if (httpClient != null) {
			synchronized (lock) {
				pool.remove(httpClient);
			}
			httpClient.close();
			return true;
		}
		return false;
	}

	public boolean remove(DefaultHttpClient httpClient) {
		String httpClientId = null;
		for (Map.Entry<String, DefaultHttpClient> e : pool.entrySet()) {
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

	public void closeAll() {
		ConcurrentHashMap<String, DefaultHttpClient> temp = new ConcurrentHashMap<>();

		synchronized (lock) {
			temp.putAll(pool);
			pool.clear();
		}

		RuntimeException exception = null;
		for (Map.Entry<String, DefaultHttpClient> e : temp.entrySet()) {
			try {
				e.getValue().close();
			} catch (RuntimeException ex) {
				exception = ex;
			}
		}
		if (exception != null) {
			throw exception;
		}
	}
}
