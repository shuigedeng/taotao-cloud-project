//package com.taotao.cloud.stream.consumer.trigger.executor;
//
//import cn.hutool.json.JSONUtil;
//import com.taotao.cloud.stream.consumer.trigger.TimeTriggerExecutor;
//import com.taotao.cloud.stream.framework.trigger.message.PintuanOrderMessage;
//import com.taotao.cloud.stream.framework.trigger.model.TimeExecuteConstant;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 促销事件触发
// */
//@Component(TimeExecuteConstant.PROMOTION_EXECUTOR)
//public class PromotionTimeTriggerExecutor implements TimeTriggerExecutor {
//    /**
//     * 订单
//     */
//    @Autowired
//    private OrderService orderService;
//
//
//    @Override
//    public void execute(Object object) {
//        //拼团订单消息
//        PintuanOrderMessage pintuanOrderMessage = JSONUtil.toBean(JSONUtil.parseObj(object), PintuanOrderMessage.class);
//        if (pintuanOrderMessage != null && pintuanOrderMessage.getPintuanId() != null) {
//            log.info("拼团订单信息消费：{}", pintuanOrderMessage);
//            //拼团订单自动处理
//            orderService.agglomeratePintuanOrder(pintuanOrderMessage.getPintuanId(), pintuanOrderMessage.getOrderSn());
//        }
//        Pintuan pintuan = JSONUtil.toBean(JSONUtil.parseObj(object), Pintuan.class);
//        if (pintuan != null && pintuan.getId() != null) {
//            this.orderService.checkFictitiousOrder(pintuan.getId(), pintuan.getRequiredNum(), pintuan.getFictitious());
//        }
//    }
//
//
//}
