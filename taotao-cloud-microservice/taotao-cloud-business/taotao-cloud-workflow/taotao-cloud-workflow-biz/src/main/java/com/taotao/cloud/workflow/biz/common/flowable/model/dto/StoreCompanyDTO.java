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

package com.taotao.cloud.workflow.biz.common.flowable.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 店铺-公司信息
 *
 * @since 2020/12/7 15:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺-公司信息")
public class StoreCompanyDTO {

    // 公司基本信息

    @Size(min = 2, max = 100)
    @NotBlank(message = "公司名称不能为空")
    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "公司地址地区Id")
    private String companyAddressIdPath;

    @Schema(description = "公司地址地区")
    private String companyAddressPath;

    @Size(min = 1, max = 200)
    @NotBlank(message = "公司地址不能为空")
    @Schema(description = "公司地址")
    private String companyAddress;

    // @Mobile
    @Schema(description = "公司电话")
    private String companyPhone;

    @Email
    @Schema(description = "电子邮箱")
    private String companyEmail;

    @Min(1)
    @Schema(description = "员工总数")
    private Integer employeeNum;

    @Min(1)
    @Schema(description = "注册资金")
    private BigDecimal registeredCapital;

    @Length(min = 2, max = 20)
    @NotBlank(message = "联系人姓名为空")
    @Schema(description = "联系人姓名")
    private String linkName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    @Schema(description = "联系人电话")
    private String linkPhone;

    // 营业执照信息

    @Size(min = 18, max = 18)
    @Schema(description = "营业执照号")
    private String licenseNum;

    @Size(min = 1, max = 200)
    @Schema(description = "法定经营范围")
    private String scope;

    @NotBlank(message = "营业执照电子版不能为空")
    @Schema(description = "营业执照电子版")
    private String licencePhoto;

    // 法人信息

    @Size(min = 2, max = 20)
    @NotBlank(message = "法人姓名不能为空")
    @Schema(description = "法人姓名")
    private String legalName;

    @Size(min = 18, max = 18)
    @NotBlank(message = "法人身份证不能为空")
    @Schema(description = "法人身份证")
    private String legalId;

    @NotBlank(message = "法人身份证不能为空")
    @Schema(description = "法人身份证照片")
    private String legalPhoto;
}
