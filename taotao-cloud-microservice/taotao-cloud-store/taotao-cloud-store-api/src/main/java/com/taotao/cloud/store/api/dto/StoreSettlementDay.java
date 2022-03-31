package com.taotao.cloud.store.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 店铺结算日
 *
 * 
 * @since 2021/2/20 3:24 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺结算日")
public class StoreSettlementDay {

	@Schema(description = "店铺ID")
	private String storeId;

	@Schema(description = "结算日")
	private LocalDate settlementDay;
}
