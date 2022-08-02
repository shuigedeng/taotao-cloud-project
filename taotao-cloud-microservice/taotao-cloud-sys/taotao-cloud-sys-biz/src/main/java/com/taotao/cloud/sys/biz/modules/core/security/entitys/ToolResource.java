package com.taotao.cloud.sys.biz.modules.core.security.entitys;

import lombok.Data;

@Data
public class ToolResource {
    /**
     * 资源Id
     */
    private String resourceId;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 资源地址
     */
    private String url;
    /**
     * 资源类型; 菜单,资源
     * Menu,SubMenu , Resource, SubResource
     */
    private String type;
    /**
     * 父级资源名称
     */
    private String parentResourceId;

    public ToolResource() {
    }

    public ToolResource(String resourceId,String resourceName, String url, String type, String parentResourceId) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.url = url;
        this.type = type;
        this.parentResourceId = parentResourceId;
    }
}
