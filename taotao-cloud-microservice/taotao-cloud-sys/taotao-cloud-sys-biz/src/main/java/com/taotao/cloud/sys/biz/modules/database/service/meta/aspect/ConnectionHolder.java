package com.taotao.cloud.sys.biz.modules.database.service.meta.aspect;

import lombok.Data;

import java.sql.Connection;

@Data
public class ConnectionHolder {
    private Connection connection;
    private ConnectionStatus connectionStatus;

    @Data
    public static final class ConnectionStatus{
        private String methodIdentification;
        private boolean newConnection;
        private ConnectionStatus oldConnectionStatus;

        public ConnectionStatus(String methodIdentification, boolean newConnection) {
            this.methodIdentification = methodIdentification;
            this.newConnection = newConnection;
        }
    }
}
