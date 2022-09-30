package com.taotao.cloud.message.biz.austin.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.api.enums.BusinessCode;
import com.taotao.cloud.message.biz.austin.api.impl.domain.SendTaskModel;
import com.taotao.cloud.message.biz.austin.common.enums.RespStatusEnum;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.support.mq.SendMqService;
import com.taotao.cloud.message.biz.austin.support.pipeline.BusinessProcess;
import com.taotao.cloud.message.biz.austin.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author 3y
 * 将消息发送到MQ
 */
@Slf4j
@Service
public class SendMqAction implements BusinessProcess<SendTaskModel> {


	@Autowired
	private SendMqService sendMqService;

	@Value("${austin.business.topic.name}")
	private String sendMessageTopic;

	@Value("${austin.business.recall.topic.name}")
	private String austinRecall;
	@Value("${austin.business.tagId.value}")
	private String tagId;

	@Value("${austin.mq.pipeline}")
	private String mqPipeline;


	@Override
	public void process(ProcessContext<SendTaskModel> context) {
		SendTaskModel sendTaskModel = context.getProcessModel();
		try {
			if (BusinessCode.COMMON_SEND.getCode().equals(context.getCode())) {
				String message = JSON.toJSONString(sendTaskModel.getTaskInfo(), new SerializerFeature[]{SerializerFeature.WriteClassName});
				sendMqService.send(sendMessageTopic, message, tagId);
			} else if (BusinessCode.RECALL.getCode().equals(context.getCode())) {
				String message = JSON.toJSONString(sendTaskModel.getMessageTemplate(), new SerializerFeature[]{SerializerFeature.WriteClassName});
				sendMqService.send(austinRecall, message, tagId);
			}
		} catch (Exception e) {
			context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
			log.error("send {} fail! e:{},params:{}", mqPipeline, Throwables.getStackTraceAsString(e)
				, JSON.toJSONString(CollUtil.getFirst(sendTaskModel.getTaskInfo().listIterator())));
		}
	}

}
