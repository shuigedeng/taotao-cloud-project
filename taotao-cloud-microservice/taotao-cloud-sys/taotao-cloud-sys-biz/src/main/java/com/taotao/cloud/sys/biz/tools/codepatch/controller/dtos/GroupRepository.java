package com.taotao.cloud.sys.biz.tools.codepatch.controller.dtos;


import java.util.ArrayList;
import java.util.List;

public class GroupRepository {
    /**
     * 分组名
     */
    private String group;
    /**
     * 仓库列表
     */
    private List<String> repositorys = new ArrayList<>();

    public GroupRepository() {
    }

    public GroupRepository(String group, List<String> repositorys) {
        this.group = group;
        this.repositorys = repositorys;
    }

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<String> getRepositorys() {
		return repositorys;
	}

	public void setRepositorys(List<String> repositorys) {
		this.repositorys = repositorys;
	}
}
