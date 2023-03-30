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

package com.taotao.cloud.payment.biz.jeepay.core.model.params;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.constants.CS;
import com.taotao.cloud.payment.biz.jeepay.core.model.params.alipay.AlipayIsvsubMchParams;
import com.taotao.cloud.payment.biz.jeepay.core.model.params.wxpay.WxpayIsvsubMchParams;
import com.taotao.cloud.payment.biz.jeepay.core.model.params.ysf.YsfpayIsvsubMchParams;

/*
 * 抽象类 特约商户参数定义
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 16:33
 */
public abstract class IsvsubMchParams {

    public static IsvsubMchParams factory(String ifCode, String paramsStr) {

        if (CS.IF_CODE.WXPAY.equals(ifCode)) {
            return JSONObject.parseObject(paramsStr, WxpayIsvsubMchParams.class);
        } else if (CS.IF_CODE.ALIPAY.equals(ifCode)) {
            return JSONObject.parseObject(paramsStr, AlipayIsvsubMchParams.class);
        } else if (CS.IF_CODE.YSFPAY.equals(ifCode)) {
            return JSONObject.parseObject(paramsStr, YsfpayIsvsubMchParams.class);
        }
        return null;
    }
}
