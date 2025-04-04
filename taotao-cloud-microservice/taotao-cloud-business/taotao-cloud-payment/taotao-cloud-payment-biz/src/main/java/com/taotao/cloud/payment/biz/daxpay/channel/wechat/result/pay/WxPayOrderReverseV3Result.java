package com.taotao.cloud.payment.biz.daxpay.channel.wechat.result.pay;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

/**
 * 微信V3撤销支付订单返回结果
 * @author xxm
 * @since 2024/7/24
 */
@Data
@Accessors(chain = true)
public class WxPayOrderReverseV3Result {
    /**
     * <pre>
     * 字段名：应用ID
     * 变量名：appid
     * 是否必填：是
     * 类型：string[1,32]
     * 描述：
     *  由微信生成的应用ID，全局唯一。请求统一下单接口时请注意APPID的应用属性，例如公众号场景下，需使用应用属性为公众号的APPID
     *  示例值：wxd678efh567hg6787
     * </pre>
     */
    @SerializedName(value = "appid")
    protected String appid;
    /**
     * <pre>
     * 字段名：直连商户号
     * 变量名：mchid
     * 是否必填：是
     * 类型：string[1,32]
     * 描述：
     *  直连商户的商户号，由微信支付生成并下发。
     *  示例值：1230000109
     * </pre>
     */
    @SerializedName(value = "mchid")
    protected String mchid;
    /**
     * <pre>
     * 字段名：商户订单号
     * 变量名：out_trade_no
     * 是否必填：是
     * 类型：string[6,32]
     * 描述：
     *  商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一
     *  示例值：1217752501201407033233368018
     * </pre>
     */
    @SerializedName(value = "out_trade_no")
    protected String outTradeNo;
}
