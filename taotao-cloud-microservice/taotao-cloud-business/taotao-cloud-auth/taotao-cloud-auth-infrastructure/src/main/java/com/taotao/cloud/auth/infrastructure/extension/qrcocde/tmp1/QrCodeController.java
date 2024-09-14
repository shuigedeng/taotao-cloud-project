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

package com.taotao.cloud.auth.infrastructure.extension.qrcocde.tmp1;

import com.taotao.boot.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 二维码API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-21 21:07:16
 */
@Validated
@Tag(name = "二维码API", description = "二维码API")
@RestController
@RequestMapping("/auth/qrcode")
public class QrCodeController {

    @Autowired
    private QrCodeService qrCodeService;

    @Operation(summary = "获取二维码", description = "获取二维码")
    // @RequestLogger
    @GetMapping("/code")
    public Result<String> qrcode() {
        return Result.success(qrCodeService.qrcode());
    }
}
