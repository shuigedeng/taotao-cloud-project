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

package com.taotao.cloud.member.application.command.receipt.dto.clientobject;

import com.taotao.cloud.member.api.enums.MemberReceiptEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 会员发票添加VO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员发票")
public class MemberReceiptAddCO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8267092982915677995L;

    @Schema(description = "唯一标识", hidden = true)
    private String id;

    @Schema(description = "发票抬头")
    private String receiptTitle;

    @Schema(description = "纳税人识别号")
    private String taxpayerId;

    @Schema(description = "发票内容")
    private String receiptContent;

    /**
     * @see MemberReceiptEnum
     */
    @Schema(description = "发票类型")
    private String receiptType;

    @Schema(description = "是否为默认选项 0：否，1：是")
    private Integer isDefault;
}
