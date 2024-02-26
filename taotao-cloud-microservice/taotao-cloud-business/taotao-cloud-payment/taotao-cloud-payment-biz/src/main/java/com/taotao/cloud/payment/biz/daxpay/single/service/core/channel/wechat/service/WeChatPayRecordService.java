package com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import com.taotao.cloud.payment.biz.daxpay.single.service.code.WechatPayRecordTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.dao.WeChatPayRecordManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.entity.WeChatPayRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayChannelOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.refund.entity.RefundChannelOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.refund.entity.RefundOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.wechat.WeChatPayRecordDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.wechat.WeChatPayRecordQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author xxm
 * @since 2024/2/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayRecordService {
    private final WeChatPayRecordManager weChatPayRecordManager;

    /**
     * 支付
     */
    public void pay(PayOrder order, PayChannelOrder channelOrder){
        WeChatPayRecord weChatPayRecord = new WeChatPayRecord()
                .setType(WechatPayRecordTypeEnum.PAY.getCode())
                .setTitle(order.getTitle())
                .setOrderId(order.getId())
                .setGatewayOrderNo(order.getGatewayOrderNo())
                .setAmount(channelOrder.getAmount());
        weChatPayRecordManager.save(weChatPayRecord);
    }

    /**
     * 退款
     */
    public void refund(RefundOrder order, RefundChannelOrder channelOrder){
        WeChatPayRecord weChatPayRecord = new WeChatPayRecord()
                .setType(WechatPayRecordTypeEnum.REFUND.getCode())
                .setTitle(order.getTitle())
                .setOrderId(order.getId())
                .setGatewayOrderNo(order.getGatewayOrderNo())
                .setAmount(channelOrder.getAmount());
        weChatPayRecordManager.save(weChatPayRecord);
    }

    /**
     * 分页
     */
    public PageResult<WeChatPayRecordDto> page(PageParam pageParam, WeChatPayRecordQuery param){
        return MpUtil.convert2DtoPageResult(weChatPayRecordManager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public WeChatPayRecordDto findById(Long id){
        return weChatPayRecordManager.findById(id).map(WeChatPayRecord::toDto).orElseThrow(DataNotExistException::new);
    }


}
