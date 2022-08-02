package com.taotao.cloud.sys.biz.modules.core.dtos;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

public class UpdateConnectEvent extends ApplicationEvent {

    public UpdateConnectEvent(ConnectInfo connectInfo) {
        super(connectInfo);
    }

    @Data
    public static class ConnectInfo{
        private String connName;
        private String data;
        private Class<?> clazz;
        private String module;

        public ConnectInfo() {
        }

        public ConnectInfo(String connName, String data, Class<?> clazz) {
            this.connName = connName;
            this.data = data;
            this.clazz = clazz;
        }

        public ConnectInfo(String connName, String data, Class<?> clazz, String module) {
            this.connName = connName;
            this.data = data;
            this.clazz = clazz;
            this.module = module;
        }
    }


}
