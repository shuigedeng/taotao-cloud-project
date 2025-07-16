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

package com.taotao.cloud.rpc.common.common.support.resource;

import com.taotao.cloud.rpc.common.common.api.Destroyable;

/**
 * @since 0.1.3
 */
public interface ResourceManager {

    /**
     * 新增可销毁的资源信息
     * @param destroyable 可销毁的资源信息
     * @return this
     * @since 0.1.3
     */
    ResourceManager addDestroy(final Destroyable destroyable);

    /**
     * 销毁所有资源
     * （1）销毁所有的列表资源
     * （2）清空可销毁的列表
     * @return this
     * @since 0.1.3
     */
    ResourceManager destroyAll();
}
