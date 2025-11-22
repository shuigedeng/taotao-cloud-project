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

package com.taotao.cloud.bff.api.controller.backend;

import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import com.taotao.cloud.bff.api.service.app.ISeckillApplyService;
import com.taotao.cloud.promotion.api.model.vo.SeckillGoodsVO;
import com.taotao.cloud.promotion.api.model.vo.SeckillTimelineVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,秒杀活动接口
 *
 * @since 2020/11/17 2:30 下午
 */
@Tag(name = "买家端,秒杀活动接口")
@RestController
@RequestMapping("/buyer/promotion/seckill")
public class SeckillBuyerController {

    /** 秒杀活动 */
    @Autowired private ISeckillApplyService seckillApplyService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取当天秒杀活动信息")
    @GetMapping
    public Result<List<SeckillTimelineVO>> getSeckillTime() {
        return Result.success(seckillApplyService.getSeckillTimeline());
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取某个时刻的秒杀活动商品信息")
    @GetMapping("/{timeline}")
    public Result<List<SeckillGoodsVO>> getSeckillGoods(@PathVariable Integer timeline) {
        return Result.success(seckillApplyService.getSeckillGoods(timeline));
    }
}
