package com.taotao.cloud.member.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.dto.ManagerMemberEditDTO;
import com.taotao.cloud.member.api.dto.MemberAddDTO;
import com.taotao.cloud.member.api.query.MemberSearchPageQuery;
import com.taotao.cloud.member.api.vo.MemberSearchVO;
import com.taotao.cloud.member.api.vo.MemberVO;
import com.taotao.cloud.member.biz.entity.Member;
import com.taotao.cloud.member.biz.mapstruct.IMemberMapStruct;
import com.taotao.cloud.member.biz.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理端,会员API
 *
 * @since 2020-02-25 14:10:16
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/member/manager/member")
@Tag(name = "管理端-会员管理API", description = "管理端-会员管理API")
public class MemberController {

	private final MemberService memberService;

	@Operation(summary = "会员分页列表", description = "会员分页列表")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<PageModel<MemberVO>> getByPage(MemberSearchPageQuery memberSearchPageQuery) {
		IPage<Member> memberPage = memberService.getMemberPage(memberSearchPageQuery);
		return Result.success(PageModel.convertMybatisPage(memberPage, MemberVO.class));
	}

	@Operation(summary = "通过ID获取会员信息", description = "通过ID获取会员信息")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{id}")
	public Result<MemberVO> get(@PathVariable Long id) {
		Member member = memberService.getById(id);
		return Result.success(IMemberMapStruct.INSTANCE.memberToMemberVO(member));
	}

	@Operation(summary = "添加会员", description = "添加会员")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public Result<Boolean> save(@Valid MemberAddDTO member) {
		return Result.success(memberService.addMember(member));
	}

	@Operation(summary = "修改会员基本信息", description = "修改会员基本信息")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping
	public Result<Boolean> update(@Valid ManagerMemberEditDTO managerMemberEditDTO) {
		return Result.success(memberService.updateMember(managerMemberEditDTO));
	}

	@Operation(summary = "修改会员状态,开启关闭会员", description = "修改会员状态,开启关闭会员")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping("/status")
	public Result<Boolean> updateMemberStatus(@RequestParam List<Long> memberIds,
											  @RequestParam Boolean disabled) {
		return Result.success(memberService.updateMemberStatus(memberIds, disabled));
	}

	@Operation(summary = "根据条件查询会员总数", description = "根据条件查询会员总数")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping("/num")
	public Result<Long> getByPage(MemberSearchVO memberSearchVO) {
		return Result.success(memberService.getMemberNum(memberSearchVO));
	}

}
