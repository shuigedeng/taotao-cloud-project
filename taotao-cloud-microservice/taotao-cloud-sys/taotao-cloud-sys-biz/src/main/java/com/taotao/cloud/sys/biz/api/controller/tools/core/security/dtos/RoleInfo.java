package com.taotao.cloud.sys.biz.api.controller.tools.core.security.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.core.security.entitys.ToolRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RoleInfo {
    @Valid
    private ToolRole toolRole;
    private List<String> resources = new ArrayList<>();
    private List<String> groups = new ArrayList<>();

    public RoleInfo(ToolRole toolRole) {
        this.toolRole = toolRole;
    }

    public void addGroup(String group){
        groups.add(group);
    }

    public void addResource(String resource){
        resources.add(resource);
    }

}
