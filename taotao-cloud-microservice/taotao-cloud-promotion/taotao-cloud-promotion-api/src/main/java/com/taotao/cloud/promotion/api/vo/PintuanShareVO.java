package com.taotao.cloud.promotion.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import lombok.NoArgsConstructor;

/**
 * 拼图会员分享对象
 *
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PintuanShareVO {

    private PromotionGoodsVO promotionGoods;

    private List<PintuanMemberVO> pintuanMemberVOS;

}
