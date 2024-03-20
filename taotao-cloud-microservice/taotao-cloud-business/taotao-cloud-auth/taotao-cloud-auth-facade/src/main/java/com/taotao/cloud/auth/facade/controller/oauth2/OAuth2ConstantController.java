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

package com.taotao.cloud.auth.facade.controller.oauth2;

import com.alibaba.nacos.api.model.v2.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>OAuth2 常量 Controller </p>
 *
 *
 * @since : 2022/3/17 15:00
 */
@RestController
@RequestMapping("/authorize/constant")
@Tags({@Tag(name = "OAuth2 认证服务接口"), @Tag(name = "常量接口")})
public class OAuth2ConstantController {

    private final OAuth2ConstantService constantService;

    @Autowired
    public OAuth2ConstantController(OAuth2ConstantService constantService) {
        this.constantService = constantService;
    }

    @Operation(summary = "获取服务常量", description = "获取服务涉及的常量以及信息")
    @GetMapping(value = "/enums")
    public Result<Map<String, Object>> findAllEnums() {
        Map<String, Object> allEnums = constantService.getAllEnums();
        //        if (MapUtils.isNotEmpty(allEnums)) {
        //            return Result.success("获取服务常量成功", allEnums);
        //        } else {
        //            return Result.failure("获取服务常量失败");
        //        }
        return null;
    }
}
