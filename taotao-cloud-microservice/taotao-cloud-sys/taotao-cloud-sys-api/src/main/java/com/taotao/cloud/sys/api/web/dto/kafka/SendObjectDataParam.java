package com.taotao.cloud.sys.api.web.dto.kafka;


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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassloaderName() {
		return classloaderName;
	}

	public void setClassloaderName(String classloaderName) {
		this.classloaderName = classloaderName;
	}

	public String getSerializer() {
		return serializer;
	}

	public void setSerializer(String serializer) {
		this.serializer = serializer;
	}
}
