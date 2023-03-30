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

package com.taotao.cloud.payment.biz.bootx.core.notify.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.payment.biz.bootx.core.notify.convert.PayNotifyConvert;
import com.taotao.cloud.payment.biz.bootx.dto.notify.PayNotifyRecordDto;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 回调记录
 *
 * @author xxm
 * @date 2021/6/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_pay_notify_record")
public class PayNotifyRecord extends MpBaseEntity
        implements EntityBaseFunction<PayNotifyRecordDto> {

    /** 支付记录id */
    private Long paymentId;
    /**
     * 支付通道
     *
     * @see PayChannelCode
     */
    private int payChannel;
    /** 通知消息 */
    private String notifyInfo;
    /**
     * 处理状态
     *
     * @see PayStatusCode#NOTIFY_PROCESS_SUCCESS
     */
    private int status;
    /** 提示信息 */
    private String msg;
    /** 回调时间 */
    private LocalDateTime notifyTime;

    @Override
    public PayNotifyRecordDto toDto() {
        return PayNotifyConvert.CONVERT.convert(this);
    }
}
