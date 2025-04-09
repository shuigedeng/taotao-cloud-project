package com.taotao.cloud.ccsr.client.listener;

import com.taotao.cloud.ccsr.common.exception.OHaraMcsClientException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractConfigListener<T extends ConfigData> implements ConfigListener<T> {

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    protected AbstractConfigListener() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            this.type = (Class<T>) ((ParameterizedType) superClass).getActualTypeArguments()[0];
        } else {
            throw new OHaraMcsClientException("Unable to determine generic type for listener.");
        }
    }

    public Class<T> getType() {
        return this.type;
    }

    @Override
    public void register() {
        ConfigListenerManager.registerListener(getType(), this);
    }
}
