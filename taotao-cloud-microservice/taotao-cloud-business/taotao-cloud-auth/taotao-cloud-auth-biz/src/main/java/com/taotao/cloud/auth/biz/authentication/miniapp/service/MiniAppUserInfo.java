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

package com.taotao.cloud.auth.biz.authentication.miniapp.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniAppUserInfo {

    // OPEN_id
    private String openId;
    // 会话密钥
    private String sessionKey;
    // 头像路径
    private String avatarUrl;
    // 城市
    private String city;
    // 国家
    private String country;
    // 性别
    private String gender;
    // 语言
    private String language;
    // 昵称
    private String nickName;
    // 备注名或真实名
    private String realName;
    // 省份
    private String province;
    private Integer stuId;
}
