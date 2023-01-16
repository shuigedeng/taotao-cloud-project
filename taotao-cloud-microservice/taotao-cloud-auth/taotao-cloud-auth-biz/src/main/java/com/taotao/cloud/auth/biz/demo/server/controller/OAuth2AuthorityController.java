/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.demo.server.controller;

import cn.herodotus.engine.assistant.core.domain.Result;
import cn.herodotus.engine.oauth2.core.definition.domain.Authority;
import cn.herodotus.engine.oauth2.core.definition.strategy.StrategyAuthorityDetailsService;
import cn.herodotus.engine.oauth2.server.authentication.entity.OAuth2Authority;
import cn.herodotus.engine.rest.core.controller.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: OAuth2 权限接口 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/4/1 18:20
 */
@RestController
@RequestMapping("/authorize/authority")
@Tags({
        @Tag(name = "OAuth2 认证服务接口"),
        @Tag(name = "OAuth2 权限读取接口")
})
public class OAuth2AuthorityController implements Controller {

    private final StrategyAuthorityDetailsService strategyAuthorityDetailsService;

    @Autowired
    public OAuth2AuthorityController(StrategyAuthorityDetailsService strategyAuthorityDetailsService) {
        this.strategyAuthorityDetailsService = strategyAuthorityDetailsService;
    }

    @Operation(summary = "查询所有权限数据", description = "查询所有权限数据用于给Scope分配权限",
            responses = {@ApiResponse(description = "权限列表", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OAuth2Authority.class)))})
    @GetMapping("/condition")
    public Result<List<OAuth2Authority>> findAll() {
        List<Authority> authorities = strategyAuthorityDetailsService.findAll();
        if (CollectionUtils.isNotEmpty(authorities)) {
            List<OAuth2Authority> result = toEntities(authorities);
            return result(result);
        } else {
            return Result.empty("未查询到数据");
        }
    }

    private List<OAuth2Authority> toEntities(List<Authority> authorities) {
        return authorities.stream().map(this::toEntity).collect(Collectors.toList());
    }

    private OAuth2Authority toEntity(Authority object) {
        OAuth2Authority authority = new OAuth2Authority();
        authority.setAuthorityId(object.getAuthorityId());
        authority.setAuthorityCode(object.getAuthorityCode());
        authority.setServiceId(object.getServiceId());
        authority.setRequestMethod(object.getRequestMethod());
        authority.setUrl(object.getUrl());
        return authority;
    }
}
