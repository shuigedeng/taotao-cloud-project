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

package com.taotao.cloud.customer.biz.api.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.webmvc.utils.OperationalJudgment;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.member.api.feign.MemberEvaluationApi;
import com.taotao.cloud.member.api.query.EvaluationPageQuery;
import com.taotao.cloud.member.api.vo.MemberEvaluationListVO;
import com.taotao.cloud.member.api.vo.MemberEvaluationVO;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 店铺端,商品评价管理接口 */
@Validated
@RestController
@Tag(name = "店铺端-商品评价管理接口", description = "店铺端-商品评价管理接口")
@RequestMapping("/store/memberEvaluation")
public class MemberEvaluationStoreController {

    @Autowired
    private MemberEvaluationApi memberEvaluationApi;

    @Operation(summary = "分页获取会员评论列表", description = "分页获取会员评论列表")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/page")
    public Result<PageResult<MemberEvaluationListVO>> getByPage(EvaluationPageQuery evaluationPageQuery) {
        evaluationPageQuery.setStoreId(SecurityUtils.getCurrentUser().getStoreId());
        IPage<MemberEvaluationListVO> memberEvaluationListVOIPage = memberEvaluationApi.queryPage(evaluationPageQuery);
        return Result.success(MpUtils.convertMybatisPage(memberEvaluationListVOIPage, MemberEvaluationListVO.class));
    }

    @Operation(summary = "通过id获取", description = "通过id获取")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{id}")
    public Result<MemberEvaluationVO> get(@PathVariable Long id) {
        return Result.success(OperationalJudgment.judgment(memberEvaluationApi.queryById(id)));
    }

    @Operation(summary = "回复评价", description = "回复评价")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/reply/{id}")
    public Result<MemberEvaluationVO> reply(
            @Parameter(description = "评价ID") @PathVariable Long id,
            @Parameter(description = "回复内容") @RequestParam String reply,
            @Parameter(description = "回复图片") @RequestParam String replyImage) {
        MemberEvaluationVO memberEvaluationVO = OperationalJudgment.judgment(memberEvaluationApi.queryById(id));
        memberEvaluationApi.reply(id, reply, replyImage);
        return Result.success(memberEvaluationVO);
    }
}
