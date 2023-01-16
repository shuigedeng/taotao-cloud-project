package com.taotao.cloud.member.biz.controller.business.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import com.taotao.cloud.member.api.model.vo.MemberPointsHistoryPageVO;
import com.taotao.cloud.member.api.model.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.model.entity.MemberPointsHistory;
import com.taotao.cloud.member.biz.service.business.IMemberPointsHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,会员积分历史API
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-会员积分历史API", description = "买家端-会员积分历史API")
@RequestMapping("/member/buyer/member/points/history")
public class MemberPointsHistoryController {

	private final IMemberPointsHistoryService memberPointsHistoryService;

	@Operation(summary = "分页获取当前会员积分历史", description = "分页获取当前会员积分历史")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/page")
	public Result<PageResult<MemberPointsHistoryPageVO>> getByPage(PageQuery page) {
		IPage<MemberPointsHistory> memberPointsHistoryPage = memberPointsHistoryService.getByPage(page);
		return Result.success(PageResult.convertMybatisPage(memberPointsHistoryPage, MemberPointsHistoryPageVO.class));
	}

	@Operation(summary = "获取当前会员积分", description = "获取当前会员积分")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/current/points")
	public Result<MemberPointsHistoryVO> getMemberPointsHistoryVO() {
		return Result.success(
			memberPointsHistoryService.getMemberPointsHistoryVO(SecurityUtils.getUserId()));
	}


}
