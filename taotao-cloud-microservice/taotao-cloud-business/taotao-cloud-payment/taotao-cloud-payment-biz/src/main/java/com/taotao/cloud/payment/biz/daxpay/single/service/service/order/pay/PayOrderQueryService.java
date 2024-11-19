package com.taotao.cloud.payment.biz.daxpay.single.service.service.order.pay;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import com.taotao.cloud.payment.biz.daxpay.core.exception.TradeNotExistException;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.pay.QueryPayParam;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.pay.PayOrderResult;
import com.taotao.cloud.payment.biz.daxpay.service.convert.order.pay.PayOrderConvert;
import com.taotao.cloud.payment.biz.daxpay.service.dao.order.pay.PayOrderManager;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.pay.PayOrder;
import com.taotao.cloud.payment.biz.daxpay.service.param.order.pay.PayOrderQuery;
import com.taotao.cloud.payment.biz.daxpay.service.result.order.pay.PayOrderVo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * 支付查询服务
 * @author xxm
 * @since 2024/1/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderQueryService {
    private final PayOrderManager payOrderManager;

    /**
     * 分页
     */
    public PageResult<PayOrderVo> page(PageParam pageParam, PayOrderQuery param) {
        Page<PayOrder> page = payOrderManager.page(pageParam, param);
        return MpUtil.toPageResult(page);
    }

    /**
     * 根据id查询
     */
    public Optional<PayOrder> findById(Long orderId) {
        return payOrderManager.findById(orderId);
    }

    /**
     * 根据订单号查询
     */
    public Optional<PayOrder> findByOrderNo(String orderNo) {
        return payOrderManager.findByOrderNo(orderNo);
    }

    /**
     * 根据商户订单号查询
     */
    public Optional<PayOrder> findByBizOrderNo(String bizOrderNo, String appId) {
        return payOrderManager.findByBizOrderNo(bizOrderNo, appId);
    }

    /**
     * 根据订单号或商户订单号查询
     */
    public Optional<PayOrder> findByBizOrOrderNo(String orderNo, String bizOrderNo, String appId) {
        if (Objects.nonNull(orderNo)){
            return this.findByOrderNo(orderNo);
        }
        if (Objects.nonNull(bizOrderNo)){
            return this.findByBizOrderNo(bizOrderNo,appId);
        }
        return Optional.empty();
    }

    /**
     * 查询支付记录
     */
    public PayOrderResult queryPayOrder(QueryPayParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getBizOrderNoeNo()) && Objects.isNull(param.getOrderNo())){
            throw new ValidationFailedException("业务号或支付单ID不能都为空");
        }
        // 查询支付单
        return this.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNoeNo(),param.getAppId())
                .map(PayOrderConvert.CONVERT::toResult)
                .orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
    }


    /**
     * 查询支付总金额
     */
    public BigDecimal getTotalAmount(PayOrderQuery param) {
        return payOrderManager.getTotalAmount(param);
    }
}
