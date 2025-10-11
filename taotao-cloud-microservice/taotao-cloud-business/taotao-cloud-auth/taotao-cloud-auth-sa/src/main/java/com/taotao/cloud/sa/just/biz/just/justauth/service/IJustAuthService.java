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

package com.taotao.cloud.sa.just.biz.just.justauth.service;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocial;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocialUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @since 2022/6/18
 */
public interface IJustAuthService {

    /** 查询第三方用户绑定关系 */
    Long userBindId(@NotBlank @RequestParam("uuid") String uuid, @NotBlank @RequestParam("source") String source);

    /** 创建或更新第三方用户信息 */
    Long userCreateOrUpdate(@NotNull @RequestBody JustAuthSocialInfoDTO justAuthSocialInfoDTO);

    /** 查询绑定第三方用户信息 */
    Result<Object> userBindQuery(@NotNull @RequestParam("socialId") Long socialId);

    /**
     * 查询第三方用户信息
     *
     * @param socialId
     * @return
     */
    JustAuthSocial querySocialInfo(@NotNull @RequestParam("socialId") Long socialId);

    /** 绑定第三方用户信息 */
    JustAuthSocialUser userBind(
            @NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId);

    /** 解绑第三方用户信息" */
    Result<JustAuthSocialUser> userUnbind(
            @NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId);
}
