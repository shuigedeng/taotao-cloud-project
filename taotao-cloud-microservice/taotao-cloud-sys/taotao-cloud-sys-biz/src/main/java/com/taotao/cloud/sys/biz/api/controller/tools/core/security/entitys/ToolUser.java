package com.taotao.cloud.sys.biz.api.controller.tools.core.security.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ToolUser {
    @NotBlank
    private String username;
    @JsonIgnore
    private String password;

    public ToolUser() {
    }

    public ToolUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
