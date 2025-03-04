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

package com.taotao.cloud.sys.biz.supports.gobrs.config;

import com.gobrs.async.core.callback.AsyncTaskPostInterceptor;
import org.springframework.stereotype.Component;

/**
 * @program: m-detail @ClassName AsyncTaskPreInterceptor
 * @description:
 * @author: sizegang
 * @create: 2022-03-24
 */
@Component
public class TaskPostInterceptor implements AsyncTaskPostInterceptor {

    /**
     * @param result 任务结果
     * @param taskName 任务名称
     */
    @Override
    public void postProcess(Object result, String taskName) {}
}
