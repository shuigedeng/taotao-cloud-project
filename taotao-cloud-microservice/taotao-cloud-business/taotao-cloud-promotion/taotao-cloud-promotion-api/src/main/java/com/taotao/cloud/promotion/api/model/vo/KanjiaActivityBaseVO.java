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

import com.taotao.cloud.promotion.api.enums.KanJiaStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 砍价活动参与实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:57
 */
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityBaseVO implements Serializable {

    /** 砍价商品id */
    private Long kanjiaActivityGoodsId;
    /** 发起砍价活动会员id */
    private Long memberId;
    /** 发起砍价活动会员名称 */
    private String memberName;
    /** 剩余购买金额 */
    private BigDecimal surplusPrice;
    /** 砍价最低购买金额 */
    private BigDecimal purchasePrice;
    /** 砍价商品skuId */
    private Long skuId;
    /** 货品名称 */
    private String goodsName;
    /** 缩略图 */
    private String thumbnail;

    /**
     * 砍价活动状态
     *
     * @see KanJiaStatusEnum
     */
    private String status;
}
