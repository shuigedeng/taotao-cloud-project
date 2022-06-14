package com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.service;

import cn.bootx.common.core.exception.BizException;
import cn.bootx.payment.code.paymodel.WeChatPayCode;
import cn.bootx.payment.core.payment.entity.Payment;
import cn.bootx.payment.core.paymodel.wechat.entity.WeChatPayConfig;
import cn.hutool.core.util.StrUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.CloseOrderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 微信支付撤销
 * @author xxm
 * @date 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayCancelService {
    /**
     * 关闭支付
     */
    public void cancelRemote(Payment payment, WeChatPayConfig weChatPayConfig) {
        // 只有部分需要调用微信网关进行关闭
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        Map<String, String> params = CloseOrderModel.builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .out_trade_no(String.valueOf(payment.getId()))
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(wxPayApiConfig.getApiKey(), SignType.HMACSHA256);
        String xmlResult = WxPayApi.closeOrder(params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        String returnCode = result.get(WeChatPayCode.RETURN_CODE);
        String resultCode = result.get(WeChatPayCode.RESULT_CODE);
        if (!WxPayKit.codeIsOk(returnCode)||!WxPayKit.codeIsOk(resultCode)) {
            String errorMsg = result.get(WeChatPayCode.ERR_CODE_DES);
            if (StrUtil.isBlank(errorMsg)){
                errorMsg = result.get(WeChatPayCode.RETURN_MSG);
            }
            log.error("关闭订单失败 {}", errorMsg);
            throw new BizException(errorMsg);
        }
    }
}
