package com.taotao.cloud.order.biz.service.order.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.order.api.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.api.dto.order.ReceiptSearchParams;
import com.taotao.cloud.order.biz.entity.order.Receipt;
import com.taotao.cloud.order.biz.mapper.order.ReceiptMapper;
import com.taotao.cloud.order.biz.service.order.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 发票业务层实现
 */
@AllArgsConstructor
@Service
public class ReceiptServiceImpl extends ServiceImpl<ReceiptMapper, Receipt> implements
	ReceiptService {

    @Override
    public IPage<OrderReceiptDTO> getReceiptData(ReceiptSearchParams searchParams, PageVO pageVO) {
        return this.baseMapper.getReceipt(PageUtil.initPage(pageVO), searchParams.wrapper());
    }

    @Override
    public Receipt getByOrderSn(String orderSn) {
        LambdaQueryWrapper<Receipt> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(Receipt::getOrderSn, orderSn);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public Receipt getDetail(String id) {
        return this.getById(id);
    }

    @Override
    public Receipt saveReceipt(Receipt receipt) {
        LambdaQueryWrapper<Receipt> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Receipt::getReceiptTitle, receipt.getReceiptTitle());
        queryWrapper.eq(Receipt::getMemberId, receipt.getMemberId());
        if (receipt.getId() != null) {
            queryWrapper.ne(Receipt::getId, receipt.getId());
        }
        if (this.getOne(queryWrapper) == null) {
            this.save(receipt);
            return receipt;
        }
        return null;
    }

    @Override
    public Receipt invoicing(Long receiptId) {
        //根据id查询发票信息
        Receipt receipt = this.getById(receiptId);
        if (receipt != null) {
            receipt.setReceiptStatus(1);
            this.saveOrUpdate(receipt);
            return receipt;
        }
        throw new BusinessException(ResultEnum.USER_RECEIPT_NOT_EXIST);
    }
}
