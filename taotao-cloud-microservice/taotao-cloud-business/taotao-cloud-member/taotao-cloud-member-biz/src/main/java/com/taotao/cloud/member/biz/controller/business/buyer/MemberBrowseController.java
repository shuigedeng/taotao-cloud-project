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

package com.taotao.cloud.member.biz.controller.business.buyer;

import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.goods.api.model.vo.EsGoodsIndexVO;
import com.taotao.cloud.member.biz.service.business.IMemberBrowseService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端-浏览历史API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:52:08
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-会员浏览历史API", description = "买家端-会员浏览历史API")
@RequestMapping("/member/buyer/member/browse")
public class MemberBrowseController {

    private final IMemberBrowseService memberBrowseService;

    @Operation(summary = "分页获取浏览历史", description = "分页获取浏览历史")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping
    public Result<List<EsGoodsIndexVO>> getByPage(PageQuery PageQuery) {
        return Result.success(memberBrowseService.footPrintPage(PageQuery));
    }

    @Operation(summary = "根据id删除浏览历史", description = "根据id删除浏览历史")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @DeleteMapping(value = "/{ids}")
    public Result<Boolean> delAllByIds(
            @Parameter(description = "会员地址ID", required = true) @NotEmpty(message = "商品ID不能为空") @PathVariable("ids")
                    List<Long> ids) {
        return Result.success(memberBrowseService.deleteByIds(ids));
    }

    @Operation(summary = "清空足迹", description = "清空足迹")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @DeleteMapping
    public Result<Boolean> deleteAll() {
        return Result.success(memberBrowseService.clean());
    }

    @Operation(summary = "获取当前会员足迹数量", description = "获取当前会员足迹数量")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/current/foot/count")
    public Result<Long> getFootprintNum() {
        return Result.success(memberBrowseService.getFootprintNum());
    }
}
