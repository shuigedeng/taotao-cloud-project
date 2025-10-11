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

package com.taotao.cloud.member.biz.controller.business.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.member.sys.model.page.EvaluationPageQuery;
import com.taotao.cloud.member.sys.model.vo.MemberEvaluationListVO;
import com.taotao.cloud.member.sys.model.vo.MemberEvaluationVO;
import com.taotao.cloud.member.biz.model.convert.MemberEvaluationConvert;
import com.taotao.cloud.member.biz.model.entity.MemberEvaluation;
import com.taotao.cloud.member.biz.service.business.IMemberEvaluationService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,商品评价管理API
 *
 * @since 2020-02-25 14:10:16
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-商品评价API", description = "店铺端-商品评价API")
@RequestMapping("/member/seller/member/evaluation")
public class MemberEvaluationController {

    private final IMemberEvaluationService memberEvaluationService;

    @Operation(summary = "分页获取会员评论列表", description = "分页获取会员评论列表")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping
    public Result<PageResult<MemberEvaluationListVO>> getByPage(EvaluationPageQuery evaluationPageQuery) {
        evaluationPageQuery.setStoreId(SecurityUtils.getCurrentUser().getStoreId());
        IPage<MemberEvaluation> memberEvaluationPage = memberEvaluationService.queryPage(evaluationPageQuery);
        return Result.success(MpUtils.convertMybatisPage(memberEvaluationPage, MemberEvaluationListVO.class));
    }

    @Operation(summary = "通过id获取", description = "通过id获取")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/{id}")
    public Result<MemberEvaluationVO> get(@PathVariable Long id) {
        MemberEvaluation memberEvaluation = OperationalJudgment.judgment(memberEvaluationService.queryById(id));
        return Result.success(MemberEvaluationConvert.INSTANCE.convert(memberEvaluation));
    }

    @Operation(summary = "回复评价", description = "回复评价")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @PutMapping(value = "/reply/{id}")
    public Result<Boolean> reply(@PathVariable Long id, @RequestParam String reply, @RequestParam String replyImage) {
        OperationalJudgment.judgment(memberEvaluationService.queryById(id));
        return Result.success(memberEvaluationService.reply(id, reply, replyImage));
    }
}
