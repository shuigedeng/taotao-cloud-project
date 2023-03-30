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

package com.taotao.cloud.payment.biz.bootx.core.pay.result;

import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付网关通知状态对象
 *
 * @author xxm
 * @date 2021/4/21
 */
@Data
@Accessors(chain = true)
public class PaySyncResult {
    /**
     * 支付网关同步状态
     *
     * @see PaySyncStatus
     */
    private int paySyncStatus = -1;

    /** 网关返回参数 */
    private Map<String, String> map;
}
