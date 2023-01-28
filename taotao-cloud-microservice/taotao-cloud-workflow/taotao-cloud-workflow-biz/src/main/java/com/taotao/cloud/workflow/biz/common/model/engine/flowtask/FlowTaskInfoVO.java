package com.taotao.cloud.workflow.biz.common.model.engine.flowtask;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FlowTaskInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "引擎id")
    private String flowId;
    @Schema(description = "界面数据")
    private String data;
}
