package com.taotao.cloud.ccsr.core.remote;


import com.taotao.cloud.ccsr.common.config.OHaraMcsConfig;
import com.taotao.cloud.ccsr.common.log.Log;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author SpringCat
 */
public abstract class AbstractRpcServer implements RpcServer {

    protected volatile boolean isStarted = false;

    protected volatile boolean isShutdown = false;

    protected OHaraMcsConfig config;

    @Override
    public void init(OHaraMcsConfig config) {
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

            Log.info("OHaraMcs {} Rpc server starting at port {}", clz, port());
            this.startServer();
            this.isStarted = true;
            Log.info("OHaraMcs {} Rpc server started at port {}", clz, port());

            this.startPostProcessor();

        } catch (Exception e) {
            Log.error("OHaraMcs {} Rpc server start fail...", clz, e);
        }

//        try {
//            this.await();
//        } catch (Exception e) {
//            PrintLog.error("OHaraMcs {} Rpc server await interrupted...", clz);
//        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void startPreProcessor() {

    }

    public void startPostProcessor() {

    }

    @Override
    public void stop() {
        if (isShutdown) {
            return;
        }

        String clz = getClass().getSimpleName();
        try {
            this.stopPreProcessor();
            Log.info("OHaraMcs {} Rpc server stopping...", clz);
            this.stopServer();
            this.isShutdown = true;
            Log.info("OHaraMcs {} Rpc server stopped successfully...", clz);
            this.stopPostProcessor();
        } catch (Exception e) {
            Log.error("OHaraMcs {} Rpc server stopped fail...", clz, e);
        }
    }

    public void stopPreProcessor() {

    }

    public void stopPostProcessor() {

    }

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
