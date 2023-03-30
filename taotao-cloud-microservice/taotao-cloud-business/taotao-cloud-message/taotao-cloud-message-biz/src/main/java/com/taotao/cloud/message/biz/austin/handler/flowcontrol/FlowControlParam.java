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

package com.taotao.cloud.message.biz.austin.handler.flowcontrol;

import com.google.common.util.concurrent.RateLimiter;
import com.taotao.cloud.message.biz.austin.handler.enums.RateLimitStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 3y
 * @date 2022/4/18 import com.taotao.cloud.message.biz.austin.handler.enums.RateLimitStrategy;
 *     <p>流量控制所需要的参数
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowControlParam {

    /** 限流器 子类初始化的时候指定 */
    protected RateLimiter rateLimiter;

    /** 限流器初始限流大小 子类初始化的时候指定 */
    protected Double rateInitValue;

    /** 限流的策略 子类初始化的时候指定 */
    protected RateLimitStrategy rateLimitStrategy;
}
