package com.taotao.cloud.sys.biz.tools.core.dtos.param;

public class MongoConnectParam extends AbstractConnectParam {
    private MongoAuthParam authParam;

	public MongoAuthParam getAuthParam() {
		return authParam;
	}

	public void setAuthParam(MongoAuthParam authParam) {
		this.authParam = authParam;
	}
}
