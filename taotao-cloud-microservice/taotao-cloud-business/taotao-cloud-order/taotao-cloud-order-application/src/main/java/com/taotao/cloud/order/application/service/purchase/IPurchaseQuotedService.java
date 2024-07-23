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

package com.taotao.cloud.order.application.service.purchase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.infrastructure.persistent.po.purchase.PurchaseQuotedPO;
import java.util.List;

/**
 * 采购单报价业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:34
 */
public interface IPurchaseQuotedService extends IService<PurchaseQuotedPO> {
    /**
     * 添加报价单
     *
     * @param purchaseQuotedVO 报价单
     * @return {@link PurchaseQuotedVO }
     * @since 2022-04-28 08:55:34
     */
    PurchaseQuotedVO addPurchaseQuoted(PurchaseQuotedVO purchaseQuotedVO);

    /**
     * 根据采购单获取报价单列表
     *
     * @param purchaseOrderId 采购单ID
     * @return {@link List }<{@link PurchaseQuotedPO }>
     * @since 2022-04-28 08:55:34
     */
    List<PurchaseQuotedPO> getByPurchaseOrderId(String purchaseOrderId);

    /**
     * 获取采购单VO
     *
     * @param id 采购单ID
     * @return {@link PurchaseQuotedVO }
     * @since 2022-04-28 08:55:34
     */
    PurchaseQuotedVO getById(String id);
}
