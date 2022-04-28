package com.taotao.cloud.order.api.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 店铺备注
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:17:04
 */
@Data
@Builder
@Schema(description = "店铺备注")
public class StoreRemarkDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -6793274046513576434L;

	@Schema(description = "店铺id")
	private String storeId;

	@Schema(description = "备注")
	private String remark;

}
