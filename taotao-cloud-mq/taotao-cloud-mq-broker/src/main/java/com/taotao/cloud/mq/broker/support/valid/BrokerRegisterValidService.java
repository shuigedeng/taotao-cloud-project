package com.taotao.cloud.mq.broker.support.valid;


import com.taotao.cloud.mq.broker.dto.BrokerRegisterReq;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class BrokerRegisterValidService implements IBrokerRegisterValidService {

	@Override
	public boolean producerValid(BrokerRegisterReq registerReq) {
		return true;
	}

	@Override
	public boolean consumerValid(BrokerRegisterReq registerReq) {
		return true;
	}

}
