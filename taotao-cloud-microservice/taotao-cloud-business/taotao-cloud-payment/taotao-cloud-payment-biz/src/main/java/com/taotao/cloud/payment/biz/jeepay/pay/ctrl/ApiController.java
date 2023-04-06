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

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.constants.CS;
import com.taotao.cloud.payment.biz.jeepay.core.ctrls.AbstractCtrl;
import com.taotao.cloud.payment.biz.jeepay.core.entity.MchApp;
import com.taotao.cloud.payment.biz.jeepay.core.exception.BizException;
import com.taotao.cloud.payment.biz.jeepay.jeepay.util.JeepayKit;
import com.taotao.cloud.payment.biz.jeepay.pay.model.MchAppConfigContext;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.AbstractMchAppRQ;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.AbstractRQ;
import com.taotao.cloud.payment.biz.jeepay.pay.service.ConfigContextQueryService;
import com.taotao.cloud.payment.biz.jeepay.pay.service.ValidateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * api 抽象接口， 公共函数
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 17:28
 */
public abstract class ApiController extends AbstractCtrl {

    @Autowired
    private ValidateService validateService;

    @Autowired
    private ConfigContextQueryService configContextQueryService;

    /** 获取请求参数并转换为对象，通用验证 * */
    protected <T extends AbstractRQ> T getRQ(Class<T> cls) {

        T bizRQ = getObject(cls);

        // [1]. 验证通用字段规则
        validateService.validate(bizRQ);

        return bizRQ;
    }

    /** 获取请求参数并转换为对象，商户通用验证 * */
    protected <T extends AbstractRQ> T getRQByWithMchSign(Class<T> cls) {

        // 获取请求RQ, and 通用验证
        T bizRQ = getRQ(cls);

        AbstractMchAppRQ abstractMchAppRQ = (AbstractMchAppRQ) bizRQ;

        // 业务校验， 包括： 验签， 商户状态是否可用， 是否支持该支付方式下单等。
        String mchNo = abstractMchAppRQ.getMchNo();
        String appId = abstractMchAppRQ.getAppId();
        String sign = bizRQ.getSign();

        if (StringUtils.isAnyBlank(mchNo, appId, sign)) {
            throw new BizException("参数有误！");
        }

        MchAppConfigContext mchAppConfigContext = configContextQueryService.queryMchInfoAndAppInfo(mchNo, appId);

        if (mchAppConfigContext == null) {
            throw new BizException("商户或商户应用不存在");
        }

        if (mchAppConfigContext.getMchInfo() == null
                || mchAppConfigContext.getMchInfo().getState() != CS.YES) {
            throw new BizException("商户信息不存在或商户状态不可用");
        }

        MchApp mchApp = mchAppConfigContext.getMchApp();
        if (mchApp == null || mchApp.getState() != CS.YES) {
            throw new BizException("商户应用不存在或应用状态不可用");
        }

        if (!mchApp.getMchNo().equals(mchNo)) {
            throw new BizException("参数appId与商户号不匹配");
        }

        // 验签
        String appSecret = mchApp.getAppSecret();

        // 转换为 JSON
        JSONObject bizReqJSON = (JSONObject) JSONObject.toJSON(bizRQ);
        bizReqJSON.remove("sign");
        if (!sign.equalsIgnoreCase(JeepayKit.getSign(bizReqJSON, appSecret))) {
            throw new BizException("验签失败");
        }

        return bizRQ;
    }
}
