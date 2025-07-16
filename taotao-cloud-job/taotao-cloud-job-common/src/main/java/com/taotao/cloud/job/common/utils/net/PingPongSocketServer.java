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

package com.taotao.cloud.job.common.utils.net;

import com.taotao.cloud.job.common.utils.CommonUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * 简易服务器
 *
 * @author shuigedeng
 * @since 2024/2/8
 */
@Slf4j
public class PingPongSocketServer implements PingPongServer {

    private Thread thread;

    private ServerSocket serverSocket;

    private volatile boolean terminated = false;

    @Override
    public void initialize(int port) throws Exception {
        serverSocket = new ServerSocket(port);

        thread =
                new Thread(
                        () -> {
                            while (true) {
                                if (terminated) {
                                    return;
                                }
                                // 接收连接，如果没有连接，accept() 方法会阻塞
                                try (Socket socket = serverSocket.accept();
                                        OutputStream outputStream = socket.getOutputStream(); ) {
                                    outputStream.write(
                                            PingPongUtils.PONG.getBytes(StandardCharsets.UTF_8));
                                    outputStream.flush();
                                } catch (Exception e) {
                                    if (!terminated) {
                                        log.warn(
                                                "[PingPongSocketServer] process accepted socket failed!",
                                                e);
                                    }
                                }
                            }
                        },
                        "PingPongSocketServer-Thread");

        thread.start();
    }

    @Override
    public void close() throws IOException {
        terminated = true;
        CommonUtils.executeIgnoreException(() -> serverSocket.close());
        thread.interrupt();
    }
}
