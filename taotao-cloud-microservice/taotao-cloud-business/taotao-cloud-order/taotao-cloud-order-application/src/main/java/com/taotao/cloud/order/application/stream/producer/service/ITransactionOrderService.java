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

package com.taotao.cloud.order.application.stream.producer.service;

/** 订单事务消息 */
public interface ITransactionOrderService {

    /** 测试事务消息 */
    void testTransaction();

    /**
     * 通过stream方式发送消息
     * 注意配置spring.cloud.stream.rocketmq.bindings.order-output.producer.transactional=true
     */
    void testStreamTransaction();
}
