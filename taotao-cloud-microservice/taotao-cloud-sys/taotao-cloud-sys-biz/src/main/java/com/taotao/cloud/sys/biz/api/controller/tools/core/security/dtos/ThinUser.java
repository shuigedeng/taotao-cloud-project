package com.taotao.cloud.sys.biz.api.controller.tools.core.security.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.taotao.cloud.sys.biz.api.controller.tools.core.security.entitys.ToolUser;
import lombok.Data;

import javax.validation.Valid;

@Data
public class ThinUser {
    @Valid
    protected ToolUser toolUser;
    protected List<String> roles = new ArrayList<>();
    protected List<String> groups = new ArrayList<>();

    public ThinUser() {
    }

    public ThinUser(ToolUser toolUser) {
        this.toolUser = toolUser;
    }

    public void addRole(String roleName){
        roles.add(roleName);
    }

    public void addGroup(String groupPath){
        groups.add(groupPath);
    }

    @JsonIgnore
    public ToolUser getToolUser() {
        return toolUser;
    }

    @JsonProperty
    public void setToolUser(ToolUser toolUser) {
        this.toolUser = toolUser;
    }
}
