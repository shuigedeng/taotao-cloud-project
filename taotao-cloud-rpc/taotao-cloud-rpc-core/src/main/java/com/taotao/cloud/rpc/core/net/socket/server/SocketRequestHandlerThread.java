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

package com.taotao.cloud.rpc.core.net.socket.server;

import com.taotao.cloud.rpc.common.exception.UnSupportBodyException;
import com.taotao.cloud.rpc.common.exception.UnrecognizedException;
import com.taotao.cloud.rpc.common.protocol.RpcRequest;
import com.taotao.cloud.rpc.common.protocol.RpcResponse;
import com.taotao.cloud.rpc.common.serializer.CommonSerializer;
import com.taotao.cloud.rpc.core.handler.RequestHandler;
import com.taotao.cloud.rpc.core.util.ObjectReader;
import com.taotao.cloud.rpc.core.util.ObjectWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 作为一个服务器线程，去处理客户端发起的 请求
 */
@Slf4j
public class SocketRequestHandlerThread implements Runnable {

    private Socket socket;
    private RequestHandler requestHandler;
    private CommonSerializer serializer;

    // 在创建 新线程时 进行赋值
    public SocketRequestHandlerThread(
            Socket socket, RequestHandler requestHandler, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        RpcResponse response = null;
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            // 读取 客户端 发回的 请求
            // RpcRequest rpcRequest = (RpcRequest) ois.readObject();
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(ois);
            Object result = requestHandler.handler(rpcRequest);

            String checkCode = new String(DigestUtils.md5(result.toString().getBytes("UTF-8")));
            // 返回 处理结果 或 处理中途抛出的 异常
            // oos.writeObject(result);
            if (result instanceof Exception) {
                RpcResponse rpcResponse =
                        RpcResponse.failure(
                                ((Exception) result).getMessage(), rpcRequest.getRequestId());
                ObjectWriter.writeObject(oos, rpcResponse, serializer);
            } else {
                RpcResponse rpcResponse =
                        RpcResponse.success(result, rpcRequest.getRequestId(), checkCode);
                ObjectWriter.writeObject(oos, rpcResponse, serializer);
            }

        } catch (IOException | UnSupportBodyException | UnrecognizedException e) {
            // e.printStackTrace();
            log.error("Error occurred while invoking or sent,info:  ", e);
        }
    }
}
