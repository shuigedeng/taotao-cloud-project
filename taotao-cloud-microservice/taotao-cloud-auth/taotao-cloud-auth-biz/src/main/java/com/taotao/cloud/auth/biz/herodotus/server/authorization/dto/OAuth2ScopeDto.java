/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.herodotus.server.authorization.dto;

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Description: OAuth2 Scope Dto </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/4/1 13:55
 */
@Schema(name = "OAuth2 范围请求 Dto")
public class OAuth2ScopeDto {

    @Schema(name = "范围ID")
    @NotNull(message = "范围ID不能为空")
    private String scopeId;

    @Schema(name = "范围权限列表")
    private Set<OAuth2AuthorityDto> authorities = new HashSet<>();

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public Set<OAuth2AuthorityDto> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<OAuth2AuthorityDto> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("scopeId", scopeId)
                .toString();
    }
}
