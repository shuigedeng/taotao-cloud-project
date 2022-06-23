package com.taotao.cloud.store.api.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 会员店铺收藏VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "租会员店铺收藏VO户id")
public class StoreCollectionVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "店铺id")
	private String id;

	@Schema(description = "店铺名称")
	private String storeName;

	@Schema(description = "店铺Logo")
	private String storeLogo;

	@Schema(description = "是否自营")
	private Boolean selfOperated;
}
