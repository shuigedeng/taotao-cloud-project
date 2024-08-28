package com.taotao.cloud.iot.biz.communication.mqtt.factory;

import lombok.RequiredArgsConstructor;
import net.maku.iot.communication.mqtt.handler.DevicePropertyChangeHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 设备运行状态变化处理器工厂，自动获取所有实现的handler实例
 *
 * @author LSF maku_lsf@163.com
 */
@Component
@RequiredArgsConstructor
public class DevicePropertyChangeHandlerFactory {
    private final ApplicationContext applicationContext;

    /**
     * 所有设备运行属性变化handlers
     */
    private List<DevicePropertyChangeHandler> handlers;

    /**
     * 获取设备运行状态变化handlers
     *
     * @return
     */
    public List<DevicePropertyChangeHandler> getHandlers() {
        if (handlers != null) {
            return handlers;
        }
        handlers = Collections.unmodifiableList(
                new ArrayList<>(applicationContext.getBeansOfType(
                        DevicePropertyChangeHandler.class).values()));
        return handlers;
    }
}
