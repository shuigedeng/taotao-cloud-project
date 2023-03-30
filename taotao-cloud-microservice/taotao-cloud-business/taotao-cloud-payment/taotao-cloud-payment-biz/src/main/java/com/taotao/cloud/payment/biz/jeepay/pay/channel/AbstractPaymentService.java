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

import com.taotao.cloud.payment.biz.jeepay.core.entity.PayOrder;
import com.taotao.cloud.payment.biz.jeepay.pay.service.ConfigContextQueryService;
import com.taotao.cloud.payment.biz.jeepay.pay.util.ChannelCertConfigKitBean;
import com.taotao.cloud.payment.biz.jeepay.service.impl.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * 支付接口抽象类
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 17:18
 */
public abstract class AbstractPaymentService implements IPaymentService {

    @Autowired protected SysConfigService sysConfigService;
    @Autowired protected ChannelCertConfigKitBean channelCertConfigKitBean;
    @Autowired protected ConfigContextQueryService configContextQueryService;

    /** 订单分账（一般用作 如微信订单将在下单处做标记） */
    protected boolean isDivisionOrder(PayOrder payOrder) {
        // 订单分账， 将冻结商户资金。
        if (payOrder.getDivisionMode() != null
                && (PayOrder.DIVISION_MODE_AUTO == payOrder.getDivisionMode()
                        || PayOrder.DIVISION_MODE_MANUAL == payOrder.getDivisionMode())) {
            return true;
        }
        return false;
    }

    protected String getNotifyUrl() {
        return sysConfigService.getDBApplicationConfig().getPaySiteUrl()
                + "/api/pay/notify/"
                + getIfCode();
    }

    protected String getNotifyUrl(String payOrderId) {
        return sysConfigService.getDBApplicationConfig().getPaySiteUrl()
                + "/api/pay/notify/"
                + getIfCode()
                + "/"
                + payOrderId;
    }

    protected String getReturnUrl() {
        return sysConfigService.getDBApplicationConfig().getPaySiteUrl()
                + "/api/pay/return/"
                + getIfCode();
    }

    protected String getReturnUrl(String payOrderId) {
        return sysConfigService.getDBApplicationConfig().getPaySiteUrl()
                + "/api/pay/return/"
                + getIfCode()
                + "/"
                + payOrderId;
    }
}
