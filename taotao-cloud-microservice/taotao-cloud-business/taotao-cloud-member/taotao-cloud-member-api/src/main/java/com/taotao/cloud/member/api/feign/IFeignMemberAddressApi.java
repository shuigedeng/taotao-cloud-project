package com.taotao.cloud.member.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.member.api.feign.fallback.FeignMemberAddressApiFallback;
import com.taotao.cloud.member.api.model.vo.MemberAddressVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = FeignMemberAddressApiFallback.class)
public interface IFeignMemberAddressApi {

	@GetMapping(value = "/member/feign/address/shippingAddressId")
	MemberAddressVO getById(@RequestParam String shippingAddressId);

}
