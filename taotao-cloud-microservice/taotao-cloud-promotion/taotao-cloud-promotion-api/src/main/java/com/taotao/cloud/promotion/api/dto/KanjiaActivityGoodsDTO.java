package com.taotao.cloud.promotion.api.dto;


import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import lombok.experimental.SuperBuilder;

/**
 * 砍价活动商品DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityGoodsDTO extends KanjiaActivityGoodsBaseDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1969340823809319805L;

	//@Schema(description =  "商品规格详细信息")
	//private GoodsSku goodsSku;

	private Long id;

}
