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

package com.taotao.cloud.mq.consistency.raft1.common.constant;

/**
 * 请求常量
 * @since 1.0.0
 */
public class RpcRequestCmdConst {

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
}
