package com.taotao.cloud.message.biz.austin.api.impl.config;


import com.taotao.cloud.message.biz.austin.common.pipeline.ProcessController;
import com.taotao.cloud.message.biz.austin.common.pipeline.ProcessTemplate;
import com.taotao.cloud.message.biz.austin.service.api.enums.BusinessCode;
import com.taotao.cloud.message.biz.austin.service.api.impl.action.recall.RecallAssembleAction;
import com.taotao.cloud.message.biz.austin.service.api.impl.action.recall.RecallMqAction;
import com.taotao.cloud.message.biz.austin.service.api.impl.action.send.SendAfterCheckAction;
import com.taotao.cloud.message.biz.austin.service.api.impl.action.send.SendAssembleAction;
import com.taotao.cloud.message.biz.austin.service.api.impl.action.send.SendMqAction;
import com.taotao.cloud.message.biz.austin.service.api.impl.action.send.SendPreCheckAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * api层的pipeline配置类
 *
 * @author shuigedeng
 */
@Configuration
public class PipelineConfig {

    @Autowired
    private SendPreCheckAction sendPreCheckAction;
    @Autowired
    private SendAssembleAction sendAssembleAction;
    @Autowired
    private SendAfterCheckAction sendAfterCheckAction;
    @Autowired
    private SendMqAction sendMqAction;

    @Autowired
    private RecallAssembleAction recallAssembleAction;
    @Autowired
    private RecallMqAction recallMqAction;


    /**
     * 普通发送执行流程
     * 1. 前置参数校验
     * 2. 组装参数
     * 3. 后置参数校验
     * 4. 发送消息至MQ
     *
     * @return
     */
    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(Arrays.asList(sendPreCheckAction, sendAssembleAction,
                sendAfterCheckAction, sendMqAction));
        return processTemplate;
    }

    /**
     * 消息撤回执行流程
     * 1.组装参数
     * 2.发送MQ
     *
     * @return
     */
    @Bean("recallMessageTemplate")
    public ProcessTemplate recallMessageTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(Arrays.asList(recallAssembleAction, recallMqAction));
        return processTemplate;
    }

    /**
     * pipeline流程控制器
     * 后续扩展则加BusinessCode和ProcessTemplate
     *
     * @return
     */
    @Bean("apiProcessController")
    public ProcessController apiProcessController() {
        ProcessController processController = new ProcessController();
        Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), commonSendTemplate());
        templateConfig.put(BusinessCode.RECALL.getCode(), recallMessageTemplate());
        processController.setTemplateConfig(templateConfig);
        return processController;
    }

}
