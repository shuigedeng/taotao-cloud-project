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
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.promotion.api.model.page.PointsGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.vo.PointsGoodsVO;
import com.taotao.cloud.promotion.biz.model.entity.PointsGoods;
import com.taotao.cloud.promotion.biz.service.business.IPointsGoodsService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.SecurityUtils;
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
 * 管理端,积分商品接口
 *
 * @since 2021/1/14
 */
@RestController
@Tag(name = "管理端,积分商品接口")
@RequestMapping("/manager/promotion/pointsGoods")
public class PointsGoodsManagerController {

    @Autowired
    private IPointsGoodsService pointsGoodsService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @PostMapping
    @Operation(summary = "添加积分商品")
    public Result<Object> addPointsGoods(@RequestBody List<PointsGoods> pointsGoodsList) {
        if (pointsGoodsService.savePointsGoodsBatch(pointsGoodsList)) {
            return Result.success();
        }
        return Result.error(ResultEnum.POINT_GOODS_ERROR);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @PutMapping
    @Operation(summary = "修改积分商品")
    public Result<Object> updatePointsGoods(@RequestBody PointsGoodsVO pointsGoods) {
        Objects.requireNonNull(SecurityUtils.getCurrentUser());
        pointsGoodsService.updatePromotions(pointsGoods);
        return Result.success();
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @PutMapping("/status/{ids}")
    @Operation(summary = "修改积分商品状态")
    public Result<Object> updatePointsGoodsStatus(@PathVariable String ids, Long startTime, Long endTime) {
        if (pointsGoodsService.updateStatus(Arrays.asList(ids.split(",")), startTime, endTime)) {
            return Result.success();
        }
        return Result.error(ResultEnum.POINT_GOODS_ERROR);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除积分商品")
    public Result<Object> delete(@PathVariable String ids) {
        if (pointsGoodsService.removePromotions(Arrays.asList(ids.split(",")))) {
            return Result.success();
        }
        throw new BusinessException(ResultEnum.POINT_GOODS_ERROR);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @GetMapping
    @Operation(summary = "分页获取积分商品")
    public Result<IPage<PointsGoods>> getPointsGoodsPage(PointsGoodsPageQuery searchParams) {
        IPage<PointsGoods> pointsGoodsByPage = pointsGoodsService.pageFindAll(searchParams, page);
        return Result.success(pointsGoodsByPage);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @GetMapping("/{id}")
    @Operation(summary = "获取积分商品详情")
    public Result<Object> getPointsGoodsDetail(@PathVariable String id) {
        PointsGoodsVO pointsGoodsDetail = pointsGoodsService.getPointsGoodsDetail(id);
        return Result.success(pointsGoodsDetail);
    }
}
