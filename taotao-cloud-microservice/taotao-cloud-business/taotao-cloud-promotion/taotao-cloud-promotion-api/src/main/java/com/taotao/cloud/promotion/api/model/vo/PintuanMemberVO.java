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

package com.taotao.cloud.promotion.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 拼图会员视图对象 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PintuanMemberVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7814832369110695758L;

    @Schema(description = "会员编号")
    private Long memberId;

    @Schema(description = "会员用户名")
    private String memberName;

    @Schema(description = "会员头像")
    private String face;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "参团订单编号")
    private String orderSn;

    @Schema(description = "已参团人数")
    private Long groupedNum;

    @Schema(description = "待参团人数")
    private Long toBeGroupedNum;

    @Schema(description = "成团人数")
    private Long groupNum;

    // public PintuanMemberVO(Member member) {
    //    this.memberId = member.getId();
    //    this.memberName = member.getUsername();
    //    this.face = member.getFace();
    //    this.nickName = member.getNickName();
    // }
}
