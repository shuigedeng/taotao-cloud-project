package com.taotao.cloud.sys.biz.tools.security.service.dtos;

import com.sanri.tools.modules.core.dtos.TreeResponseDto;
import com.sanri.tools.modules.core.security.dtos.ResourceInfo;

import java.util.ArrayList;
import java.util.List;

public class ResourceTree implements TreeResponseDto<ResourceInfo> {
    private ResourceInfo resourceInfo;
    private List<ResourceTree> children = new ArrayList<>();

    public ResourceTree(ResourceInfo resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    @Override
    public String getId() {
        return resourceInfo.getToolResource().getResourceId();
    }

    @Override
    public String getParentId() {
        return resourceInfo.getToolResource().getParentResourceId();
    }

    @Override
    public String getLabel() {
        return resourceInfo.getToolResource().getResourceName();
    }

    @Override
    public ResourceInfo getOrigin() {
        return resourceInfo;
    }

    @Override
    public List<ResourceTree> getChildren() {
        return children;
    }
}
