package com.taotao.cloud.workflow.biz.engine.model.flowtask.method;

import jnpf.engine.entity.FlowTaskEntity;
import jnpf.engine.model.flowengine.FlowModel;
import jnpf.engine.model.flowengine.shuntjson.nodejson.ChildNodeList;
import lombok.Data;


/**
 *
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021-08-26
 */
@Data
public class TaskOperator {
    /**当前节点数据*/
    private ChildNodeList childNode;
    /**引擎实例*/
    private FlowTaskEntity taskEntity;
    /**提交数据*/
    private FlowModel flowModel;
    /**true记录 false不记录*/
    private Boolean details;
    /**经办id*/
    private String id;
}
