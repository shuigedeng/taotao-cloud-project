package com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import com.taotao.cloud.payment.biz.daxpay.single.service.code.AliPayRecordTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.alipay.dao.AliPayRecordManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.alipay.entity.AliPayRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayChannelOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.refund.entity.RefundChannelOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.refund.entity.RefundOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.alipay.AliPayRecordDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.alipay.AliPayRecordQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付宝流水
 * @author xxm
 * @since 2024/2/1+9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayRecordService {

    private final AliPayRecordManager aliPayRecordManager;

    /**
     * 支付
     */
    public void pay(PayOrder order, PayChannelOrder channelOrder){
        AliPayRecord aliPayRecord = new AliPayRecord()
                .setType(AliPayRecordTypeEnum.PAY.getCode())
                .setTitle(order.getTitle())
                .setOrderId(order.getId())
                .setGatewayOrderNo(order.getGatewayOrderNo())
                .setAmount(channelOrder.getAmount());
        aliPayRecordManager.save(aliPayRecord);
    }

    /**
     * 退款
     */
    public void refund(RefundOrder order, RefundChannelOrder channelOrder){
        AliPayRecord aliPayRecord = new AliPayRecord()
                .setType(AliPayRecordTypeEnum.REFUND.getCode())
                .setTitle(order.getTitle())
                .setOrderId(order.getId())
                .setGatewayOrderNo(order.getGatewayOrderNo())
                .setAmount(channelOrder.getAmount());
        aliPayRecordManager.save(aliPayRecord);
    }

    /**
     * 分页
     */
    public PageResult<AliPayRecordDto> page(PageParam pageParam, AliPayRecordQuery param){
        return MpUtil.convert2DtoPageResult(aliPayRecordManager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public AliPayRecordDto findById(Long id){
        return aliPayRecordManager.findById(id).map(AliPayRecord::toDto).orElseThrow(DataNotExistException::new);
    }
}
