/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.promotion.biz.controller.business.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.promotion.api.model.page.PintuanPageQuery;
import com.taotao.cloud.promotion.api.model.vo.PintuanVO;
import com.taotao.cloud.promotion.biz.model.entity.Pintuan;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.business.IPintuanService;
import com.taotao.cloud.promotion.biz.service.business.IPromotionGoodsService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Objects;

/**
 * 店铺端,拼团管理接口
 *
 * @since 2020/10/9
 */
@RestController
@Tag(name = "店铺端,拼团管理接口")
@RequestMapping("/store/promotion/pintuan")
public class PintuanStoreController {

	@Autowired
	private IPintuanService pintuanService;

	@Autowired
	private IPromotionGoodsService promotionGoodsService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping
	@Operation(summary = "根据条件分页查询拼团活动列表")
	public Result<IPage<Pintuan>> getPintuanByPage(PintuanPageQuery queryParam) {
		SecurityUser currentUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
		queryParam.setStoreId(currentUser.getStoreId());
		return Result.success(pintuanService.pageFindAll(queryParam, queryParam.getPageParm()));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping(value = "/{id}")
	@Operation(summary = "通过id获取")
	public Result<PintuanVO> get(@PathVariable String id) {
		PintuanVO pintuan = OperationalJudgment.judgment(pintuanService.getPintuanVO(id));
		return Result.success(pintuan);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/goods/{pintuanId}")
	@Operation(summary = "根据条件分页查询拼团活动商品列表")
	public Result<IPage<PromotionGoods>> getPintuanGoodsByPage(@PathVariable String pintuanId, PageQuery pageQuery) {
		SecurityUser currentUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
		PromotionGoodsSearchParams searchParams = new PromotionGoodsSearchParams();
		searchParams.setStoreId(currentUser.getStoreId());
		searchParams.setPromotionId(pintuanId);
		searchParams.setPromotionType(PromotionTypeEnum.PINTUAN.name());
		return Result.success(promotionGoodsService.pageFindAll(searchParams, pageQuery));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PostMapping
	@Operation(summary = "添加拼团活动")
	public Result<String> addPintuan(@RequestBody @Validated PintuanVO pintuan) {
		SecurityUser currentUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
		pintuan.setStoreId(currentUser.getStoreId());
		pintuan.setStoreName(currentUser.getStoreName());
		if (pintuanService.savePromotions(pintuan)) {
			return Result.success(ResultEnum.PINTUAN_ADD_SUCCESS);
		}
		throw new BusinessException(ResultEnum.PINTUAN_ADD_ERROR);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PutMapping
	@Operation(summary = "修改拼团活动")
	public Result<String> editPintuan(@RequestBody @Validated PintuanVO pintuan) {
		OperationalJudgment.judgment(pintuanService.getById(pintuan.getId()));
		SecurityUser currentUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
		pintuan.setStoreId(currentUser.getStoreId());
		pintuan.setStoreName(currentUser.getStoreName());
		if (pintuanService.updatePromotions(pintuan)) {
			return Result.success(ResultEnum.PINTUAN_EDIT_SUCCESS);
		}
		throw new BusinessException(ResultEnum.PINTUAN_EDIT_ERROR);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PutMapping("/status/{pintuanId}")
	@Operation(summary = "操作拼团活动状态")
	public Result<String> openPintuan(@PathVariable String pintuanId, Long startTime, Long endTime) {
		OperationalJudgment.judgment(pintuanService.getById(pintuanId));
		if (pintuanService.updateStatus(Collections.singletonList(pintuanId), startTime, endTime)) {
			return Result.success(ResultEnum.PINTUAN_MANUAL_OPEN_SUCCESS);
		}
		throw new BusinessException(ResultEnum.PINTUAN_MANUAL_OPEN_ERROR);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@DeleteMapping("/{pintuanId}")
	@Operation(summary = "手动删除拼团活动")
	public Result<String> deletePintuan(@PathVariable String pintuanId) {
		OperationalJudgment.judgment(pintuanService.getById(pintuanId));
		if (pintuanService.removePromotions(Collections.singletonList(pintuanId))) {
			return Result.success(ResultEnum.PINTUAN_DELETE_SUCCESS);
		}
		throw new BusinessException(ResultEnum.PINTUAN_DELETE_ERROR);
	}
}
