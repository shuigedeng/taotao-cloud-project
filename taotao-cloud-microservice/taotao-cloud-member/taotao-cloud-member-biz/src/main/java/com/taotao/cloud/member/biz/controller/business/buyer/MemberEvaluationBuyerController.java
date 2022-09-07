package com.taotao.cloud.member.biz.controller.business.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.SwitchEnum;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.model.dto.MemberEvaluationDTO;
import com.taotao.cloud.member.api.model.query.EvaluationPageQuery;
import com.taotao.cloud.member.api.model.vo.EvaluationNumberVO;
import com.taotao.cloud.member.api.model.vo.MemberEvaluationVO;
import com.taotao.cloud.member.biz.model.entity.MemberEvaluation;
import com.taotao.cloud.member.biz.mapstruct.IMemberEvaluationMapStruct;
import com.taotao.cloud.member.biz.service.MemberEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 买家端-会员商品评价API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:57:55
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-会员商品评价API", description = "买家端-会员商品评价API")
@RequestMapping("/member/buyer/member/evaluation")
public class MemberEvaluationBuyerController {

	/**
	 * 会员商品评价
	 */
	private final MemberEvaluationService memberEvaluationService;

	@Operation(summary = "添加会员评价", description = "添加会员评价")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public Result<Boolean> save(@Valid @RequestBody MemberEvaluationDTO memberEvaluationDTO) {
		return Result.success(memberEvaluationService.addMemberEvaluation(memberEvaluationDTO));
	}

	@Operation(summary = "查看会员评价详情", description = "查看会员评价详情")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{id}")
	public Result<MemberEvaluationVO> queryById(
		@Parameter(description = "评价ID", required = true) @NotBlank(message = "评价ID不能为空") @PathVariable("id") Long id) {
		MemberEvaluation memberEvaluation = memberEvaluationService.queryById(id);
		return Result.success(IMemberEvaluationMapStruct.INSTANCE.memberEvaluationToMemberEvaluationVO(memberEvaluation));
	}

	@Operation(summary = "查看当前会员评价列表", description = "查看当前会员评价列表")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<PageResult<MemberEvaluationVO>> queryMineEvaluation(@Validated EvaluationPageQuery evaluationPageQuery) {
		//设置当前登录会员
		evaluationPageQuery.setMemberId(SecurityUtils.getUserId());
		IPage<MemberEvaluation> memberEvaluationPage = memberEvaluationService.managerQuery(evaluationPageQuery);
		return Result.success(PageResult.convertMybatisPage(memberEvaluationPage, MemberEvaluationVO.class));
	}

	@Operation(summary = "查看某一个商品的评价列表", description = "查看某一个商品的评价列表")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/goods-evaluation/{goodsId}")
	public Result<PageResult<MemberEvaluationVO>> queryGoodsEvaluation(EvaluationPageQuery evaluationPageQuery,
                                                                       @Parameter(description = "商品ID", required = true) @NotBlank(message = "商品ID不能为空") @PathVariable("goodsId") Long goodsId) {
		//设置查询查询商品
		evaluationPageQuery.setGoodsId(goodsId);
		evaluationPageQuery.setStatus(SwitchEnum.OPEN.name());
		IPage<MemberEvaluation> memberEvaluationPage = memberEvaluationService.managerQuery(evaluationPageQuery);
		return Result.success(PageResult.convertMybatisPage(memberEvaluationPage, MemberEvaluationVO.class));
	}

	@Operation(summary = "查看某一个商品的评价数量", description = "查看某一个商品的评价数量")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/goods-evaluation/number/{goodsId}")
	public Result<EvaluationNumberVO> queryEvaluationNumber(
		@Parameter(description = "商品ID", required = true) @NotBlank(message = "商品ID不能为空") @PathVariable("goodsId") Long goodsId) {
		return Result.success(memberEvaluationService.getEvaluationNumber(goodsId));
	}
}
