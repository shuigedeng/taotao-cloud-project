package com.taotao.cloud.sys.biz.tools.security.controller.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Mount {
    private Set<String> users = new HashSet<>();
    private Set<String> roles = new HashSet<>();
    private Set<String> resources = new HashSet<>();

    public Mount(Set<String> users, Set<String> roles, Set<String> resources) {
        this.users = users;
        this.roles = roles;
        this.resources = resources;
    }
}
