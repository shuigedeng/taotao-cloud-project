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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.application.service.purchase.IPurchaseOrderItemService;
import com.taotao.cloud.order.application.service.purchase.IPurchaseOrderService;
import com.taotao.cloud.order.infrastructure.persistent.mapper.purchase.IPurchaseOrderMapper;
import com.taotao.cloud.order.infrastructure.persistent.po.purchase.PurchaseOrderPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 采购单业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:40
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<IPurchaseOrderMapper, PurchaseOrderPO>
        implements IPurchaseOrderService {

    @Autowired
    private IPurchaseOrderItemService purchaseOrderItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseOrderVO addPurchaseOrder(PurchaseOrderVO purchaseOrderVO) {
        PurchaseOrderPO purchaseOrderPO = new PurchaseOrderPO();
        BeanUtil.copyProperties(purchaseOrderVO, purchaseOrderPO);
        // 添加采购单
        purchaseOrderPO.setStatus("OPEN");
        purchaseOrderPO.setMemberId(UserContext.getCurrentUser().getId());
        this.save(purchaseOrderPO);
        // 添加采购单子内容
        purchaseOrderItemService.addPurchaseOrderItem(purchaseOrderPO.getId(), purchaseOrderVO.getPurchaseOrderItems());
        return purchaseOrderVO;
    }

    @Override
    public PurchaseOrderVO getPurchaseOrder(String id) {
        PurchaseOrderVO purchaseOrderVO = new PurchaseOrderVO();
        // 获取采购单内容
        PurchaseOrderPO purchaseOrderPO = this.getById(id);
        BeanUtil.copyProperties(purchaseOrderPO, purchaseOrderVO);

        // 获取采购单子内容
        purchaseOrderVO.setPurchaseOrderItems(purchaseOrderItemService.list(
                new LambdaQueryWrapper<PurchaseOrderItem>().eq(PurchaseOrderItem::getPurchaseOrderId, id)));
        return purchaseOrderVO;
    }

    @Override
    public IPage<PurchaseOrderPO> page(PurchaseOrderSearchParams purchaseOrderSearchParams) {

        LambdaQueryWrapper<PurchaseOrderPO> lambdaQueryWrapper = Wrappers.lambdaQuery();

        lambdaQueryWrapper.eq(
                purchaseOrderSearchParams.getMemberId() != null,
                PurchaseOrderPO::getMemberId,
                purchaseOrderSearchParams.getMemberId());
        lambdaQueryWrapper.eq(
                purchaseOrderSearchParams.getCategoryId() != null,
                PurchaseOrderPO::getCategoryId,
                purchaseOrderSearchParams.getCategoryId());
        lambdaQueryWrapper.eq(
                purchaseOrderSearchParams.getStatus() != null,
                PurchaseOrderPO::getStatus,
                purchaseOrderSearchParams.getStatus());
        lambdaQueryWrapper.orderByDesc(PurchaseOrderPO::getCreateTime);
        return this.page(PageUtil.initPage(purchaseOrderSearchParams), lambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean close(String id) {
        PurchaseOrderPO purchaseOrderPO = this.getById(id);
        purchaseOrderPO.setStatus("CLOSE");

        UpdateWrapper<PurchaseOrderPO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("status", "CLOSE");

        return this.update(updateWrapper);
    }
}
