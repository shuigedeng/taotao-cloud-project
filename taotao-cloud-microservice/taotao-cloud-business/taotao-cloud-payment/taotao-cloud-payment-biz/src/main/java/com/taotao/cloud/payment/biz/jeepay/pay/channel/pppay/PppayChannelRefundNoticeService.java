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

package com.taotao.cloud.payment.biz.jeepay.pay.channel.pppay;

import org.dromara.hutooljson.JSONObject;
import org.dromara.hutooljson.JSONUtil;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.http.serializer.Json;
import com.paypal.payments.Refund;
import com.paypal.payments.RefundsGetRequest;
import com.taotao.cloud.payment.biz.jeepay.core.constants.CS;
import com.taotao.cloud.payment.biz.jeepay.core.entity.RefundOrder;
import com.taotao.cloud.payment.biz.jeepay.core.exception.ResponseException;
import com.taotao.cloud.payment.biz.jeepay.pay.channel.AbstractChannelRefundNoticeService;
import com.taotao.cloud.payment.biz.jeepay.pay.model.MchAppConfigContext;
import com.taotao.cloud.payment.biz.jeepay.pay.model.PaypalWrapper;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.msg.ChannelRetMsg;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.stereotype.Service;

/**
 * none.
 *
 * @author 陈泉
 * @package com.jeequan.jeepay.pay.channel.pppay
 * @create 2021/11/16 20:39
 */
@Service
@Slf4j
public class PppayChannelRefundNoticeService extends AbstractChannelRefundNoticeService {

    @Override
    public String getIfCode() {
        return CS.IF_CODE.PPPAY;
    }

    @Override
    public MutablePair<String, Object> parseParams(
            HttpServletRequest request, String urlOrderId, NoticeTypeEnum noticeTypeEnum) {
        JSONObject params = JSONUtil.parseObj(getReqParamJSON().toJSONString());
        // 获取退款订单 Paypal ID
        String orderId = params.getByPath("resource.invoice_id", String.class);
        return MutablePair.of(orderId, params);
    }

    @Override
    public ChannelRetMsg doNotice(
            HttpServletRequest request,
            Object params,
            RefundOrder refundOrder,
            MchAppConfigContext mchAppConfigContext,
            NoticeTypeEnum noticeTypeEnum) {
        try {
            JSONObject object = (JSONObject) params;
            String orderId = object.getByPath("resource.id", String.class);

            PaypalWrapper wrapper = mchAppConfigContext.getPaypalWrapper();
            PayPalHttpClient client = wrapper.getClient();

            // 查询退款详情以及状态
            RefundsGetRequest refundRequest = new RefundsGetRequest(orderId);
            HttpResponse<Refund> response = client.execute(refundRequest);

            ChannelRetMsg channelRetMsg = ChannelRetMsg.waiting();
            channelRetMsg.setResponseEntity(wrapper.textResp("ERROR"));

            if (response.statusCode() == 200) {
                String responseJson = new Json().serialize(response.result());
                channelRetMsg = wrapper.dispatchCode(response.result().status(), channelRetMsg);
                channelRetMsg.setChannelAttach(responseJson);
                channelRetMsg.setChannelOrderId(response.result().id());
                channelRetMsg.setResponseEntity(wrapper.textResp("SUCCESS"));
            } else {
                channelRetMsg.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_FAIL);
                channelRetMsg.setChannelErrCode("201");
                channelRetMsg.setChannelErrMsg("异步退款失败，Paypal 响应非 200");
            }

            return channelRetMsg;
        } catch (Exception e) {
            log.error("error", e);
            throw ResponseException.buildText("ERROR");
        }
    }
}
