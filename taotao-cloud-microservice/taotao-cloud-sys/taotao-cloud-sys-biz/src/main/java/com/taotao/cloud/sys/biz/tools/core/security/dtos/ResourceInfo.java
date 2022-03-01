package com.taotao.cloud.sys.biz.tools.core.security.dtos;


import com.taotao.cloud.sys.biz.tools.core.security.entitys.ToolResource;
import java.util.ArrayList;
import java.util.List;

public class ResourceInfo {
    /**
     * 资源信息
     */
    private ToolResource toolResource;
    /**
     * 资源所属分组列表
     */
    private List<String> groups = new ArrayList<>();

    public ResourceInfo(ToolResource toolResource) {
        this.toolResource = toolResource;
    }

    public void addGroup(String groupPath){
        groups.add(groupPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResourceInfo that = (ResourceInfo) o;

       return that.getToolResource().getResourceId().equals(this.toolResource.getResourceId());
    }

    @Override
    public int hashCode() {
        return toolResource != null ? toolResource.getResourceId().hashCode() : 0;
    }

	public ToolResource getToolResource() {
		return toolResource;
	}

	public void setToolResource(ToolResource toolResource) {
		this.toolResource = toolResource;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
}
