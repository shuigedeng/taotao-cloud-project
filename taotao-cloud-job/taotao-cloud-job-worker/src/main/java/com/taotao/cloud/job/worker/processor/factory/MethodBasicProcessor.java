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

package com.taotao.cloud.job.worker.processor.factory;

import com.taotao.cloud.job.common.utils.JsonUtils;
import com.taotao.cloud.job.worker.processor.ProcessResult;
import com.taotao.cloud.job.worker.processor.task.TaskContext;
import com.taotao.cloud.job.worker.processor.type.BasicProcessor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.lang3.exception.ExceptionUtils;

class MethodBasicProcessor implements BasicProcessor {

    private final Object bean;

    private final Method method;

    public MethodBasicProcessor(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        try {
            Object result = method.invoke(bean, context);
            return new ProcessResult(true, JsonUtils.toJSONString(result));
        } catch (InvocationTargetException ite) {
            ExceptionUtils.rethrow(ite.getTargetException());
        }

        return new ProcessResult(false, "IMPOSSIBLE");
    }
}
