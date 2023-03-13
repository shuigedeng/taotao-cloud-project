package com.taotao.cloud.member.biz.controller.feign;

import com.taotao.cloud.member.api.feign.IFeignMemberAddressApi;
import com.taotao.cloud.member.api.model.vo.MemberAddressVO;
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
@Tag(name = "内部调用端-会员地址API", description = "内部调用端-会员地址API")
public class FeignMemberAddressController implements IFeignMemberAddressApi {

	private final IMemberService memberService;

	@Override
	public MemberAddressVO getById(String shippingAddressId) {
		return null;
	}
}
