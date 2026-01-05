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

package com.taotao.cloud.cache.support.listener.slow;

/**
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheSlowListenerContext implements com.taotao.cloud.cache.api.CacheSlowListenerContext {

    /**
     * 方法名称
     * @since 2024.06
     */
    private String methodName;

    /**
     * 参数信息
     * @since 2024.06
     */
    private Object[] params;

    /**
     * 方法结果
     * @since 2024.06
     */
    private Object result;

    /**
     * 开始时间
     * @since 2024.06
     */
    private long startTimeMills;

    /**
     * 结束时间
     * @since 2024.06
     */
    private long endTimeMills;

    /**
     * 消耗时间
     * @since 2024.06
     */
    private long costTimeMills;

    /**
     * @since 2024.06
     * @return 实例
     */
    public static CacheSlowListenerContext newInstance() {
        return new CacheSlowListenerContext();
    }

    @Override
    public String methodName() {
        return methodName;
    }

    public CacheSlowListenerContext methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public CacheSlowListenerContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Object result() {
        return result;
    }

    public CacheSlowListenerContext result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public long startTimeMills() {
        return startTimeMills;
    }

    public CacheSlowListenerContext startTimeMills(long startTimeMills) {
        this.startTimeMills = startTimeMills;
        return this;
    }

    @Override
    public long endTimeMills() {
        return endTimeMills;
    }

    public CacheSlowListenerContext endTimeMills(long endTimeMills) {
        this.endTimeMills = endTimeMills;
        return this;
    }

    @Override
    public long costTimeMills() {
        return costTimeMills;
    }

    public CacheSlowListenerContext costTimeMills(long costTimeMills) {
        this.costTimeMills = costTimeMills;
        return this;
    }
}
