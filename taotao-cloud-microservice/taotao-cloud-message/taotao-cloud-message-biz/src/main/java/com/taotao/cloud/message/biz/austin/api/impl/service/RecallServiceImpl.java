package com.taotao.cloud.message.biz.austin.api.impl.service;

import com.taotao.cloud.message.biz.austin.api.domain.SendRequest;
import com.taotao.cloud.message.biz.austin.api.domain.SendResponse;
import com.taotao.cloud.message.biz.austin.api.impl.domain.SendTaskModel;
import com.taotao.cloud.message.biz.austin.api.service.RecallService;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.support.pipeline.ProcessContext;
import com.taotao.cloud.message.biz.austin.support.pipeline.ProcessController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 撤回接口
 *
 * @author 3y
 */
@Service
public class RecallServiceImpl implements RecallService {

	@Autowired
	private ProcessController processController;

	@Override
	public SendResponse recall(SendRequest sendRequest) {
		SendTaskModel sendTaskModel = SendTaskModel.builder()
			.messageTemplateId(sendRequest.getMessageTemplateId())
			.build();
		ProcessContext context = ProcessContext.builder()
			.code(sendRequest.getCode())
			.processModel(sendTaskModel)
			.needBreak(false)
			.response(BasicResultVO.success()).build();
		ProcessContext process = processController.process(context);
		return new SendResponse(process.getResponse().getStatus(), process.getResponse().getMsg());
	}
}
