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

package com.taotao.cloud.rpc.common.common.support.status.service.impl;

import com.taotao.cloud.rpc.common.common.support.status.enums.StatusEnum;
import com.taotao.cloud.rpc.common.common.support.status.service.StatusManager;
import javax.annotation.concurrent.ThreadSafe;

/**
 * 状态管理
 *
 * <p> project: rpc-StatusManager </p>
 * <p> create on 2019/10/30 20:48 </p>
 *
 * @author Administrator
 * @since 0.1.3
 */
@ThreadSafe
public class DefaultStatusManager implements StatusManager {

    /**
     * 状态
     * @since 0.1.3
     */
    private volatile int status = StatusEnum.INIT.code();

    @Override
    public int status() {
        return status;
    }

    @Override
    public DefaultStatusManager status(int status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultStatusManager{" + "status=" + status + '}';
    }
}
