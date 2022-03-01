package com.taotao.cloud.sys.biz.tools.codepatch.service.dtos;


import java.util.ArrayList;
import java.util.List;

public class BatchCommitIdPatch {

	/**
	 * 分组名
	 */
	private String group;
	/**
	 * 仓库名
	 */
	private String repository;
	/**
	 * 上一个提交, 放弃, {@link commitIds}
	 */
	@Deprecated
	private String commitBeforeId;
	/**
	 * 下一个提交 , 放弃, {@link commitIds}
	 */
	@Deprecated
	private String commitAfterId;
	/**
	 * 提交记录列表
	 */
	private List<String> commitIds = new ArrayList<>();
	/**
	 * 补丁名称
	 */
	private String title;

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

	public String getCommitBeforeId() {
		return commitBeforeId;
	}

	public void setCommitBeforeId(String commitBeforeId) {
		this.commitBeforeId = commitBeforeId;
	}

	public String getCommitAfterId() {
		return commitAfterId;
	}

	public void setCommitAfterId(String commitAfterId) {
		this.commitAfterId = commitAfterId;
	}

	public List<String> getCommitIds() {
		return commitIds;
	}

	public void setCommitIds(List<String> commitIds) {
		this.commitIds = commitIds;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
