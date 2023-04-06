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

package com.taotao.cloud.payment.biz.jeepay.pay.channel.ysfpay.payway;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.entity.PayOrder;
import com.taotao.cloud.payment.biz.jeepay.pay.channel.ysfpay.YsfpayPaymentService;
import com.taotao.cloud.payment.biz.jeepay.pay.model.MchAppConfigContext;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.AbstractRS;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.msg.ChannelRetMsg;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.UnifiedOrderRQ;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.payway.YsfJsapiOrderRQ;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.payway.YsfJsapiOrderRS;
import com.taotao.cloud.payment.biz.jeepay.pay.util.ApiResBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/*
 * 云闪付 jsapi
 *
 * @author pangxiaoyu
 * @site https://www.jeequan.com
 * @date 2021/6/8 18:11
 */
@Service("ysfpayPaymentByJsapiService") // Service Name需保持全局唯一性
public class YsfJsapi extends YsfpayPaymentService {

    @Override
    public String preCheck(UnifiedOrderRQ rq, PayOrder payOrder) {
        return null;
    }

    @Override
    public AbstractRS pay(UnifiedOrderRQ rq, PayOrder payOrder, MchAppConfigContext mchAppConfigContext)
            throws Exception {
        String logPrefix = "【云闪付(unionpay)jsapi支付】";
        JSONObject reqParams = new JSONObject();
        YsfJsapiOrderRS res = ApiResBuilder.buildSuccess(YsfJsapiOrderRS.class);
        ChannelRetMsg channelRetMsg = new ChannelRetMsg();
        res.setChannelRetMsg(channelRetMsg);

        YsfJsapiOrderRQ bizRQ = (YsfJsapiOrderRQ) rq;

        // 请求参数赋值
        jsapiParamsSet(reqParams, payOrder, getNotifyUrl(), getReturnUrl());
        // 云闪付扫一扫支付， 需要传入termInfo参数
        reqParams.put(
                "termInfo", "{\"ip\": \"" + StringUtils.defaultIfEmpty(payOrder.getClientIp(), "127.0.0.1") + "\"}");

        // 客户端IP
        reqParams.put("customerIp", StringUtils.defaultIfEmpty(payOrder.getClientIp(), "127.0.0.1"));
        // 发送请求并返回订单状态
        JSONObject resJSON =
                packageParamAndReq("/gateway/api/pay/unifiedorder", reqParams, logPrefix, mchAppConfigContext);
        // 请求 & 响应成功， 判断业务逻辑
        String respCode = resJSON.getString("respCode"); // 应答码
        String respMsg = resJSON.getString("respMsg"); // 应答信息

        try {
            // 00-交易成功， 02-用户支付中 , 12-交易重复， 需要发起查询处理    其他认为失败
            if ("00".equals(respCode)) {
                // 付款信息
                res.setPayData(resJSON.getString("payData"));
                channelRetMsg.setChannelState(ChannelRetMsg.ChannelState.WAITING);
            } else {
                channelRetMsg.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_FAIL);
                channelRetMsg.setChannelErrCode(respCode);
                channelRetMsg.setChannelErrMsg(respMsg);
            }
        } catch (Exception e) {
            channelRetMsg.setChannelErrCode(respCode);
            channelRetMsg.setChannelErrMsg(respMsg);
        }
        return res;
    }
}
