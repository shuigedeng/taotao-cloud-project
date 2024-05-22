package com.taotao.cloud.mq.common.support.invoke;


import com.taotao.cloud.mq.common.rpc.RpcMessageDto;

/**
 * 调用服务接口
 *
 * @author shuigedeng
 * @since 2024.05
 */
public interface IInvokeService {

	/**
	 * 添加请求信息
	 *
	 * @param seqId        序列号
	 * @param timeoutMills 超时时间
	 * @return this
	 * @since 2024.05
	 */
	IInvokeService addRequest(final String seqId,
		final long timeoutMills);

	/**
	 * 放入结果
	 *
	 * @param seqId       唯一标识
	 * @param rpcResponse 响应结果
	 * @return this
	 * @since 2024.05
	 */
	IInvokeService addResponse(final String seqId, final RpcMessageDto rpcResponse);

	/**
	 * 获取标志信息对应的结果
	 *
	 * @param seqId 序列号
	 * @return 结果
	 * @since 2024.05
	 */
	RpcMessageDto getResponse(final String seqId);

	/**
	 * 是否依然包含请求待处理
	 *
	 * @return 是否
	 * @since 2024.05
	 */
	boolean remainsRequest();

}
