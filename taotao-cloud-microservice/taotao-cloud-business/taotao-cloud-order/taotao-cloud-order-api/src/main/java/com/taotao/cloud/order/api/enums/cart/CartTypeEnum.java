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

package com.taotao.cloud.order.api.enums.cart;

/**
 * 购物车类型
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:21:37
 */
public enum CartTypeEnum {

    /** 购物车 */
    CART,
    /** 立即购买 */
    BUY_NOW,
    /** 虚拟商品 */
    VIRTUAL,
    /** 拼团 */
    PINTUAN,
    /** 积分 */
    POINTS,
    /** 砍价商品 */
    KANJIA;

    public String getPrefix() {
        return "{" + this.name() + "}_";
    }
}
