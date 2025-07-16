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

package com.taotao.cloud.mq.common.dto.resp;

import com.taotao.cloud.mq.common.dto.req.MqMessage;
import java.util.List;

/**
 * 消费者拉取
 *
 * @author shuigedeng
 * @since 2024.05
 */
public class MqConsumerPullResp extends MqCommonResp {

    /**
     * 消息列表
     */
    private List<MqMessage> list;

    public List<MqMessage> getList() {
        return list;
    }

    public void setList(List<MqMessage> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "MqConsumerPullResp{" + "list=" + list + "} " + super.toString();
    }
}
