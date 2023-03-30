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

package com.taotao.cloud.payment.biz.bootx.param.payconfig;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付通道支持的支付方式
 *
 * @author xxm
 * @date 2021/6/30
 */
@Data
@Accessors(chain = true)
public class PayChannelWayParam {
    /** 通道id */
    private Long channelId;

    /** 支付方式代码 */
    private String code;

    /** 支付方式名称 */
    private String name;

    /** 备注 */
    private String remark;
}
