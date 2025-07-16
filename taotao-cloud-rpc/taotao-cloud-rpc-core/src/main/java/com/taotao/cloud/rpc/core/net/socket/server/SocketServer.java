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

import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.factory.ThreadPoolFactory;
import com.taotao.cloud.rpc.common.serializer.CommonSerializer;
import com.taotao.cloud.rpc.core.handler.RequestHandler;
import com.taotao.cloud.rpc.core.hook.ServerShutdownHook;
import com.taotao.cloud.rpc.core.net.AbstractRpcServer;
import com.taotao.cloud.rpc.core.provider.DefaultServiceProvider;
import com.taotao.cloud.rpc.core.registry.NacosServiceRegistry;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketServer extends AbstractRpcServer {

    // static final 修饰的 基本变量 字符串常量 在虚拟机 类加载 就已经 确定，确保线程安全
    /**
     * private static final int maximumPoolSize = 50; private static final long keepAliveTime = 60;
     * private static final int BLOCKING_QUEUE_CAPACITY = 100; private static final int corePoolSize
     * = Runtime.getRuntime().availableProcessors(); fix : 采用 工厂 模式 来 创建 线程池
     */

    // 非静态 变量在 用户初始化时 传入参数来决定，不需静态
    private final ExecutorService threadPool;

    private final CommonSerializer serializer;
    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port, Integer serializerCode) throws RpcException {
        /**
         * BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
         * ThreadFactory threadFactory = Executors.defaultThreadFactory();
         * threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
         */
        this.hostName = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        /**
         * 继承了 抽象类 AbstractRpcServer 需要 在子类 中 赋值 父类的 字段，才可 注册服务到 服务提供者 和 发布服务到 nacos
         * 即可 调用 父类 的 publishService() 将 服务注册到 macos 上
         */
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new DefaultServiceProvider();
        this.serializer = CommonSerializer.getByCode(serializerCode);
        scanServices();
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Server is running...");
            ServerShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            // 监听 客户端连接
            while ((socket = serverSocket.accept()) != null) {
                log.info("customer has connected successfully! ip = " + socket.getInetAddress());
                threadPool.execute(
                        new SocketRequestHandlerThread(socket, requestHandler, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("Exception throws when connecting, info: {}", e);
        }
    }
}
