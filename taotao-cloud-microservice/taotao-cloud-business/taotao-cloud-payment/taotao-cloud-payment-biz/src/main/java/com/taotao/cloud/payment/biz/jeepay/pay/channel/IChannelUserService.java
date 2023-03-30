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

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.pay.model.MchAppConfigContext;

/*
 * @Description: 301方式获取渠道侧用户ID， 如微信openId 支付宝的userId等
 * @author terrfly
 * @date 2021/5/2 15:10
 */
public interface IChannelUserService {

    /** 获取到接口code * */
    String getIfCode();

    /** 获取重定向地址 * */
    String buildUserRedirectUrl(String callbackUrlEncode, MchAppConfigContext mchAppConfigContext);

    /** 获取渠道用户ID * */
    String getChannelUserId(JSONObject reqParams, MchAppConfigContext mchAppConfigContext);
}
