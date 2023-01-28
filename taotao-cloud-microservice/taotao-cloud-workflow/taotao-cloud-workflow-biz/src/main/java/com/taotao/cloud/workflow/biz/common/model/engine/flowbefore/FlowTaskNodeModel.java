package com.taotao.cloud.workflow.biz.common.model.engine.flowbefore;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
public class FlowTaskNodeModel {
    @Schema(description = "节点实例主键")
    private String id;
    @Schema(description = "节点编码")
    private String nodeCode;
    @Schema(description = "节点名称")
    private String nodeName;
    @Schema(description = "节点类型")
    private String nodeType;
    @Schema(description = "节点属性Json")
    private String nodePropertyJson;
    @Schema(description = "上一节点")
    private String nodeUp;
    @Schema(description = "下一节点")
    private String nodeNext;
    @Schema(description = "是否完成")
    private Integer completion;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "排序码")
    private Long  sortCode;
    @Schema(description = "创建时间")
    private Long  creatorTime;
    @Schema(description = "任务主键")
    private String taskId;
    @Schema(description = "审核用户")
    private String userName;
    @Schema(description = "审批类型")
    private String assigneeName;
    @Schema(description = "节点状态")
    /**-1没有经过,0.经过 1.当前 2.未经过**/
    private String type;

}
