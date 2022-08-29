package com.taotao.cloud.sys.api.model.dto.kafka;

/**
 * 对应 kafka 数据的 Node
 */
public class BrokerInfo {
    private int id;
    private String host;
    private int port;
    private int jxmPort;

    public BrokerInfo() {
    }

    public BrokerInfo(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }

    public BrokerInfo(int id, String host, int port, int jxmPort) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.jxmPort = jxmPort;
    }

    public String hostAndPort(){
        return host+":"+port;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getJxmPort() {
		return jxmPort;
	}

	public void setJxmPort(int jxmPort) {
		this.jxmPort = jxmPort;
	}
}
