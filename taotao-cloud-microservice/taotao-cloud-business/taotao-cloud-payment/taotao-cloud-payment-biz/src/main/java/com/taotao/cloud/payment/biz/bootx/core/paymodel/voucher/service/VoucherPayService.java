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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.payment.biz.bootx.code.paymodel.VoucherCode;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.dao.VoucherLogManager;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.dao.VoucherManager;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.dao.VoucherPaymentManager;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.entity.Voucher;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.entity.VoucherLog;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.entity.VoucherPayment;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayFailureException;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.voucher.VoucherPayParam;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 储值卡支付
 *
 * @author xxm
 * @date 2022/3/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherPayService {

    private final VoucherManager voucherManager;
    private final VoucherPaymentManager voucherPaymentManager;
    private final VoucherLogManager voucherLogManager;

    /** 获取并检查储值卡 */
    public List<Voucher> getAndCheckVoucher(PayModeParam payModeParam) {
        VoucherPayParam voucherPayParam;
        try {
            // 储值卡参数验证
            String extraParamsJson = payModeParam.getExtraParamsJson();
            if (StrUtil.isNotBlank(extraParamsJson)) {
                voucherPayParam = JSONUtil.toBean(extraParamsJson, VoucherPayParam.class);
            } else {
                throw new PayFailureException("储值卡支付参数错误");
            }
        } catch (JSONException e) {
            throw new PayFailureException("储值卡支付参数错误");
        }

        List<String> cardNoList = voucherPayParam.getCardNoList();
        List<Voucher> vouchers = voucherManager.findByCardNoList(cardNoList);
        // 判断是否有重复or无效的储值卡
        if (vouchers.size() != cardNoList.size()) {
            throw new PayFailureException("储值卡支付参数错误");
        }
        // 判断有效期
        boolean timeCheck = vouchers.stream()
                .allMatch(voucher ->
                        LocalDateTimeUtil.between(LocalDateTime.now(), voucher.getStartTime(), voucher.getEndTime()));
        if (!timeCheck) {
            throw new PayFailureException("储值卡不再有效期内");
        }
        // 金额是否满足
        BigDecimal amount = vouchers.stream()
                .map(Voucher::getBalance)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        if (BigDecimalUtil.compareTo(amount, payModeParam.getAmount()) < 0) {
            throw new PayFailureException("储值卡余额不足");
        }

        return vouchers;
    }

    /** 支付 */
    public void pay(BigDecimal amount, Payment payment, List<Voucher> vouchers) {
        // 排序,金额小的在前 TODO 有期限的在前面, 同样有期限到期时间短的在前面, 同样到期日金额小的在前面
        vouchers.sort((o1, o2) -> BigDecimalUtil.compareTo(o1.getBalance(), o2.getBalance()));
        List<VoucherLog> voucherLogs = new ArrayList<>();

        for (Voucher voucher : vouchers) {
            // 待支付余额为零, 不在处理后面的储值卡
            if (BigDecimalUtil.compareTo(amount, BigDecimal.ZERO) < 1) {
                break;
            }

            BigDecimal balance = voucher.getBalance();
            // 日志
            VoucherLog voucherLog = new VoucherLog()
                    .setPaymentId(payment.getId())
                    .setBusinessId(payment.getBusinessId())
                    .setType(VoucherCode.LOG_PAY)
                    .setVoucherId(voucher.getId())
                    .setVoucherNo(voucher.getCardNo());

            // 待支付额大于储值卡余额. 全扣光
            if (BigDecimalUtil.compareTo(amount, balance) == 1) {
                amount = amount.subtract(balance);
                voucher.setBalance(BigDecimal.ZERO);
                voucherLog.setAmount(balance);
            } else {
                voucher.setBalance(balance.subtract(amount));
                voucherLog.setAmount(amount);
            }
            voucherLogs.add(voucherLog);
        }
        voucherManager.updateAllById(vouchers);
        voucherLogManager.saveAll(voucherLogs);
    }

    /** 取消支付 */
    public void close(Long paymentId) {
        // 查找支付记录日志
        List<VoucherLog> voucherLogs = voucherLogManager.findByPaymentIdAndType(paymentId, VoucherCode.LOG_PAY);
        // 查出关联的储值卡
        Map<Long, VoucherLog> voucherLogMap =
                voucherLogs.stream().collect(Collectors.toMap(VoucherLog::getVoucherId, o -> o));
        List<Voucher> vouchers = voucherManager.findAllByIds(voucherLogMap.keySet());
        // 执行退款并记录日志
        List<VoucherLog> logs = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            VoucherLog voucherLog = voucherLogMap.get(voucher.getId());
            voucher.setBalance(voucher.getBalance().add(voucherLog.getAmount()));
            VoucherLog log = new VoucherLog()
                    .setAmount(voucherLog.getAmount())
                    .setPaymentId(paymentId)
                    .setBusinessId(voucherLog.getBusinessId())
                    .setVoucherId(voucher.getId())
                    .setVoucherNo(voucher.getCardNo())
                    .setType(VoucherCode.LOG_CLOSE);
            logs.add(log);
        }
        voucherManager.updateAllById(vouchers);
        voucherLogManager.saveAll(logs);
    }

    /** 退款 退到使用的第一个卡上 */
    public void refund(Long paymentId, BigDecimal amount) {
        VoucherPayment voucherPayment =
                voucherPaymentManager.findByPaymentId(paymentId).orElseThrow(() -> new BizException("储值卡支付记录不存在"));

        Long voucherId = Long.valueOf(voucherPayment.getVoucherIds().split(",")[0]);
        Voucher voucher = voucherManager.findById(voucherId).orElseThrow(DataNotExistException::new);

        voucher.setBalance(voucher.getBalance().add(amount));

        VoucherLog log = new VoucherLog()
                .setAmount(amount)
                .setPaymentId(paymentId)
                .setBusinessId(voucherPayment.getBusinessId())
                .setVoucherId(voucher.getId())
                .setVoucherNo(voucher.getCardNo())
                .setType(VoucherCode.LOG_REFUND);
        voucherManager.updateById(voucher);
        voucherLogManager.save(log);
    }
}
