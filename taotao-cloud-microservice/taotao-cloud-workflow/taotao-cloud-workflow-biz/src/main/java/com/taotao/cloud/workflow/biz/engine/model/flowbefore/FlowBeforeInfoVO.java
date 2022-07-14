package com.taotao.cloud.workflow.biz.engine.model.flowbefore;

import java.util.List;
import java.util.Map;
import jnpf.engine.model.flowengine.shuntjson.childnode.FormOperates;
import jnpf.engine.model.flowengine.shuntjson.childnode.Properties;
import lombok.Data;

/**
 *
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:18
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
