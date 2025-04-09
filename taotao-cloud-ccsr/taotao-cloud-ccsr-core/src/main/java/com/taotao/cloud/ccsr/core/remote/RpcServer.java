package com.taotao.cloud.ccsr.core.remote;

import org.ohara.msc.common.config.OHaraMcsConfig;
import com.taotao.cloud.ccsr.spi.SPI;

@SPI
public interface RpcServer {

    void init(OHaraMcsConfig config);

    int port();

    int portOffset();

    void start();

    void stop();

    void await();

}
