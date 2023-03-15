package com.taotao.cloud.promotion.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.promotion.api.feign.fallback.FeignMemberCouponApiFallback;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_PROMOTION, fallbackFactory = FeignMemberCouponApiFallback.class)
public interface IFeignMemberCouponApi {

	@GetMapping(value = "/used")
	void used(List<String> ids);

	@GetMapping(value = "/receiveCoupon")
	void receiveCoupon(String couponId, Long memberId, String memberName);
}

