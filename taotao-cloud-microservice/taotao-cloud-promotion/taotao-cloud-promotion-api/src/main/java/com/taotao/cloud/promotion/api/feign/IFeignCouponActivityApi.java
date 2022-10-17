package com.taotao.cloud.promotion.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.promotion.api.feign.fallback.FeignCouponApiFallback;
import com.taotao.cloud.promotion.api.model.dto.MemberDTO;
import com.taotao.cloud.promotion.api.model.vo.CouponActivityVO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = ServiceName.TAOTAO_CLOUD_AFTERSALE_CENTER, fallbackFactory = FeignCouponApiFallback.class)
public interface IFeignCouponActivityApi {

	void registered(List<CouponActivityVO> couponActivityVOS, MemberDTO memberDTO);

}