package com.taotao.cloud.workflow.biz.engine.model.flowmessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jnpf.engine.entity.FlowTaskOperatorRecordEntity;
import jnpf.engine.enums.FlowMessageEnum;
import jnpf.engine.model.flowengine.shuntjson.childnode.MsgConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowMessageModel {
    private String title = "";
    private Integer type =  FlowMessageEnum.wait.getCode();
    private Integer status;
    private MsgConfig msgConfig = new MsgConfig();
    private List<String> userList = new ArrayList<>();
    private Map<String, Object> data = new HashMap<>();
    private Map<String,String> contMsg = new HashMap<>();
    private String  fullName;
    private FlowTaskOperatorRecordEntity recordEntity;
}
