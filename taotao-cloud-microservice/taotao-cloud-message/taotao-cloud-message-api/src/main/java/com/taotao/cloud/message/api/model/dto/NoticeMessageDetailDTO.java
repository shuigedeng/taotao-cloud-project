package com.taotao.cloud.message.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 站内信消息DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-19 14:59:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeMessageDetailDTO extends NoticeMessageDTO {

	@Schema(description = "消息变量")
	private List<String> variables;
}
