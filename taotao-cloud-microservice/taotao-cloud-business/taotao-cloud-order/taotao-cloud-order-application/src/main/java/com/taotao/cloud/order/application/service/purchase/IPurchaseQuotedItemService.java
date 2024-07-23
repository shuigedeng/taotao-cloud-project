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
import com.taotao.cloud.order.infrastructure.persistent.po.purchase.PurchaseQuotedItemPO;
import java.util.List;

/**
 * 采购单子内容业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:32
 */
public interface IPurchaseQuotedItemService extends IService<PurchaseQuotedItemPO> {

    /**
     * 添加报价单子内容
     *
     * @param PurchaseQuotedId 采购单ID
     * @param purchaseQuotedItemPOList 采购单子内容列表
     * @return boolean
     * @since 2022-04-28 08:55:32
     */
    boolean addPurchaseQuotedItem(String PurchaseQuotedId, List<PurchaseQuotedItemPO> purchaseQuotedItemPOList);

    /**
     * 获取报价单子内容列表
     *
     * @param purchaseQuotedId 报价单ID
     * @return {@link List }<{@link PurchaseQuotedItemPO }>
     * @since 2022-04-28 08:55:32
     */
    List<PurchaseQuotedItemPO> purchaseQuotedItemList(String purchaseQuotedId);
}
