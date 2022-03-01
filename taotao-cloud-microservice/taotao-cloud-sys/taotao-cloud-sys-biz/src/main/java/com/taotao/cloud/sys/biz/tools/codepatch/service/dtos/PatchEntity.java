package com.taotao.cloud.sys.biz.tools.codepatch.service.dtos;

import org.apache.commons.lang3.StringUtils;

/**
 * 保存增量实体对象, 某在,在某个时间在哪个库的哪个分支打了增量
 */
public class PatchEntity {

	/**
	 * 当前增量说明
	 */
	private String title;
	/**
	 * 分组
	 */
	private String group;
	/**
	 * 仓库
	 */
	private String repository;
	/**
	 * 分支
	 */
	private String branch;
	/**
	 * 增量时间
	 */
	private long time;
	/**
	 * 打增量的人
	 */
	private String user;
	/**
	 * 文件相对路径, 相对于临时路径的路径
	 */
	private String filePath;

	/**
	 * 是否还有效, 当增量文件被删除时,会被标记为失效
	 */
	private boolean effect;

	public PatchEntity() {
	}

	public PatchEntity(String title, String group, String repository, String branch, long time,
		String user, String filePath, boolean effect) {
		this.title = title;
		this.group = group;
		this.repository = repository;
		this.branch = branch;
		this.time = time;
		this.user = user;
		this.filePath = filePath;
		this.effect = effect;
	}

	@Override
	public String toString() {
		final String[] fields = {title, group, repository, branch, time + "", user, filePath,
			effect + ""};
		return StringUtils.join(fields, ':');
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isEffect() {
		return effect;
	}

	public void setEffect(boolean effect) {
		this.effect = effect;
	}
}
