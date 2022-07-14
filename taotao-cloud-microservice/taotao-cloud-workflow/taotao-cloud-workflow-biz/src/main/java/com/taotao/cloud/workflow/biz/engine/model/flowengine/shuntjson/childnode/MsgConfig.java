package com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.childnode;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 解析引擎
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
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
