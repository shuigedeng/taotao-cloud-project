package com.taotao.cloud.payment.biz.daxpay.channel.alipay.strategy;

import cn.bootx.platform.core.exception.ValidationFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.service.transfer.AliPayTransferService;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.transfer.TransferParam;
import com.taotao.cloud.payment.biz.daxpay.service.bo.trade.TransferResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsTransferStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.taotao.cloud.payment.biz.daxpay.core.enums.TransferPayeeTypeEnum.*;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝转账策略
 * @author xxm
 * @since 2024/3/21
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class AliPayTransferStrategy extends AbsTransferStrategy {

    private final AliPayTransferService aliPayTransferService;

    /**
     * 策略标识
     */
     @Override
    public String getChannel() {
        return ChannelEnum.ALI.getCode();
    }

    @Override
    public void doValidateParam(TransferParam transferParam) {
        // 转账接收方类型校验
        String payeeType = transferParam.getPayeeType();
        if (!List.of(USER_ID.getCode(), OPEN_ID.getCode(), LOGIN_NAME.getCode()).contains(payeeType)){
            throw new ValidationFailedException("支付宝不支持该类型收款人");
        }
    }

    /**
     * 转账操作
     */
    @Override
    public TransferResultBo doTransferHandler() {
        return aliPayTransferService.transfer(this.getTransferOrder());
    }
}
