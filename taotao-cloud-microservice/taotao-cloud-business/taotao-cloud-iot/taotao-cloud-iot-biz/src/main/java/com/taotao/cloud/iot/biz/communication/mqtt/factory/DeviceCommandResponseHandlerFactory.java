package com.taotao.cloud.iot.biz.communication.mqtt.factory;

import lombok.RequiredArgsConstructor;
import com.taotao.cloud.iot.biz.communication.mqtt.handler.DeviceCommandResponseHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 设备命令响应处理器工厂，自动获取所有实现的handler实例
 *
 * @author 
 */
@Component
@RequiredArgsConstructor
public class DeviceCommandResponseHandlerFactory {
    private final ApplicationContext applicationContext;

    /**
     * 所有设备命令响应handlers
     */
    private List<DeviceCommandResponseHandler> handlers;

    /**
     * 获取设备命令响应handlers
     *
     * @return
     */
    public List<DeviceCommandResponseHandler> getHandlers() {
        if (handlers != null) {
            return handlers;
        }
        handlers = Collections.unmodifiableList(
                new ArrayList<>(applicationContext.getBeansOfType(
                        DeviceCommandResponseHandler.class).values()));
        return handlers;
    }
}
