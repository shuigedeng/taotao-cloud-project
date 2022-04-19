package com.taotao.cloud.order.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.dto.aftersale.AfterSaleReasonDTO;
import com.taotao.cloud.order.api.dto.aftersale.AfterSaleReasonPageQuery;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleReasonVO;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleReason;
import com.taotao.cloud.order.biz.mapstruct.IAfterSaleReasonMapStruct;
import com.taotao.cloud.order.biz.service.aftersale.AfterSaleReasonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端,售后原因API
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-售后原因管理API", description = "平台管理端-售后原因管理API")
@RequestMapping("/order/manager/aftersale/reason")
public class AfterSaleReasonController {

	/**
	 * 售后原因
	 */
	private final AfterSaleReasonService afterSaleReasonService;

	@Operation(summary = "查看售后原因", description = "查看售后原因")
	@RequestLogger("查看售后原因")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<AfterSaleReasonVO> get(@PathVariable String id) {
		AfterSaleReason afterSaleReason = afterSaleReasonService.getById(id);
		return Result.success(IAfterSaleReasonMapStruct.INSTANCE.afterSaleReasonToAfterSaleReasonVO(afterSaleReason));
	}

	@Operation(summary = "分页获取售后原因", description = "分页获取售后原因")
	@RequestLogger("分页获取售后原因")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<PageModel<AfterSaleReasonVO>> getByPage(@Validated AfterSaleReasonPageQuery afterSaleReasonPageQuery) {
		IPage<AfterSaleReason> afterSaleReasonPage = afterSaleReasonService.getByPage(afterSaleReasonPageQuery);
		return Result.success(PageModel.convertMybatisPage(afterSaleReasonPage, AfterSaleReasonVO.class));
	}

	@Operation(summary = "添加售后原因", description = "添加售后原因")
	@RequestLogger("添加售后原因")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(@Validated @RequestBody AfterSaleReasonDTO afterSaleReasonDTO) {
		afterSaleReasonService.save(IAfterSaleReasonMapStruct.INSTANCE.afterSaleReasonDTOToAfterSaleReason(afterSaleReasonDTO));
		return Result.success(true);
	}

	@Operation(summary = "修改售后原因", description = "修改售后原因")
	@RequestLogger("修改售后原因")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}")
	public Result<Boolean> update(@Validated @RequestBody AfterSaleReasonDTO afterSaleReasonDTO,
								  @PathVariable("id") Long id) {
		AfterSaleReason afterSaleReason = IAfterSaleReasonMapStruct.INSTANCE.afterSaleReasonDTOToAfterSaleReason(afterSaleReasonDTO);
		afterSaleReason.setId(id);
		return Result.success(afterSaleReasonService.editAfterSaleReason(afterSaleReason));
	}

	@Operation(summary = "删除售后原因", description = "删除售后原因")
	@RequestLogger("删除售后原因")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delAllByIds(@PathVariable String id) {
		afterSaleReasonService.removeById(id);
		return Result.success(true);
	}
}
