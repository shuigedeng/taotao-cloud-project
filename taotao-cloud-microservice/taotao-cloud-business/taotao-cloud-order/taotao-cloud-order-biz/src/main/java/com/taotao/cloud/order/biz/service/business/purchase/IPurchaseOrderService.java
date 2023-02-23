package com.taotao.cloud.order.biz.service.business.purchase;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.biz.model.entity.purchase.PurchaseOrder;

/**
 * 采购单业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:29
 */
public interface IPurchaseOrderService extends IService<PurchaseOrder> {

	/**
	 * 添加采购单
	 *
	 * @param purchaseOrderVO 采购单
	 * @return {@link PurchaseOrderVO }
	 * @since 2022-04-28 08:55:29
	 */
	PurchaseOrderVO addPurchaseOrder(PurchaseOrderVO purchaseOrderVO);

	/**
	 * 根据ID获取采购单
	 *
	 * @param id 采购单ID
	 * @return {@link PurchaseOrderVO }
	 * @since 2022-04-28 08:55:29
	 */
	PurchaseOrderVO getPurchaseOrder(String id);

	/**
	 * 获取采购单分页列表
	 *
	 * @param purchaseOrderSearchParams 查询参数
	 * @return {@link IPage }<{@link PurchaseOrder }>
	 * @since 2022-04-28 08:55:29
	 */
	IPage<PurchaseOrder> page(PurchaseOrderSearchParams purchaseOrderSearchParams);

	/**
	 * 关闭供求单
	 *
	 * @param id 供求单ID
	 * @return boolean
	 * @since 2022-04-28 08:55:29
	 */
	boolean close(String id);

}
