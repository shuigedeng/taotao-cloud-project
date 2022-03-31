package com.taotao.cloud.message.api.dto;

import com.taotao.cloud.message.api.enums.NoticeMessageNodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站内信消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeMessageDTO {

	@Schema(description = "会员ID")
	private String memberId;

	@Schema(description = "消息节点")
	private NoticeMessageNodeEnum noticeMessageNodeEnum;

	@Schema(description = "消息参数")
	private Map<String, String> parameter;
}
