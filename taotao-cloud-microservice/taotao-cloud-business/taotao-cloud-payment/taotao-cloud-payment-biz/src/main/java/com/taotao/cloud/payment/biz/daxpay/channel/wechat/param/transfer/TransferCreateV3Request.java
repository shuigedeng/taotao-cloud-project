package com.taotao.cloud.payment.biz.daxpay.channel.wechat.param.transfer;

import com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateRequest;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.*;

/**
 * 微信转账到零钱操作, 添加回调参数
 * @author xxm
 * @since 2024/7/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TransferCreateV3Request extends TransferCreateRequest {

    /** 通知地址 说明：通知地址 */
    @SerializedName("notify_url")
    private String notifyUrl;
}
