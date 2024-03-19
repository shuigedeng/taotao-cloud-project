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

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 会员评价VO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员评价VO")
public class MemberEvaluationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6696978796248845481L;

    @Schema(description = "会员ID")
    private String memberId;

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "会员头像")
    private String memberProfile;

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "商品ID")
    private String goodsId;

    @Schema(description = "SKU_ID")
    private String skuId;

    @Schema(description = "会员ID")
    private String goodsName;

    @Schema(description = "商品图片")
    private String goodsImage;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "好中差评 , GOOD：好评，MODERATE：中评，WORSE：差评")
    private String grade;

    @Schema(description = "评价内容")
    private String content;

    @Schema(description = "评价图片 逗号分割")
    private String images;

    @Schema(description = "状态  OPEN 正常 ,CLOSE 关闭")
    private String status;

    @Schema(description = "评论图片")
    private String reply;

    @Schema(description = "评价回复图片")
    private String replyImage;

    @Schema(description = "评论是否有图片 true 有 ,false 没有")
    private Boolean haveImage;

    @Schema(description = "回复是否有图片 true 有 ,false 没有")
    private Boolean haveReplyImage;

    @Schema(description = "回复状态")
    private Boolean replyStatus;

    @Schema(description = "物流评分")
    private Integer deliveryScore;

    @Schema(description = "服务评分")
    private Integer serviceScore;

    @Schema(description = "描述评分")
    private Integer descriptionScore;

    @Schema(description = "评论图片")
    private List<String> evaluationImages;

    @Schema(description = "回复评论图片")
    private List<String> replyEvaluationImages;
}
