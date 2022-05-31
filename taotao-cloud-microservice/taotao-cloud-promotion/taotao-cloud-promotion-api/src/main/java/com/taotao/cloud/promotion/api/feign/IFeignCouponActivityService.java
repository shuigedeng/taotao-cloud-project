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

	///**
	// * 根据id查询提现申请信息
	// *
	// * @param id id
	// * @return com.taotao.cloud.core.model.Result<com.taotao.cloud.aftersale.api.vo.WithdrawVO>
	// * @author shuigedeng
	// * @since 2020/11/20 上午9:50
	// * @version 2022.03
	// */
	//@GetMapping(value = "/withdraw/info/id/{id:[0-9]*}")
	//Result<CouponVO> getMemberSecurityUser(@PathVariable(value = "id") Long id);
}
