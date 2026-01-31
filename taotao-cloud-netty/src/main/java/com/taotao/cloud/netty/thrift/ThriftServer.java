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

package com.taotao.cloud.netty.thrift;

import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.transport.TNonblockingServerSocket;

/**
 * ThriftServer
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class ThriftServer {

    public static void main( String[] args ) throws Exception {
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
        // PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new
        // PersonServiceImpl());
        //
        // arg.protocolFactory(new TCompactProtocol.Factory());
        // arg.transportFactory(new TFramedTransport.Factory());
        // arg.processorFactory(new TProcessorFactory(processor));
        //
        // TServer server = new THsHaServer(arg);
        //
        // System.out.println("Thrift Server Started!");
        //
        // server.serve();
    }
}
