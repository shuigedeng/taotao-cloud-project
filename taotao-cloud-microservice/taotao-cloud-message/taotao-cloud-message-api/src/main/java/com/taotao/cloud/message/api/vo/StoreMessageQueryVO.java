package com.taotao.cloud.message.api.vo;

import com.taotao.cloud.message.api.enums.MessageStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 店铺消息查询
 */
@Data
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
