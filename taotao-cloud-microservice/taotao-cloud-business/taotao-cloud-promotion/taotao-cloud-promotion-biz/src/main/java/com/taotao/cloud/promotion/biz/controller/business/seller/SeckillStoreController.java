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
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.webmvc.utils.OperationalJudgment;
import com.taotao.cloud.promotion.api.model.page.SeckillPageQuery;
import com.taotao.cloud.promotion.api.model.vo.SeckillApplyVO;
import com.taotao.cloud.promotion.biz.model.entity.Seckill;
import com.taotao.cloud.promotion.biz.model.entity.SeckillApply;
import com.taotao.cloud.promotion.biz.service.business.ISeckillApplyService;
import com.taotao.cloud.promotion.biz.service.business.ISeckillService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 店铺端,秒杀活动接口
 *
 * @since 2020/8/26
 */
@RestController
@Tag(name = "店铺端,秒杀活动接口")
@RequestMapping("/store/promotion/seckill")
public class SeckillStoreController {
	@Autowired
	private ISeckillService seckillService;

	@Autowired
	private ISeckillApplyService seckillApplyService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping
	@Operation(summary = "获取秒杀活动列表")
	public Result<IPage<Seckill>> getSeckillPage(SeckillPageQuery queryParam) {
		IPage<Seckill> seckillPage = seckillService.pageFindAll(queryParam, queryParam.getPageParm());
		return Result.success(seckillPage);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/apply")
	@Operation(summary = "获取秒杀活动申请列表")
	public Result<IPage<SeckillApply>> getSeckillApplyPage(SeckillPageQuery queryParam) {
		String storeId = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getStoreId();
		queryParam.setStoreId(storeId);
		IPage<SeckillApply> seckillPage = seckillApplyService.getSeckillApply(queryParam, queryParam.getPageParm());
		return Result.success(seckillPage);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/{seckillId}")
	@Operation(summary = "获取秒杀活动信息")
	public Result<Seckill> getSeckill(@PathVariable String seckillId) {
		return Result.success(seckillService.getById(seckillId));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/apply/{seckillApplyId}")
	@Operation(summary = "获取秒杀活动申请")
	public Result<SeckillApply> getSeckillApply(@PathVariable String seckillApplyId) {
		SeckillApply seckillApply = OperationalJudgment.judgment(seckillApplyService.getById(seckillApplyId));
		return Result.success(seckillApply);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PostMapping
	@Operation(summary = "添加秒杀活动申请")
	public Result<String> addSeckillApply(@PathVariable String seckillId, @RequestBody List<SeckillApplyVO> applyVos) {
		String storeId = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getStoreId();
		seckillApplyService.addSeckillApply(seckillId, storeId, applyVos);
		return Result.success();
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@DeleteMapping("/apply/{seckillId}/{id}")
	@Operation(summary = "删除秒杀活动商品")
	public Result<String> deleteSeckillApply(@PathVariable String seckillId, @PathVariable String id) {
		OperationalJudgment.judgment(seckillApplyService.getById(id));
		seckillApplyService.removeSeckillApply(seckillId, id);
		return Result.success();
	}
}
