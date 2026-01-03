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

package com.taotao.cloud.mq.consistency.raft1.common.entity.resp;

/**
 * ClientKeyValueResponse
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class ClientKeyValueResponse extends BaseRaftResponse {

    private Object result;

    public ClientKeyValueResponse() {
    }

    public ClientKeyValueResponse( Object result ) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public void setResult( Object result ) {
        this.result = result;
    }

    public static ClientKeyValueResponse fail() {
        return new ClientKeyValueResponse("fail");
    }

    public static ClientKeyValueResponse ok() {
        return new ClientKeyValueResponse("ok");
    }
}
