package com.taotao.cloud.flowable.biz.bpm.service.oa.listener;

import com.taotao.cloud.flowable.biz.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.taotao.cloud.flowable.biz.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.taotao.cloud.flowable.biz.bpm.service.oa.BpmOALeaveService;
import com.taotao.cloud.flowable.biz.bpm.service.oa.BpmOALeaveServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OA 请假单的结果的监听器实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmOALeaveResultListener extends BpmProcessInstanceResultEventListener {

    @Resource
    private BpmOALeaveService leaveService;

    @Override
    protected String getProcessDefinitionKey() {
        return BpmOALeaveServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        leaveService.updateLeaveResult(Long.parseLong(event.getBusinessKey()), event.getResult());
    }

}
