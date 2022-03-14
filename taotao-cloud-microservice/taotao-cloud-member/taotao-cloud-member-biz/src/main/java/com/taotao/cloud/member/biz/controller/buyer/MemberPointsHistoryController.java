package com.taotao.cloud.member.biz.controller.buyer;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.vo.MemberPointsHistoryPageVO;
import com.taotao.cloud.member.api.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.service.MemberPointsHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,会员积分历史接口
 */
@Validated
@RestController
@RequestMapping("/member/buyer/member-points-history")
@Tag(name = "买家端-会员积分历史API", description = "买家端-会员积分历史API")
public class MemberPointsHistoryController {

	@Autowired
	private MemberPointsHistoryService memberPointsHistoryService;

	@Operation(summary = "分页获取当前会员积分历史", description = "分页获取当前会员积分历史", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取当前会员积分历史")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/page")
	public Result<PageModel<MemberPointsHistoryPageVO>> getByPage(PageParam page) {
		PageModel<MemberPointsHistoryPageVO> result = memberPointsHistoryService.getByPage(page);
		return Result.success(result);
	}

	@Operation(summary = "获取当前会员积分", description = "获取当前会员积分", method = CommonConstant.GET)
	@RequestLogger(description = "获取当前会员积分")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/current/points")
	public Result<MemberPointsHistoryVO> getMemberPointsHistoryVO() {
		return Result.success(
			memberPointsHistoryService.getMemberPointsHistoryVO(SecurityUtil.getUserId()));
	}


}
