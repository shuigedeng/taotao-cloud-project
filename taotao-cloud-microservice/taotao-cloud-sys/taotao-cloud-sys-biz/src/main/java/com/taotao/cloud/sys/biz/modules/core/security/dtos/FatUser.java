package com.taotao.cloud.sys.biz.modules.core.security.dtos;

import java.util.ArrayList;
import java.util.List;


import com.taotao.cloud.sys.biz.modules.core.security.entitys.ToolUser;
import com.taotao.cloud.sys.biz.modules.core.security.entitys.UserProfile;
import lombok.Data;

@Data
public class FatUser {
    private ToolUser toolUser;
    private List<String> roles = new ArrayList<>();
    private List<String> groups = new ArrayList<>();
    private UserProfile profile;

    public FatUser() {
    }
    public FatUser(ThinUser thinUser) {
        this.toolUser = thinUser.getToolUser();
        this.roles = thinUser.getRoles();
        this.groups = thinUser.getGroups();
    }
}
