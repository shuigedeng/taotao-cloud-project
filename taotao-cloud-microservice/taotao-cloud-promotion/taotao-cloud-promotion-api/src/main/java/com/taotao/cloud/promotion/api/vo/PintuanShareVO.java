package com.taotao.cloud.promotion.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 * 拼图会员分享对象
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PintuanShareVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 7814832369110695758L;

    private PromotionGoodsVO promotionGoods;

    private List<PintuanMemberVO> pintuanMembers;

}
