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

package com.taotao.cloud.payment.biz.jeepay.pay.ctrl.payorder;

import com.taotao.cloud.payment.biz.jeepay.core.entity.PayOrder;
import com.taotao.cloud.payment.biz.jeepay.core.exception.BizException;
import com.taotao.cloud.payment.biz.jeepay.core.model.ApiRes;
import com.taotao.cloud.payment.biz.jeepay.core.utils.SpringBeansUtil;
import com.taotao.cloud.payment.biz.jeepay.pay.channel.IPayOrderCloseService;
import com.taotao.cloud.payment.biz.jeepay.pay.ctrl.ApiController;
import com.taotao.cloud.payment.biz.jeepay.pay.model.MchAppConfigContext;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.msg.ChannelRetMsg;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.ClosePayOrderRQ;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.ClosePayOrderRS;
import com.taotao.cloud.payment.biz.jeepay.pay.service.ConfigContextQueryService;
import com.taotao.cloud.payment.biz.jeepay.service.impl.PayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * 关闭订单 controller
 *
 * @author xiaoyu
 * @site https://www.jeequan.com
 * @date 2022/1/25 9:19
 */
@Slf4j
@RestController
public class CloseOrderController extends ApiController {

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private ConfigContextQueryService configContextQueryService;

    /**
     * @author: xiaoyu
     * @date: 2022/1/25 9:19
     * @describe: 关闭订单
     */
    @RequestMapping("/api/pay/close")
    public ApiRes queryOrder() {

        // 获取参数 & 验签
        ClosePayOrderRQ rq = getRQByWithMchSign(ClosePayOrderRQ.class);

        if (StringUtils.isEmpty(rq.getPayOrderId())) {
            throw new BizException("payOrderId不能为空");
        }

        PayOrder payOrder = payOrderService.queryMchOrder(rq.getMchNo(), rq.getPayOrderId(), rq.getMchOrderNo());
        if (payOrder == null) {
            throw new BizException("订单不存在");
        }

        if (payOrder.getState() != PayOrder.STATE_INIT && payOrder.getState() != PayOrder.STATE_ING) {
            throw new BizException("当前订单不可关闭");
        }

        ClosePayOrderRS bizRes = new ClosePayOrderRS();
        try {

            String payOrderId = payOrder.getPayOrderId();

            // 查询支付接口是否存在
            IPayOrderCloseService closeService =
                    SpringBeansUtil.getBean(payOrder.getIfCode() + "PayOrderCloseService", IPayOrderCloseService.class);

            // 支付通道接口实现不存在
            if (closeService == null) {
                log.error("{} interface not exists!", payOrder.getIfCode());
                return null;
            }

            // 查询出商户应用的配置信息
            MchAppConfigContext mchAppConfigContext =
                    configContextQueryService.queryMchInfoAndAppInfo(payOrder.getMchNo(), payOrder.getAppId());

            ChannelRetMsg channelRetMsg = closeService.close(payOrder, mchAppConfigContext);
            if (channelRetMsg == null) {
                log.error("channelRetMsg is null");
                return null;
            }

            log.info("关闭订单[{}]结果为：{}", payOrderId, channelRetMsg);

            // 关闭订单 成功
            if (channelRetMsg.getChannelState() == ChannelRetMsg.ChannelState.CONFIRM_SUCCESS) {
                payOrderService.updateIng2Close(payOrderId);
            } else {
                return ApiRes.customFail(channelRetMsg.getChannelErrMsg());
            }

            bizRes.setChannelRetMsg(channelRetMsg);
        } catch (Exception e) { // 关闭订单异常
            log.error("error payOrderId = {}", payOrder.getPayOrderId(), e);
            return null;
        }

        return ApiRes.okWithSign(
                bizRes,
                configContextQueryService
                        .queryMchApp(rq.getMchNo(), rq.getAppId())
                        .getAppSecret());
    }
}
