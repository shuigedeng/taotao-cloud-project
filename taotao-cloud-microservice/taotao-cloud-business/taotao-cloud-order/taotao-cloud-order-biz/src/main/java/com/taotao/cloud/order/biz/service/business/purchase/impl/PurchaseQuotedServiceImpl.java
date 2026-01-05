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

package com.taotao.cloud.order.biz.service.business.purchase.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.cloud.order.biz.mapper.purchase.PurchaseQuotedMapper;
import com.taotao.cloud.order.biz.model.entity.purchase.PurchaseQuoted;
import com.taotao.cloud.order.biz.service.business.purchase.PurchaseQuotedItemService;
import com.taotao.cloud.order.biz.service.business.purchase.PurchaseQuotedService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 采购单报价业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:44
 */
@Service
public class PurchaseQuotedServiceImpl extends ServiceImpl<PurchaseQuotedMapper, PurchaseQuoted>
        implements PurchaseQuotedService {

    @Autowired
    private PurchaseQuotedItemService purchaseQuotedItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseQuotedVO addPurchaseQuoted(PurchaseQuotedVO purchaseQuotedVO) {
        PurchaseQuoted purchaseQuoted = new PurchaseQuoted();
        BeanUtils.copyProperties(purchaseQuotedVO, purchaseQuoted);
        // 添加报价单
        this.save(purchaseQuoted);
        // 添加采购单子内容
        purchaseQuotedItemService.addPurchaseQuotedItem(
                purchaseQuoted.getId(), purchaseQuotedVO.getPurchaseQuotedItems());
        return purchaseQuotedVO;
    }

    @Override
    public List<PurchaseQuoted> getByPurchaseOrderId(String purchaseOrderId) {
        LambdaQueryWrapper<PurchaseQuoted> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(PurchaseQuoted::getPurchaseOrderId, purchaseOrderId);
        lambdaQueryWrapper.orderByDesc(PurchaseQuoted::getCreateTime);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public PurchaseQuotedVO getById(String id) {
        // 获取报价单
        PurchaseQuotedVO purchaseQuotedVO = new PurchaseQuotedVO();
        PurchaseQuoted purchaseQuoted = this.baseMapper.selectById(id);
        BeanUtils.copyProperties(purchaseQuoted, purchaseQuotedVO);
        // 获取报价单子内容
        purchaseQuotedVO.setPurchaseQuotedItems(purchaseQuotedItemService.purchaseQuotedItemList(id));
        return purchaseQuotedVO;
    }
}
