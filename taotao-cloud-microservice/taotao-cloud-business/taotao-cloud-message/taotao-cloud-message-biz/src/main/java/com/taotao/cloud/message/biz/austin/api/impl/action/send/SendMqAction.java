package com.taotao.cloud.message.biz.austin.api.impl.action.send;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.domain.SimpleTaskInfo;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.enums.RespStatusEnum;
import com.taotao.cloud.message.biz.austin.common.pipeline.BusinessProcess;
import com.taotao.cloud.message.biz.austin.common.pipeline.ProcessContext;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.service.api.impl.domain.SendTaskModel;
import com.taotao.cloud.message.biz.austin.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shuigedeng
 * 1. 将消息发送到MQ
 * 2. 返回拼装好的messageId给到接口调用方
 */
@Slf4j
@Service
public class SendMqAction implements BusinessProcess<SendTaskModel> {


    @Autowired
    private SendMqService sendMqService;

    @Value("${austin.business.topic.name}")
    private String sendMessageTopic;

    @Value("${austin.business.tagId.value}")
    private String tagId;

    @Value("${austin.mq.pipeline}")
    private String mqPipeline;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        List<TaskInfo> taskInfo = sendTaskModel.getTaskInfo();
        try {
            String message = JSON.toJSONString(sendTaskModel.getTaskInfo(), SerializerFeature.WriteClassName);
            sendMqService.send(sendMessageTopic, message, tagId);

            context.setResponse(BasicResultVO.success(taskInfo.stream().map(v -> SimpleTaskInfo.builder().businessId(v.getBusinessId()).messageId(v.getMessageId()).bizId(v.getBizId()).build()).collect(Collectors.toList())));
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
            log.error("send {} fail! e:{},params:{}", mqPipeline, Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(CollUtil.getFirst(taskInfo.listIterator())));
        }
    }

}
