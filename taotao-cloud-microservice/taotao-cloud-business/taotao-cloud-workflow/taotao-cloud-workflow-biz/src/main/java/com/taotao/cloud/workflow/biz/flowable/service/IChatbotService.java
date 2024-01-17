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

package com.taotao.cloud.workflow.biz.flowable.service;

import com.taotao.cloud.flowable.biz.model.entity.Chatbot;

/**
 * @author shuigedeng
 * @since 2020/11/13 09:59
 * @version 2022.03
 */
public interface IChatbotService {
    /**
     * 根据id查询机器人客服信息
     *
     * @param id id
     * @return com.taotao.cloud.customer.biz.entity.Chatbot
     * @author shuigedeng
     * @since 2020/11/20 上午10:42
     * @version 2022.03
     */
    Chatbot findChatbotById(Long id);
}
