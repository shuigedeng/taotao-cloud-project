//package com.taotao.cloud.stream.consumer.listener;
//
//import cn.hutool.json.JSONUtil;
//import com.taotao.cloud.stream.consumer.event.AfterSaleStatusChangeEvent;
//import com.taotao.cloud.stream.framework.rocketmq.tags.AfterSaleTagsEnum;
//import java.util.List;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 售后通知
// */
//@Component
//@RocketMQMessageListener(topic = "${lili.data.rocketmq.after-sale-topic}", consumerGroup = "${lili.data.rocketmq.after-sale-group}")
//public class AfterSaleMessageListener implements RocketMQListener<MessageExt> {
//
//    /**
//     * 售后订单状态
//     */
//    @Autowired
//    private List<AfterSaleStatusChangeEvent> afterSaleStatusChangeEvents;
//
//    @Override
//    public void onMessage(MessageExt messageExt) {
//        if (AfterSaleTagsEnum.valueOf(messageExt.getTags()) == AfterSaleTagsEnum.AFTER_SALE_STATUS_CHANGE) {
//            for (AfterSaleStatusChangeEvent afterSaleStatusChangeEvent : afterSaleStatusChangeEvents) {
//                try {
//                    AfterSale afterSale = JSONUtil.toBean(new String(messageExt.getBody()), AfterSale.class);
//                    afterSaleStatusChangeEvent.afterSaleStatusChange(afterSale);
//                } catch (Exception e) {
//                    log.error("售后{},在{}业务中，状态修改事件执行异常",
//                            new String(messageExt.getBody()),
//                            afterSaleStatusChangeEvent.getClass().getName(),
//                            e);
//                }
//            }
//        }
//
//    }
//}
