package com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.allocation;

import com.github.binarywang.wxpay.bean.profitsharing.request.ProfitSharingReceiverV3Request;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.ProfitSharingService;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.entity.config.WechatPayConfig;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.config.WechatPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.core.enums.AllocReceiverTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.core.exception.ConfigErrorException;
import com.taotao.cloud.payment.biz.daxpay.core.exception.OperationFailException;
import com.taotao.cloud.payment.biz.daxpay.service.entity.allocation.receiver.AllocReceiver;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

import static com.taotao.cloud.payment.biz.daxpay.core.enums.AllocReceiverTypeEnum.MERCHANT_NO;
import static com.taotao.cloud.payment.biz.daxpay.core.enums.AllocReceiverTypeEnum.OPEN_ID;

/**
 * 微信分账接收方服务
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayAllocReceiverV3Service {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 绑定
     */
    public void bind(AllocReceiver allocReceiver, WechatPayConfig config){
        AllocReceiverTypeEnum receiverTypeEnum = AllocReceiverTypeEnum.findByCode(allocReceiver.getReceiverType());
        String receiverType = this.getReceiverType(receiverTypeEnum);

        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();

        ProfitSharingReceiverV3Request request = new ProfitSharingReceiverV3Request();
        request.setAppid(config.getWxAppId());
        request.setType(receiverType);
        request.setAccount(allocReceiver.getReceiverAccount());
        request.setName(allocReceiver.getReceiverName());
        request.setRelationType(getRelationType(allocReceiver.getRelationType()));
        request.setCustomRelation(allocReceiver.getRelationName());
        try {
            sharingService.addReceiverV3(request);
        } catch (WxPayException e) {
            throw new OperationFailException("微信添加分账方V3失败: "+e.getMessage());
        }
    }

    /**
     * 解除绑定
     */
    public void unbind(AllocReceiver allocReceiver, WechatPayConfig config){
        AllocReceiverTypeEnum receiverTypeEnum = AllocReceiverTypeEnum.findByCode(allocReceiver.getReceiverType());
        String receiverType = this.getReceiverType(receiverTypeEnum);

        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();

        ProfitSharingReceiverV3Request request = new ProfitSharingReceiverV3Request();
        request.setAppid(config.getAppId());
        request.setType(receiverType);
        request.setAccount(allocReceiver.getReceiverAccount());
        request.setName(allocReceiver.getReceiverName());
        request.setRelationType(getRelationType(allocReceiver.getRelationType()));
        request.setCustomRelation(allocReceiver.getRelationName());
        try {
            sharingService.removeReceiverV3(request);
        } catch (WxPayException e) {
            throw new OperationFailException("微信添加分账方V3失败: "+e.getMessage());
        }
    }

    /**
     * 获取分账接收方类型编码
     */
    private String getReceiverType(AllocReceiverTypeEnum receiverTypeEnum){
        if (receiverTypeEnum == OPEN_ID){
            return "PERSONAL_OPENID";
        }
        if (receiverTypeEnum == MERCHANT_NO){
            return "MERCHANT_ID";
        }
        throw new ConfigErrorException("分账接收方类型错误");
    }
    /**
     * 获取分账关系类型编码
     */
    private String getRelationType(String relationType){
        if (Objects.isNull(relationType)){
            return null;
        }
        return relationType.toUpperCase(Locale.ROOT);
    }
}
