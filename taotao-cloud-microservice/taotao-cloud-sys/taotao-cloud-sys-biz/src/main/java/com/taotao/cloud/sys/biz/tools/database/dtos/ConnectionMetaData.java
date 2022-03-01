package com.taotao.cloud.sys.biz.tools.database.dtos;


import com.taotao.cloud.sys.biz.tools.core.dtos.param.AuthParam;

public class ConnectionMetaData {
    private AuthParam authParam;
    private String driverClass;
    private String connectionURL;

    public ConnectionMetaData() {
    }

    public ConnectionMetaData(AuthParam authParam, String driverClass, String connectionURL) {
        this.authParam = authParam;
        this.driverClass = driverClass;
        this.connectionURL = connectionURL;
    }

	public AuthParam getAuthParam() {
		return authParam;
	}

	public void setAuthParam(AuthParam authParam) {
		this.authParam = authParam;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}
}
