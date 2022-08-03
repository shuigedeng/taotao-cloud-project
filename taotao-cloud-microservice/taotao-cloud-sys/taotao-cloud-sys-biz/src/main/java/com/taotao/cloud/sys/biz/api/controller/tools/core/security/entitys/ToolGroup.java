package com.taotao.cloud.sys.biz.api.controller.tools.core.security.entitys;

import lombok.Data;

@Data
public class ToolGroup {
    private String groupName;
    private String path;

    public ToolGroup() {
    }

    public ToolGroup(String groupName, String path) {
        this.groupName = groupName;
        this.path = path;
    }
}
