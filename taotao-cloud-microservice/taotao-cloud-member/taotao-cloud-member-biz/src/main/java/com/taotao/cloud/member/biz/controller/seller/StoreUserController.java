package com.taotao.cloud.member.biz.controller.seller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.biz.entity.Member;
import com.taotao.cloud.member.biz.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StoreUserController {

	private final MemberService memberService;

	@Operation(summary = "获取当前登录用户API", description = "获取当前登录用户API", method = CommonConstant.GET)
	@RequestLogger("获取当前登录用户API")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/info")
	public Result<Member> getUserInfo() {
		Member member = memberService.findByUsername(SecurityUtil.getUsername());
		member.setPassword(null);
		return Result.success(member);
	}


}
