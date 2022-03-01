package com.taotao.cloud.sys.biz.tools.core.security.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openjdk.nashorn.internal.objects.annotations.Setter;

public class GroupTree {
    /**
     * 组织路径
     */
    private String path;
    /**
     * 组织名称
     */
    private String name;
    /**
     * 上级组织
     */
    @JsonIgnore
    private GroupTree parent;
    /**
     * 子组织
     */
    private List<GroupTree> children = new ArrayList<>();

    public GroupTree() {
    }

    public GroupTree(String name) {
        this.name = name;
    }

    public void addChild(GroupTree groupTree){
        children.add(groupTree);
    }

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GroupTree getParent() {
		return parent;
	}

	public void setParent(GroupTree parent) {
		this.parent = parent;
	}

	public List<GroupTree> getChildren() {
		return children;
	}

	public void setChildren(
		List<GroupTree> children) {
		this.children = children;
	}
}
