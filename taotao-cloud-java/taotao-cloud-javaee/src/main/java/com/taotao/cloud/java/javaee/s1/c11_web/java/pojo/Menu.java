package com.taotao.cloud.java.javaee.s1.c11_web.java.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Menu {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private String name;
    private String url;
    private Integer sort;
    private List<Menu> children = new ArrayList<>();
    private String icon;
    private String perms;
    private String parentName;//必须加，否则前端修改菜单选择父节点之后输入框不变
}
