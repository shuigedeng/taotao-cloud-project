package com.taotao.cloud.sys.biz.tools.core.dtos.param;


public class GitParam extends AbstractConnectParam{
    private AuthParam authParam;
    private String sshKey;
    private String mavenHome;
    private String mavenConfigFilePath;

	public AuthParam getAuthParam() {
		return authParam;
	}

	public void setAuthParam(AuthParam authParam) {
		this.authParam = authParam;
	}

	public String getSshKey() {
		return sshKey;
	}

	public void setSshKey(String sshKey) {
		this.sshKey = sshKey;
	}

	public String getMavenHome() {
		return mavenHome;
	}

	public void setMavenHome(String mavenHome) {
		this.mavenHome = mavenHome;
	}

	public String getMavenConfigFilePath() {
		return mavenConfigFilePath;
	}

	public void setMavenConfigFilePath(String mavenConfigFilePath) {
		this.mavenConfigFilePath = mavenConfigFilePath;
	}
}
