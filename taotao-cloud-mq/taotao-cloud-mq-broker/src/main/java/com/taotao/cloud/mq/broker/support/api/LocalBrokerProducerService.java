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

package com.taotao.cloud.mq.broker.support.api;

import com.taotao.cloud.mq.broker.api.BrokerProducerService;
import com.taotao.cloud.mq.broker.dto.BrokerServiceEntryChannel;
import com.taotao.cloud.mq.broker.dto.ServiceEntry;
import com.taotao.cloud.mq.broker.resp.MqBrokerRespCode;
import com.taotao.cloud.mq.broker.utils.InnerChannelUtils;
import com.taotao.cloud.mq.common.dto.resp.MqCommonResp;
import com.taotao.cloud.mq.common.resp.MqCommonRespCode;
import com.taotao.cloud.mq.common.resp.MqException;
import com.taotao.cloud.mq.common.util.ChannelUtil;
import io.netty.channel.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> 生产者注册服务类 </p>
 *
 * @since 2024.05
 */
public class LocalBrokerProducerService implements BrokerProducerService {

    private static final Logger LOG = LoggerFactory.getLogger(LocalBrokerProducerService.class);

    private final Map<String, BrokerServiceEntryChannel> registerMap = new ConcurrentHashMap<>();

    @Override
    public MqCommonResp register(ServiceEntry serviceEntry, Channel channel) {
        final String channelId = ChannelUtil.getChannelId(channel);
        BrokerServiceEntryChannel entryChannel =
                InnerChannelUtils.buildEntryChannel(serviceEntry, channel);
        registerMap.put(channelId, entryChannel);

        MqCommonResp resp = new MqCommonResp();
        resp.setRespCode(MqCommonRespCode.SUCCESS.getCode());
        resp.setRespMessage(MqCommonRespCode.SUCCESS.getMsg());
        return resp;
    }

    @Override
    public MqCommonResp unRegister(ServiceEntry serviceEntry, Channel channel) {
        final String channelId = ChannelUtil.getChannelId(channel);
        registerMap.remove(channelId);

        MqCommonResp resp = new MqCommonResp();
        resp.setRespCode(MqCommonRespCode.SUCCESS.getCode());
        resp.setRespMessage(MqCommonRespCode.SUCCESS.getMsg());
        return resp;
    }

    @Override
    public ServiceEntry getServiceEntry(String channelId) {
        return registerMap.get(channelId);
    }

    @Override
    public void checkValid(String channelId) {
        if (!registerMap.containsKey(channelId)) {
            LOG.error("channelId: {} 未注册", channelId);
            throw new MqException(MqBrokerRespCode.P_REGISTER_CHANNEL_NOT_VALID);
        }
    }
}
