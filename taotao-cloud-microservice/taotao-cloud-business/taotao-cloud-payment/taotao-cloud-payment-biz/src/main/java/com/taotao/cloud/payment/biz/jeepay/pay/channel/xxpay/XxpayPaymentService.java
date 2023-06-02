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

package com.taotao.cloud.payment.biz.jeepay.pay.channel.xxpay;

import org.dromara.hutoolhttp.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.constants.CS;
import com.taotao.cloud.payment.biz.jeepay.core.entity.PayOrder;
import com.taotao.cloud.payment.biz.jeepay.core.model.params.xxpay.XxpayNormalMchParams;
import com.taotao.cloud.payment.biz.jeepay.core.utils.JeepayKit;
import com.taotao.cloud.payment.biz.jeepay.pay.channel.AbstractPaymentService;
import com.taotao.cloud.payment.biz.jeepay.pay.model.MchAppConfigContext;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.AbstractRS;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.msg.ChannelRetMsg;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.UnifiedOrderRQ;
import com.taotao.cloud.payment.biz.jeepay.pay.util.PaywayUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/*
 * 支付接口： 小新支付
 * 支付方式： 自适应
 *
 * @author jmdhappy
 * @site https://www.jeequan.com
 * @date 2021/9/20 20:00
 */
@Service
@Slf4j
public class XxpayPaymentService extends AbstractPaymentService {

    @Override
    public String getIfCode() {
        return CS.IF_CODE.XXPAY;
    }

    @Override
    public boolean isSupport(String wayCode) {
        return true;
    }

    @Override
    public String preCheck(UnifiedOrderRQ rq, PayOrder payOrder) {
        return PaywayUtil.getRealPaywayService(this, payOrder.getWayCode()).preCheck(rq, payOrder);
    }

    @Override
    public AbstractRS pay(UnifiedOrderRQ rq, PayOrder payOrder, MchAppConfigContext mchAppConfigContext)
            throws Exception {
        return PaywayUtil.getRealPaywayService(this, payOrder.getWayCode()).pay(rq, payOrder, mchAppConfigContext);
    }

    /**
     * 统一支付处理
     *
     * @param payOrder
     * @param params
     * @param paramMap
     * @param channelRetMsg
     * @return
     */
    protected JSONObject doPay(
            PayOrder payOrder, XxpayNormalMchParams params, Map paramMap, ChannelRetMsg channelRetMsg) {
        // 生成签名
        String sign = XxpayKit.getSign(paramMap, params.getKey());
        paramMap.put("sign", sign);
        // 支付下单地址
        String payUrl = XxpayKit.getPaymentUrl(params.getPayUrl()) + "?" + JeepayKit.genUrlParams(paramMap);
        String resStr = "";
        try {
            log.info("发起支付[{}]参数：{}", getIfCode(), payUrl);
            resStr = HttpUtil.createPost(payUrl).timeout(60 * 1000).execute().body();
            log.info("发起支付[{}]结果：{}", getIfCode(), resStr);
        } catch (Exception e) {
            log.error("http error", e);
        }

        if (StringUtils.isEmpty(resStr)) {
            channelRetMsg.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_FAIL);
            channelRetMsg.setChannelErrCode("");
            channelRetMsg.setChannelErrMsg("请求" + getIfCode() + "接口异常");
            return null;
        }

        JSONObject resObj = JSONObject.parseObject(resStr);
        if (!"0".equals(resObj.getString("retCode"))) {
            String retMsg = resObj.getString("retMsg");
            channelRetMsg.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_FAIL);
            channelRetMsg.setChannelErrCode("");
            channelRetMsg.setChannelErrMsg(retMsg);
            return null;
        }

        // 验证响应数据签名
        String checkSign = resObj.getString("sign");
        resObj.remove("sign");
        if (!checkSign.equals(XxpayKit.getSign(resObj, params.getKey()))) {
            channelRetMsg.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_FAIL);
            return null;
        }

        // 订单状态-2:订单已关闭,0-订单生成,1-支付中,2-支付成功,3-业务处理完成,4-已退款
        String orderStatus = resObj.getString("orderStatus");
        if ("2".equals(orderStatus) || "3".equals(orderStatus)) {
            channelRetMsg.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_SUCCESS);
        } else {
            channelRetMsg.setChannelState(ChannelRetMsg.ChannelState.WAITING);
        }
        return resObj;
    }
}
