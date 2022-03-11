package com.taotao.cloud.member.biz.controller.seller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.biz.entity.Member;
import com.taotao.cloud.member.biz.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 店铺端,管理员接口
 *
 * @since 2020/11/16 10:57
 */
@Validated
@RestController
@RequestMapping("/member/seller/store-user")
@Tag(name = "店铺端-管理员接口", description = "店铺端-管理员接口")
public class StoreUserController {

	@Autowired
	private MemberService memberService;

	@Operation(summary = "获取当前登录用户接口", description = "获取当前登录用户接口", method = CommonConstant.GET)
	@RequestLogger(description = "获取当前登录用户接口")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/info")
	public Result<Member> getUserInfo() {
		AuthUser tokenUser = UserContext.getCurrentUser();
		if (tokenUser != null) {
			Member member = memberService.findByUsername(tokenUser.getUsername());
			member.setPassword(null);
			return Result.success(member);
		}
		throw new ServiceException(ResultCode.USER_NOT_LOGIN);
	}


}
