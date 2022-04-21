package com.taotao.cloud.message.biz.austin.api.impl.service;

import cn.monitor4all.logRecord.annotation.OperationLog;
import com.taotao.cloud.message.biz.austin.api.domain.BatchSendRequest;
import com.taotao.cloud.message.biz.austin.api.domain.SendRequest;
import com.taotao.cloud.message.biz.austin.api.domain.SendResponse;
import com.taotao.cloud.message.biz.austin.api.impl.domain.SendTaskModel;
import com.taotao.cloud.message.biz.austin.api.service.SendService;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.support.pipeline.ProcessContext;
import com.taotao.cloud.message.biz.austin.support.pipeline.ProcessController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 发送接口
 *
 * @author 3y
 */
@Service
public class SendServiceImpl implements SendService {

    @Autowired
    private ProcessController processController;

    @Override
    @OperationLog(bizType = "SendService#send", bizId = "#sendRequest.messageTemplateId", msg = "#sendRequest")
    public SendResponse send(SendRequest sendRequest) {

        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .messageParamList(Collections.singletonList(sendRequest.getMessageParam()))
                .build();

        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success()).build();

        ProcessContext process = processController.process(context);

        return new SendResponse(process.getResponse().getStatus(), process.getResponse().getMsg());
    }

    @Override
    @OperationLog(bizType = "SendService#batchSend", bizId = "#batchSendRequest.messageTemplateId", msg = "#batchSendRequest")
    public SendResponse batchSend(BatchSendRequest batchSendRequest) {
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(batchSendRequest.getMessageTemplateId())
                .messageParamList(batchSendRequest.getMessageParamList())
                .build();

        ProcessContext context = ProcessContext.builder()
                .code(batchSendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success()).build();

        ProcessContext process = processController.process(context);

        return new SendResponse(process.getResponse().getStatus(), process.getResponse().getMsg());
    }


}
