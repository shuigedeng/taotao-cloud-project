package com.taotao.cloud.workflow.biz.engine.model.flowcomment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 版本: V3.1.0
 * 版权: 引迈信息技术有限公司
 * 作者： JNPF开发平台组
 */
@Data
public class FlowCommentInfoVO {

    @ApiModelProperty(value = "附件")
    private String file;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "流程id")
    private String taskId;

    @ApiModelProperty(value = "文本")
    private String text;

    @ApiModelProperty(value = "主键")
    private String id;
}
