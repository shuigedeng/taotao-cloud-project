//package com.taotao.cloud.stream.consumer.event.impl;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * 微信消息执行器
// *
// */
//
//@Service
//public class WechatMessageExecute implements OrderStatusChangeEvent, TradeEvent {
//
//    @Autowired
//    private WechatMessageUtil wechatMessageUtil;
//
//    @Override
//    public void orderCreate(TradeDTO tradeDTO) {
//        for (OrderVO orderVO : tradeDTO.getOrderVO()) {
//            try {
//                wechatMessageUtil.sendWechatMessage(orderVO.getSn());
//            } catch (Exception e) {
//                log.error("微信消息发送失败：" + orderVO.getSn(), e);
//            }
//        }
//    }
//
//    @Override
//    public void orderChange(OrderMessage orderMessage) {
//
//        switch (orderMessage.getNewStatus()) {
//            case PAID:
//            case UNDELIVERED:
//            case DELIVERED:
//            case COMPLETED:
//                try {
//                    wechatMessageUtil.sendWechatMessage(orderMessage.getOrderSn());
//                } catch (Exception e) {
//                    log.error("微信消息发送失败", e);
//                }
//                break;
//            default:
//                break;
//        }
//
//    }
//}
