package com.taotao.cloud.member.biz.controller.business.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.model.query.EvaluationPageQuery;
import com.taotao.cloud.member.api.model.vo.MemberEvaluationListVO;
import com.taotao.cloud.member.api.model.vo.MemberEvaluationVO;
import com.taotao.cloud.member.biz.model.entity.MemberEvaluation;
import com.taotao.cloud.member.biz.mapstruct.IMemberEvaluationMapStruct;
import com.taotao.cloud.member.biz.service.MemberEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,商品评价管理API
 *
 * @since 2020-02-25 14:10:16
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-商品评价API", description = "店铺端-商品评价API")
@RequestMapping("/member/seller/member/evaluation")
public class MemberEvaluationController {

	private final MemberEvaluationService memberEvaluationService;

	@Operation(summary = "分页获取会员评论列表", description = "分页获取会员评论列表")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<PageModel<MemberEvaluationListVO>> getByPage(
		EvaluationPageQuery evaluationPageQuery) {
		evaluationPageQuery.setStoreId(SecurityUtils.getCurrentUser().getStoreId());
		IPage<MemberEvaluation> memberEvaluationPage = memberEvaluationService.queryPage(evaluationPageQuery);
		return Result.success(PageModel.convertMybatisPage(memberEvaluationPage, MemberEvaluationListVO.class));
	}

	@Operation(summary = "通过id获取", description = "通过id获取")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{id}")
	public Result<MemberEvaluationVO> get(@PathVariable Long id) {
		MemberEvaluation memberEvaluation = OperationalJudgment.judgment(memberEvaluationService.queryById(id));
		return Result.success(IMemberEvaluationMapStruct.INSTANCE.memberEvaluationToMemberEvaluationVO(memberEvaluation));
	}

	@Operation(summary = "回复评价", description = "回复评价")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping(value = "/reply/{id}")
	public Result<Boolean> reply(@PathVariable Long id, @RequestParam String reply,
											@RequestParam String replyImage) {
		OperationalJudgment.judgment(memberEvaluationService.queryById(id));
		return Result.success(memberEvaluationService.reply(id, reply, replyImage));
	}
}
