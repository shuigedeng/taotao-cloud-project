package com.taotao.cloud.sys.biz.modules.core.security.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
