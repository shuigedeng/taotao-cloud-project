package com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.voucher.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import com.taotao.cloud.payment.biz.daxpay.single.service.code.VoucherRecordTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.voucher.dao.VoucherRecordManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.voucher.entity.Voucher;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.voucher.entity.VoucherRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayChannelOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.refund.entity.RefundChannelOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.voucher.VoucherRecordDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.voucher.VoucherRecordQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 储值卡记录
 * @author xxm
 * @since 2024/2/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherRecordService {
    private final VoucherRecordManager manager;

    /**
     * 导入保存
     */
    public void importVoucher(Voucher voucher){
        VoucherRecord voucherRecord = new VoucherRecord()
                .setTitle("导入储值卡")
                .setType(VoucherRecordTypeEnum.IMPORT.getCode())
                .setAmount(voucher.getBalance())
                .setNewAmount(voucher.getBalance())
                .setOldAmount(0)
                .setVoucherId(voucher.getId());
        manager.save(voucherRecord);
    }


    /**
     * 支付保存
     */
    public void pay(PayChannelOrder channelOrder, String title, Voucher voucher){
        VoucherRecord voucherRecord = new VoucherRecord()
                .setTitle(title)
                .setType(VoucherRecordTypeEnum.PAY.getCode())
                .setAmount(channelOrder.getAmount())
                .setNewAmount(voucher.getBalance())
                .setOldAmount(voucher.getBalance() - channelOrder.getAmount())
                .setOrderId(String.valueOf(channelOrder.getPaymentId()))
                .setVoucherId(voucher.getId());
        manager.save(voucherRecord);
    }

    /**
     * 退款保存
     */
    public void refund(RefundChannelOrder channelOrder, String title, Voucher voucher){
        VoucherRecord voucherRecord = new VoucherRecord()
                .setTitle(title)
                .setType(VoucherRecordTypeEnum.REFUND.getCode())
                .setAmount(channelOrder.getAmount())
                .setNewAmount(voucher.getBalance())
                .setOldAmount(voucher.getBalance() + channelOrder.getAmount())
                .setOrderId(String.valueOf(channelOrder.getRefundId()))
                .setVoucherId(voucher.getId());
        manager.save(voucherRecord);
    }

    /**
     * 支付关闭
     */
    public void payClose(PayChannelOrder channelOrder, String title, Voucher voucher){
        VoucherRecord voucherRecord = new VoucherRecord()
                .setTitle(title)
                .setType(VoucherRecordTypeEnum.CLOSE_PAY.getCode())
                .setAmount(channelOrder.getAmount())
                .setNewAmount(voucher.getBalance())
                .setOldAmount(voucher.getBalance() - channelOrder.getAmount())
                .setOrderId(String.valueOf(channelOrder.getPaymentId()))
                .setVoucherId(voucher.getId());
        manager.save(voucherRecord);
    }

    /**
     * 分页查询
     */
    public PageResult<VoucherRecordDto> page(PageParam pageParam, VoucherRecordQuery param) {
        return MpUtil.convert2DtoPageResult(manager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public VoucherRecordDto findById(Long id){
        return manager.findById(id).map(VoucherRecord::toDto).orElseGet(VoucherRecordDto::new);
    }
}
