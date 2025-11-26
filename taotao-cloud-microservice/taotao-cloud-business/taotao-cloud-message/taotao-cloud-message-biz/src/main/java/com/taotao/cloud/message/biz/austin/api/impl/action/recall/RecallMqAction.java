package com.taotao.cloud.message.biz.austin.api.impl.action.recall;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.domain.RecallTaskInfo;
import com.taotao.cloud.message.biz.austin.common.enums.RespStatusEnum;
import com.taotao.cloud.message.biz.austin.common.pipeline.BusinessProcess;
import com.taotao.cloud.message.biz.austin.common.pipeline.ProcessContext;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.service.api.impl.domain.RecallTaskModel;
import com.taotao.cloud.message.biz.austin.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author shuigedeng
 * 将撤回消息发送到MQ
 */
@Slf4j
@Service
public class RecallMqAction implements BusinessProcess<RecallTaskModel> {
    @Autowired
    private SendMqService sendMqService;

    @Value("${austin.business.recall.topic.name}")
    private String austinRecall;
    @Value("${austin.business.tagId.value}")
    private String tagId;

    @Value("${austin.mq.pipeline}")
    private String mqPipeline;

    @Override
    public void process(ProcessContext<RecallTaskModel> context) {
        RecallTaskInfo recallTaskInfo = context.getProcessModel().getRecallTaskInfo();
        try {
            String message = JSON.toJSONString(recallTaskInfo, SerializerFeature.WriteClassName);
            sendMqService.send(austinRecall, message, tagId);
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
            log.error("send {} fail! e:{},params:{}", mqPipeline, Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(recallTaskInfo));
        }
    }

}
