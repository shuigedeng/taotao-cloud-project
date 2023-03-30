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

package com.taotao.cloud.payment.biz.jeepay.pay.ctrl;

import cn.hutool.core.codec.Base64;
import com.taotao.cloud.payment.biz.jeepay.core.ctrls.AbstractCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * 通用处理
 *
 * @author jmdhappy
 * @site https://www.jeequan.com
 * @date 2022/01/25 23:38
 */
@Slf4j
@Controller
@RequestMapping("/api/common")
public class CommonController extends AbstractCtrl {

    /**
     * 跳转到支付页面(适合网关支付form表单输出)
     *
     * @param payData
     * @return
     */
    @RequestMapping(value = "/payForm/{payData}")
    private String toPayForm(@PathVariable("payData") String payData) {
        request.setAttribute("payHtml", Base64.decodeStr(payData));
        return "common/toPay";
    }

    /**
     * 跳转到支付页面(适合微信H5跳转与referer一致)
     *
     * @param payData
     * @return
     */
    @RequestMapping(value = "/payUrl/{payData}")
    private String toPayUrl(@PathVariable("payData") String payData) {
        String payUrl = Base64.decodeStr(payData);
        request.setAttribute(
                "payHtml", "<script>window.location.href = '" + payUrl + "';</script>");
        return "common/toPay";
    }
}
