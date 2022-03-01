package com.taotao.cloud.sys.biz.tools.core.security.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taotao.cloud.sys.biz.tools.core.security.entitys.ToolUser;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

public class ThinUser {

	@Valid
	protected ToolUser toolUser;
	protected List<String> roles = new ArrayList<>();
	protected List<String> groups = new ArrayList<>();

	public ThinUser() {
	}

	public ThinUser(ToolUser toolUser) {
		this.toolUser = toolUser;
	}

	public void addRole(String roleName) {
		roles.add(roleName);
	}

	public void addGroup(String groupPath) {
		groups.add(groupPath);
	}

	@JsonIgnore
	public ToolUser getToolUser() {
		return toolUser;
	}

	@JsonProperty
	public void setToolUser(ToolUser toolUser) {
		this.toolUser = toolUser;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
}
