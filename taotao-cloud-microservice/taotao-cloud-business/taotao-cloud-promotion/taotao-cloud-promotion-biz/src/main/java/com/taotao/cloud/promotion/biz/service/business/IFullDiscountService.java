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

package com.taotao.cloud.promotion.biz.service.business;

import com.taotao.cloud.order.api.model.vo.cart.FullDiscountVO;
import com.taotao.cloud.promotion.biz.model.entity.FullDiscount;
import java.util.List;

/**
 * 满优惠业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:32
 */
public interface IFullDiscountService extends AbstractPromotionsService<FullDiscount> {

    /**
     * 当前满优惠活动
     *
     * @param storeId 商家编号
     * @return {@link List }<{@link FullDiscountVO }>
     * @since 2022-04-27 16:43:32
     */
    List<FullDiscountVO> currentPromotion(List<String> storeId);

    /**
     * 获取满优惠活动详情
     *
     * @param id 满优惠KID
     * @return {@link FullDiscountVO }
     * @since 2022-04-27 16:43:32
     */
    FullDiscountVO getFullDiscount(String id);
}
