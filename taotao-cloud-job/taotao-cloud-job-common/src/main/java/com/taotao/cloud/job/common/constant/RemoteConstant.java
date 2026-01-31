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

package com.taotao.cloud.job.common.constant;

/**
 * RemoteConstant
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class RemoteConstant {

    public static final int DEFAULT_WORKER_GRPC_PORT = 9082;
    public static final int DEFAULT_SERVER_GRPC_PORT = 9081;
    public static final int DEFAULT_NAMESERVER_GRPC_PORT = 9081;

    public static final int SUCCESS = 200;
    public static final int FAULT = 500;

    public static final int MATCH = 10001;
    public static final int NO_MATCH = 10002;

    public static final String SERVER = "SERVER";
    public static final String WORKER = "WORKER";
    public static final String NAMESERVER = "NAMESERVER";

    // when kjobserver register
    public static final String INCREMENTAL_ADD_SERVER = "INCREMENTAL_ADD_SERVER";
    //
    public static final String INCREMENTAL_ADD_WORKER = "INCREMENTAL_ADD_WORKER";

    // when client shutdown or timeout
    public static final String INCREMENTAL_REMOVE_SERVER = "INCREMENTAL_REMOVE_SERVER";
    public static final String INCREMENTAL_REMOVE_WORKER = "INCREMENTAL_REMOVE_WORKER";
    // data check
    public static final String FULL_SYNC = "FULL_SYNC";

    // IP
    public static final String LOOPBACKIP = "127.0.0.1";
}
