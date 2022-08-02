package com.taotao.cloud.sys.biz.modules.core.security.dtos;

import com.taotao.cloud.sys.biz.modules.core.security.entitys.ToolResource;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
}
