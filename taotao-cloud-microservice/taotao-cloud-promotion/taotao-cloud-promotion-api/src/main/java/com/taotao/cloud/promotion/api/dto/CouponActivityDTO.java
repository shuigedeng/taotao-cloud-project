package com.taotao.cloud.promotion.api.dto;

import cn.lili.modules.promotion.entity.dos.CouponActivity;
import cn.lili.modules.promotion.entity.dos.CouponActivityItem;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 优惠券活动DTO
 *
 * @author Bulbasaur
 * @since 2021/5/21 7:16 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponActivityDTO extends CouponActivity {

    @Schema(description =  "优惠券列表")
    private List<CouponActivityItem> couponActivityItems;

    @Schema(description =  "会员列表")
    private List<MemberDTO> memberDTOS;
}
