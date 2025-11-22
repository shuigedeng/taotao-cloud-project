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

package com.taotao.cloud.promotion.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.promotion.api.model.page.SeckillPageQuery;
import com.taotao.cloud.promotion.api.model.vo.SeckillVO;
import com.taotao.cloud.promotion.biz.model.entity.Seckill;
import com.taotao.cloud.promotion.biz.model.entity.SeckillApply;
import com.taotao.cloud.promotion.biz.service.business.ISeckillApplyService;
import com.taotao.cloud.promotion.biz.service.business.ISeckillService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,秒杀活动接口
 *
 * @since 2020/8/20
 */
@RestController
@Tag(name = "管理端,秒杀活动接口")
@RequestMapping("/manager/promotion/seckill")
public class SeckillManagerController {

    @Autowired
    private ISeckillService seckillService;

    @Autowired
    private ISeckillApplyService seckillApplyService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "初始化秒杀活动(初始化方法，默认初始化30天内的活动）")
    @GetMapping("/init")
    public void addSeckill() {
        seckillService.init();
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "修改秒杀活动")
    @PutMapping("/{id}")
    public Result<Seckill> updateSeckill(@RequestBody SeckillVO seckillVO) {
        seckillService.updatePromotions(seckillVO);
        return Result.success(seckillVO);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "通过id获取")
    @ApiImplicitParam(name = "id", value = "秒杀活动ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/{id}")
    public Result<Seckill> get(@PathVariable String id) {
        Seckill seckill = seckillService.getById(id);
        return Result.success(seckill);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "分页查询秒杀活动列表")
    @GetMapping
    public Result<IPage<Seckill>> getAll(SeckillPageQuery param, PageVO pageVo) {
        return Result.success(seckillService.pageFindAll(param, pageVo));
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "删除一个秒杀活动")
    @DeleteMapping("/{id}")
    public Result<Object> deleteSeckill(@Parameter(description = "秒杀活动ID") @PathVariable String id) {
        seckillService.removePromotions(Collections.singletonList(id));
        return Result.success();
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "操作秒杀活动状态")
    @PutMapping("/status/{id}")
    public Result<Object> updateSeckillStatus(
            @Parameter(description = "秒杀活动ID") @PathVariable String id, Long startTime, Long endTime) {
        seckillService.updateStatus(Collections.singletonList(id), startTime, endTime);
        return Result.success();
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取秒杀活动申请列表")
    @GetMapping("/apply")
    public Result<IPage<SeckillApply>> getSeckillApply(SeckillPageQuery param, PageVO pageVo) {
        IPage<SeckillApply> seckillApply = seckillApplyService.getSeckillApply(param, pageVo);
        return Result.success(seckillApply);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @DeleteMapping("/apply/{seckillId}/{id}")
    @Operation(summary = "删除秒杀活动申请")
    public Result<String> deleteSeckillApply(@PathVariable String seckillId, @PathVariable String id) {
        seckillApplyService.removeSeckillApply(seckillId, id);
        return Result.success();
    }
}
