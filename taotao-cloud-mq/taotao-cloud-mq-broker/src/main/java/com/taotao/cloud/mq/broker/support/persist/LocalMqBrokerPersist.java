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

package com.taotao.cloud.mq.broker.support.persist;

import com.alibaba.fastjson2.JSON;
import com.taotao.boot.common.utils.collection.CollectionUtils;
import com.taotao.cloud.mq.broker.dto.persist.MqMessagePersistPut;
import com.taotao.cloud.mq.common.constant.MessageStatusConst;
import com.taotao.cloud.mq.common.dto.req.MqConsumerPullReq;
import com.taotao.cloud.mq.common.dto.req.MqMessage;
import com.taotao.cloud.mq.common.dto.req.component.MqConsumerUpdateStatusDto;
import com.taotao.cloud.mq.common.dto.resp.MqCommonResp;
import com.taotao.cloud.mq.common.dto.resp.MqConsumerPullResp;
import com.taotao.cloud.mq.common.resp.MqCommonRespCode;
import com.taotao.cloud.mq.common.util.MapUtil;
import com.taotao.cloud.mq.common.util.RegexUtil;
import io.netty.channel.Channel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 本地持久化策略
 *
 * @author shuigedeng
 * @since 2024.05
 */
public class LocalMqBrokerPersist implements MqBrokerPersist {

    private static final Logger LOG = LoggerFactory.getLogger(LocalMqBrokerPersist.class);

    /**
     * 队列 ps: 这里只是简化实现，暂时不考虑并发等问题。
     */
    private final Map<String, List<MqMessagePersistPut>> map = new ConcurrentHashMap<>();

    // 1. 接收
    // 2. 持久化
    // 3. 通知消费
    @Override
    public synchronized MqCommonResp put(MqMessagePersistPut put) {
        this.doPut(put);

        MqCommonResp commonResp = new MqCommonResp();
        commonResp.setRespCode(MqCommonRespCode.SUCCESS.getCode());
        commonResp.setRespMessage(MqCommonRespCode.SUCCESS.getMsg());
        return commonResp;
    }

    private void doPut(MqMessagePersistPut put) {
        LOG.info("put elem: {}", JSON.toJSON(put));

        MqMessage mqMessage = put.getMqMessage();
        final String topic = mqMessage.getTopic();

        // 放入元素
        MapUtil.putToListMap(map, topic, put);
    }

    @Override
    public MqCommonResp putBatch(List<MqMessagePersistPut> putList) {
        // 构建列表
        for (MqMessagePersistPut put : putList) {
            this.doPut(put);
        }

        MqCommonResp commonResp = new MqCommonResp();
        commonResp.setRespCode(MqCommonRespCode.SUCCESS.getCode());
        commonResp.setRespMessage(MqCommonRespCode.SUCCESS.getMsg());
        return commonResp;
    }

    @Override
    public MqCommonResp updateStatus(String messageId, String consumerGroupName, String status) {
        // 这里性能比较差，所以不可以用于生产。仅作为测试验证
        this.doUpdateStatus(messageId, consumerGroupName, status);

        MqCommonResp commonResp = new MqCommonResp();
        commonResp.setRespCode(MqCommonRespCode.SUCCESS.getCode());
        commonResp.setRespMessage(MqCommonRespCode.SUCCESS.getMsg());
        return commonResp;
    }

    private void doUpdateStatus(String messageId, String consumerGroupName, String status) {
        // 这里性能比较差，所以不可以用于生产。仅作为测试验证
        for (List<MqMessagePersistPut> list : map.values()) {
            for (MqMessagePersistPut put : list) {
                MqMessage mqMessage = put.getMqMessage();
                if (mqMessage.getTraceId().equals(messageId)) {
                    put.setMessageStatus(status);

                    break;
                }
            }
        }
    }

    @Override
    public MqCommonResp updateStatusBatch(List<MqConsumerUpdateStatusDto> statusDtoList) {
        for (MqConsumerUpdateStatusDto statusDto : statusDtoList) {
            this.doUpdateStatus(
                    statusDto.getMessageId(),
                    statusDto.getConsumerGroupName(),
                    statusDto.getMessageStatus());
        }

        MqCommonResp commonResp = new MqCommonResp();
        commonResp.setRespCode(MqCommonRespCode.SUCCESS.getCode());
        commonResp.setRespMessage(MqCommonRespCode.SUCCESS.getMsg());
        return commonResp;
    }

    @Override
    public MqConsumerPullResp pull(MqConsumerPullReq pullReq, Channel channel) {
        // 1. 拉取匹配的信息
        // 2. 状态更新为代理中
        // 3. 如何更新对应的消费状态呢？

        // 获取状态为 W 的订单
        final int fetchSize = pullReq.getSize();
        final String topic = pullReq.getTopicName();
        final String tagRegex = pullReq.getTagRegex();

        List<MqMessage> resultList = new ArrayList<>(fetchSize);
        List<MqMessagePersistPut> putList = map.get(topic);
        // 性能比较差
        if (CollectionUtils.isNotEmpty(putList)) {
            for (MqMessagePersistPut put : putList) {
                if (!isEnableStatus(put)) {
                    continue;
                }

                final MqMessage mqMessage = put.getMqMessage();
                List<String> tagList = mqMessage.getTags();
                if (RegexUtil.hasMatch(tagList, tagRegex)) {
                    // 设置为处理中
                    // TODO： 消息的最终状态什么时候更新呢？
                    // 可以给 broker 一个 ACK
                    put.setMessageStatus(MessageStatusConst.TO_CONSUMER_PROCESS);
                    resultList.add(mqMessage);
                }

                if (resultList.size() >= fetchSize) {
                    break;
                }
            }
        }

        MqConsumerPullResp resp = new MqConsumerPullResp();
        resp.setRespCode(MqCommonRespCode.SUCCESS.getCode());
        resp.setRespMessage(MqCommonRespCode.SUCCESS.getMsg());
        resp.setList(resultList);
        return resp;
    }

    private boolean isEnableStatus(final MqMessagePersistPut persistPut) {
        final String status = persistPut.getMessageStatus();
        // 数据库可以设计一个字段，比如待消费时间，进行排序。
        // 这里只是简化实现，仅用于测试。
        List<String> statusList =
                Arrays.asList(MessageStatusConst.WAIT_CONSUMER, MessageStatusConst.CONSUMER_LATER);
        return statusList.contains(status);
    }
}
