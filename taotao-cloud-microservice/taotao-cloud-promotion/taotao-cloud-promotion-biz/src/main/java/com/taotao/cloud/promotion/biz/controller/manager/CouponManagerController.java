package com.taotao.cloud.promotion.biz.controller.manager;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.promotion.api.query.CouponSearchParams;
import com.taotao.cloud.promotion.api.vo.CouponVO;
import com.taotao.cloud.promotion.biz.entity.Coupon;
import com.taotao.cloud.promotion.biz.entity.MemberCoupon;
import com.taotao.cloud.promotion.biz.service.CouponService;
import com.taotao.cloud.promotion.biz.service.MemberCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
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
 * 管理端,优惠券接口
 *
 * @since 2020/10/9
 **/
@RestController
@Tag(name = "管理端,优惠券接口")
@RequestMapping("/manager/promotion/coupon")
public class CouponManagerController {

	@Autowired
	private CouponService couponService;
	@Autowired
	private MemberCouponService memberCouponService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取优惠券列表")
	@GetMapping
	public Result<IPage<CouponVO>> getCouponList(CouponSearchParams queryParam, PageVO page) {
		queryParam.setStoreId("platform");
		return Result.success(couponService.pageVOFindAll(queryParam, page));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取优惠券详情")
	@GetMapping("/{couponId}")
	public Result<CouponVO> getCoupon(@PathVariable String couponId) {
		CouponVO coupon = couponService.getDetail(couponId);
		return Result.success(coupon);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "添加优惠券")
	@PostMapping(consumes = "application/json", produces = "application/json")
	public Result<CouponVO> addCoupon(@RequestBody CouponVO couponVO) {
		this.setStoreInfo(couponVO);
		couponService.savePromotions(couponVO);
		return Result.success(couponVO);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "修改优惠券")
	@PutMapping(consumes = "application/json", produces = "application/json")
	public Result<Coupon> updateCoupon(@RequestBody CouponVO couponVO) {
		this.setStoreInfo(couponVO);
		Coupon coupon = couponService.getById(couponVO.getId());
		couponService.updatePromotions(couponVO);
		return Result.success(coupon);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "修改优惠券状态")
	@PutMapping("/status")
	public Result<Object> updateCouponStatus(String couponIds, Long startTime, Long endTime) {
		String[] split = couponIds.split(",");
		if (couponService.updateStatus(Arrays.asList(split), startTime, endTime)) {
			return Result.success(ResultEnum.COUPON_EDIT_STATUS_SUCCESS);
		}
		throw new BusinessException(ResultEnum.COUPON_EDIT_STATUS_ERROR);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "批量删除")
	@DeleteMapping(value = "/{ids}")
	public Result<Object> delAllByIds(@PathVariable List<String> ids) {
		couponService.removePromotions(ids);
		return Result.success();
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "会员优惠券作废")
	@PutMapping(value = "/member/cancellation/{id}")
	public Result<Object> cancellation(@PathVariable String id) {
		memberCouponService.cancellation(id);
		return Result.success(ResultEnum.COUPON_CANCELLATION_SUCCESS);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "根据优惠券id券分页获取会员领详情")
	@GetMapping(value = "/member/{id}")
	public Result<IPage<MemberCoupon>> getByPage(@PathVariable String id,
		PageVO page) {
		QueryWrapper<MemberCoupon> queryWrapper = new QueryWrapper<>();
		IPage<MemberCoupon> data = memberCouponService.page(PageUtil.initPage(page),
			queryWrapper.eq("coupon_id", id)
		);
		return Result.success(data);

	}

	private void setStoreInfo(CouponVO couponVO) {
		AuthUser currentUser = UserContext.getCurrentUser();
		if (currentUser == null) {
			throw new BusinessException(ResultEnum.USER_NOT_EXIST);
		}
		couponVO.setStoreId("platform");
		couponVO.setStoreName("platform");
	}

}
