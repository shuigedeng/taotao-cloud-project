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

package com.taotao.cloud.member.biz.model.vo;

import com.taotao.cloud.member.api.enums.PointTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员积分历史表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:26:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员积分历史VO")
public class MemberPointsHistoryPageVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7605952923416404638L;

    @Schema(description = "会员id")
    private String memberId;

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "当前积分")
    private Long point;

    @Schema(description = "消费之前积分")
    private Long beforePoint;

    @Schema(description = "变动积分")
    private Long variablePoint;

    @Schema(description = "内容")
    private String content;

    /**
     * @see PointTypeEnum
     */
    @Schema(description = "积分类型")
    private String pointType;
}
