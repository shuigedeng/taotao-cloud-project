package com.taotao.cloud.distribution.biz.api.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.distribution.api.web.vo.DistributionCashSearchVO;
import com.taotao.cloud.distribution.biz.model.entity.DistributionCash;
import com.taotao.cloud.distribution.biz.service.DistributionCashService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,分销佣金管理接口
 */
@Validated
@RestController
@Tag(name = "管理端-分销佣金管理接口", description = "管理端-分销佣金管理接口")
@RequestMapping("/manager/distribution/cash")
public class DistributionCashManagerController {

	@Autowired
	private DistributionCashService distributorCashService;

	@Operation(summary = "通过id获取分销佣金详情", description = "通过id获取分销佣金详情")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/get/{id}")
	public Result<DistributionCash> get(@PathVariable String id) {
		return Result.success(distributorCashService.getById(id));
	}

	@Operation(summary = "分页获取", description = "分页获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/getByPage")
	public Result<IPage<DistributionCash>> getByPage(
			DistributionCashSearchVO distributionCashSearchVO) {
		return Result.success(distributorCashService.getDistributionCash(distributionCashSearchVO));
	}

	@Operation(summary = "审核", description = "审核")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
	@PreventDuplicateSubmissions
	@PostMapping(value = "/audit/{id}")
	public Result<DistributionCash> audit(
			@Parameter(description = "分销佣金ID") @PathVariable String id,
			@Parameter(description = "处理结果") @NotNull String result) {
		return Result.success(distributorCashService.audit(id, result));
	}
}

