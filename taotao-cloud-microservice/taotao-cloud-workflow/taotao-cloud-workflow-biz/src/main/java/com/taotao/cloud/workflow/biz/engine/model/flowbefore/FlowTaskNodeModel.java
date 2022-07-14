package com.taotao.cloud.workflow.biz.engine.model.flowbefore;

import io.swagger.annotations.ApiModelProperty;
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
public class FlowTaskNodeModel {
    @ApiModelProperty(value = "节点实例主键")
    private String id;
    @ApiModelProperty(value = "节点编码")
    private String nodeCode;
    @ApiModelProperty(value = "节点名称")
    private String nodeName;
    @ApiModelProperty(value = "节点类型")
    private String nodeType;
    @ApiModelProperty(value = "节点属性Json")
    private String nodePropertyJson;
    @ApiModelProperty(value = "上一节点")
    private String nodeUp;
    @ApiModelProperty(value = "下一节点")
    private String nodeNext;
    @ApiModelProperty(value = "是否完成")
    private Integer completion;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "排序码")
    private Long  sortCode;
    @ApiModelProperty(value = "创建时间")
    private Long  creatorTime;
    @ApiModelProperty(value = "任务主键")
    private String taskId;
    @ApiModelProperty(value = "审核用户")
    private String userName;
    @ApiModelProperty(value = "审批类型")
    private String assigneeName;
    @ApiModelProperty(value = "节点状态")
    /**-1没有经过,0.经过 1.当前 2.未经过**/
    private String type;

}
