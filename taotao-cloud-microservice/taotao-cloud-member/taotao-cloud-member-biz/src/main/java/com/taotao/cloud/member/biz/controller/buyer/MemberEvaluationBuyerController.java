package com.taotao.cloud.member.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.dto.EvaluationQueryParams;
import com.taotao.cloud.member.api.dto.MemberEvaluationDTO;
import com.taotao.cloud.member.api.vo.EvaluationNumberVO;
import com.taotao.cloud.member.api.vo.MemberEvaluationVO;
import com.taotao.cloud.member.biz.entity.MemberEvaluation;
import com.taotao.cloud.member.biz.service.MemberEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.apache.maven.model.building.Result;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端-会员商品评价接口
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:57:55
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/member/buyer/member-evaluation")
@Tag(name = "买家端-会员商品评价API", description = "买家端-会员商品评价API")
public class MemberEvaluationBuyerController {

	/**
	 * 会员商品评价
	 */
	private final MemberEvaluationService memberEvaluationService;

	@Operation(summary = "添加会员评价", description = "添加会员评价", method = CommonConstant.POST)
	@RequestLogger("添加会员评价")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public Result<Boolean> save(@Valid @RequestBody MemberEvaluationDTO memberEvaluationDTO) {
		return Result.success(memberEvaluationService.addMemberEvaluation(memberEvaluationDTO));
	}

	@Operation(summary = "查看会员评价详情", description = "查看会员评价详情", method = CommonConstant.GET)
	@RequestLogger("查看会员评价详情")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{id}")
	public Result<MemberEvaluationVO> queryById(
		@Parameter(description = "评价ID", required = true) @NotBlank(message = "评价ID不能为空") @PathVariable("id") Long id) {
		return Result.success(memberEvaluationService.queryById(id));
	}

	@Operation(summary = "查看当前会员评价列表", description = "查看当前会员评价列表", method = CommonConstant.GET)
	@RequestLogger("查看当前会员评价列表")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<PageModel<MemberEvaluationVO>> queryMineEvaluation(@Validated EvaluationQueryParams evaluationQueryParams) {
		//设置当前登录会员
		evaluationQueryParams.setMemberId(SecurityUtil.getUserId());
		return Result.success(memberEvaluationService.managerQuery(evaluationQueryParams));
	}

	@Operation(summary = "查看某一个商品的评价列表", description = "查看某一个商品的评价列表", method = CommonConstant.GET)
	@RequestLogger("查看某一个商品的评价列表")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/goods-evaluation/{goodsId}")
	public Result<IPage<MemberEvaluationVO>> queryGoodsEvaluation(EvaluationQueryParams evaluationQueryParams,
		@Parameter(description = "商品ID", required = true) @NotBlank(message = "商品ID不能为空") @PathVariable("goodsId") String goodsId) {
		//设置查询查询商品
		evaluationQueryParams.setGoodsId(goodsId);
		evaluationQueryParams.setStatus(SwitchEnum.OPEN.name());
		return Result.success(memberEvaluationService.managerQuery(evaluationQueryParams));
	}

	@Operation(summary = "查看某一个商品的评价数量", description = "查看某一个商品的评价数量", method = CommonConstant.GET)
	@RequestLogger("查看某一个商品的评价数量")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/goods-evaluation/number/{goodsId}")
	public Result<EvaluationNumberVO> queryEvaluationNumber(
		@Parameter(description = "商品ID", required = true) @NotBlank(message = "商品ID不能为空")  @PathVariable("goodsId") String goodsId) {
		return Result.success(memberEvaluationService.getEvaluationNumber(goodsId));
	}
}
