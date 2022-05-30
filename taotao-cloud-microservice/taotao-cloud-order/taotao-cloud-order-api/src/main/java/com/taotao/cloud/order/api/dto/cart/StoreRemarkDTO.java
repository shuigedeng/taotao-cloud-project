package com.taotao.cloud.order.api.dto.cart;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * 店铺备注
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:17:04
 */
@RecordBuilder
@Schema(description = "店铺备注")
public record StoreRemarkDTO(

	@Schema(description = "店铺id")
	String storeId,

	@Schema(description = "备注")
	String remark
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -6793274046513576434L;


}
