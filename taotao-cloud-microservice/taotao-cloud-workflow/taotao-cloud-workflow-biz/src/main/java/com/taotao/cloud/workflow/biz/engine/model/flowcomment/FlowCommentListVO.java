package com.taotao.cloud.workflow.biz.engine.model.flowcomment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 版本: V3.1.0
 * 版权: 引迈信息技术有限公司
 * 作者： JNPF开发平台组
 */
@Data
public class FlowCommentListVO {

    @ApiModelProperty(value = "附件")
    private String file;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "流程id")
    private String taskId;

    @ApiModelProperty(value = "文本")
    private String text;

    @ApiModelProperty(value = "创建人")
    private String creatorUserId;

    @ApiModelProperty(value = "创建人")
    private String creatorUserName;

    @ApiModelProperty(value = "头像")
    private String creatorUserHeadIcon;

    @ApiModelProperty(value = "创建时间")
    private Long  creatorTime;

    @ApiModelProperty(value = "是否本人")
    private Boolean isDel;

    @ApiModelProperty(value = "主键")
    private String id;

}
