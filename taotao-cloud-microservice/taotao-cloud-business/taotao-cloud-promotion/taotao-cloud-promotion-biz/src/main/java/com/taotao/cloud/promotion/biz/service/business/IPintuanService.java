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

import com.taotao.cloud.promotion.api.model.vo.PintuanMemberVO;
import com.taotao.cloud.promotion.api.model.vo.PintuanShareVO;
import com.taotao.cloud.promotion.api.model.vo.PintuanVO;
import com.taotao.cloud.promotion.biz.model.entity.Pintuan;
import java.util.List;

/**
 * 拼图活动业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:56
 */
public interface IPintuanService extends AbstractPromotionsService<Pintuan> {

    /**
     * 获取当前拼团的会员
     *
     * @param pintuanId 拼图id
     * @return {@link List }<{@link PintuanMemberVO }>
     * @since 2022-04-27 16:43:56
     */
    List<PintuanMemberVO> getPintuanMember(String pintuanId);

    /**
     * 查询拼团活动详情
     *
     * @param id 拼团ID
     * @return {@link PintuanVO }
     * @since 2022-04-27 16:43:56
     */
    PintuanVO getPintuanVO(String id);

    /**
     * 获取拼团分享信息
     *
     * @param parentOrderSn 拼团团长订单sn
     * @param skuId 商品skuId
     * @return {@link PintuanShareVO }
     * @since 2022-04-27 16:43:56
     */
    PintuanShareVO getPintuanShareInfo(String parentOrderSn, String skuId);
}
