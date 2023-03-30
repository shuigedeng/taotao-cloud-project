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

package com.taotao.cloud.workflow.biz.common.database.plugins;

import com.taotao.cloud.workflow.biz.common.database.util.DataSourceUtil;

/** 动态生成数据源接口 */
public interface DynamicSourceGeneratorInterface {

    /**
     * 获取当前需要切换的数据源配置
     *
     * @return
     */
    DataSourceUtil getDataSource();

    /**
     * 是否缓存链接
     *
     * @return true: 不可用时重新获取, false: 每次都重新获取配置
     */
    default boolean cachedConnection() {
        return true;
    }
}
