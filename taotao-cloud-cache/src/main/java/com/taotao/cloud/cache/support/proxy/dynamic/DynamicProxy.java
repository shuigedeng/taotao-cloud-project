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

package com.taotao.cloud.cache.support.proxy.dynamic;

import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.support.proxy.ICacheProxy;
import com.taotao.cloud.cache.support.proxy.bs.CacheProxyBs;
import com.taotao.cloud.cache.support.proxy.bs.CacheProxyBsContext;
import com.taotao.cloud.cache.support.proxy.bs.ICacheProxyBsContext;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletionService;

/**
 * <p> 动态代理 </p>
 *
 * 1. 对于 executor 的抽象，使用 {@link CompletionService}
 * 2. 确保唯一初始化 executor，在任务执行的最后关闭 executor。
 * 3. 异步执行结果的获取，异常信息的获取。
 * @since 2024.06
 */
public class DynamicProxy implements InvocationHandler, ICacheProxy {

    /**
     * 被代理的对象
     */
    private final ICache target;

    public DynamicProxy(ICache target) {
        this.target = target;
    }

    /**
     * 这种方式虽然实现了异步执行，但是存在一个缺陷：
     * 强制用户返回值为 Future 的子类。
     *
     * 如何实现不影响原来的值，要怎么实现呢？
     * @param proxy 原始对象
     * @param method 方法
     * @param args 入参
     * @return 结果
     * @throws Throwable 异常
     */
    @Override
    @SuppressWarnings("all")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ICacheProxyBsContext context =
                CacheProxyBsContext.newInstance().method(method).params(args).target(target);
        return CacheProxyBs.newInstance().context(context).execute();
    }

    @Override
    public Object proxy() {
        // 我们要代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法的
        InvocationHandler handler = new DynamicProxy(target);

        return Proxy.newProxyInstance(
                handler.getClass().getClassLoader(), target.getClass().getInterfaces(), handler);
    }
}
