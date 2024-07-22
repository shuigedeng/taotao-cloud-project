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

package com.taotao.cloud.sys.api.feign.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 公司查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:31:52
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "公司查询对象")
public class FileQueryApiRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -4132785717179910025L;

    @Schema(description = "租户id")
    private String tenantId;

    @Schema(description = "租户密钥")
    private String tenantSecret;

    @Schema(description = "公司名称")
    private String name;

    @Schema(description = "企业全称")
    private String fullName;

    @Pattern(regexp = "^|[a-zA-Z0-9]{18}$", message = "信用代码格式错误")
    @Schema(description = "信用代码")
    private String creditCode;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "联系人")
    private String username;

    @Schema(description = "联系人手机号")
    private String phone;

    @Schema(description = "联系人地址")
    private String address;

    @Schema(description = "请求域名")
    private String domain;

    @Schema(description = "公司网址")
    private String webSite;

    @Schema(description = "所在地区")
    private String regionInfo;

    @Schema(description = "公司类型")
    private Integer type;
}
