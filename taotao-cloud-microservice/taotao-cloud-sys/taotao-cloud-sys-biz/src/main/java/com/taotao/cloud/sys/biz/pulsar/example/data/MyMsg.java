package com.taotao.cloud.sys.biz.pulsar.example.data;

public class MyMsg {

	private String data;

	public MyMsg(String data) {
		this.data = data;
	}

	public MyMsg() {
	}

	public String getData() {
		return data;
	}
}
