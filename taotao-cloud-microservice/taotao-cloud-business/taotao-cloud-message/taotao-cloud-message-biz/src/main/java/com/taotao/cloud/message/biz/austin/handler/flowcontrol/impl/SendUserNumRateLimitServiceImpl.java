package com.taotao.cloud.message.biz.austin.handler.flowcontrol.impl;

import com.google.common.util.concurrent.RateLimiter;

/**
 * Created by TOM On 2022/7/21 17:14
 *
 * @author TOM
 */
@LocalRateLimit(rateLimitStrategy = RateLimitStrategy.SEND_USER_NUM_RATE_LIMIT)
public class SendUserNumRateLimitServiceImpl implements FlowControlService {

	/**
	 * 根据渠道进行流量控制
	 *
	 * @param taskInfo
	 * @param flowControlParam
	 */
	@Override
	public Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
		RateLimiter rateLimiter = flowControlParam.getRateLimiter();
		return rateLimiter.acquire(taskInfo.getReceiver().size());
	}
}
