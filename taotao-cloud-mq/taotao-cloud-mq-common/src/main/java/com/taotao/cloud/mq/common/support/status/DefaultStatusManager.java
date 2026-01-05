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

package com.taotao.cloud.mq.common.support.status;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class DefaultStatusManager implements StatusManager {

    private boolean status;

    private boolean initFailed;

    @Override
    public boolean status() {
        return this.status;
    }

    @Override
    public StatusManager status(boolean status) {
        this.status = status;

        return this;
    }

    @Override
    public boolean initFailed() {
        return initFailed;
    }

    @Override
    public DefaultStatusManager initFailed(boolean initFailed) {
        this.initFailed = initFailed;
        return this;
    }
}
