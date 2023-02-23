package com.taotao.cloud.order.biz.service.business.purchase.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.biz.mapper.purchase.IPurchaseQuotedItemMapper;
import com.taotao.cloud.order.biz.model.entity.purchase.PurchaseQuotedItem;
import com.taotao.cloud.order.biz.service.business.purchase.IPurchaseQuotedItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 采购单子内容业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:42
 */
@Service
public class PurchaseQuotedItemServiceImpl extends ServiceImpl<IPurchaseQuotedItemMapper, PurchaseQuotedItem> implements
	IPurchaseQuotedItemService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addPurchaseQuotedItem(String purchaseQuotedId, List<PurchaseQuotedItem> purchaseQuotedItemList) {
		for (PurchaseQuotedItem purchaseQuotedItem : purchaseQuotedItemList) {
			purchaseQuotedItem.setPurchaseQuotedId(purchaseQuotedId);
		}

		return this.saveBatch(purchaseQuotedItemList);
	}

	@Override
	public List<PurchaseQuotedItem> purchaseQuotedItemList(String purchaseQuotedId) {
		return this.list(new LambdaQueryWrapper<PurchaseQuotedItem>().eq(PurchaseQuotedItem::getPurchaseQuotedId, purchaseQuotedId));
	}
}
