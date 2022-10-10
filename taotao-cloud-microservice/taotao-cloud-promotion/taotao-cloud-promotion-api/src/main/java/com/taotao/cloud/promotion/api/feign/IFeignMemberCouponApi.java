package com.taotao.cloud.promotion.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.promotion.api.feign.fallback.FeignMemberCouponApiFallback;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = FeignMemberCouponApiFallback.class)
public interface IFeignMemberCouponApi {


	void used(List<String> ids);

	void receiveCoupon(String couponId, Long memberId, String memberName);
}

