package com.taotao.cloud.ccsr.api.listener;

/**
 * @author SpringCat
 * @date 2025-03-25 14:55
 */
public interface Listener<T> {
    void onSubscribe(T event);
}
