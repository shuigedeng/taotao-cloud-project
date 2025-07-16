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

package com.taotao.cloud.auth.biz.authentication.federation;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 三方登录账户信息表
 * </p>
 *
 */
@Getter
@Setter
@TableName("oauth2_third_Account")
public class Oauth2ThirdAccount implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户表主键
     */
    private Integer userId;

    /**
     * 三方登录唯一id
     */
    private String uniqueId;

    /**
     * 三方登录用户名
     */
    private String thirdUsername;

    /**
     * 三方登录获取的认证信息
     */
    private String credentials;

    /**
     * 三方登录获取的认证信息的过期时间
     */
    private LocalDateTime credentialsExpiresAt;

    /**
     * 三方登录类型
     */
    private String type;

    /**
     * 博客地址
     */
    private String blog;

    /**
     * 地址
     */
    private String location;

    /**
     * 绑定时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 用户名、昵称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 头像地址
     */
    @TableField(exist = false)
    private String avatarUrl;
}
