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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.SwitchEnum;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.member.sys.model.dto.MemberEvaluationDTO;
import com.taotao.cloud.member.sys.model.page.EvaluationPageQuery;
import com.taotao.cloud.member.sys.model.vo.EvaluationNumberVO;
import com.taotao.cloud.member.sys.model.vo.MemberEvaluationVO;
import com.taotao.cloud.member.biz.model.convert.MemberEvaluationConvert;
import com.taotao.cloud.member.biz.model.entity.MemberEvaluation;
import com.taotao.cloud.member.biz.service.business.IMemberEvaluationService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端-会员商品评价API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:57:55
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-会员商品评价API", description = "买家端-会员商品评价API")
@RequestMapping("/member/buyer/member/evaluation")
public class MemberEvaluationController {

    /** 会员商品评价 */
    private final IMemberEvaluationService memberEvaluationService;

    @Operation(summary = "添加会员评价", description = "添加会员评价")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @PostMapping
    public Result<Boolean> save(@Valid @RequestBody MemberEvaluationDTO memberEvaluationDTO) {
        return Result.success(memberEvaluationService.addMemberEvaluation(memberEvaluationDTO));
    }

    @Operation(summary = "查看会员评价详情", description = "查看会员评价详情")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/{id}")
    public Result<MemberEvaluationVO> queryById(
            @Parameter(description = "评价ID", required = true) @NotBlank(message = "评价ID不能为空") @PathVariable("id")
                    Long id) {
        MemberEvaluation memberEvaluation = memberEvaluationService.queryById(id);
        return Result.success(MemberEvaluationConvert.INSTANCE.convert(memberEvaluation));
    }

    @Operation(summary = "查看当前会员评价列表", description = "查看当前会员评价列表")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping
    public Result<PageResult<MemberEvaluationVO>> queryMineEvaluation(
            @Validated EvaluationPageQuery evaluationPageQuery) {
        // 设置当前登录会员
        evaluationPageQuery.setMemberId(SecurityUtils.getUserId());
        IPage<MemberEvaluation> memberEvaluationPage = memberEvaluationService.managerQuery(evaluationPageQuery);
        return Result.success(MpUtils.convertMybatisPage(memberEvaluationPage, MemberEvaluationVO.class));
    }

    @Operation(summary = "查看某一个商品的评价列表", description = "查看某一个商品的评价列表")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/goods-evaluation/{goodsId}")
    public Result<PageResult<MemberEvaluationVO>> queryGoodsEvaluation(
            EvaluationPageQuery evaluationPageQuery,
            @Parameter(description = "商品ID", required = true) @NotBlank(message = "商品ID不能为空") @PathVariable("goodsId")
                    Long goodsId) {
        // 设置查询查询商品
        evaluationPageQuery.setGoodsId(goodsId);
        evaluationPageQuery.setStatus(SwitchEnum.OPEN.name());
        IPage<MemberEvaluation> memberEvaluationPage = memberEvaluationService.managerQuery(evaluationPageQuery);
        return Result.success(MpUtils.convertMybatisPage(memberEvaluationPage, MemberEvaluationVO.class));
    }

    @Operation(summary = "查看某一个商品的评价数量", description = "查看某一个商品的评价数量")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/goods-evaluation/number/{goodsId}")
    public Result<EvaluationNumberVO> queryEvaluationNumber(
            @Parameter(description = "商品ID", required = true) @NotBlank(message = "商品ID不能为空") @PathVariable("goodsId")
                    Long goodsId) {
        return Result.success(memberEvaluationService.getEvaluationNumber(goodsId));
    }
}
