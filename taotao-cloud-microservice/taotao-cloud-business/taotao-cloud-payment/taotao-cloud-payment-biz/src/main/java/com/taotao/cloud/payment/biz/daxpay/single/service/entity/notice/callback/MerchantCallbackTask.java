package com.taotao.cloud.payment.biz.daxpay.single.service.entity.notice.callback;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.taotao.cloud.payment.biz.daxpay.core.enums.TradeTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.pay.PayOrderResult;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.refund.RefundOrderResult;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.transfer.TransferOrderResult;
import com.taotao.cloud.payment.biz.daxpay.service.common.entity.MchAppBaseEntity;
import com.taotao.cloud.payment.biz.daxpay.service.convert.notice.MerchantCallbackConvert;
import com.taotao.cloud.payment.biz.daxpay.service.result.notice.callback.MerchantCallbackTaskResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 客户回调消息任务
 * @author xxm
 * @since 2024/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_merchant_callback_task")
public class MerchantCallbackTask extends MchAppBaseEntity implements ToResult<MerchantCallbackTaskResult> {

    /** 本地交易ID */
    private Long tradeId;

    /** 平台交易号 */
    private String tradeNo;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    private String tradeType;

    /**
     * 消息内容
     * @see PayOrderResult
     * @see RefundOrderResult
     * @see TransferOrderResult
     */
    private String content;

    /** 是否发送成功 */
    private boolean success;

    /** 下次发送时间 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime nextTime;

    /** 发送次数 */
    private Integer sendCount;

    /** 延迟重试次数 */
    private Integer delayCount;

    /** 发送地址 */
    private String url;

    /** 最后发送时间 */
    private LocalDateTime latestTime;

    /**
     * 转换
     */
    @Override
    public MerchantCallbackTaskResult toResult() {
        return MerchantCallbackConvert.CONVERT.toResult(this);
    }
}
