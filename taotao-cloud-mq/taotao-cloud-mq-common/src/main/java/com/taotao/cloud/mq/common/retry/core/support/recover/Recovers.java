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

package com.taotao.cloud.mq.common.retry.core.support.recover;

import com.taotao.cloud.mq.common.retry.api.support.recover.Recover;

/**
 * <p> 恢复现场工具类 </p>
 *
 * <pre> Created: 2019/5/28 10:47 PM  </pre>
 * <pre> Project: sisyphus  </pre>
 *
 * @author houbinbin
 * @since 0.0.6
 */
public final class Recovers {

    private Recovers() {}

    /**
     * 没有任何恢复操作实例
     * @return recover 实例
     */
    public static Recover noRecover() {
        return NoRecover.getInstance();
    }
}
