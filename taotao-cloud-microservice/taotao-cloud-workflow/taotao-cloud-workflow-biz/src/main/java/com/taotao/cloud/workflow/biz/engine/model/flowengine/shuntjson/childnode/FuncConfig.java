package com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.childnode;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author ：JNPF开发平台组
 * @version: V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date ：2022/3/31 14:53
 */
@Data
public class FuncConfig {

    private boolean on = false;
    private String msgId;
    private String interfaceId;
    private String msgName;
    private List<TemplateJsonModel> templateJson = new ArrayList<>();
}
