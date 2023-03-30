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

package com.taotao.cloud.payment.biz.jeepay.pay.channel;

import com.taotao.cloud.payment.biz.jeepay.core.entity.PayOrder;
import com.taotao.cloud.payment.biz.jeepay.core.entity.RefundOrder;
import com.taotao.cloud.payment.biz.jeepay.pay.model.MchAppConfigContext;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.msg.ChannelRetMsg;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.refund.RefundOrderRQ;

/*
 * 调起上游渠道侧退款接口
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/17 9:35
 */
public interface IRefundService {

    /** 获取到接口code * */
    String getIfCode();

    /** 前置检查如参数等信息是否符合要求， 返回错误信息或直接抛出异常即可 */
    String preCheck(RefundOrderRQ bizRQ, RefundOrder refundOrder, PayOrder payOrder);

    /** 调起退款接口，并响应数据； 内部处理普通商户和服务商模式 * */
    ChannelRetMsg refund(
            RefundOrderRQ bizRQ,
            RefundOrder refundOrder,
            PayOrder payOrder,
            MchAppConfigContext mchAppConfigContext)
            throws Exception;

    /** 退款查单接口 * */
    ChannelRetMsg query(RefundOrder refundOrder, MchAppConfigContext mchAppConfigContext)
            throws Exception;
}
