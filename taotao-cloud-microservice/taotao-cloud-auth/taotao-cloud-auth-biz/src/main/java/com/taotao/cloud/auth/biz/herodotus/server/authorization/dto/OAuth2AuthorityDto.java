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

import cn.herodotus.engine.rest.core.definition.dto.BaseDto;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * <p>Description: OAuth2 Authority Dto </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/4/1 13:55
 */
@Schema(name = "OAuth2 权限请求 Dto")
public class OAuth2AuthorityDto extends BaseDto {

    @Schema(name = "权限ID")
    @NotNull(message = "权限ID不能为空")
    private String authorityId;

    @Schema(name = "权限代码")
    @NotNull(message = "权限代码不能为空")
    private String authorityCode;

    @Schema(name = "服务ID")
    @NotNull(message = "服务ID不能为空")
    private String serviceId;

    @Schema(name = "请求方法")
    @NotNull(message = "请求方法不能为空")
    private String requestMethod;

    @Schema(name = "请求URL")
    @NotNull(message = "请求URL不能为空")
    private String url;

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("authorityId", authorityId)
                .add("authorityCode", authorityCode)
                .add("serviceId", serviceId)
                .add("requestMethod", requestMethod)
                .add("url", url)
                .toString();
    }
}
