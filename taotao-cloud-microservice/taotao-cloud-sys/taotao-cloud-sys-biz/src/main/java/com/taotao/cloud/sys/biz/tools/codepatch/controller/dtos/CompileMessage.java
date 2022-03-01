package com.taotao.cloud.sys.biz.tools.codepatch.controller.dtos;

/**
 * 编译消息
 */
public class CompileMessage {
	private String group;
	private String repository;
	private String websocketId;
	private String relativePath;
	/**
	 * maven 命令, 默认是编译命令
	 */
	private String mvnCommand = "clean compile";

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getWebsocketId() {
		return websocketId;
	}

	public void setWebsocketId(String websocketId) {
		this.websocketId = websocketId;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getMvnCommand() {
		return mvnCommand;
	}

	public void setMvnCommand(String mvnCommand) {
		this.mvnCommand = mvnCommand;
	}
}
