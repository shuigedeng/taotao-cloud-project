package com.taotao.cloud.job.server.jobserver.common.config;

import lombok.Getter;
import com.taotao.cloud.common.constant.RemoteConstant;
import com.taotao.cloud.common.utils.net.MyNetUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;


@Getter
@ConfigurationProperties(prefix = "ttcjob.server")
public class TtcJobServerConfig {


    private  String address = MyNetUtil.address;
    private  Integer workerPort = RemoteConstant.DEFAULT_WORKER_GRPC_PORT;
    private String nameServerAddress;
    private Integer serverPort = RemoteConstant.DEFAULT_SERVER_GRPC_PORT;


    // 为了让单例池拿到
    public static String staticNameServerAddress;
    public static Integer staticServerPort;
    public static Integer staticWorkerPort;

    @PostConstruct
    public void initStaticFields() {
        staticNameServerAddress = this.nameServerAddress;
        staticServerPort = this.serverPort;
        staticWorkerPort = this.workerPort;
    }

    public void setNameServerAddress(String nameServerAddress) {
        this.nameServerAddress = nameServerAddress;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setWorkerPort(int workerPort) {
        this.workerPort = workerPort;
    }
    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

}
