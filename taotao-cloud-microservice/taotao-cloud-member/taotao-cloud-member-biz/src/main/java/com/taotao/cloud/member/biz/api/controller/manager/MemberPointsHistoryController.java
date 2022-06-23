package com.taotao.cloud.member.biz.api.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.web.query.MemberPointHistoryPageQuery;
import com.taotao.cloud.member.api.web.vo.MemberPointsHistoryPageVO;
import com.taotao.cloud.member.api.web.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.model.entity.MemberPointsHistory;
import com.taotao.cloud.member.biz.service.MemberPointsHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,会员积分历史API
 *
 * @since 2020-02-25 14:10:16
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/member/manager/member/points/history")
@Tag(name = "管理端-会员积分历史管理API", description = "管理端-会员积分历史管理API")
public class MemberPointsHistoryController {

	private final MemberPointsHistoryService memberPointsHistoryService;

	@Operation(summary = "分页获取", description = "分页获取")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/page")
	public Result<PageModel<MemberPointsHistoryPageVO>> getByPage(MemberPointHistoryPageQuery pageQuery) {
		IPage<MemberPointsHistory> memberPointsHistoryIPage = memberPointsHistoryService.memberPointsHistoryList(pageQuery.getPageParm(), pageQuery.getMemberId(), pageQuery.getMemberName());
		return Result.success(PageModel.convertMybatisPage(memberPointsHistoryIPage, MemberPointsHistoryPageVO.class));
	}

	@Operation(summary = "获取会员积分", description = "获取会员积分")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "")
	public Result<MemberPointsHistoryVO> getMemberPointsHistoryVO(Long memberId) {
		return Result.success(memberPointsHistoryService.getMemberPointsHistoryVO(memberId));
	}

}
