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

import cn.bootx.iam.core.user.entity.UserInfo;
import cn.bootx.payment.core.paymodel.wallet.entity.Wallet;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 钱包
 *
 * @author xxm
 * @date 2021/2/24
 */
@Mapper
public interface WalletMapper extends BaseSuperMapper<Wallet> {

    /**
     * 增加余额
     *
     * @param walletId 钱包ID
     * @param amount 增加的额度
     * @param operator 操作人
     * @param date 时间
     * @return 更新数量
     */
    int increaseBalance(
            @Param("walletId") Long walletId,
            @Param("amount") BigDecimal amount,
            @Param("operator") Long operator,
            @Param("date") LocalDateTime date);

    /**
     * 减少余额
     *
     * @param walletId 钱包ID
     * @param amount 减少的额度
     * @param operator 操作人
     * @param date 操作时间
     * @return 操作条数
     */
    int reduceBalance(
            @Param("walletId") Long walletId,
            @Param("amount") BigDecimal amount,
            @Param("operator") Long operator,
            @Param("date") LocalDateTime date);

    /**
     * 减少余额,允许扣成负数
     *
     * @param walletId 钱包ID
     * @param amount 减少的额度
     * @param operator 操作人
     * @param date 操作时间
     * @return 操作条数
     */
    int reduceBalanceUnlimited(
            @Param("walletId") Long walletId,
            @Param("amount") BigDecimal amount,
            @Param("operator") Long operator,
            @Param("date") LocalDateTime date);

    /** 待开通钱包的用户列表 */
    Page<UserInfo> pageByNotWallet(Page<UserInfo> mpPage, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
