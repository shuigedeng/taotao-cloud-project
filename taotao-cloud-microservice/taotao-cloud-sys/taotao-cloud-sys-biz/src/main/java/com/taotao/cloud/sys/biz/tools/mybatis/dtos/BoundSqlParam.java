package com.taotao.cloud.sys.biz.tools.mybatis.dtos;

import com.alibaba.fastjson.JSONObject;

import javax.validation.constraints.NotNull;

public class BoundSqlParam {
   @NotNull
   private String project;
   @NotNull
   private String statementId;
   @NotNull
   private String className;
   @NotNull
   private String classloaderName;
   private JSONObject arg;
   @NotNull
   private String connName;

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getStatementId() {
		return statementId;
	}

	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassloaderName() {
		return classloaderName;
	}

	public void setClassloaderName(String classloaderName) {
		this.classloaderName = classloaderName;
	}

	public JSONObject getArg() {
		return arg;
	}

	public void setArg(JSONObject arg) {
		this.arg = arg;
	}

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}
}
