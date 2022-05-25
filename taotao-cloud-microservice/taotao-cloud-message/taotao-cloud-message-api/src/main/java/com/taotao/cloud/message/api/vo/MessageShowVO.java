package com.taotao.cloud.message.api.vo;

import com.taotao.cloud.message.api.enums.MessageShowType;
import com.taotao.cloud.message.api.enums.RangeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-25 15:46:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "消息")
public class MessageShowVO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "标题")
	private String title;

	/**
	 * @see MessageShowType
	 */
	@Schema(description = "消息类型")
	private String type;

	@Schema(description = "消息内容")
	private String content;
	/**
	 * @see RangeEnum
	 */
	@Schema(description = "发送范围")
	private String messageRange;

}
