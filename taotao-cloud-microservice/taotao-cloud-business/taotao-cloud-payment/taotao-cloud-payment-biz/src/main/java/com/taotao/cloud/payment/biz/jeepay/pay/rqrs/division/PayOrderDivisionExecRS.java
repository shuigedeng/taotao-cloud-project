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

package com.taotao.cloud.payment.biz.jeepay.pay.rqrs.division;

import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.AbstractRS;
import lombok.Data;

/**
 * 发起订单分账 响应参数
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/8/26 17:20
 */
@Data
public class PayOrderDivisionExecRS extends AbstractRS {

    /** 分账状态 1-分账成功, 2-分账失败 */
    private Byte state;

    /** 上游分账批次号 */
    private String channelBatchOrderId;

    /** 支付渠道错误码 */
    private String errCode;

    /** 支付渠道错误信息 */
    private String errMsg;
}
