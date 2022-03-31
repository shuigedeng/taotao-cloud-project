package com.taotao.cloud.promotion.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import lombok.NoArgsConstructor;

/**
 * 优惠券活动DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//public class CouponActivityDTO extends CouponActivity {
public class CouponActivityDTO  {

    //@Schema(description =  "优惠券列表")
    //private List<CouponActivityItem> couponActivityItems;

    @Schema(description =  "会员列表")
    private List<MemberDTO> memberDTOS;
}
