package com.taotao.cloud.order.biz.service.purchase.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.biz.entity.purchase.PurchaseOrderItem;
import com.taotao.cloud.order.biz.mapper.purchase.PurchaseOrderItemMapper;
import com.taotao.cloud.order.biz.service.purchase.PurchaseOrderItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 采购单子内容业务层实现
 *
 */
@Service
public class PurchaseOrderItemServiceImpl extends ServiceImpl<PurchaseOrderItemMapper, PurchaseOrderItem> implements
	PurchaseOrderItemService {

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
