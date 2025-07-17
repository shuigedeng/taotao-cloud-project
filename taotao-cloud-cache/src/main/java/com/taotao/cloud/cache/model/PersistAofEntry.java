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

package com.taotao.cloud.cache.model;

import java.util.Arrays;

/**
 * AOF 持久化明细
 * @author shuigedeng
 * @since 2024.06
 */
public class PersistAofEntry {

    /**
     * 参数信息
     */
    private Object[] params;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 新建对象实例
     * @return this
     */
    public static PersistAofEntry newInstance() {
        return new PersistAofEntry();
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "PersistAofEntry{"
                + "params="
                + Arrays.toString(params)
                + ", methodName='"
                + methodName
                + '\''
                + '}';
    }
}
