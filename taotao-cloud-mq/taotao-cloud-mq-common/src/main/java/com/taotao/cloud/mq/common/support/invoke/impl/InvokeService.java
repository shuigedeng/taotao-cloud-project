package com.taotao.cloud.mq.common.support.invoke.impl;


import com.alibaba.fastjson2.JSON;
import com.taotao.boot.common.utils.lang.ObjectUtils;
import com.taotao.cloud.mq.common.resp.MqCommonRespCode;
import com.taotao.cloud.mq.common.resp.MqException;
import com.taotao.cloud.mq.common.rpc.RpcMessageDto;
import com.taotao.cloud.mq.common.support.invoke.IInvokeService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 调用服务接口
 *
 * @author shuigedeng
 * @since 2024.05
 */
public class InvokeService implements IInvokeService {

	private static final Logger logger = LoggerFactory.getLogger(InvokeService.class);

	/**
	 * 请求序列号 map （1）这里后期如果要添加超时检测，可以添加对应的超时时间。 可以把这里调整为 map
	 * <p>
	 * key: seqId 唯一标识一个请求 value: 存入该请求最长的有效时间。用于定时删除和超时判断。
	 *
	 * @since 2024.05
	 */
	private final ConcurrentHashMap<String, Long> requestMap;
	private boolean condition = false;
	/**
	 * 响应结果
	 *
	 * @since 2024.05
	 */
	private final ConcurrentHashMap<String, RpcMessageDto> responseMap;

	public InvokeService() {
		requestMap = new ConcurrentHashMap<>();
		responseMap = new ConcurrentHashMap<>();

		final Runnable timeoutThread = new TimeoutCheckThread(requestMap, responseMap);
		Executors.newScheduledThreadPool(1)
			.scheduleAtFixedRate(timeoutThread, 60, 60, TimeUnit.SECONDS);
	}

	@Override
	public IInvokeService addRequest(String seqId, long timeoutMills) {
		logger.info("[Invoke] start add request for seqId: {}, timeoutMills: {}", seqId,
			timeoutMills);

		final long expireTime = System.currentTimeMillis() + timeoutMills;
		requestMap.putIfAbsent(seqId, expireTime);

		return this;
	}

	@Override
	public IInvokeService addResponse(String seqId, RpcMessageDto rpcResponse) {
		// 1. 判断是否有效
		Long expireTime = this.requestMap.get(seqId);
		// 如果为空，可能是这个结果已经超时了，被定时 job 移除之后，响应结果才过来。直接忽略
		if (ObjectUtils.isNull(expireTime)) {
			return this;
		}

		//2. 判断是否超时
		if (System.currentTimeMillis() > expireTime) {
			logger.info("[Invoke] seqId:{} 信息已超时，直接返回超时结果。", seqId);
			rpcResponse = RpcMessageDto.timeout();
		}

		// 这里放入之前，可以添加判断。
		// 如果 seqId 必须处理请求集合中，才允许放入。或者直接忽略丢弃。
		// 通知所有等待方
		responseMap.putIfAbsent(seqId, rpcResponse);
		logger.info("[Invoke] 获取结果信息，seqId: {}, rpcResponse: {}", seqId,
			JSON.toJSON(rpcResponse));
		logger.info("[Invoke] seqId:{} 信息已经放入，通知所有等待方", seqId);

		// 移除对应的 requestMap
		requestMap.remove(seqId);
		logger.info("[Invoke] seqId:{} remove from request map", seqId);

		logger.info("notifyAll"+this.toString());
		// 同步锁
		//synchronized (this) {
		//	this.notifyAll();
		//	logger.info("[Invoke] {} notifyAll()", seqId);
		//}
		synchronized (this) {
			condition = true; // 满足条件
			//logger.info("[Invoke] {} notifyAll()", seqId);
			this.notifyAll(); // 唤醒等待队列中的线程
		}

		return this;
	}

	@Override
	public RpcMessageDto getResponse(String seqId) {
		try {
			RpcMessageDto rpcResponse = this.responseMap.get(seqId);
			if (ObjectUtils.isNotNull(rpcResponse)) {
				logger.info("[Invoke] seq {} 对应结果已经获取: {}", seqId, rpcResponse);
				return rpcResponse;
			}

			// 进入等待
			while (rpcResponse == null) {
				logger.info("[Invoke] seq {} 对应结果为空，进入等待", seqId);

				logger.info("wait"+this.toString());
				// 同步等待锁
				//synchronized (this) {
				//	this.wait();
				//}
				synchronized (this) {
					while (!condition) {
						logger.info("条件不满足，释放锁并等待...");
						this.wait(); // 释放锁，进入等待
					}
				}

				logger.info("[Invoke] {} wait has notified!", seqId);

				rpcResponse = this.responseMap.get(seqId);
				logger.info("[Invoke] seq {} 对应结果已经获取: {}", seqId, rpcResponse);
			}

			return rpcResponse;
		}
		catch (InterruptedException e) {
			logger.error("获取响应异常", e);
			throw new MqException(MqCommonRespCode.RPC_GET_RESP_FAILED);
		}
	}

	@Override
	public boolean remainsRequest() {
		return this.requestMap.size() > 0;
	}

}
