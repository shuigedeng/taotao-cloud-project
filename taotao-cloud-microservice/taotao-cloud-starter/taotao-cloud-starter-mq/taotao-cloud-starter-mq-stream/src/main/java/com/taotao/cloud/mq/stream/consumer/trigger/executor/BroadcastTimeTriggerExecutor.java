package com.taotao.cloud.mq.stream.consumer.trigger.executor;//package com.taotao.cloud.stream.consumer.trigger.executor;
//
//import cn.hutool.json.JSONUtil;
//import com.taotao.cloud.stream.consumer.trigger.TimeTriggerExecutor;
//import com.taotao.cloud.stream.framework.trigger.message.BroadcastMessage;
//import com.taotao.cloud.stream.framework.trigger.model.TimeExecuteConstant;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 直播间事件触发
// *
// */
//@Component(TimeExecuteConstant.BROADCAST_EXECUTOR)
//public class BroadcastTimeTriggerExecutor implements TimeTriggerExecutor {
//
//
//    @Autowired
//    private StudioService studioService;
//
//    @Override
//    public void execute(Object object) {
//        //直播间订单消息
//        BroadcastMessage broadcastMessage = JSONUtil.toBean(JSONUtil.parseObj(object), BroadcastMessage.class);
//        if (broadcastMessage != null && broadcastMessage.getStudioId() != null) {
//            log.info("直播间消费：{}", broadcastMessage);
//            //修改直播间状态
//            studioService.updateStudioStatus(broadcastMessage);
//        }
//    }
//}
