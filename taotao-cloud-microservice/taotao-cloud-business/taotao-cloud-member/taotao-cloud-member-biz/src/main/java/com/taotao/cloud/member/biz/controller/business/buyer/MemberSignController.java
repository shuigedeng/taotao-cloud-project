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

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.member.sys.model.vo.MemberSignVO;
import com.taotao.cloud.member.biz.service.business.IMemberSignService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端-会员签到API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:58:40
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-会员签到API", description = "买家端-会员签到API")
@RequestMapping("/member/buyer/member/sign")
public class MemberSignController {

    private final IMemberSignService memberSignService;

    @Operation(summary = "会员签到", description = "会员签到")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @PostMapping
    public Result<Boolean> memberSign() {
        return Result.success(memberSignService.memberSign());
    }

    @Operation(summary = "根据时间查询会员签到表，类型是YYYYmm", description = "根据时间查询会员签到表，类型是YYYYmm")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping
    public Result<List<MemberSignVO>> memberSign(@RequestParam String time) {
        return Result.success(memberSignService.getMonthSignDay(time));
    }
}
