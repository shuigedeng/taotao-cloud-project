package com.taotao.cloud.message.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 站内信消息DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-19 14:59:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeMessageDetailDTO extends NoticeMessage {

	@Schema(description = "消息变量")
	private List<String> variables;
}
