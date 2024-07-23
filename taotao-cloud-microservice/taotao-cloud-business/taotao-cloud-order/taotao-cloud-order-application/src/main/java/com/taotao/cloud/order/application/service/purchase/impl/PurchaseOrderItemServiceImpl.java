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

package com.taotao.cloud.order.application.service.purchase.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.application.service.purchase.IPurchaseOrderItemService;
import com.taotao.cloud.order.infrastructure.persistent.mapper.purchase.IPurchaseOrderItemMapper;
import com.taotao.cloud.order.infrastructure.persistent.po.purchase.PurchaseOrderItemPO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 采购单子内容业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:37
 */
@Service
public class PurchaseOrderItemServiceImpl extends
	ServiceImpl<IPurchaseOrderItemMapper, PurchaseOrderItemPO>
	implements IPurchaseOrderItemService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addPurchaseOrderItem(String purchaseOrderId,
		List<PurchaseOrderItemPO> purchaseOrderItemPOList) {
		// 添加采购单子内容
		for (PurchaseOrderItemPO purchaseOrderItemPO : purchaseOrderItemPOList) {
			purchaseOrderItemPO.setPurchaseOrderId(purchaseOrderId);
			this.save(purchaseOrderItemPO);
		}
		return true;
	}
}
