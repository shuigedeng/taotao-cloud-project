package com.taotao.cloud.member.biz.controller.buyer;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.dto.MemberDTO;
import com.taotao.cloud.member.api.query.MemberQuery;
import com.taotao.cloud.member.api.vo.MemberVO;
import com.taotao.cloud.member.biz.mapper.MemberMapper;
import com.taotao.cloud.member.biz.service.IMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员管理API
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:49
 */
@Validated
@RestController
@RequestMapping("/member")
@Tag(name = "会员管理API", description = "会员管理API")
public class MemberController {

	private final IMemberService memberService;

	public MemberController(IMemberService memberService) {
		this.memberService = memberService;
	}

	@RequestLogger(description = "根据id查询会员信息")
	@PreAuthorize("hasAuthority('member:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<MemberVO> findMemberById(@PathVariable(value = "id") Long id) {
		MemberBack member = memberService.findMemberById(id);
		MemberVO vo = MemberMapper.INSTANCE.memberToMemberVO(member);
		return Result.success(vo);
	}

	@RequestLogger(description = "查询会员是否已(注册)存在")
	@GetMapping("/exist")
	public Result<Boolean> existMember(
		@Validated @NotNull(message = "查询条件不能为空") MemberQuery memberQuery) {
		Boolean result = memberService.existMember(memberQuery);
		return Result.success(result);
	}

	@RequestLogger(description = "注册新会员用户")
	@PostMapping
	public Result<Boolean> registerUser(@Validated @RequestBody MemberDTO memberDTO) {
		MemberBack result = memberService.registerUser(memberDTO);
		return Result.success(Objects.nonNull(result));
	}

	// **********************内部微服务接口*****************************

	@RequestLogger(description = "查询会员用户")
	@GetMapping("/info/security")
	public Result<MemberBack> findMember(@Validated @NotBlank(message = "查询条件不能为空")
	@RequestParam(value = "nicknameOrUserNameOrPhoneOrEmail") String nicknameOrUserNameOrPhoneOrEmail) {
		MemberBack result = memberService.findMember(nicknameOrUserNameOrPhoneOrEmail);
		return Result.success(result);
	}
}
