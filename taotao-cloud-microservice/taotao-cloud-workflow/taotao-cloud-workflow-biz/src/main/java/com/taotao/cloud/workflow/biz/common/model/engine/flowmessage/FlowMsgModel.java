package com.taotao.cloud.workflow.biz.common.model.engine.flowmessage;

import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskCirculateEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskNodeEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 */
@Data
@NoArgsConstructor
public class FlowMsgModel {
    private String title;
    private FlowEngineEntity engine;
    private FlowTaskEntity taskEntity;
    private FlowTaskNodeEntity taskNodeEntity;
    private List<FlowTaskNodeEntity> nodeList;
    private List<FlowTaskOperatorEntity> operatorList;
    private List<FlowTaskCirculateEntity> circulateList;
    private Map<String, Object> data;
    //代办 (通知代办)
    private boolean wait = true;
    //同意
    private boolean approve = false;
    //拒绝
    private boolean reject = false;
    //抄送人
    private boolean copy = false;
    //结束 (通知发起人)
    private boolean end = false;
    //子流程通知
    private boolean launch = false;
    //拒绝发起节点
    private boolean start = false;
}
