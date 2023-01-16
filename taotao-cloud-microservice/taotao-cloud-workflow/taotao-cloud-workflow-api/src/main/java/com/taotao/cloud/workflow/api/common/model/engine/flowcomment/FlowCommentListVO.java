package com.taotao.cloud.workflow.api.common.model.engine.flowcomment;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
public class FlowCommentListVO {

	@Schema(description = "附件")
	private String file;

	@Schema(description = "图片")
	private String image;

	@Schema(description = "流程id")
	private String taskId;

	@Schema(description = "文本")
	private String text;

	@Schema(description = "创建人")
	private Long creatorUserId;

	@Schema(description = "创建人")
	private String creatorUserName;

	@Schema(description = "头像")
	private String creatorUserHeadIcon;

	@Schema(description = "创建时间")
	private Long creatorTime;

	@Schema(description = "是否本人")
	private Boolean isDel;

	@Schema(description = "主键")
	private String id;

}
