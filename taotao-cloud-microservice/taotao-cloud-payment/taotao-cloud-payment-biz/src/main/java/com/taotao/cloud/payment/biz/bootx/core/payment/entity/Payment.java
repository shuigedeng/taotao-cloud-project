package com.taotao.cloud.payment.biz.bootx.core.payment.entity;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.payment.biz.bootx.core.payment.convert.PaymentConvert;
import com.taotao.cloud.payment.biz.bootx.dto.payment.PayChannelInfo;
import com.taotao.cloud.payment.biz.bootx.dto.payment.PaymentDto;
import com.taotao.cloud.payment.biz.bootx.dto.payment.RefundableInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
* 支付记录
* @author xxm
* @date 2020/12/8
*/
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_payment")
public class Payment extends MpBaseEntity implements EntityBaseFunction<PaymentDto> {

    /** 关联的业务id */
    private String businessId;

    /** 用户ID */
    private Long userId;

    /** 标题 */
    private String title;

    /** 描述 */
    private String description;

    /** 是否是异步支付 */
    private boolean asyncPayMode;

    /** 异步支付通道 */
    private Integer asyncPayChannel;

    /** 金额 */
    private BigDecimal amount;

    /** 可退款余额 */
    private BigDecimal refundableBalance;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    /**
     * 支付通道信息列表
     * @see PayChannelInfo
     */
    private String payChannelInfo;

    /**
     * 退款信息列表
     * @see RefundableInfo
     */
    private String refundableInfo;

    /**
     * 支付状态
     * @see PayStatusCode
     */
    private Integer payStatus;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 支付终端ip */
    private String clientIp;

    /** 超时时间 */
    private LocalDateTime expiredTime;

    @Override
    public PaymentDto toDto() {
        return PaymentConvert.CONVERT.convert(this);
    }

    /**
     * 获取支付通道
     */
    public List<PayChannelInfo> getPayChannelInfoList(){
        if (StrUtil.isNotBlank(this.payChannelInfo)){
            JSONArray array = JSONUtil.parseArray(this.payChannelInfo);
            return JSONUtil.toList(array, PayChannelInfo.class);
        }
        return new ArrayList<>(0);
    }

    /**
     * 获取可退款信息列表
     */
    public List<RefundableInfo> getRefundableInfoList(){
        if (StrUtil.isNotBlank(this.refundableInfo)){
            JSONArray array = JSONUtil.parseArray(this.refundableInfo);
            return JSONUtil.toList(array, RefundableInfo.class);
        }
        return new ArrayList<>(0);
    }
}
