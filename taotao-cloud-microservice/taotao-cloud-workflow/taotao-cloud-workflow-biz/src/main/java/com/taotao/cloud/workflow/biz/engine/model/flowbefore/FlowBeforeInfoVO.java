package com.taotao.cloud.workflow.biz.engine.model.flowbefore;

import java.util.List;
import java.util.Map;

import com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.childnode.FormOperates;
import lombok.Data;

/**
 *
 */
@Data
public class FlowBeforeInfoVO {
    private FlowTaskModel flowTaskInfo;
    private List<FlowTaskNodeModel> flowTaskNodeList;
    private List<FlowTaskOperatorModel> flowTaskOperatorList;
    private List<FlowTaskOperatorRecordModel> flowTaskOperatorRecordList;
    private String flowFormInfo;
    private List<FormOperates> formOperates;
    private Properties approversProperties;
    private Map<String,Object> draftData;
}
