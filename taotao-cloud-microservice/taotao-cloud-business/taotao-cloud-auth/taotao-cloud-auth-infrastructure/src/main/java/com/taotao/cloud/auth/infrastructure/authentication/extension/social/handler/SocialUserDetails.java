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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.handler;

/**
 * <p>社交登录用户信息详情 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 10:04:22
 */
public interface SocialUserDetails {

    /**
     * 获取社交登录唯一标识
     *
     * @return {@link String }
     * @since 2023-07-04 10:04:22
     */
    String getUuid();

    /**
     * 获取社交登录分类标识
     *
     * @return {@link String }
     * @since 2023-07-04 10:04:22
     */
    String getSource();

    /**
     * 获得电话号码
     *
     * @return {@link String }
     * @since 2023-07-04 10:04:22
     */
    String getPhoneNumber();

    /**
     * 让《阿凡达》
     *
     * @return {@link String }
     * @since 2023-07-04 10:04:22
     */
    String getAvatar();

    /**
     * 获得用户名
     *
     * @return {@link String }
     * @since 2023-07-04 10:04:22
     */
    String getUserName();

    /**
     * 得到尼克名字
     *
     * @return {@link String }
     * @since 2023-07-04 10:04:22
     */
    String getNickName();
}
