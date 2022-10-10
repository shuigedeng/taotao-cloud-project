package com.taotao.cloud.order.biz.service.business.purchase.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.biz.mapper.purchase.IPurchaseOrderItemMapper;
import com.taotao.cloud.order.biz.model.entity.purchase.PurchaseOrderItem;
import com.taotao.cloud.order.biz.service.business.purchase.IPurchaseOrderItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 采购单子内容业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:37
 */
@Service
public class PurchaseOrderItemServiceImpl extends ServiceImpl<IPurchaseOrderItemMapper, PurchaseOrderItem> implements
	IPurchaseOrderItemService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addPurchaseOrderItem(String purchaseOrderId, List<PurchaseOrderItem> purchaseOrderItemList) {
		//添加采购单子内容
		for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
			purchaseOrderItem.setPurchaseOrderId(purchaseOrderId);
			this.save(purchaseOrderItem);
		}
		return true;
	}
}
