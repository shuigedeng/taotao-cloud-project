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

package com.taotao.cloud.auth.biz.management.controller;

import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.common.model.Result;
import com.taotao.boot.data.jpa.utils.JpaUtils;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Compliance;
import com.taotao.cloud.auth.biz.management.service.OAuth2ComplianceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>OAuth2ComplianceController </p>
 *
 *
 * @since : 2022/7/7 22:56
 */
@RestController
@RequestMapping("/authorize/compliance")
@Tags({@Tag(name = "OAuth2 认证服务接口"), @Tag(name = "OAuth2 应用安全合规接口"), @Tag(name = "OAuth2 审计管理接口")})
public class OAuth2ComplianceController {

    private final OAuth2ComplianceService complianceService;

    @Autowired
    public OAuth2ComplianceController(OAuth2ComplianceService complianceService) {
        this.complianceService = complianceService;
    }

    @Operation(
            summary = "模糊条件查询合规信息",
            description = "根据动态输入的字段模糊查询合规信息",
            responses = {
                @ApiResponse(
                        description = "人员分页列表",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = Map.class)))
            })
    @Parameters({
        @Parameter(name = "pageNumber", required = true, description = "当前页码"),
        @Parameter(name = "pageSize", required = true, description = "每页显示数量"),
        @Parameter(name = "principalName", description = "用户账号"),
        @Parameter(name = "clientId", description = "客户端ID"),
        @Parameter(name = "ip", description = "IP地址"),
    })
    @GetMapping("/condition")
    public Result<PageResult<OAuth2Compliance>> findByCondition(
            @NotBlank @RequestParam("pageNumber") Integer pageNumber,
            @NotBlank @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "principalName", required = false) String principalName,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "ip", required = false) String ip) {
        Page<OAuth2Compliance> pages =
                complianceService.findByCondition(
                        pageNumber, pageSize, principalName, clientId, ip);
        return Result.success(JpaUtils.convertJpaPage(pages, OAuth2Compliance.class));
    }
}
