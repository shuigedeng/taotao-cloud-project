package com.taotao.cloud.promotion.api.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * 拼团视图对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PintuanVO extends PintuanBaseVO {

    @Serial
	private static final long serialVersionUID = 218582640653676201L;

    private List<PromotionGoodsVO> promotionGoodsList;
}
