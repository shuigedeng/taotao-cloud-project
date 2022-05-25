package com.taotao.cloud.message.api.vo;

import com.taotao.cloud.message.api.enums.MessageStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 店铺消息查询
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-25 15:46:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "消息")
public class StoreMessageQueryVO {

	private static final long serialVersionUID = 1L;

	/**
	 * @see MessageStatusEnum
	 */
	@Schema(description = "状态")
	private String status;

	@Schema(description = "消息id")
	private String messageId;

	@Schema(description = "商家id")
	private String storeId;

}
