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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.dao;

import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.payment.core.paymodel.wallet.entity.WalletLog;
import cn.bootx.payment.param.paymodel.wallet.WalletLogQueryParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 钱包日志
 *
 * @author xxm
 * @date 2020/12/8
 */
@Repository
@RequiredArgsConstructor
public class WalletLogManager extends BaseManager<WalletLogMapper, WalletLog> {

    /** 按付款查找优先 */
    public Optional<WalletLog> findFirstByPayment(Long paymentId) {
        return lambdaQuery()
                .eq(WalletLog::getPaymentId, paymentId)
                .orderByDesc(MpBaseEntity::getId)
                .last("limit 1")
                .oneOpt();
    }

    /** 分页查询指定用户的钱包日志 */
    public Page<WalletLog> pageByUserId(PageQuery PageQuery, WalletLogQueryParam param, Long userId) {
        Page<WalletLog> mpPage = MpUtil.getMpPage(PageQuery, WalletLog.class);

        return this.lambdaQuery()
                .orderByDesc(MpBaseEntity::getId)
                .eq(WalletLog::getUserId, userId)
                .page(mpPage);
    }

    /** 分页查询 */
    public Page<WalletLog> page(PageQuery PageQuery, WalletLogQueryParam param) {
        Page<WalletLog> mpPage = MpUtil.getMpPage(PageQuery, WalletLog.class);
        return this.lambdaQuery().orderByDesc(MpBaseEntity::getId).page(mpPage);
    }
    /** 分页查询 根据钱包id */
    public Page<WalletLog> pageByWalletId(PageQuery PageQuery, WalletLogQueryParam param) {
        Page<WalletLog> mpPage = MpUtil.getMpPage(PageQuery, WalletLog.class);
        return this.lambdaQuery()
                .orderByDesc(MpBaseEntity::getId)
                .eq(WalletLog::getWalletId, param.getWalletId())
                .page(mpPage);
    }
}
