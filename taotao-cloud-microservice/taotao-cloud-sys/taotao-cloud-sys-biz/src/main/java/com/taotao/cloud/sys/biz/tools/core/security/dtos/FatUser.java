package com.taotao.cloud.sys.biz.tools.core.security.dtos;

import com.taotao.cloud.sys.biz.tools.core.security.entitys.ToolUser;
import com.taotao.cloud.sys.biz.tools.core.security.entitys.UserProfile;
import java.util.ArrayList;
import java.util.List;

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

	public ToolUser getToolUser() {
		return toolUser;
	}

	public void setToolUser(ToolUser toolUser) {
		this.toolUser = toolUser;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}
}
