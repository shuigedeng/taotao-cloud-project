package com.taotao.cloud.sys.biz.tools.core.dtos.param;


public class ConnectParam {
    private String host;
    private int port;

    public static final int DEFAULT_SESSION_TIMEOUT = 30000;
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
    public static final int DEFAULT_MAX_ATTEMPTS = 5;

    private int sessionTimeout = DEFAULT_SESSION_TIMEOUT;
    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int maxAttempts = DEFAULT_MAX_ATTEMPTS;

    /**
     * 获取连接字符串
     * @return
     */
    public String getConnectString(){
        return host+":"+port;
    }

    /**
     * 获取 http 协议的地址
     * @return
     */
    public String httpConnectString(){return "http://"+host+":"+port;}

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

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}
}
