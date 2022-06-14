package com.taotao.cloud.payment.biz.bootx.dto.pay;

import com.taotao.cloud.payment.biz.bootx.dto.payment.PayChannelInfo;
import com.taotao.cloud.payment.biz.bootx.dto.payment.RefundableInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付信息
 * @author xxm
 * @date 2022/5/6
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付信息")
public class PaymentInfo {

    @Schema(description= "用户ID")
    private Long userId;

    @Schema(description= "关联的业务id")
    private String businessId;

    @Schema(description= "标题")
    private String title;

    @Schema(description= "描述")
    private String description;

    @Schema(description= "是否是异步支付")
    private boolean asyncPayMode;

    /**
     * @see cn.bootx.payment.code.pay.PayChannelCode
     */
    @Schema(description= "异步支付通道")
    private Integer asyncPayChannel;

    /**
     * @see cn.bootx.payment.code.pay.PayStatusCode
     */
    @Schema(description= "支付状态")
    private int payStatus;

    @Schema(description= "金额")
    private BigDecimal amount;

    @Schema(description= "可退款余额")
    private BigDecimal refundableBalance;

    @Schema(description= "错误码")
    private String errorCode;

    @Schema(description= "错误信息")
    private String errorMsg;

    @Schema(description= "支付通道信息")
    private List<PayChannelInfo> payChannelInfo;

    @Schema(description= "可退款信息列表")
    private List<RefundableInfo> refundableInfo;

    @Schema(description= "支付时间")
    private LocalDateTime payTime;

    @Schema(description= "支付终端ip")
    private String clientIp;

    @Schema(description= "过期时间")
    private LocalDateTime expiredTime;
}
