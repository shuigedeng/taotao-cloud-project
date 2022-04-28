package com.taotao.cloud.order.biz.service.purchase;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.biz.entity.purchase.PurchaseQuotedItem;

import java.util.List;

/**
 * 采购单子内容业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:32
 */
public interface IPurchaseQuotedItemService extends IService<PurchaseQuotedItem> {

	/**
	 * 添加报价单子内容
	 *
	 * @param PurchaseQuotedId       采购单ID
	 * @param purchaseQuotedItemList 采购单子内容列表
	 * @return boolean
	 * @since 2022-04-28 08:55:32
	 */
	boolean addPurchaseQuotedItem(String PurchaseQuotedId, List<PurchaseQuotedItem> purchaseQuotedItemList);

	/**
	 * 获取报价单子内容列表
	 *
	 * @param purchaseQuotedId 报价单ID
	 * @return {@link List }<{@link PurchaseQuotedItem }>
	 * @since 2022-04-28 08:55:32
	 */
	List<PurchaseQuotedItem> purchaseQuotedItemList(String purchaseQuotedId);

}
