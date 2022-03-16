package com.taotao.cloud.member.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.entity.MemberPointsHistory;
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
 * 管理端,会员积分历史接口
 *
 * @since 2020-02-25 14:10:16
 */
@Validated
@RestController
@RequestMapping("/member/manager/member-points-history")
@Tag(name = "管理端-会员积分历史API", description = "管理端-会员积分历史API")
public class MemberPointsHistoryController {

	@Autowired
	private MemberPointsHistoryService memberPointsHistoryService;

	@Operation(summary = "分页获取", description = "分页获取", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/page")
	public Result<IPage<MemberPointsHistory>> getByPage(PageVO page, String memberId,
		String memberName) {
		return Result.success(
			memberPointsHistoryService.MemberPointsHistoryList(page, memberId, memberName));
	}

	@Operation(summary = "获取会员积分", description = "获取会员积分", method = CommonConstant.GET)
	@RequestLogger(description = "获取会员积分")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "")
	public Result<MemberPointsHistoryVO> getMemberPointsHistoryVO(String memberId) {
		return Result.success(memberPointsHistoryService.getMemberPointsHistoryVO(memberId));
	}

}
