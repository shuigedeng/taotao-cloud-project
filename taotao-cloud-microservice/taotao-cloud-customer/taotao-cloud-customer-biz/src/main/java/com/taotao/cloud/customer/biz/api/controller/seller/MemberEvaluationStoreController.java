package com.taotao.cloud.customer.biz.api.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.feign.IFeignMemberEvaluationService;
import com.taotao.cloud.member.api.query.EvaluationPageQuery;
import com.taotao.cloud.member.api.vo.MemberEvaluationListVO;
import com.taotao.cloud.member.api.vo.MemberEvaluationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,商品评价管理接口
 */
@Validated
@RestController
@Tag(name = "店铺端-商品评价管理接口", description = "店铺端-商品评价管理接口")
@RequestMapping("/store/memberEvaluation")
public class MemberEvaluationStoreController {

	@Autowired
	private IFeignMemberEvaluationService memberEvaluationService;

	@Operation(summary = "分页获取会员评论列表", description = "分页获取会员评论列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<PageResult<MemberEvaluationListVO>> getByPage(EvaluationPageQuery evaluationPageQuery) {
		evaluationPageQuery.setStoreId(SecurityUtils.getCurrentUser().getStoreId());
		IPage<MemberEvaluationListVO> memberEvaluationListVOIPage = memberEvaluationService.queryPage(evaluationPageQuery);
		return Result.success(PageResult.convertMybatisPage(memberEvaluationListVOIPage, MemberEvaluationListVO.class));
	}

	@Operation(summary = "通过id获取", description = "通过id获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/get/{id}")
	public Result<MemberEvaluationVO> get(@PathVariable Long id) {
		return Result.success(OperationalJudgment.judgment(memberEvaluationService.queryById(id)));
	}

	@Operation(summary = "回复评价", description = "回复评价")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/reply/{id}")
	public Result<MemberEvaluationVO> reply(@Parameter(description = "评价ID") @PathVariable Long id,
											@Parameter(description = "回复内容") @RequestParam String reply,
											@Parameter(description = "回复图片") @RequestParam String replyImage) {
		MemberEvaluationVO memberEvaluationVO = OperationalJudgment.judgment(memberEvaluationService.queryById(id));
		memberEvaluationService.reply(id, reply, replyImage);
		return Result.success(memberEvaluationVO);
	}
}
