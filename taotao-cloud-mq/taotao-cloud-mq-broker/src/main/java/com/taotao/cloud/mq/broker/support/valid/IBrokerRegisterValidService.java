package com.taotao.cloud.mq.broker.support.valid;


import com.taotao.cloud.mq.broker.dto.BrokerRegisterReq;

/**
 * 注册验证方法
 *
 * @author shuigedeng
 * @since 2024.05
 */
public interface IBrokerRegisterValidService {

	/**
	 * 生产者验证合法性
	 *
	 * @param registerReq 注册信息
	 * @return 结果
	 * @since 2024.05
	 */
	boolean producerValid(BrokerRegisterReq registerReq);

	/**
	 * 消费者验证合法性
	 *
	 * @param registerReq 注册信息
	 * @return 结果
	 * @since 2024.05
	 */
	boolean consumerValid(BrokerRegisterReq registerReq);

}
