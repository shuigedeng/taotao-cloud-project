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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.entity;

import cn.bootx.common.core.function.EntityBaseFunction;
import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.payment.core.paymodel.wallet.convert.WalletConvert;
import cn.bootx.payment.dto.paymodel.wallet.WalletLogDto;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 钱包日志表
 *
 * @author xxm
 * @date 2020/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_wallet_log")
public class WalletLog extends MpBaseEntity implements EntityBaseFunction<WalletLogDto> {

    /** 钱包id */
    private Long walletId;

    /** 用户id */
    private Long userId;

    /** 类型 */
    private Integer type;

    /** 交易记录ID */
    private Long paymentId;

    /** 备注 */
    private String remark;

    /** 业务ID */
    private String businessId;

    /** 操作类型 */
    private int operationSource;

    /** 金额 */
    private BigDecimal amount;

    @Override
    public WalletLogDto toDto() {
        return WalletConvert.CONVERT.convert(this);
    }
}
