package com.taotao.cloud.workflow.api.common.model.engine.flowtask.method;


import java.util.Date;

import com.taotao.cloud.workflow.api.common.model.engine.flowengine.shuntjson.nodejson.ChildNodeList;
import lombok.Data;

/**
 *
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

