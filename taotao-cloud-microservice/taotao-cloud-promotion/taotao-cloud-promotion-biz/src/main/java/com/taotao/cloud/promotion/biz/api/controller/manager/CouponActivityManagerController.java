package com.taotao.cloud.promotion.biz.api.controller.manager;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.promotion.api.web.dto.CouponActivityDTO;
import com.taotao.cloud.promotion.api.web.vo.CouponActivityVO;
import com.taotao.cloud.promotion.biz.model.entity.CouponActivity;
import com.taotao.cloud.promotion.biz.service.CouponActivityService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 优惠券活动
 *
 * @since 2021/5/21 7:11 下午
 */
@RestController
@Tag(name = "管理端,优惠券活动接口")
@RequestMapping("/manager/promotion/couponActivity")
public class CouponActivityManagerController {

	@Autowired
	private CouponActivityService couponActivityService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取优惠券活动分页")
	@GetMapping
	public Result<IPage<CouponActivity>> getCouponActivityPage(PageVO page) {
		return Result.success(couponActivityService.page(PageUtil.initPage(page)));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取优惠券活动")
	@ApiImplicitParam(name = "couponActivityId", value = "优惠券活动ID", required = true, paramType = "path")
	@GetMapping("/{couponActivityId}")
	public Result<CouponActivityVO> getCouponActivity(@PathVariable String couponActivityId) {
		return Result.success(couponActivityService.getCouponActivityVO(couponActivityId));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "添加优惠券活动")
	@PostMapping
	@PutMapping(consumes = "application/json", produces = "application/json")
	public Result<CouponActivity> addCouponActivity(
		@RequestBody(required = false) CouponActivityDTO couponActivityDTO) {
		if (couponActivityService.savePromotions(couponActivityDTO)) {
			return Result.success(couponActivityDTO);
		}
		return Result.error(ResultEnum.COUPON_ACTIVITY_SAVE_ERROR);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "关闭优惠券活动")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "优惠券活动ID", required = true, dataType = "String", paramType = "path")
	})
	@DeleteMapping("/{id}")
	public Result<CouponActivity> updateStatus(@PathVariable String id) {
		if (couponActivityService.updateStatus(Collections.singletonList(id), null, null)) {
			return Result.success(ResultEnum.SUCCESS);
		}
		throw new BusinessException(ResultEnum.ERROR);
	}


}
