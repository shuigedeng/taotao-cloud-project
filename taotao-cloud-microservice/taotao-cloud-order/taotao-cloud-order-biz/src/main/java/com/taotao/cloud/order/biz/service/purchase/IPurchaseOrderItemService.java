package com.taotao.cloud.order.biz.service.purchase;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.biz.entity.purchase.PurchaseOrderItem;

import java.util.List;

/**
 * 采购单子内容业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:27
 */
public interface IPurchaseOrderItemService extends IService<PurchaseOrderItem> {

	/**
	 * 添加采购单子内容
	 *
	 * @param purchaseOrderId       采购单ID
	 * @param purchaseOrderItemList 采购单子内容列表
	 * @return boolean
	 * @since 2022-04-28 08:55:27
	 */
	boolean addPurchaseOrderItem(String purchaseOrderId, List<PurchaseOrderItem> purchaseOrderItemList);

}
