package com.taotao.cloud.member.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.dto.EvaluationQueryParams;
import com.taotao.cloud.member.api.vo.MemberEvaluationListVO;
import com.taotao.cloud.member.api.vo.MemberEvaluationVO;
import com.taotao.cloud.member.biz.service.MemberEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,会员商品评价接口
 *
 * @since 2020-02-25 14:10:16
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/member/manager/membe-evaluation")
@Tag(name = "管理端-会员商品评价API", description = "管理端-会员商品评价API")
public class MemberEvaluationController {

	private final MemberEvaluationService memberEvaluationService;

	@Operation(summary = "通过id获取评论", description = "通过id获取评论", method = CommonConstant.GET)
	@RequestLogger("通过id获取评论")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{id}")
	public Result<MemberEvaluationVO> get(@PathVariable String id) {
		return Result.success(memberEvaluationService.queryById(id));
	}

	@Operation(summary = "获取评价分页", description = "获取评价分页", method = CommonConstant.GET)
	@RequestLogger("获取评价分页")
	@PreAuthorize("@el.check('admin','timing:list')")
	public Result<IPage<MemberEvaluationListVO>> getByPage(
		EvaluationQueryParams evaluationQueryParams, PageVO page) {
		return Result.success(memberEvaluationService.queryPage(evaluationQueryParams));
	}

	@Operation(summary = "修改评价状态", description = "修改评价状态", method = CommonConstant.PUT)
	@RequestLogger("修改评价状态")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/status/{id}")
	public Result<Boolean> updateStatus(@PathVariable String id,
		@Parameter(description = "显示状态,OPEN 正常 ,CLOSE 关闭", required = true) @NotNull String status) {
		;
		return Result.success(memberEvaluationService.updateStatus(id, status));
	}

	@Operation(summary = "删除评论", description = "删除评论", method = CommonConstant.DELETE)
	@RequestLogger("删除评论")
	@PreAuthorize("@el.check('admin','timing:list')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delete(@PathVariable String id) {
		return Result.success(memberEvaluationService.delete(id));
	}

}
