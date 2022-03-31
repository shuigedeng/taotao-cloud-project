package com.taotao.cloud.store.api.vo;

import com.taotao.cloud.store.api.dto.StoreEditDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 店铺详细VO
 *
 * 
 * @since 2020-03-09 21:53:20
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺详细VO")
public class StoreDetailVO extends StoreEditDTO {

	@Schema(description = "会员名称")
	private String memberName;
}
