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

import com.taotao.cloud.payment.biz.jeepay.core.entity.MchDivisionReceiver;
import com.taotao.cloud.payment.biz.jeepay.core.entity.PayOrder;
import com.taotao.cloud.payment.biz.jeepay.core.entity.PayOrderDivisionRecord;
import com.taotao.cloud.payment.biz.jeepay.pay.model.MchAppConfigContext;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.msg.ChannelRetMsg;
import java.util.List;

/**
 * 分账接口
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/8/22 08:59
 */
public interface IDivisionService {

    /** 获取到接口code * */
    String getIfCode();

    /** 是否支持该分账 */
    boolean isSupport();

    /** 绑定关系 * */
    ChannelRetMsg bind(
            MchDivisionReceiver mchDivisionReceiver, MchAppConfigContext mchAppConfigContext);

    /** 单次分账 （无需调用完结接口，或自动解冻商户资金) * */
    ChannelRetMsg singleDivision(
            PayOrder payOrder,
            List<PayOrderDivisionRecord> recordList,
            MchAppConfigContext mchAppConfigContext);
}
