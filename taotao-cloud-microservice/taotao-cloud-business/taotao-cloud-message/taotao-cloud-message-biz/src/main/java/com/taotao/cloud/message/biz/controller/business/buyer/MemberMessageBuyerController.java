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

package com.taotao.cloud.message.biz.controller.business.buyer;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.message.api.enums.MessageStatusEnum;
import com.taotao.cloud.message.biz.service.business.MemberMessageService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 买家端,会员站内消息接口 */
@Validated
@RestController
@Tag(name = "管理端-会员站内消息API", description = "管理端-会员站内消息API")
@RequestMapping("/message/buyer/member")
public class MemberMessageBuyerController {

    /** 会员站内消息 */
    @Autowired
    private MemberMessageService memberMessageService;

    // @Operation(summary = "分页获取会员站内消息", description = "分页获取会员站内消息")
    // @RequestLogger
    // @PreAuthorize("hasAuthority('dept:tree:data')")
    // @GetMapping
    // public Result<IPage<MemberMessage>> page(MemberMessageQueryVO memberMessageQueryVO,
    // 										 PageVO page) {
    // 	memberMessageQueryVO.setMemberId(UserContext.getCurrentUser().getId());
    // 	return Result.success(memberMessageService.getPage(memberMessageQueryVO, page));
    // }

    @Operation(summary = "消息已读", description = "消息已读")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping("/{message_id}")
    public Result<Boolean> read(@PathVariable("message_id") String messageId) {
        return Result.success(memberMessageService.editStatus(MessageStatusEnum.ALREADY_READY.name(), messageId));
    }

    @Operation(summary = "消息放入回收站", description = "消息放入回收站")
    @RequestLogger("消息放入回收站")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @DeleteMapping("/{message_id}")
    public Result<Boolean> deleteMessage(@PathVariable("message_id") String messageId) {
        return Result.success(memberMessageService.editStatus(MessageStatusEnum.ALREADY_REMOVE.name(), messageId));
    }
}
