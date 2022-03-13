package com.taotao.cloud.order.biz.controller.manager;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleReason;
import com.taotao.cloud.order.biz.service.aftersale.AfterSaleReasonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,售后原因接口
 */
@Validated
@RestController
@Tag(name = "平台管理端-售后原因管理API", description = "平台管理端-售后原因管理API")
@RequestMapping("/order/manager/afterSaleReason")
public class AfterSaleReasonController {

	/**
	 * 售后原因
	 */
	@Autowired
	private AfterSaleReasonService afterSaleReasonService;

	@Operation(summary = "查看售后原因", description = "查看售后原因", method = CommonConstant.GET)
	@RequestLogger(description = "查看售后原因")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<AfterSaleReason> get(@PathVariable String id) {
		return Result.success(afterSaleReasonService.getById(id));
	}

	@Operation(summary = "分页获取售后原因", description = "分页获取售后原因", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取售后原因")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<IPage<AfterSaleReason>> getByPage(PageVO page,
		@RequestParam String serviceType) {
		return Result.success(afterSaleReasonService.page(PageUtil.initPage(page),
			new QueryWrapper<AfterSaleReason>().eq("service_Type", serviceType)));
	}

	@Operation(summary = "添加售后原因", description = "添加售后原因", method = CommonConstant.POST)
	@RequestLogger(description = "添加售后原因")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<AfterSaleReason> save(@Valid AfterSaleReason afterSaleReason) {
		afterSaleReasonService.save(afterSaleReason);
		return Result.success(afterSaleReason);
	}

	@Operation(summary = "修改售后原因", description = "修改售后原因", method = CommonConstant.PUT)
	@RequestLogger(description = "修改售后原因")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}")
	public Result<AfterSaleReason> update(@Valid AfterSaleReason afterSaleReason,
		@PathVariable("id") String id) {
		afterSaleReason.setId(id);
		return Result.success(afterSaleReasonService.editAfterSaleReason(afterSaleReason));
	}

	@Operation(summary = "删除售后原因", description = "删除售后原因", method = CommonConstant.DELETE)
	@RequestLogger(description = "删除售后原因")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<Object> delAllByIds(@PathVariable String id) {
		afterSaleReasonService.removeById(id);
		return ResultUtil.success();
	}
}
