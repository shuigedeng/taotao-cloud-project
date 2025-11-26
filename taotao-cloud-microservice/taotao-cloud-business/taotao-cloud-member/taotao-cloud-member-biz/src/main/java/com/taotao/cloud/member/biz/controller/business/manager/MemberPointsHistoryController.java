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

package com.taotao.cloud.member.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.member.sys.model.page.MemberPointHistoryPageQuery;
import com.taotao.cloud.member.sys.model.vo.MemberPointsHistoryPageVO;
import com.taotao.cloud.member.sys.model.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.model.entity.MemberPointsHistory;
import com.taotao.cloud.member.biz.service.business.IMemberPointsHistoryService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,会员积分历史API
 *
 * @since 2020-02-25 14:10:16
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/member/manager/member/points/history")
@Tag(name = "管理端-会员积分历史管理API", description = "管理端-会员积分历史管理API")
public class MemberPointsHistoryController {

    private final IMemberPointsHistoryService memberPointsHistoryService;

    @Operation(summary = "分页获取", description = "分页获取")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/page")
    public Result<PageResult<MemberPointsHistoryPageVO>> getByPage(MemberPointHistoryPageQuery pageQuery) {
        IPage<MemberPointsHistory> page = memberPointsHistoryService.memberPointsHistoryPageQuery(
                pageQuery.getPageParm(), pageQuery.getMemberId(), pageQuery.getMemberName());
        return Result.success(MpUtils.convertMybatisPage(page, MemberPointsHistoryPageVO.class));
    }

    @Operation(summary = "获取会员积分", description = "获取会员积分")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "")
    public Result<MemberPointsHistoryVO> getMemberPointsHistoryVO(Long memberId) {
        return Result.success(memberPointsHistoryService.getMemberPointsHistoryVO(memberId));
    }
}
