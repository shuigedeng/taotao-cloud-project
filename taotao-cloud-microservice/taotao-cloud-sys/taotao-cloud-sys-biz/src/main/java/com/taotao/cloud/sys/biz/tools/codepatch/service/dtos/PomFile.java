package com.taotao.cloud.sys.biz.tools.codepatch.service.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class PomFile implements Comparable<PomFile> {

	@JsonIgnore
	private File repository;

	private String relativePath;
	private String moduleName;
	// 模块上次编译时间
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastCompileTime;

	public PomFile() {
	}

	public PomFile(File repository, String relativePath, String moduleName) {
		this.repository = repository;
		this.relativePath = relativePath;
		this.moduleName = moduleName;
	}

	@Override
	public int compareTo(PomFile o) {
		if (o.relativePath == this.relativePath) {
			return 0;
		}
		if (StringUtils.isBlank(o.relativePath)) {
			return -1;
		}
		if (StringUtils.isBlank(this.relativePath)) {
			return 1;
		}
		return this.relativePath.length() - o.relativePath.length();
	}

	public File getRepository() {
		return repository;
	}

	public void setRepository(File repository) {
		this.repository = repository;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Date getLastCompileTime() {
		return lastCompileTime;
	}

	public void setLastCompileTime(Date lastCompileTime) {
		this.lastCompileTime = lastCompileTime;
	}
}
