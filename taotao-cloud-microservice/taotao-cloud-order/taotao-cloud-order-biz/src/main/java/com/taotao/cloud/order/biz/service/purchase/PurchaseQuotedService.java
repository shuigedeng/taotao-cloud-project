package com.taotao.cloud.order.biz.service.purchase;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.biz.entity.purchase.PurchaseQuoted;

import java.util.List;

/**
 * 采购单报价业务层
 */
public interface PurchaseQuotedService extends IService<PurchaseQuoted> {
	/**
	 * 添加报价单
	 *
	 * @param purchaseQuotedVO 报价单
	 * @return 报价单
	 */
	PurchaseQuotedVO addPurchaseQuoted(PurchaseQuotedVO purchaseQuotedVO);

	/**
	 * 根据采购单获取报价单列表
	 *
	 * @param purchaseOrderId 采购单ID
	 * @return 报价单列表
	 */
	List<PurchaseQuoted> getByPurchaseOrderId(String purchaseOrderId);

	/**
	 * 获取采购单VO
	 *
	 * @param id 采购单ID
	 * @return 采购单VO
	 */
	PurchaseQuotedVO getById(String id);
}
