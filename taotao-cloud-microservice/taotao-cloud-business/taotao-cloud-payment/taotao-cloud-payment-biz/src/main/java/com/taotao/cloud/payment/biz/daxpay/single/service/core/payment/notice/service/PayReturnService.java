package com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.notice.service;

import com.taotao.cloud.payment.biz.daxpay.single.service.configuration.DaxPayProperties;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.dao.PayOrderExtraManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrderExtra;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.system.config.service.PlatformConfigService;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.alipay.AliPayReturnParam;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付同步跳转服务类
 * @author xxm
 * @since 2024/2/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayReturnService {
    private final PayOrderQueryService payOrderQueryService;
    private final PayOrderExtraManager payOrderExtraManager;
    private final PlatformConfigService platformConfigService;

    private final DaxPayProperties properties;

    /**
     * 支付宝同步回调
     */
    public String alipay(AliPayReturnParam param){
        PayOrderExtra payOrderExtra = payOrderExtraManager.findById(param.getOut_trade_no()).orElse(null);
        PayOrder prOrder = payOrderQueryService.findById(param.getOut_trade_no()).orElse(null);
        if (Objects.isNull(payOrderExtra) || Objects.isNull(prOrder)){
            return StrUtil.format("{}/result/error?msg={}", properties.getFrontH5Url(), URLEncodeUtil.encode("支付订单有问题，请排查"));
        }

        // 如果同步跳转参数为空, 获取系统配置地址, 系统配置如果也为空, 则返回默认地址
        String returnUrl = payOrderExtra.getReturnUrl();
        if (StrUtil.isBlank(returnUrl)){
            returnUrl = platformConfigService.getConfig().getReturnUrl();
        }
        if (StrUtil.isNotBlank(returnUrl)){
            return StrUtil.format("{}?paymentId={}&businessNo={}", payOrderExtra.getReturnUrl(),prOrder.getId(),prOrder.getBusinessNo());
        }
        // 跳转到默认页
        return StrUtil.format("{}/result/success?msg={}", properties.getFrontH5Url(), URLEncodeUtil.encode("支付成功..."));
    }
}
