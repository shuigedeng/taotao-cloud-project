package com.taotao.cloud.member.biz.controller;

import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.member.api.dto.member.MemberDTO;
import com.taotao.cloud.member.api.query.member.MemberQuery;
import com.taotao.cloud.member.api.vo.MemberVO;
import com.taotao.cloud.member.biz.entity.Member;
import com.taotao.cloud.member.biz.mapper.MemberMapper;
import com.taotao.cloud.member.biz.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 会员管理API
 *
 * @author dengtao
 * @date 2020-10-16 16:23:49
 * @since 1.0
 */
@Validated
@RestController
@RequestMapping("/member")
@Api(value = "会员管理API", tags = {"会员管理API"})
@AllArgsConstructor
public class MemberController {

	private final IMemberService memberService;

	@ApiOperation("根据id查询会员信息")
	@RequestOperateLog(description = "根据id查询会员信息")
	@PreAuthorize("hasAuthority('member:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<MemberVO> findMemberById(@PathVariable(value = "id") Long id) {
		Member member = memberService.findMemberById(id);
		MemberVO vo = MemberMapper.INSTANCE.memberToMemberVO(member);
		return Result.succeed(vo);
	}

	@ApiOperation("查询会员是否已(注册)存在")
	@RequestOperateLog(description = "查询会员是否已(注册)存在")
	@GetMapping("/exist")
	public Result<Boolean> existMember(@Validated @NotNull(message = "查询条件不能为空") MemberQuery memberQuery) {
		Boolean result = memberService.existMember(memberQuery);
		return Result.succeed(result);
	}

	@ApiOperation("注册新会员用户")
	@RequestOperateLog(description = "注册新会员用户")
	@PostMapping
	public Result<Boolean> registerUser(@Validated @RequestBody MemberDTO memberDTO) {
		Member result = memberService.registerUser(memberDTO);
		return Result.succeed(Objects.nonNull(result));
	}

	// **********************内部微服务接口*****************************

	@ApiIgnore
	@ApiOperation("查询会员用户")
	@RequestOperateLog(description = "查询会员用户")
	@GetMapping("/info/security")
	public Result<Member> findMember(@Validated @NotBlank(message = "查询条件不能为空")
									 @RequestParam(value = "nicknameOrUserNameOrPhoneOrEmail") String nicknameOrUserNameOrPhoneOrEmail) {
		Member result = memberService.findMember(nicknameOrUserNameOrPhoneOrEmail);
		return Result.succeed(result);
	}
}
