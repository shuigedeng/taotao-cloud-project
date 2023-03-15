package com.taotao.cloud.promotion.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.promotion.api.feign.fallback.FeignCouponApiFallback;
import com.taotao.cloud.promotion.api.model.dto.MemberDTO;
import com.taotao.cloud.promotion.api.model.vo.CouponActivityVO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = ServiceName.TAOTAO_CLOUD_PROMOTION, fallbackFactory = FeignCouponApiFallback.class)
public interface IFeignCouponActivityApi {

	@GetMapping(value = "/registered")
	void registered(List<CouponActivityVO> couponActivityVOS, MemberDTO memberDTO);

}
