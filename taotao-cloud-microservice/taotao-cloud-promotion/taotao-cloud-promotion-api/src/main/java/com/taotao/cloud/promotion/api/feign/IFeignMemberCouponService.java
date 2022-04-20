package com.taotao.cloud.promotion.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import java.util.List;

import com.taotao.cloud.promotion.api.feign.fallback.FeignMemberCouponServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignMemberService", value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = FeignMemberCouponServiceFallback.class)
public interface IFeignMemberCouponService {


	void used(List<String> ids);

}

