package com.taotao.cloud.order.biz.service.purchase.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.order.biz.entity.purchase.PurchaseQuoted;
import com.taotao.cloud.order.biz.mapper.purchase.PurchaseQuotedMapper;
import com.taotao.cloud.order.biz.service.purchase.PurchaseQuotedItemService;
import com.taotao.cloud.order.biz.service.purchase.PurchaseQuotedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 采购单报价业务层实现
 *
 */
@Service
public class PurchaseQuotedServiceImpl extends ServiceImpl<PurchaseQuotedMapper, PurchaseQuoted> implements
	PurchaseQuotedService {
    @Autowired
    private PurchaseQuotedItemService purchaseQuotedItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseQuotedVO addPurchaseQuoted(PurchaseQuotedVO purchaseQuotedVO) {
        PurchaseQuoted purchaseQuoted = new PurchaseQuoted();
        BeanUtil.copyProperties(purchaseQuotedVO, purchaseQuoted);
        //添加报价单
        this.save(purchaseQuoted);
        //添加采购单子内容
        purchaseQuotedItemService.addPurchaseQuotedItem(purchaseQuoted.getId(), purchaseQuotedVO.getPurchaseQuotedItems());
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
        //获取报价单
        PurchaseQuotedVO purchaseQuotedVO = new PurchaseQuotedVO();
        PurchaseQuoted purchaseQuoted=this.baseMapper.selectById(id);
        BeanUtil.copyProperties(purchaseQuoted, purchaseQuotedVO);
        //获取报价单子内容
        purchaseQuotedVO.setPurchaseQuotedItems(purchaseQuotedItemService.purchaseQuotedItemList(id));
        return purchaseQuotedVO;
    }
}
