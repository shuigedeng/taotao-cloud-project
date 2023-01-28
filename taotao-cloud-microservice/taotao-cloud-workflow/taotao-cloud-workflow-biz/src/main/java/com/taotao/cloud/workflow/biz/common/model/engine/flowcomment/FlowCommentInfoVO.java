package com.taotao.cloud.workflow.biz.common.model.engine.flowcomment;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 */
@Data
public class FlowCommentInfoVO {

    @Schema(description = "附件")
    private String file;

    @Schema(description = "图片")
    private String image;

    @Schema(description = "流程id")
    private String taskId;

    @Schema(description = "文本")
    private String text;

    @Schema(description = "主键")
    private String id;
}
