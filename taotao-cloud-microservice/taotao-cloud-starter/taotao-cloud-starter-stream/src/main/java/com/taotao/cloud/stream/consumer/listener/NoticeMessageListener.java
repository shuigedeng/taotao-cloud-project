//package com.taotao.cloud.stream.consumer.listener;
//
//import cn.hutool.json.JSONUtil;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 站内信通知
// */
//@Component
//@RocketMQMessageListener(topic = "${taotao.data.rocketmq.notice-topic}", consumerGroup = "${taotao.data.rocketmq.notice-group}")
//public class NoticeMessageListener implements RocketMQListener<MessageExt> {
//
//    /**
//     * 站内信
//     */
//    @Autowired
//    private NoticeMessageService noticeMessageService;
//
//    @Override
//    public void onMessage(MessageExt messageExt) {
//        NoticeMessageDTO noticeMessageDTO = JSONUtil.toBean(new String(messageExt.getBody()), NoticeMessageDTO.class);
//        noticeMessageService.noticeMessage(noticeMessageDTO);
//    }
//}
