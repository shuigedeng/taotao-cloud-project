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

package com.taotao.cloud.distribution.biz.service;

public interface RedPacketService {

    /**
     * 获取红包
     *
     * @param redPacketId
     * @return
     */
    RedPacket get(long redPacketId);
    /**
     * 抢红包业务实现
     *
     * @param redPacketId
     * @return
     */
    Result startSeckil(long redPacketId, int userId);

    /**
     * 微信抢红包业务实现
     *
     * @param redPacketId
     * @param userId
     * @return
     */
    Result startTwoSeckil(long redPacketId, int userId);
}
