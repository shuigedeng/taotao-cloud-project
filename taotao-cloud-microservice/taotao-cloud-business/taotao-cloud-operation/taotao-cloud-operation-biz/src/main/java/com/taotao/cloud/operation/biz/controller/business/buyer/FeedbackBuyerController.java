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

package com.taotao.cloud.operation.biz.controller.business.buyer;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.operation.biz.model.entity.Feedback;
import com.taotao.cloud.operation.biz.service.business.FeedbackService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 买家端,意见反馈接口 */
@RestController
@Tag(name = "买家端,意见反馈接口")
@RequestMapping("/buyer/other/feedback")
public class FeedbackBuyerController {

    /** 意见反馈 */
    @Autowired
    private FeedbackService feedbackService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @PreventDuplicateSubmissions
    @Operation(summary = "添加意见反馈")
    @PostMapping()
    public Result<Object> save(@Valid Feedback feedback) {
        feedback.setUserName(UserContext.getCurrentUser().getNickName());
        feedbackService.save(feedback);
        return Result.success();
    }
}
