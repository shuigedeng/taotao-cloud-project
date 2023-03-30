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

package com.taotao.cloud.im.biz.platform.common.shiro.vo;

import com.platform.modules.chat.domain.ChatUser;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 登录用户身份权限 */
@Data
@NoArgsConstructor
public class LoginUser {

    /** 用户唯一标识 */
    private String tokenId;

    /** 用户ID */
    private Long userId;

    /** 用户手机号 */
    private String phone;

    /** 权限列表 */
    private List<String> permissions;

    /** 角色key */
    private String roleKey;

    /** 登陆时间 */
    private String loginTime;

    /** 登录IP地址 */
    private String ipAddr;

    /** 登录地点 */
    private String loginLocation;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    public LoginUser(ChatUser chatUser, String roleKey, List<String> permissions) {
        this.userId = chatUser.getUserId();
        this.phone = chatUser.getPhone();
        this.roleKey = roleKey;
        this.permissions = permissions;
    }
}
