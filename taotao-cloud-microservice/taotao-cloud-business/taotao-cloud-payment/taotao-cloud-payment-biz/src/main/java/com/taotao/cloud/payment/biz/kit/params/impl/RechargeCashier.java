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

package com.taotao.cloud.payment.biz.kit.params.impl;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.member.api.feign.IFeignMemberRechargeApi;
import com.taotao.cloud.member.api.model.vo.MemberRechargeVO;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.payment.api.enums.CashierEnum;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.dto.PaymentSuccessParams;
import com.taotao.cloud.payment.biz.kit.params.CashierExecute;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.BaseSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 充值信息获取 */
@Component
public class RechargeCashier implements CashierExecute {

    /** 余额 */
    @Autowired
    private IFeignMemberRechargeApi memberRechargeApi;
    /** 设置 */
    @Autowired
    private IFeignSettingApi settingApi;

    @Override
    public CashierEnum cashierEnum() {
        return CashierEnum.RECHARGE;
    }

    @Override
    public void paymentSuccess(PaymentSuccessParams paymentSuccessParams) {
        PayParam payParam = paymentSuccessParams.getPayParam();
        if (payParam.getOrderType().equals(CashierEnum.RECHARGE.name())) {
            memberRechargeApi.paySuccess(
                    payParam.getSn(), paymentSuccessParams.getReceivableNo(), paymentSuccessParams.getPaymentMethod());
            LogUtils.info("会员充值-订单号{},第三方流水：{}", payParam.getSn(), paymentSuccessParams.getReceivableNo());
        }
    }

    @Override
    public CashierParam getPaymentParams(PayParam payParam) {
        if (payParam.getOrderType().equals(CashierEnum.RECHARGE.name())) {
            // 准备返回的数据
            CashierParam cashierParam = new CashierParam();
            // 订单信息获取
            MemberRechargeVO recharge = memberRechargeApi.getRecharge(payParam.getSn());

            // 如果订单已支付，则不能发器支付
            if (recharge.getPayStatus().equals(PayStatusEnum.PAID.name())) {
                throw new BusinessException(ResultEnum.PAY_DOUBLE_ERROR);
            }

            cashierParam.setPrice(recharge.getRechargeMoney());

            try {
                BaseSetting baseSetting = settingApi.getBaseSetting(SettingCategoryEnum.BASE_SETTING.name());
                cashierParam.setTitle(baseSetting.getSiteName());
            } catch (Exception e) {
                cashierParam.setTitle("多用户商城，在线充值");
            }
            cashierParam.setDetail("余额充值");
            cashierParam.setCreateTime(recharge.getCreateTime());
            return cashierParam;
        }

        return null;
    }

    @Override
    public Boolean paymentResult(PayParam payParam) {
        if (payParam.getOrderType().equals(CashierEnum.RECHARGE.name())) {
            MemberRechargeVO recharge = memberRechargeApi.getRecharge(payParam.getSn());
            if (recharge != null) {
                return recharge.getPayStatus().equals(PayStatusEnum.PAID.name());
            } else {
                throw new BusinessException(ResultEnum.PAY_NOT_EXIST_ORDER);
            }
        }
        return false;
    }
}
