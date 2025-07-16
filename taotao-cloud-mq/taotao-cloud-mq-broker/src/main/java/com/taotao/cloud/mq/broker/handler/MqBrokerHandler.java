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

package com.taotao.cloud.mq.broker.handler;

import com.alibaba.fastjson2.JSON;
import com.taotao.boot.common.utils.collection.CollectionUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.cloud.mq.broker.api.IBrokerConsumerService;
import com.taotao.cloud.mq.broker.api.IBrokerProducerService;
import com.taotao.cloud.mq.broker.dto.BrokerRegisterReq;
import com.taotao.cloud.mq.broker.dto.ChannelGroupNameDto;
import com.taotao.cloud.mq.broker.dto.ServiceEntry;
import com.taotao.cloud.mq.broker.dto.consumer.ConsumerSubscribeReq;
import com.taotao.cloud.mq.broker.dto.consumer.ConsumerUnSubscribeReq;
import com.taotao.cloud.mq.broker.dto.persist.MqMessagePersistPut;
import com.taotao.cloud.mq.broker.resp.MqBrokerRespCode;
import com.taotao.cloud.mq.broker.support.persist.IMqBrokerPersist;
import com.taotao.cloud.mq.broker.support.push.BrokerPushContext;
import com.taotao.cloud.mq.broker.support.push.IBrokerPushService;
import com.taotao.cloud.mq.broker.support.valid.IBrokerRegisterValidService;
import com.taotao.cloud.mq.common.constant.MessageStatusConst;
import com.taotao.cloud.mq.common.constant.MethodType;
import com.taotao.cloud.mq.common.dto.req.MqConsumerPullReq;
import com.taotao.cloud.mq.common.dto.req.MqConsumerUpdateStatusBatchReq;
import com.taotao.cloud.mq.common.dto.req.MqConsumerUpdateStatusReq;
import com.taotao.cloud.mq.common.dto.req.MqHeartBeatReq;
import com.taotao.cloud.mq.common.dto.req.MqMessage;
import com.taotao.cloud.mq.common.dto.req.MqMessageBatchReq;
import com.taotao.cloud.mq.common.dto.req.component.MqConsumerUpdateStatusDto;
import com.taotao.cloud.mq.common.dto.resp.MqCommonResp;
import com.taotao.cloud.mq.common.resp.MqCommonRespCode;
import com.taotao.cloud.mq.common.resp.MqException;
import com.taotao.cloud.mq.common.rpc.RpcMessageDto;
import com.taotao.cloud.mq.common.support.invoke.IInvokeService;
import com.taotao.cloud.mq.common.util.ChannelUtil;
import com.taotao.cloud.mq.common.util.DelimiterUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqBrokerHandler extends SimpleChannelInboundHandler {

    private static final Logger log = LoggerFactory.getLogger(MqBrokerHandler.class);

    /**
     * 调用管理类
     *
     * @since 2024.05
     */
    private IInvokeService invokeService;

    /**
     * 消费者管理
     *
     * @since 2024.05
     */
    private IBrokerConsumerService registerConsumerService;

    /**
     * 生产者管理
     *
     * @since 2024.05
     */
    private IBrokerProducerService registerProducerService;

    /**
     * 持久化类
     *
     * @since 2024.05
     */
    private IMqBrokerPersist mqBrokerPersist;

    /**
     * 推送服务
     *
     * @since 2024.05
     */
    private IBrokerPushService brokerPushService;

    /**
     * 获取响应超时时间
     *
     * @since 2024.05
     */
    private long respTimeoutMills;

    /**
     * 推送最大尝试次数
     *
     * @since 2024.05
     */
    private int pushMaxAttempt;

    /**
     * 注册验证服务类
     *
     * @since 2024.05
     */
    private IBrokerRegisterValidService brokerRegisterValidService;

    public MqBrokerHandler brokerRegisterValidService(
            IBrokerRegisterValidService brokerRegisterValidService) {
        this.brokerRegisterValidService = brokerRegisterValidService;
        return this;
    }

    public MqBrokerHandler invokeService(IInvokeService invokeService) {
        this.invokeService = invokeService;
        return this;
    }

    public MqBrokerHandler registerConsumerService(IBrokerConsumerService registerConsumerService) {
        this.registerConsumerService = registerConsumerService;
        return this;
    }

    public MqBrokerHandler registerProducerService(IBrokerProducerService registerProducerService) {
        this.registerProducerService = registerProducerService;
        return this;
    }

    public MqBrokerHandler mqBrokerPersist(IMqBrokerPersist mqBrokerPersist) {
        this.mqBrokerPersist = mqBrokerPersist;
        return this;
    }

    public MqBrokerHandler brokerPushService(IBrokerPushService brokerPushService) {
        this.brokerPushService = brokerPushService;
        return this;
    }

    public MqBrokerHandler respTimeoutMills(long respTimeoutMills) {
        this.respTimeoutMills = respTimeoutMills;
        return this;
    }

    public MqBrokerHandler pushMaxAttempt(int pushMaxAttempt) {
        this.pushMaxAttempt = pushMaxAttempt;
        return this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        RpcMessageDto rpcMessageDto = null;
        try {
            rpcMessageDto = JSON.parseObject(bytes, RpcMessageDto.class);
        } catch (Exception exception) {
            log.error("RpcMessageDto json 格式转换异常 {}", new String(bytes));
            return;
        }

        if (rpcMessageDto.isRequest()) {
            MqCommonResp commonResp = this.dispatch(rpcMessageDto, ctx);

            if (commonResp == null) {
                log.info("当前消息为 null，忽略处理。");
                return;
            }

            writeResponse(rpcMessageDto, commonResp, ctx);
        } else {
            final String traceId = rpcMessageDto.getTraceId();

            // 丢弃掉 traceId 为空的信息
            if (StringUtils.isBlank(traceId)) {
                log.info("[Server Response] response traceId 为空，直接丢弃", JSON.toJSON(rpcMessageDto));
                return;
            }

            // 添加消息
            invokeService.addResponse(traceId, rpcMessageDto);
        }
    }

    /**
     * 消息的分发
     *
     * @param rpcMessageDto 入参
     * @param ctx           上下文
     * @return 结果
     */
    private MqCommonResp dispatch(RpcMessageDto rpcMessageDto, ChannelHandlerContext ctx) {
        try {
            final String methodType = rpcMessageDto.getMethodType();
            final String json = rpcMessageDto.getJson();

            String channelId = ChannelUtil.getChannelId(ctx);
            final Channel channel = ctx.channel();
            log.info("channelId: {} 接收到 method: {} 内容：{}", channelId, methodType, json);

            // 生产者注册
            if (MethodType.P_REGISTER.equals(methodType)) {
                BrokerRegisterReq registerReq = JSON.parseObject(json, BrokerRegisterReq.class);
                if (!brokerRegisterValidService.producerValid(registerReq)) {
                    log.error("{} 生产者注册验证失败", JSON.toJSON(registerReq));
                    throw new MqException(MqBrokerRespCode.P_REGISTER_VALID_FAILED);
                }

                return registerProducerService.register(registerReq.getServiceEntry(), channel);
            }
            // 生产者注销
            if (MethodType.P_UN_REGISTER.equals(methodType)) {
                registerProducerService.checkValid(channelId);

                BrokerRegisterReq registerReq = JSON.parseObject(json, BrokerRegisterReq.class);
                return registerProducerService.unRegister(registerReq.getServiceEntry(), channel);
            }
            // 生产者消息发送
            if (MethodType.P_SEND_MSG.equals(methodType)) {
                registerProducerService.checkValid(channelId);

                return handleProducerSendMsg(channelId, json);
            }
            // 生产者消息发送-ONE WAY
            if (MethodType.P_SEND_MSG_ONE_WAY.equals(methodType)) {
                registerProducerService.checkValid(channelId);

                handleProducerSendMsg(channelId, json);

                return null;
            }
            // 生产者消息发送-批量
            if (MethodType.P_SEND_MSG_BATCH.equals(methodType)) {
                registerProducerService.checkValid(channelId);

                return handleProducerSendMsgBatch(channelId, json);
            }
            // 生产者消息发送-ONE WAY-批量
            if (MethodType.P_SEND_MSG_ONE_WAY_BATCH.equals(methodType)) {
                registerProducerService.checkValid(channelId);

                handleProducerSendMsgBatch(channelId, json);

                return null;
            }

            // 消费者注册
            if (MethodType.C_REGISTER.equals(methodType)) {
                BrokerRegisterReq registerReq = JSON.parseObject(json, BrokerRegisterReq.class);
                if (!brokerRegisterValidService.consumerValid(registerReq)) {
                    log.error("{} 消费者注册验证失败", JSON.toJSON(registerReq));
                    throw new MqException(MqBrokerRespCode.C_REGISTER_VALID_FAILED);
                }

                return registerConsumerService.register(registerReq.getServiceEntry(), channel);
            }
            // 消费者注销
            if (MethodType.C_UN_REGISTER.equals(methodType)) {
                registerConsumerService.checkValid(channelId);

                BrokerRegisterReq registerReq = JSON.parseObject(json, BrokerRegisterReq.class);
                return registerConsumerService.unRegister(registerReq.getServiceEntry(), channel);
            }
            // 消费者监听注册
            if (MethodType.C_SUBSCRIBE.equals(methodType)) {
                registerConsumerService.checkValid(channelId);

                ConsumerSubscribeReq req = JSON.parseObject(json, ConsumerSubscribeReq.class);
                return registerConsumerService.subscribe(req, channel);
            }
            // 消费者监听注销
            if (MethodType.C_UN_SUBSCRIBE.equals(methodType)) {
                registerConsumerService.checkValid(channelId);

                ConsumerUnSubscribeReq req = JSON.parseObject(json, ConsumerUnSubscribeReq.class);
                return registerConsumerService.unSubscribe(req, channel);
            }
            // 消费者主动 pull
            if (MethodType.C_MESSAGE_PULL.equals(methodType)) {
                registerConsumerService.checkValid(channelId);

                MqConsumerPullReq req = JSON.parseObject(json, MqConsumerPullReq.class);
                return mqBrokerPersist.pull(req, channel);
            }
            // 消费者心跳
            if (MethodType.C_HEARTBEAT.equals(methodType)) {
                registerConsumerService.checkValid(channelId);

                MqHeartBeatReq req = JSON.parseObject(json, MqHeartBeatReq.class);
                registerConsumerService.heartbeat(req, channel);
                return null;
            }
            // 消费者消费状态 ACK
            if (MethodType.C_CONSUMER_STATUS.equals(methodType)) {
                registerConsumerService.checkValid(channelId);

                MqConsumerUpdateStatusReq req =
                        JSON.parseObject(json, MqConsumerUpdateStatusReq.class);
                final String messageId = req.getMessageId();
                final String messageStatus = req.getMessageStatus();
                final String consumerGroupName = req.getConsumerGroupName();
                return mqBrokerPersist.updateStatus(messageId, consumerGroupName, messageStatus);
            }
            // 消费者消费状态 ACK-批量
            if (MethodType.C_CONSUMER_STATUS_BATCH.equals(methodType)) {
                registerConsumerService.checkValid(channelId);

                MqConsumerUpdateStatusBatchReq req =
                        JSON.parseObject(json, MqConsumerUpdateStatusBatchReq.class);
                final List<MqConsumerUpdateStatusDto> statusDtoList = req.getStatusList();
                return mqBrokerPersist.updateStatusBatch(statusDtoList);
            }

            log.error("暂时不支持的方法类型 {}", methodType);
            throw new MqException(MqBrokerRespCode.B_NOT_SUPPORT_METHOD);
        } catch (MqException mqException) {
            log.error("业务执行异常", mqException);
            MqCommonResp resp = new MqCommonResp();
            resp.setRespCode(mqException.getCode());
            resp.setRespMessage(mqException.getMsg());
            return resp;
        } catch (Exception exception) {
            log.error("执行异常", exception);
            MqCommonResp resp = new MqCommonResp();
            resp.setRespCode(MqCommonRespCode.FAIL.getCode());
            resp.setRespMessage(MqCommonRespCode.FAIL.getMsg());
            return resp;
        }
    }

    /**
     * 处理生产者发送的消息
     *
     * @param json 消息体
     * @since 2024.05
     */
    private MqCommonResp handleProducerSendMsg(String channelId, String json) {
        MqMessage mqMessage = JSON.parseObject(json, MqMessage.class);
        MqMessagePersistPut persistPut = new MqMessagePersistPut();
        persistPut.setMqMessage(mqMessage);
        persistPut.setMessageStatus(MessageStatusConst.WAIT_CONSUMER);
        // 构建 rpc 信息
        final ServiceEntry serviceEntry = registerProducerService.getServiceEntry(channelId);
        persistPut.setRpcAddress(serviceEntry);

        MqCommonResp commonResp = mqBrokerPersist.put(persistPut);
        this.asyncHandleMessage(persistPut);
        return commonResp;
    }

    /**
     * 处理生产者发送的消息
     *
     * @param channelId 通道标识
     * @param json      消息体
     * @since 2024.05
     */
    private MqCommonResp handleProducerSendMsgBatch(String channelId, String json) {
        MqMessageBatchReq batchReq = JSON.parseObject(json, MqMessageBatchReq.class);
        final ServiceEntry serviceEntry = registerProducerService.getServiceEntry(channelId);

        List<MqMessagePersistPut> putList = buildPersistPutList(batchReq, serviceEntry);

        MqCommonResp commonResp = mqBrokerPersist.putBatch(putList);

        // 遍历异步推送
        for (MqMessagePersistPut persistPut : putList) {
            this.asyncHandleMessage(persistPut);
        }

        return commonResp;
    }

    /**
     * 构建列表
     *
     * @param batchReq     入参
     * @param serviceEntry 实例
     * @return 结果
     */
    private List<MqMessagePersistPut> buildPersistPutList(
            MqMessageBatchReq batchReq, final ServiceEntry serviceEntry) {
        List<MqMessagePersistPut> resultList = new ArrayList<>();

        // 构建列表
        List<MqMessage> messageList = batchReq.getMqMessageList();
        for (MqMessage mqMessage : messageList) {
            MqMessagePersistPut put = new MqMessagePersistPut();
            put.setRpcAddress(serviceEntry);
            put.setMessageStatus(MessageStatusConst.WAIT_CONSUMER);
            put.setMqMessage(mqMessage);

            resultList.add(put);
        }

        return resultList;
    }

    /**
     * 异步处理消息
     *
     * @param put 消息
     * @since 2024.05
     */
    private void asyncHandleMessage(MqMessagePersistPut put) {
        final MqMessage mqMessage = put.getMqMessage();
        List<ChannelGroupNameDto> channelList =
                registerConsumerService.getPushSubscribeList(mqMessage);
        if (CollectionUtils.isEmpty(channelList)) {
            log.info("监听列表为空，忽略处理");
            return;
        }

        BrokerPushContext brokerPushContext =
                BrokerPushContext.newInstance()
                        .channelList(channelList)
                        .mqMessagePersistPut(put)
                        .mqBrokerPersist(mqBrokerPersist)
                        .invokeService(invokeService)
                        .respTimeoutMills(respTimeoutMills)
                        .pushMaxAttempt(pushMaxAttempt);

        brokerPushService.asyncPush(brokerPushContext);
    }

    /**
     * 结果写回
     *
     * @param req  请求
     * @param resp 响应
     * @param ctx  上下文
     */
    private void writeResponse(RpcMessageDto req, Object resp, ChannelHandlerContext ctx) {
        final String id = ctx.channel().id().asLongText();

        RpcMessageDto rpcMessageDto = new RpcMessageDto();
        // 响应类消息
        rpcMessageDto.setRequest(false);
        rpcMessageDto.setTraceId(req.getTraceId());
        rpcMessageDto.setMethodType(req.getMethodType());
        rpcMessageDto.setRequestTime(System.currentTimeMillis());
        String json = JSON.toJSONString(resp);
        rpcMessageDto.setJson(json);

        // 回写到 client 端
        ByteBuf byteBuf = DelimiterUtil.getMessageDelimiterBuffer(rpcMessageDto);
        ctx.writeAndFlush(byteBuf);
        log.info("[Server] channel {} response {}", id, JSON.toJSON(rpcMessageDto));
    }
}
