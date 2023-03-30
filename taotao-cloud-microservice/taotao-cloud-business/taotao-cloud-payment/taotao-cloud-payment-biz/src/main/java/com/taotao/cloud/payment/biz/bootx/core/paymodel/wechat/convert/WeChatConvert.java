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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.convert;

import cn.bootx.payment.core.paymodel.wechat.entity.WeChatPayConfig;
import cn.bootx.payment.core.paymodel.wechat.entity.WeChatPayment;
import cn.bootx.payment.dto.paymodel.wechat.WeChatPayConfigDto;
import cn.bootx.payment.dto.paymodel.wechat.WeChatPaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 微信转换类
 *
 * @author xxm
 * @date 2021/6/21
 */
@Mapper
public interface WeChatConvert {
    WeChatConvert CONVERT = Mappers.getMapper(WeChatConvert.class);

    WeChatPayConfig convert(WeChatPayConfigDto in);

    WeChatPayConfigDto convert(WeChatPayConfig in);

    WeChatPaymentDto convert(WeChatPayment in);

    WeChatPayment convert(WeChatPaymentDto in);
}
