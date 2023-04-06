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

package com.taotao.cloud.im.biz.platform.modules.common.controller;

import com.platform.common.constant.HeadConstant;
import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.chat.service.ChatVersionService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 通用请求处理 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Resource
    private ChatVersionService versionService;

    /** 校验版本号 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/getVersion")
    public AjaxResult getVersion(HttpServletRequest request) {
        // 请求的版本
        String version = request.getHeader(HeadConstant.VERSION);
        // 请求的设备
        String device = request.getHeader(HeadConstant.DEVICE);
        return AjaxResult.success(versionService.getVersion(version, device));
    }

    /** 用户协议 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/getAgreement")
    public AjaxResult getAgreement() {
        return AjaxResult.success(versionService.getAgreement());
    }
}
