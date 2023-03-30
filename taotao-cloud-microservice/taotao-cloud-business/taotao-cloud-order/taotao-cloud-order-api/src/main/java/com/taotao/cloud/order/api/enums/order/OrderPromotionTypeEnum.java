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

package com.taotao.cloud.order.api.enums.order;

/**
 * 订单促销类型枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:22:21
 */
public enum OrderPromotionTypeEnum {

    /** 普通订单 */
    NORMAL,
    /** 赠品订单 */
    GIFT,
    /** 拼团订单 */
    PINTUAN,
    /** 积分订单 */
    POINTS,
    /** 砍价订单 */
    KANJIA
}
