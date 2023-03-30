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

package com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder;

import com.alibaba.fastjson.annotation.JSONField;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.AbstractRS;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.msg.ChannelRetMsg;
import lombok.Data;

/*
 * 关闭订单 响应参数
 *
 * @author xiaoyu
 * @site https://www.jeequan.com
 * @date 2022/1/25 9:17
 */
@Data
public class ClosePayOrderRS extends AbstractRS {

    /** 上游渠道返回数据包 (无需JSON序列化) * */
    @JSONField(serialize = false)
    private ChannelRetMsg channelRetMsg;
}
