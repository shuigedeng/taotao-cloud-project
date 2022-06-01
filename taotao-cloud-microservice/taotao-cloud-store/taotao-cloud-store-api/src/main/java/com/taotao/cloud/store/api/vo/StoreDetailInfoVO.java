package com.taotao.cloud.store.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 店铺详细VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:25:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺详细VO")
public class StoreDetailInfoVO extends StoreDetailVO {

	@Schema(description = "会员名称")
	private String memberName;
}
