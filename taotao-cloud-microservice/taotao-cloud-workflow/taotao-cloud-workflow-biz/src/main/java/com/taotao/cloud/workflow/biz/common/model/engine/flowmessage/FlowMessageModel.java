package com.taotao.cloud.workflow.biz.common.model.engine.flowmessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorRecordEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowMessageEnum;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.MsgConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
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
