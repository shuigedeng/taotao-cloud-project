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

package com.taotao.cloud.member.application.command.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/** 会员评价DTO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "租户id")
public class MemberEvaluationAddCmd implements Serializable {

    @Serial
    private static final long serialVersionUID = -7605952923416404638L;

    @Schema(description = "子订单编号")
    @NotEmpty(message = "订单异常")
    private String orderItemSn;

    @Schema(description = "商品ID")
    @NotEmpty(message = "订单商品异常不能为空")
    private Long goodsId;

    @Schema(description = "规格ID")
    @NotEmpty(message = "订单商品不能为空")
    private Long skuId;

    @Schema(description = "好中差评价")
    @NotEmpty(message = "请评价")
    private String grade;

    @Schema(description = "评论内容")
    @NotEmpty(message = "评论内容不能为空")
    @Length(max = 500, message = "评论内容不能超过500字符")
    private String content;

    @Schema(description = "评论图片")
    private String images;

    @Schema(description = "物流评分")
    private Integer deliveryScore;

    @Schema(description = "服务评分")
    private Integer serviceScore;

    @Schema(description = "描述评分")
    private Integer descriptionScore;
}
