package com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.childnode;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 解析引擎
 *
 */
@Data
public class MsgConfig {
    //关闭 0  自定义 1 同步发起配置 2
    private int on = 0;
    private String msgId;
    private String interfaceId;
    private String msgName;
    private List<TemplateJsonModel> templateJson = new ArrayList<>();
}
