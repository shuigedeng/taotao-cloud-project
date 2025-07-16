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

package com.taotao.cloud.rpc.core.net.socket.client;

import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.exception.SerializerNotSetException;
import com.taotao.cloud.rpc.common.protocol.RpcRequest;
import com.taotao.cloud.rpc.common.protocol.RpcResponse;
import com.taotao.cloud.rpc.common.serializer.CommonSerializer;
import com.taotao.cloud.rpc.core.net.RpcClient;
import com.taotao.cloud.rpc.core.util.ObjectReader;
import com.taotao.cloud.rpc.core.util.ObjectWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketClient implements RpcClient {

    private String hostName;
    private int port;

    private final CommonSerializer serializer;

    public SocketClient(String hostName, int port, Integer serializerCode) {
        this.hostName = hostName;
        this.port = port;
        this.serializer = CommonSerializer.getByCode(serializerCode);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) throws RpcException {
        if (serializer == null) {
            log.error("Serializer not set");
            throw new SerializerNotSetException("Serializer not set Exception");
        }
        // 使用jdk9 使用的 try catch 可以自动关闭, 必须实现 Closeable
        try (Socket socket = new Socket(hostName, port)) {
            // 使用了装饰者模式
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            ObjectWriter.writeObject(oos, rpcRequest, serializer);
            Object obj = ObjectReader.readObject(ois);
            RpcResponse rpcResponse = (RpcResponse) obj;

            // RpcMessageChecker.check(rpcRequest, rpcResponse);

            return rpcResponse;

        } catch (IOException e) {
            // e.printStackTrace();
            log.error("Error occurred while invoking badly,info: {}", e);
            return null;
        }
    }
}
