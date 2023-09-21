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

package com.taotao.cloud.wechat.biz.wechat.controller;

import cn.bootx.common.core.annotation.IgnoreAuth;
import cn.bootx.common.core.rest.Res;
import cn.bootx.common.core.rest.ResResult;
import cn.bootx.starter.wechat.core.login.service.WeChatQrLoginService;
import cn.bootx.starter.wechat.dto.login.WeChatLoginQrCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信
 *
 * @author xxm
 * @since 2022/8/4
 */
@IgnoreAuth
@Tag(name = "微信扫码登录")
@RestController
@RequestMapping("/token/wechat/qr")
@RequiredArgsConstructor
public class WeChatQrLoginController {
    private final WeChatQrLoginService weChatQrLoginService;

    @Operation(summary = "申请登录用QR码")
    @PostMapping("/applyQrCode")
    public ResResult<WeChatLoginQrCode> applyQrCode() {
        return Res.ok(weChatQrLoginService.applyQrCode());
    }

    @Operation(summary = "获取扫码状态")
    @GetMapping("/getStatus")
    public ResResult<String> getStatus(String qrCodeKey) {
        return Res.ok(weChatQrLoginService.getStatus(qrCodeKey));
    }
}
