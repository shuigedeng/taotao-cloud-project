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

package com.taotao.cloud.ccsr.core.remote;

import com.taotao.cloud.ccsr.common.config.CcsrConfig;
import com.taotao.cloud.ccsr.common.log.Log;
import jakarta.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author shuigedeng
 */
public abstract class AbstractRpcServer implements RpcServer {

    protected volatile boolean isStarted = false;

    protected volatile boolean isShutdown = false;

    protected CcsrConfig config;

    @Override
    public void init(CcsrConfig config) {
        // do nothing
    }

    @Override
    public int port() {
        return config.getPort() + portOffset();
    }

    @Override
    public int portOffset() {
        // 默认端口不偏移
        return 0;
    }

    @Override
    public synchronized void start() {
        if (this.isStarted) {
            return;
        }

        String clz = getClass().getSimpleName();
        try {
            this.startPreProcessor();

            Log.info("Ccsr {} Rpc server starting at port {}", clz, port());
            this.startServer();
            this.isStarted = true;
            Log.info("Ccsr {} Rpc server started at port {}", clz, port());

            this.startPostProcessor();

        } catch (Exception e) {
            Log.error("Ccsr {} Rpc server start fail...", clz, e);
        }

        //        try {
        //            this.await();
        //        } catch (Exception e) {
        //            PrintLog.error("Ccsr {} Rpc server await interrupted...", clz);
        //        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void startPreProcessor() {}

    public void startPostProcessor() {}

    @Override
    public void stop() {
        if (isShutdown) {
            return;
        }

        String clz = getClass().getSimpleName();
        try {
            this.stopPreProcessor();
            Log.info("Ccsr {} Rpc server stopping...", clz);
            this.stopServer();
            this.isShutdown = true;
            Log.info("Ccsr {} Rpc server stopped successfully...", clz);
            this.stopPostProcessor();
        } catch (Exception e) {
            Log.error("Ccsr {} Rpc server stopped fail...", clz, e);
        }
    }

    public void stopPreProcessor() {}

    public void stopPostProcessor() {}

    /**
     * Start Server.
     */
    public abstract void startServer() throws IOException;

    /**
     * Stop Server.
     */
    @PreDestroy
    public abstract void stopServer();

    @Override
    public abstract void await();
}
