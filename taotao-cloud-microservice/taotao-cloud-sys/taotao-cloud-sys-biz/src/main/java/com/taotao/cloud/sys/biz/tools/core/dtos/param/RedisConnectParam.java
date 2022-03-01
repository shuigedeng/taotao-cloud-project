package com.taotao.cloud.sys.biz.tools.core.dtos.param;

public class RedisConnectParam extends AbstractConnectParam {
    private AuthParam authParam = new AuthParam();

	public AuthParam getAuthParam() {
		return authParam;
	}

	public void setAuthParam(AuthParam authParam) {
		this.authParam = authParam;
	}
}
