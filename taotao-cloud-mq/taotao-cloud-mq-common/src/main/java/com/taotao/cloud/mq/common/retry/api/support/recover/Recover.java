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

package com.taotao.cloud.mq.common.retry.api.support.recover;

import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;

/**
 * 恢复现场接口
 * 1. 只会在所有的尝试都执行完成之后才会执行。
 * 2. 触发了重试，且所有的重试都完成了，但结果依然是失败。
 * 3. 根据实际使用，一次失败对应的 recover 应该是唯一的，不然复杂度会没完没了了的扩散。
 *
 * 注意：实现类应该有无参构造函数
 * @author shuigedeng
 * @since 0.0.1
 */
public interface Recover {

    /**
     * 执行恢复
     * @param retryAttempt 重试信息
     * @param <R> 泛型
     */
    <R> void recover(final RetryAttempt<R> retryAttempt);
}
