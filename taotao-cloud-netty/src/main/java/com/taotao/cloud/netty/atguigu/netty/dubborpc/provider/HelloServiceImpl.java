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

package com.taotao.cloud.netty.atguigu.netty.dubborpc.provider;

import com.taotao.cloud.netty.atguigu.netty.dubborpc.publicinterface.HelloService;

/**
 * HelloServiceImpl
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class HelloServiceImpl implements HelloService {

    private static int count = 0;

    // 当有消费方调用该方法时， 就返回一个结果
    @Override
    public String hello( String mes ) {
        System.out.println("收到客户端消息=" + mes);
        // 根据mes 返回不同的结果
        if (mes != null) {
            return "你好客户端, 我已经收到你的消息 [" + mes + "] 第" + ( ++count ) + " 次";
        } else {
            return "你好客户端, 我已经收到你的消息 ";
        }
    }
}
