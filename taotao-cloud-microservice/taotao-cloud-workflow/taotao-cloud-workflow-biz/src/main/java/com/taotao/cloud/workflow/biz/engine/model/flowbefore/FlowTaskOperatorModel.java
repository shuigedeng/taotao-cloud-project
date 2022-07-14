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
public class FlowTaskOperatorModel {
    @ApiModelProperty(value = "节点经办主键")
    private String id;
    @ApiModelProperty(value = "经办对象")
    private String handleType;
    @ApiModelProperty(value = "经办主键")
    private String handleId;
    @ApiModelProperty(value = "处理状态 0-拒绝、1-同意")
    private Integer handleStatus;
    @ApiModelProperty(value = "处理时间")
    private Long handleTime;
    @ApiModelProperty(value = "节点编码")
    private String nodeCode;
    @ApiModelProperty(value = "节点名称")
    private String nodeName;
    @ApiModelProperty(value = "是否完成")
    private Integer completion;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "创建时间")
    private Long creatorTime;
    @ApiModelProperty(value = "节点主键")
    private String taskNodeId;
    @ApiModelProperty(value = "任务主键")
    private String taskId;
}
