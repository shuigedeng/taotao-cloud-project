package com.taotao.cloud.job.common.constant;

import org.springframework.beans.factory.annotation.Value;

public class RemoteConstant {

    public static final int DEFAULT_WORKER_GRPC_PORT = 9082;
    public static final int DEFAULT_SERVER_GRPC_PORT = 9081;
    public static final int DEFAULT_NAMESERVER_GRPC_PORT = 9081;

    public static final int SUCCESS = 200;
    public static final int FAULT = 500;

    public static final String SERVER = "SERVER";
    public static final String WORKER = "WORKER";
    public static final String NAMESERVER = "NAMESERVER";

}
