package com.taotao.cloud.sys.biz.tools.core.security.dtos;


import com.taotao.cloud.sys.biz.tools.core.security.entitys.ToolRole;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class RoleInfo {
    @Valid
    private ToolRole toolRole;
    private List<String> resources = new ArrayList<>();
    private List<String> groups = new ArrayList<>();

    public RoleInfo(ToolRole toolRole) {
        this.toolRole = toolRole;
    }

    public void addGroup(String group){
        groups.add(group);
    }

    public void addResource(String resource){
        resources.add(resource);
    }

	public ToolRole getToolRole() {
		return toolRole;
	}

	public void setToolRole(ToolRole toolRole) {
		this.toolRole = toolRole;
	}

	public List<String> getResources() {
		return resources;
	}

	public void setResources(List<String> resources) {
		this.resources = resources;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
}
