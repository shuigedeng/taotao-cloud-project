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

package com.taotao.cloud.rpc.common.common.support.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc 关闭 hook
 * （1）可以添加对应的 hook 管理类
 *
 * @since 0.1.3
 */
public abstract class AbstractShutdownHook implements RpcShutdownHook {

    /**
     * AbstractShutdownHook logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractShutdownHook.class);

    @Override
    public void hook() {
        LOG.info("[Shutdown Hook] start");
        this.doHook();
        LOG.info("[Shutdown Hook] end");
    }

    /**
     * 执行 hook 操作
     * @since 0.1.3
     */
    protected abstract void doHook();
}
