package com.taotao.cloud.member.biz.controller.feign;

import com.taotao.cloud.member.api.feign.IFeignMemberWalletApi;
import com.taotao.cloud.member.api.model.dto.MemberWalletUpdateDTO;
import com.taotao.cloud.member.biz.service.business.IMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;


/**
 * 店铺端,管理员API
 *
 * @since 2020/11/16 10:57
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "内部调用端-会员钱包API", description = "内部调用端-会员钱包API")
public class FeignMemberWalletController implements IFeignMemberWalletApi {

	private final IMemberService memberService;

	@Override
	public boolean increase(MemberWalletUpdateDTO memberWalletUpdateDTO) {
		return false;
	}

	@Override
	public boolean save(Long id, String username) {
		return false;
	}
}
