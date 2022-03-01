package com.taotao.cloud.sys.biz.tools.core.security.entitys;

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

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParentResourceId() {
		return parentResourceId;
	}

	public void setParentResourceId(String parentResourceId) {
		this.parentResourceId = parentResourceId;
	}
}
