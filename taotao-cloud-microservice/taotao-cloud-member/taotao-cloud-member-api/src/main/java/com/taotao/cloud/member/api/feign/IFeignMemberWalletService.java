package com.taotao.cloud.member.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.member.api.web.dto.MemberWalletUpdateDTO;
import com.taotao.cloud.member.api.feign.fallback.FeignMemberServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = FeignMemberServiceFallback.class)
public interface IFeignMemberWalletService {
	@GetMapping(value = "/member/wallet/increase")
	void increase(@RequestParam MemberWalletUpdateDTO memberWalletUpdateDTO);

	@GetMapping(value = "/member/recharge/save")
	void save(@RequestParam Long id, @RequestParam String username);
}
