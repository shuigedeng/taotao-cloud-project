/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.payment.biz.bootx.dto.payment;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @date 2020/12/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付记录")
public class PaymentDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 3269223993950227228L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "关联的业务id")
    private String businessId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "是否是异步支付")
    private boolean asyncPayMode;

    /**
     * @see cn.bootx.payment.code.pay.PayChannelCode
     */
    @Schema(description = "异步支付通道")
    private Integer asyncPayChannel;

    /**
     * @see cn.bootx.payment.code.pay.PayStatusCode
     */
    @Schema(description = "支付状态")
    private int payStatus;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "可退款余额")
    private BigDecimal refundableBalance;

    @Schema(description = "错误码")
    private String errorCode;

    @Schema(description = "错误信息")
    private String errorMsg;

    /**
     * @see PayChannelInfo
     */
    @Schema(description = "支付通道信息")
    private String payChannelInfo;

    /**
     * @see cn.bootx.payment.dto.payment.RefundableInfo
     */
    @Schema(description = "可退款信息列表")
    private String refundableInfo;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "支付终端ip")
    private String clientIp;

    @Schema(description = "过期时间")
    private LocalDateTime expiredTime;
    /** 获取支付通道 */
    public List<PayChannelInfo> getPayChannelInfoList() {
        if (StrUtil.isNotBlank(this.payChannelInfo)) {
            JSONArray array = JSONUtil.parseArray(this.payChannelInfo);
            return JSONUtil.toList(array, PayChannelInfo.class);
        }
        return new ArrayList<>(0);
    }

    /** 获取可退款信息列表 */
    public List<RefundableInfo> getRefundableInfoList() {
        if (StrUtil.isNotBlank(this.refundableInfo)) {
            JSONArray array = JSONUtil.parseArray(this.refundableInfo);
            return JSONUtil.toList(array, RefundableInfo.class);
        }
        return new ArrayList<>(0);
    }
}
