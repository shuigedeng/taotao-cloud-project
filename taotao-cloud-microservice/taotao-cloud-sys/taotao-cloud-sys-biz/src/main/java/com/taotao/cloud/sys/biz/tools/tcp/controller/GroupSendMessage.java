package com.taotao.cloud.sys.biz.tools.tcp.controller;


import java.util.ArrayList;
import java.util.List;

/**
 * 服务端对客户端分组数据发送
 */
public class GroupSendMessage {
    private List<String> hostAndPorts = new ArrayList<>();
    private String ascii;
    private String hex;

	public List<String> getHostAndPorts() {
		return hostAndPorts;
	}

	public void setHostAndPorts(List<String> hostAndPorts) {
		this.hostAndPorts = hostAndPorts;
	}

	public String getAscii() {
		return ascii;
	}

	public void setAscii(String ascii) {
		this.ascii = ascii;
	}

	public String getHex() {
		return hex;
	}

	public void setHex(String hex) {
		this.hex = hex;
	}
}
