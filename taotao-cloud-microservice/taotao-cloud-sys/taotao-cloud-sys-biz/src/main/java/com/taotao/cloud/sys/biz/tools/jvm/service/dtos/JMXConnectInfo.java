package com.taotao.cloud.sys.biz.tools.jvm.service.dtos;

public class JMXConnectInfo {
    /**
     * 开启了JMX的主机:端口
     */
    private String jmxHostAndPort;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

	public String getJmxHostAndPort() {
		return jmxHostAndPort;
	}

	public void setJmxHostAndPort(String jmxHostAndPort) {
		this.jmxHostAndPort = jmxHostAndPort;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
