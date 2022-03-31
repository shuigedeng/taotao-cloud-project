package com.taotao.cloud.member.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.dto.EvaluationQueryParams;
import com.taotao.cloud.member.api.vo.MemberEvaluationListVO;
import com.taotao.cloud.member.api.vo.MemberEvaluationVO;
import com.taotao.cloud.member.biz.service.MemberEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
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
 *
 * @since 2020-02-25 14:10:16
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/member/manager/member-evaluation")
@Tag(name = "店铺端-商品评价管理接口", description = "店铺端-商品评价管理接口")
public class MemberEvaluationController {

	private final MemberEvaluationService memberEvaluationService;

	@Operation(summary = "分页获取会员评论列表", description = "分页获取会员评论列表", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取会员评论列表")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<IPage<MemberEvaluationListVO>> getByPage(
		EvaluationQueryParams evaluationQueryParams) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		evaluationQueryParams.setStoreId(storeId);
		return Result.success(memberEvaluationService.queryPage(evaluationQueryParams));
	}

	@Operation(summary = "通过id获取", description = "通过id获取", method = CommonConstant.GET)
	@RequestLogger(description = "通过id获取")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{id}")
	public Result<MemberEvaluationVO> get(@PathVariable String id) {
		return Result.success(OperationalJudgment.judgment(memberEvaluationService.queryById(id)));
	}

	@Operation(summary = "回复评价", description = "回复评价", method = CommonConstant.PUT)
	@RequestLogger(description = "回复评价")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping(value = "/reply/{id}")
	public Result<MemberEvaluationVO> reply(@PathVariable String id, @RequestParam String reply,
		@RequestParam String replyImage) {
		OperationalJudgment.judgment(memberEvaluationService.queryById(id));
		memberEvaluationService.reply(id, reply, replyImage);
		return Result.success();
	}
}
