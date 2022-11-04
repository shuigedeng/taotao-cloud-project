package com.taotao.cloud.member.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.member.api.feign.fallback.FeignMemberWalletApiFallback;
import com.taotao.cloud.member.api.model.dto.MemberWalletUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = FeignMemberWalletApiFallback.class)
public interface FeignMemberWalletApi {

	@GetMapping(value = "/member/wallet/increase")
	boolean increase(@RequestParam MemberWalletUpdateDTO memberWalletUpdateDTO);

	@GetMapping(value = "/member/recharge/save")
	boolean save(@RequestParam Long id, @RequestParam String username);
}
