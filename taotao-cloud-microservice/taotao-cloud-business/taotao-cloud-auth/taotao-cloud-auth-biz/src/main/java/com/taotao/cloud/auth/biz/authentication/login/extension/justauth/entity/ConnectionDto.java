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

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * 查询本地账号下的第三方绑定账号 dto
 * @author YongWu zheng
 * @weixin z56133
 * @since 2021.3.3 20:03
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDto implements Serializable {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    /**
     * 本地主键 id
     */
    private String id;
    /**
     * 本地用户 id
     */
    private String userId;
    /**
     * 第三方服务商 id
     */
    private String providerId;
    /**
     * 第三方用户 id
     */
    private String providerUserId;

    /**
     * 第三方用户 token id
     */
    private Long tokenId;
}
