package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MemberCouponVO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//public class MemberCouponVO extends MemberCoupon {
public class MemberCouponVO  {

    @Schema(description =  "无法使用原因")
    private String reason;
    
    //public MemberCouponVO(MemberCoupon memberCoupon, String reason) {
    //    BeanUtil.copyProperties(memberCoupon, this);
    //    this.reason = reason;
    //}

    public MemberCouponVO(){

    }
}
