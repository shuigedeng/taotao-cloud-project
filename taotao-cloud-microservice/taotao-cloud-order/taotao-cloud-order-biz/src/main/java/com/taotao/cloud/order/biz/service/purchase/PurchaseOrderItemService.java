package com.taotao.cloud.order.biz.service.purchase;


import com.baomidou.mybatisplus.extension.service.IService;

import com.taotao.cloud.order.biz.entity.purchase.PurchaseOrderItem;
import java.util.List;

/**
 * 采购单子内容业务层
 */
public interface PurchaseOrderItemService extends IService<PurchaseOrderItem> {

    /**
     * 添加采购单子内容
     *
     * @param purchaseOrderId       采购单ID
     * @param purchaseOrderItemList 采购单子内容列表
     * @return 操作结果
     */
    boolean addPurchaseOrderItem(String purchaseOrderId, List<PurchaseOrderItem> purchaseOrderItemList);

}
