package com.taotao.cloud.workflow.biz.common.model.engine.flowbefore;

import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.Properties;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.FormOperates;
import java.util.List;
import java.util.Map;

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
