package com.taotao.cloud.message.biz.austin.handler.pending;


import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.pipeline.ProcessContext;
import com.taotao.cloud.message.biz.austin.common.pipeline.ProcessController;
import com.taotao.cloud.message.biz.austin.common.pipeline.ProcessModel;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.handler.config.TaskPipelineConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Task 执行器
 *
 * @author shuigedeng
 */
@Data
@Accessors(chain = true)
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task implements Runnable {
    private TaskInfo taskInfo;
    @Autowired
    @Qualifier("handlerProcessController")
    private ProcessController processController;

    @Override
    public void run() {
        ProcessContext<ProcessModel> context = ProcessContext.builder()
                .processModel(taskInfo).code(TaskPipelineConfig.PIPELINE_HANDLER_CODE)
                .needBreak(false).response(BasicResultVO.success())
                .build();
        processController.process(context);
    }
}
