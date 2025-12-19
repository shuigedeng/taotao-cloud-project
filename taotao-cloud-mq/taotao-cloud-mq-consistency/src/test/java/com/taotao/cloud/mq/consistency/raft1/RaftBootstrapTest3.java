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

package com.taotao.cloud.mq.consistency.raft1;

import com.taotao.cloud.mq.consistency.raft1.server.bs.RaftBootstrap;

import java.util.Arrays;
import java.util.List;

/**
 * RaftBootstrapTest3
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class RaftBootstrapTest3 {

    public static void main( String[] args ) throws Throwable {
        System.setProperty("serverPort", "8777");

        List<String> clusterList =
                Arrays.asList(
                        "localhost:8775",
                        "localhost:8776",
                        "localhost:8777",
                        "localhost:8778",
                        "localhost:8779");
        RaftBootstrap bootstrap = new RaftBootstrap(8777, clusterList);

        // 启动
        bootstrap.boot();
    }
}
