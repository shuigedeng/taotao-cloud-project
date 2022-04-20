package com.taotao.cloud.order.biz.service.purchase;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.biz.entity.purchase.PurchaseOrder;

/**
 * 采购单业务层
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {

	/**
	 * 添加采购单
	 *
	 * @param purchaseOrderVO 采购单
	 * @return 采购单
	 */
	PurchaseOrderVO addPurchaseOrder(PurchaseOrderVO purchaseOrderVO);

	/**
	 * 根据ID获取采购单
	 *
	 * @param id 采购单ID
	 * @return 采购单
	 */
	PurchaseOrderVO getPurchaseOrder(String id);

	/**
	 * 获取采购单分页列表
	 *
	 * @param purchaseOrderSearchParams 查询参数
	 * @return
	 */
	IPage<PurchaseOrder> page(PurchaseOrderSearchParams purchaseOrderSearchParams);

	/**
	 * 关闭供求单
	 *
	 * @param id 供求单ID
	 * @return 供求单
	 */
	boolean close(String id);

}
