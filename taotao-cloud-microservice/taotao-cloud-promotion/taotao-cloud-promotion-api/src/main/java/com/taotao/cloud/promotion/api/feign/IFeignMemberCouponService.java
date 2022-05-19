package com.taotao.cloud.promotion.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.promotion.api.feign.fallback.FeignMemberCouponServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = FeignMemberCouponServiceFallback.class)
public interface IFeignMemberCouponService {


	void used(List<String> ids);

    void receiveCoupon(String couponId, Long memberId, String memberName);
}

