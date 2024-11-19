package com.taotao.cloud.message.biz.austin.api.impl.action.recall;

import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import com.taotao.cloud.message.biz.austin.common.domain.RecallTaskInfo;
import com.taotao.cloud.message.biz.austin.common.enums.RespStatusEnum;
import com.taotao.cloud.message.biz.austin.common.pipeline.BusinessProcess;
import com.taotao.cloud.message.biz.austin.common.pipeline.ProcessContext;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.service.api.impl.domain.RecallTaskModel;
import com.taotao.cloud.message.biz.austin.support.dao.MessageTemplateDao;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author shuigedeng
 * 组装撤回参数
 */
@Slf4j
@Service
public class RecallAssembleAction implements BusinessProcess<RecallTaskModel> {

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Override
    public void process(ProcessContext<RecallTaskModel> context) {
        RecallTaskModel recallTaskModel = context.getProcessModel();
        Long messageTemplateId = recallTaskModel.getMessageTemplateId();
        try {
            Optional<MessageTemplate> messageTemplate = messageTemplateDao.findById(messageTemplateId);
            if (!messageTemplate.isPresent() || messageTemplate.get().getIsDeleted().equals(CommonConstant.TRUE)) {
                context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.TEMPLATE_NOT_FOUND));
                return;
            }

            RecallTaskInfo recallTaskInfo = RecallTaskInfo.builder().messageTemplateId(messageTemplateId)
                    .recallMessageId(recallTaskModel.getRecallMessageId())
                    .sendAccount(messageTemplate.get().getSendAccount())
                    .sendChannel(messageTemplate.get().getSendChannel())
                    .build();
            recallTaskModel.setRecallTaskInfo(recallTaskInfo);

        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
            log.error("assemble recall task fail! templateId:{}, e:{}", messageTemplateId, Throwables.getStackTraceAsString(e));
        }
    }

}
