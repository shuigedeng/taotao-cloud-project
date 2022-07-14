package com.taotao.cloud.workflow.biz.engine.model.flowbefore;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
public class FlowTaskOperatorModel {
    @Schema(description = "节点经办主键")
    private String id;
    @Schema(description = "经办对象")
    private String handleType;
    @Schema(description = "经办主键")
    private String handleId;
    @Schema(description = "处理状态 0-拒绝、1-同意")
    private Integer handleStatus;
    @Schema(description = "处理时间")
    private Long handleTime;
    @Schema(description = "节点编码")
    private String nodeCode;
    @Schema(description = "节点名称")
    private String nodeName;
    @Schema(description = "是否完成")
    private Integer completion;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "创建时间")
    private Long creatorTime;
    @Schema(description = "节点主键")
    private String taskNodeId;
    @Schema(description = "任务主键")
    private String taskId;
}
