package com.taotao.cloud.workflow.biz.engine.model.flowtask.method;


import java.util.Date;
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
public class TaskOperatoUser {
    /**审批人id*/
    private String handLeId;
    /**审批日期*/
    private Date date;
    /**当前节点数据*/
    private ChildNodeList childNode;
    /**经办id*/
    private String id;
}

