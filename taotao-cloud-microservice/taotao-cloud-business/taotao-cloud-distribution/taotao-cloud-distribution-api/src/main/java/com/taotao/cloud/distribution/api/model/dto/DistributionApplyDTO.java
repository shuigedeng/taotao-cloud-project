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

package com.taotao.cloud.distribution.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/** 分销员申请DTO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分销员申请DTO")
public class DistributionApplyDTO {

    @Serial
    private static final long serialVersionUID = 7814832369110695758L;

    @NotBlank(message = "姓名不能为空")
    @Schema(description = "会员姓名")
    private String name;

    @NotBlank(message = "身份证号不能为空")
    @Schema(description = "身份证号")
    private String idNumber;

    @Length(min = 1, max = 200, message = "结算银行开户行名称长度为1-200位")
    @NotBlank(message = "结算银行开户行名称不能为空")
    @Schema(description = "结算银行开户行名称")
    private String settlementBankAccountName;

    @Length(min = 1, max = 200, message = "结算银行开户账号长度为1-200位")
    @NotBlank(message = "结算银行开户账号不能为空")
    @Schema(description = "结算银行开户账号")
    private String settlementBankAccountNum;

    @Length(min = 1, max = 200, message = "结算银行开户支行名称长度为1-200位")
    @NotBlank(message = "结算银行开户支行名称不能为空")
    @Schema(description = "结算银行开户支行名称")
    private String settlementBankBranchName;
}
