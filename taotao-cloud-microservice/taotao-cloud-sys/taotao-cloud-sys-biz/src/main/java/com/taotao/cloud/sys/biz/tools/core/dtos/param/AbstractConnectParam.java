package com.taotao.cloud.sys.biz.tools.core.dtos.param;


public abstract class AbstractConnectParam {
    protected ConnectIdParam connectIdParam;
    protected ConnectParam connectParam;

	public ConnectIdParam getConnectIdParam() {
		return connectIdParam;
	}

	public void setConnectIdParam(
		ConnectIdParam connectIdParam) {
		this.connectIdParam = connectIdParam;
	}

	public ConnectParam getConnectParam() {
		return connectParam;
	}

	public void setConnectParam(ConnectParam connectParam) {
		this.connectParam = connectParam;
	}
}
