package com.taotao.cloud.iot.biz.communication.tcp.config;

import io.netty.bootstrap.Bootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 *  Netty启动顺序配置
 *
 * @author LSF maku_lsf@163.com
 */
@Configuration
@Slf4j
public class DeviceClientStartupConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final ObjectProvider<Bootstrap> deviceClientProvider;
    private final DeviceEmulatorConfig deviceEmulatorConfig;

    public DeviceClientStartupConfig(ObjectProvider<Bootstrap> deviceClientProvider, DeviceEmulatorConfig deviceEmulatorConfig) {
        this.deviceClientProvider = deviceClientProvider;
        this.deviceEmulatorConfig = deviceEmulatorConfig;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            Thread.sleep(5000); // 延迟5秒以确保服务器启动
            Bootstrap deviceClientBootstrap = deviceClientProvider.getIfAvailable();
            if (deviceClientBootstrap != null) {
                deviceEmulatorConfig.configureBootstrap(deviceClientBootstrap);
                deviceClientBootstrap.connect("127.0.0.1", 8888).sync();
                log.info("Connected to Netty server on port 8888");
            }
        } catch (InterruptedException e) {
            log.error("Failed to connect to Netty server", e);
        }
    }
}
