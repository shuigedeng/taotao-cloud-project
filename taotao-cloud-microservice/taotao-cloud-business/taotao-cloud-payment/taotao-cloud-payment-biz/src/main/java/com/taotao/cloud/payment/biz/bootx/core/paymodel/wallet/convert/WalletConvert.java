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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.convert;

import cn.bootx.payment.core.paymodel.wallet.entity.Wallet;
import cn.bootx.payment.core.paymodel.wallet.entity.WalletLog;
import cn.bootx.payment.core.paymodel.wallet.entity.WalletPayment;
import cn.bootx.payment.dto.paymodel.wallet.WalletDto;
import cn.bootx.payment.dto.paymodel.wallet.WalletLogDto;
import cn.bootx.payment.dto.paymodel.wallet.WalletPaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 转换
 *
 * @author xxm
 * @date 2021/8/20
 */
@Mapper
public interface WalletConvert {
    WalletConvert CONVERT = Mappers.getMapper(WalletConvert.class);

    WalletDto convert(Wallet in);

    WalletPaymentDto convert(WalletPayment in);

    WalletLogDto convert(WalletLog walletLog);
}
