/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.common.support.handler;


import com.taotao.cloud.common.utils.lang.ObjectUtil;

/**
 * 抽象处理器
 */
public abstract class AbstractHandler<T, R> implements IHandler<T, R> {

    @Override
    public R handle(T t) {
        if(ObjectUtil.isNull(t)) {
            return null;
        }

        return this.doHandle(t);
    }

    /**
     * 执行操作
     * @param target 原始对象
     * @return 结果
     */
    protected abstract R doHandle(T target);

}
