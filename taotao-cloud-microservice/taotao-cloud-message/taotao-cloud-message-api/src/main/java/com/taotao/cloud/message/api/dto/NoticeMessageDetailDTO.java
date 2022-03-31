package com.taotao.cloud.message.api.dto;

import cn.lili.modules.message.entity.dos.NoticeMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站内信消息DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeMessageDetailDTO extends NoticeMessage {

	@Schema(description = "消息变量")
	private List<String> variables;
}
