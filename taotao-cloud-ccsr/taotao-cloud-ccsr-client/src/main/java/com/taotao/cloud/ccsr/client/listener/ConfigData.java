package com.taotao.cloud.ccsr.client.listener;


public interface ConfigData {

    default String key() {
        return this.getClass().getSimpleName();
    }

}
