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

package com.taotao.cloud.mq.consistency.raft.rpc;

import com.taotao.cloud.mq.consistency.raft.client.ClientKVReq;
import com.taotao.cloud.mq.consistency.raft.entity.AentryParam;
import com.taotao.cloud.mq.consistency.raft.entity.RvoteParam;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author shuigedeng
 */
@Builder
@Data
public class Request implements Serializable {

    /** 请求投票 */
    public static final int R_VOTE = 0;

    /** 附加日志 */
    public static final int A_ENTRIES = 1;

    /** 客户端 */
    public static final int CLIENT_REQ = 2;

    /** 配置变更. add */
    public static final int CHANGE_CONFIG_ADD = 3;

    /** 配置变更. remove */
    public static final int CHANGE_CONFIG_REMOVE = 4;

    /** 请求类型 */
    private int cmd = -1;

    /**
     * param
     *
     * @see AentryParam
     * @see RvoteParam
     * @see ClientKVReq
     */
    private Object obj;

    private String url;

    public Request() {}

    public Request(int cmd, Object obj, String url) {
        this.cmd = cmd;
        this.obj = obj;
        this.url = url;
    }
}
