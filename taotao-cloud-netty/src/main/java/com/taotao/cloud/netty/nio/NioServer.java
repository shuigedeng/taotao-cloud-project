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

package com.taotao.cloud.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * NioServer
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class NioServer {

    private static Map<String, SocketChannel> clientMap = new HashMap();

    public static void main( String[] args ) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            try {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                selectionKeys.forEach(
                        selectionKey -> {
                            final SocketChannel client;

                            try {
                                if (selectionKey.isAcceptable()) {
                                    ServerSocketChannel server =
                                            (ServerSocketChannel) selectionKey.channel();
                                    client = server.accept();
                                    client.configureBlocking(false);
                                    client.register(selector, SelectionKey.OP_READ);

                                    String key = "【" + UUID.randomUUID().toString() + "】";

                                    clientMap.put(key, client);
                                } else if (selectionKey.isReadable()) {
                                    client = (SocketChannel) selectionKey.channel();
                                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                                    int count = client.read(readBuffer);

                                    if (count > 0) {
                                        readBuffer.flip();

                                        Charset charset = Charset.forName("utf-8");
                                        String receivedMessage =
                                                String.valueOf(charset.decode(readBuffer).array());

                                        System.out.println(client + ": " + receivedMessage);

                                        String senderKey = null;

                                        for (Map.Entry<String, SocketChannel> entry :
                                                clientMap.entrySet()) {
                                            if (client == entry.getValue()) {
                                                senderKey = entry.getKey();
                                                break;
                                            }
                                        }

                                        for (Map.Entry<String, SocketChannel> entry :
                                                clientMap.entrySet()) {
                                            SocketChannel value = entry.getValue();

                                            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

                                            writeBuffer.put(
                                                    ( senderKey + ": " + receivedMessage )
                                                            .getBytes());
                                            writeBuffer.flip();

                                            value.write(writeBuffer);
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });

                selectionKeys.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
