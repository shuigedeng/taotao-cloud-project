package com.taotao.cloud.promotion.biz.api.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.model.vo.cart.FullDiscountVO;
import com.taotao.cloud.promotion.api.web.query.FullDiscountPageQuery;
import com.taotao.cloud.promotion.biz.model.entity.FullDiscount;
import com.taotao.cloud.promotion.biz.service.FullDiscountService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,满额活动接口
 *
 * @since 2021/1/12
 */
@RestController
@Tag(name = "管理端,满额活动接口")
@RequestMapping("/manager/promotion/fullDiscount")
public class FullDiscountManagerController {

	@Autowired
	private FullDiscountService fullDiscountService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取满优惠列表")
	@GetMapping
	public Result<IPage<FullDiscount>> getCouponList(FullDiscountPageQuery searchParams,
													 PageVO page) {
		page.setNotConvert(true);
		return Result.success(fullDiscountService.pageFindAll(searchParams, page));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取满优惠详情")
	@GetMapping("/{id}")
	public Result<FullDiscountVO> getCouponDetail(@PathVariable String id) {
		return Result.success(fullDiscountService.getFullDiscount(id));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "修改满额活动状态")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "满额活动ID", required = true, paramType = "path"),
		@ApiImplicitParam(name = "promotionStatus", value = "满额活动状态", required = true, paramType = "path")
	})
	@PutMapping("/status/{id}")
	public Result<Object> updateCouponStatus(@PathVariable String id, Long startTime,
		Long endTime) {
		if (fullDiscountService.updateStatus(Collections.singletonList(id), startTime, endTime)) {
			return Result.success(ResultEnum.SUCCESS);
		}
		return Result.error(ResultEnum.ERROR);
	}
}
