package com.taotao.cloud.ttcmq.broker.support.valid;


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
