package com.taotao.cloud.ccsr.client.listener;

import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.common.exception.CcsrClientException;
import com.taotao.cloud.ccsr.common.utils.GsonUtils;
import java.util.*;

public class ConfigListenerManager {

    private static final Map<String, ConfigListenerWrapper> listenerMap = new HashMap<>();

    public static <T extends ConfigData> void registerListener(Class<T> dataClass, ConfigListener<T> listener) {
        try {
            T instance = dataClass.getDeclaredConstructor().newInstance();
            listenerMap.put(instance.key(), new ConfigListenerWrapper(dataClass, listener));
        } catch (Exception e) {
            throw new CcsrClientException("Failed to register listener", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends ConfigData> void fireEvent(Metadata metadata, EventType eventType) {
        String dataKey = metadata.getDataKey();
        String content = metadata.getContent();
        ConfigListenerWrapper wrapper = listenerMap.get(dataKey);

        if (wrapper != null) {
            Class<T> dataClass = (Class<T>) wrapper.dataClass();
            ConfigListener<T> listener = (ConfigListener<T>) wrapper.listener();
            T configData = GsonUtils.getInstance().fromJson(content, dataClass);

            listener.receive(content, configData, eventType);
        } else {
            throw new CcsrClientException("No listener registered for key: " + dataKey);
        }
    }

    public record ConfigListenerWrapper(Class<? extends ConfigData> dataClass, ConfigListener<? extends ConfigData> listener) {
    }

}
