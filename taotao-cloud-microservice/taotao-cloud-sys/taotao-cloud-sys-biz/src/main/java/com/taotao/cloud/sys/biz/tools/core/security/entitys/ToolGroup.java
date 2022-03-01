package com.taotao.cloud.sys.biz.tools.core.security.entitys;

public class ToolGroup {
    private String groupName;
    private String path;

    public ToolGroup() {
    }

    public ToolGroup(String groupName, String path) {
        this.groupName = groupName;
        this.path = path;
    }

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
