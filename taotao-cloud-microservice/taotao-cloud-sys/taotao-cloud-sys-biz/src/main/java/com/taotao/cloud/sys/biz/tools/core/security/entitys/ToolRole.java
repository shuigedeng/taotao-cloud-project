package com.taotao.cloud.sys.biz.tools.core.security.entitys;


import javax.validation.constraints.NotBlank;

public class ToolRole {
    @NotBlank
    private String rolename;

    public ToolRole() {
    }

    public ToolRole(String rolename) {
        this.rolename = rolename;
    }


	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
}
