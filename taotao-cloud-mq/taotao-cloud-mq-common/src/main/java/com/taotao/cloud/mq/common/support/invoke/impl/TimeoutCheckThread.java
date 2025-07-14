package com.taotao.cloud.mq.common.support.invoke.impl;

import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.mq.common.rpc.RpcMessageDto;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 超时检测线程
 *
 * @author shuigedeng
 * @since 2024.05
 */
public class TimeoutCheckThread implements Runnable {

	/**
	 * 请求信息
	 *
	 * @since 2024.05
	 */
	private final ConcurrentHashMap<String, Long> requestMap;

	/**
	 * 请求信息
	 *
	 * @since 2024.05
	 */
	private final ConcurrentHashMap<String, RpcMessageDto> responseMap;

	/**
	 * 新建
	 *
	 * @param requestMap  请求 Map
	 * @param responseMap 结果 map
	 * @since 2024.05
	 */
	public TimeoutCheckThread(ConcurrentHashMap<String, Long> requestMap,
		ConcurrentHashMap<String, RpcMessageDto> responseMap) {
		ArgUtils.notNull(requestMap, "requestMap");
		this.requestMap = requestMap;
		this.responseMap = responseMap;
	}

	@Override
	public void run() {
		for (Map.Entry<String, Long> entry : requestMap.entrySet()) {
			long expireTime = entry.getValue();
			long currentTime = System.currentTimeMillis();

			if (currentTime > expireTime) {
				final String key = entry.getKey();
				// 结果设置为超时，从请求 map 中移除
				responseMap.putIfAbsent(key, RpcMessageDto.timeout());
				requestMap.remove(key);
			}
		}
	}

}
