package com.taotao.cloud.workflow.biz.common.model.engine.flowtask.method;

import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.ChildNodeList;
import java.util.List;

import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskNodeEntity;
import lombok.Data;

@Data
public class TaskHandleIdStatus {
   /**审批类型（0：拒绝，1：同意）**/
   private Integer status;
   /**当前节点属性**/
   private ChildNodeList nodeModel;
   /**用户**/
   private UserInfo userInfo;
   /**审批对象**/
   private FlowModel flowModel;
   /**节点list**/
   private List<FlowTaskNodeEntity> taskNodeList;

}
