package com.taotao.cloud.promotion.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.promotion.api.dto.MemberDTO;
import com.taotao.cloud.promotion.api.feign.fallback.FeignCouponServiceFallback;
import com.taotao.cloud.promotion.api.vo.CouponActivityVO;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(value = ServiceName.TAOTAO_CLOUD_AFTERSALE_CENTER, fallbackFactory = FeignCouponServiceFallback.class)
public interface IFeignCouponActivityService {

	void registered(List<CouponActivityVO> couponActivityVOS, MemberDTO memberDTO);

}
