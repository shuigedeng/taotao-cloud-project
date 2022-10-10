package com.taotao.cloud.member.biz.controller.feign;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.model.vo.MemberVO;
import com.taotao.cloud.member.biz.model.convert.MemberConvert;
import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.cloud.member.biz.service.business.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 店铺端,管理员API
 *
 * @since 2020/11/16 10:57
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-管理员API", description = "店铺端-管理员API")
@RequestMapping("/member/seller/store/user")
public class FeignUserController {

	private final MemberService memberService;

	@Operation(summary = "获取当前登录用户API", description = "获取当前登录用户API")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/info")
	public Result<MemberVO> getUserInfo() {
		Member member = memberService.findByUsername(SecurityUtils.getUsername());
		member.setPassword(null);
		return Result.success(MemberConvert.INSTANCE.convert(member));
	}


}
