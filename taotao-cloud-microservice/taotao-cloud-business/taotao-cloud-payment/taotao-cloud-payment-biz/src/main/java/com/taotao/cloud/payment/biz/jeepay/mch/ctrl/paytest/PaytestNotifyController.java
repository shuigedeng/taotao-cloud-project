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

package com.taotao.cloud.payment.biz.jeepay.mch.ctrl.paytest;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.entity.MchApp;
import com.taotao.cloud.payment.biz.jeepay.core.utils.JeepayKit;
import com.taotao.cloud.payment.biz.jeepay.mch.ctrl.CommonCtrl;
import com.taotao.cloud.payment.biz.jeepay.service.impl.MchAppService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * 支付测试 - 回调函数
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/22 14:22
 */
@RestController
@RequestMapping("/api/anon/paytestNotify")
public class PaytestNotifyController extends CommonCtrl {

    @Autowired private MchAppService mchAppService;

    @RequestMapping("/payOrder")
    public void payOrderNotify() throws IOException {

        // 请求参数
        JSONObject params = getReqParamJSON();

        String mchNo = params.getString("mchNo");
        String appId = params.getString("appId");
        String sign = params.getString("sign");
        MchApp mchApp = mchAppService.getById(appId);
        if (mchApp == null || !mchApp.getMchNo().equals(mchNo)) {
            response.getWriter().print("app is not exists");
            return;
        }

        params.remove("sign");
        if (!JeepayKit.getSign(params, mchApp.getAppSecret()).equalsIgnoreCase(sign)) {
            response.getWriter().print("sign fail");
            return;
        }

        JSONObject msg = new JSONObject();
        msg.put("state", params.getIntValue("state"));
        msg.put("errCode", params.getString("errCode"));
        msg.put("errMsg", params.getString("errMsg"));

        // 推送到前端
        WsPayOrderServer.sendMsgByOrderId(params.getString("payOrderId"), msg.toJSONString());

        response.getWriter().print("SUCCESS");
    }
}
