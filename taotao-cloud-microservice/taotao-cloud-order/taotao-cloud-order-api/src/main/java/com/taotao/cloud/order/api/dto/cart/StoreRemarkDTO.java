package com.taotao.cloud.order.api.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 店铺备注
 *
 * @since 2020-03-25 2:30 下午
 */
@Schema(description = "店铺备注")
public class StoreRemarkDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -6793274046513576434L;

	@Schema(description = "店铺id")
	private String storeId;

	@Schema(description = "备注")
	private String remark;

}
