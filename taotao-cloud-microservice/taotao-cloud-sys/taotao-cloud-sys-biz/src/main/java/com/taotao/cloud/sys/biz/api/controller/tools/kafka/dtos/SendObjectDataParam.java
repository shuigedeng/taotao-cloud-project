package com.taotao.cloud.sys.biz.api.controller.tools.kafka.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendObjectDataParam extends SendJsonDataParam{
    private String className;
    private String classloaderName;
    private String serializer;

    public SendObjectDataParam() {
    }

    /**
     * @param clusterName
     * @param topic
     * @param key
     * @param data
     * @param className 指定类名来进行序列化
     * @param classloaderName
     * @param serializer
     */
    public SendObjectDataParam(String clusterName, String topic, String key, String data, String className, String classloaderName, String serializer) {
        super(clusterName,topic,key,data);

        this.className = className;
        this.classloaderName = classloaderName;
        this.serializer = serializer;
    }

}
