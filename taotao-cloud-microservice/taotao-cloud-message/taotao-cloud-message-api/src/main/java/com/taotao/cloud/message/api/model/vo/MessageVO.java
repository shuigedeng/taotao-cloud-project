package com.taotao.cloud.message.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "消息")
public class MessageVO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "标题")
	private String title;

	@Schema(description = "内容")
	private String content;

}
