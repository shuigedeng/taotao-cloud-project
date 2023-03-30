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

package com.taotao.cloud.message.biz.austin.support.pending;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 3y pending初始化参数类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class PendingParam<T> {

    /** 阻塞队列实现类【必填】 */
    private BlockingQueue<T> queue;

    /** batch 触发执行的数量阈值【必填】 */
    private Integer numThreshold;

    /** batch 触发执行的时间阈值，单位毫秒【必填】 */
    private Long timeThreshold;

    /** 消费线程池实例【必填】 */
    protected ExecutorService executorService;
}
