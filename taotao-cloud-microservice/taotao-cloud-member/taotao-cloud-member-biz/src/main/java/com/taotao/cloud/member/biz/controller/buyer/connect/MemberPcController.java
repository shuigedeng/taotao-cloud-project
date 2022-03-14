package com.taotao.cloud.member.biz.controller.buyer.connect;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.dto.MemberEditDTO;
import com.taotao.cloud.member.biz.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,会员接口
 */
@Validated
@RestController
@Tag(name = "买家端-会员登录API", description = "买家端-会员登录API")
@RequestMapping("/member/buyer/passport/connect/pc")
public class MemberPcController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private SmsUtil smsUtil;
	@Autowired
	private VerificationService verificationService;

	@Operation(summary = "登录接口", description = "登录接口", method = CommonConstant.POST)
	@RequestLogger(description = "登录接口")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/login")
	public Result<Object> userLogin(
		@NotNull(message = "用户名不能为空") @RequestParam String username,
		@NotNull(message = "密码不能为空") @RequestParam String password,
		@RequestHeader String uuid) {
		verificationService.check(uuid, VerificationEnums.LOGIN);
		return Result.success(this.memberService.usernameLogin(username, password));
	}

	@Operation(summary = "注销接口", description = "注销接口", method = CommonConstant.POST)
	@RequestLogger(description = "注销接口")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/logout")
	public Result<Object> logout() {
		this.memberService.logout(UserEnums.MEMBER);
		return ResultUtil.success();
	}

	@Operation(summary = "短信登录接口", description = "短信登录接口", method = CommonConstant.POST)
	@RequestLogger(description = "短信登录接口")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/sms/login")
	public Result<Object> smsLogin(@NotNull(message = "手机号为空") @RequestParam String mobile,
		@NotNull(message = "验证码为空") @RequestParam String code,
		@RequestHeader String uuid) {
		if (smsUtil.verifyCode(mobile, VerificationEnums.LOGIN, uuid, code)) {
			return Result.success(memberService.mobilePhoneLogin(mobile));
		} else {
			throw new ServiceException(ResultCode.VERIFICATION_SMS_CHECKED_ERROR);
		}
	}

	@Operation(summary = "注册用户", description = "注册用户", method = CommonConstant.POST)
	@RequestLogger(description = "注册用户")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/register")
	public Result<Object> register(
		@NotNull(message = "用户名不能为空") @RequestParam String username,
		@NotNull(message = "密码不能为空") @RequestParam String password,
		@NotNull(message = "手机号为空") @RequestParam String mobilePhone,
		@RequestHeader String uuid,
		@NotNull(message = "验证码不能为空") @RequestParam String code) {
		if (smsUtil.verifyCode(mobilePhone, VerificationEnums.REGISTER, uuid, code)) {
			return Result.success(memberService.register(username, password, mobilePhone));
		} else {
			throw new ServiceException(ResultCode.VERIFICATION_SMS_CHECKED_ERROR);
		}
	}

	@Operation(summary = "获取当前登录用户接口", description = "获取当前登录用户接口", method = CommonConstant.GET)
	@RequestLogger(description = "获取当前登录用户接口")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<Member> getUserInfo() {
		return Result.success(memberService.getUserInfo());
	}

	@Operation(summary = "通过短信重置密码", description = "通过短信重置密码", method = CommonConstant.POST)
	@RequestLogger(description = "通过短信重置密码")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	@PostMapping("/reset/mobile")
	public Result<Member> resetByMobile(
		@NotNull(message = "手机号为空") @RequestParam String mobile,
		@NotNull(message = "验证码为空") @RequestParam String code,
		@RequestHeader String uuid) {
		//校验短信验证码是否正确
		if (smsUtil.verifyCode(mobile, VerificationEnums.FIND_USER, uuid, code)) {
			//校验是否通过手机号可获取会员,存在则将会员信息存入缓存，有效时间3分钟
			memberService.findByMobile(uuid, mobile);
			return ResultUtil.success();
		} else {
			throw new ServiceException(ResultCode.VERIFICATION_SMS_CHECKED_ERROR);
		}
	}

	@Operation(summary = "修改密码", description = "修改密码", method = CommonConstant.PUT)
	@RequestLogger(description = "修改密码")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping("/password")
	public Result<Object> resetByMobile(
		@NotNull(message = "密码为空") @RequestParam String password, @RequestHeader String uuid) {
		return Result.success(memberService.resetByMobile(uuid, password));
	}

	@Operation(summary = "修改用户自己资料", description = "修改用户自己资料", method = CommonConstant.PUT)
	@RequestLogger(description = "修改用户自己资料")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping("/own")
	public Result<Member> editOwn(MemberEditDTO memberEditDTO) {
		return Result.success(memberService.editOwn(memberEditDTO));
	}

	@Operation(summary = "修改密码", description = "修改密码", method = CommonConstant.PUT)
	@RequestLogger(description = "修改密码")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping("/modifyPass")
	public Result<Member> modifyPass(
		@NotNull(message = "旧密码不能为空") @RequestParam String password,
		@NotNull(message = "新密码不能为空") @RequestParam String newPassword) {
		return Result.success(memberService.modifyPass(password, newPassword));
	}

	@Operation(summary = "刷新token", description = "刷新token", method = CommonConstant.GET)
	@RequestLogger(description = "刷新token")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping("/refresh/{refreshToken}")
	public Result<Object> refreshToken(
		@NotNull(message = "刷新token不能为空") @PathVariable String refreshToken) {
		return Result.success(this.memberService.refreshToken(refreshToken));
	}

}
