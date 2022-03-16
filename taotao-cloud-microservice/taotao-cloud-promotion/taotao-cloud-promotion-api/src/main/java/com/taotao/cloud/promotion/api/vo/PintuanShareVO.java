package com.taotao.cloud.promotion.api.vo;

import lombok.Data;

import java.util.List;

/**
 * 拼图会员分享对象
 *
 **/
@Data
public class PintuanShareVO {

    //private PromotionGoods promotionGoods;

    private List<PintuanMemberVO> pintuanMemberVOS;

}
