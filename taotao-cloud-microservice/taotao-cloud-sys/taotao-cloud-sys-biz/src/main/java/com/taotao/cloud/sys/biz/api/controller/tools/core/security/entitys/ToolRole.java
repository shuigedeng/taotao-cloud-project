package com.taotao.cloud.sys.biz.api.controller.tools.core.security.entitys;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ToolRole {
    @NotBlank
    private String rolename;

    public ToolRole() {
    }

    public ToolRole(String rolename) {
        this.rolename = rolename;
    }
}
