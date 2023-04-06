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

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.constants.CS;
import com.taotao.cloud.payment.biz.jeepay.core.entity.PayOrder;
import com.taotao.cloud.payment.biz.jeepay.core.model.params.xxpay.XxpayNormalMchParams;
import com.taotao.cloud.payment.biz.jeepay.core.utils.JeepayKit;
import com.taotao.cloud.payment.biz.jeepay.pay.channel.IPayOrderQueryService;
import com.taotao.cloud.payment.biz.jeepay.pay.model.MchAppConfigContext;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.msg.ChannelRetMsg;
import com.taotao.cloud.payment.biz.jeepay.pay.service.ConfigContextQueryService;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * 小新支付 查单接口实现类
 *
 * @author jmdhappy
 * @site https://www.jeequan.com
 * @date 2021/9/21 01:05
 */
@Service
@Slf4j
public class XxpayPayOrderQueryService implements IPayOrderQueryService {

    @Autowired
    private ConfigContextQueryService configContextQueryService;

    @Override
    public String getIfCode() {
        return CS.IF_CODE.XXPAY;
    }

    @Override
    public ChannelRetMsg query(PayOrder payOrder, MchAppConfigContext mchAppConfigContext) {
        XxpayNormalMchParams xxpayParams = (XxpayNormalMchParams) configContextQueryService.queryNormalMchParams(
                mchAppConfigContext.getMchNo(), mchAppConfigContext.getAppId(), getIfCode());
        Map<String, Object> paramMap = new TreeMap();
        // 接口类型
        paramMap.put("mchId", xxpayParams.getMchId());
        paramMap.put("mchOrderNo", payOrder.getPayOrderId());
        String sign = XxpayKit.getSign(paramMap, xxpayParams.getKey());
        paramMap.put("sign", sign);
        String resStr = "";
        String queryPayOrderUrl =
                XxpayKit.getQueryPayOrderUrl(xxpayParams.getPayUrl()) + "?" + JeepayKit.genUrlParams(paramMap);
        try {
            log.info("支付查询[{}]参数：{}", getIfCode(), queryPayOrderUrl);
            resStr = HttpUtil.createPost(queryPayOrderUrl)
                    .timeout(60 * 1000)
                    .execute()
                    .body();
            log.info("支付查询[{}]结果：{}", getIfCode(), resStr);
        } catch (Exception e) {
            log.error("http error", e);
        }
        if (StringUtils.isEmpty(resStr)) {
            return ChannelRetMsg.waiting(); // 支付中
        }
        JSONObject resObj = JSONObject.parseObject(resStr);
        if (!"0".equals(resObj.getString("retCode"))) {
            return ChannelRetMsg.waiting(); // 支付中
        }
        // 支付状态,0-订单生成,1-支付中,2-支付成功,3-业务处理完成
        String status = resObj.getString("status");
        if ("2".equals(status) || "3".equals(status)) {
            return ChannelRetMsg.confirmSuccess(resObj.getString("channelOrderNo")); // 支付成功
        }
        return ChannelRetMsg.waiting(); // 支付中
    }
}
