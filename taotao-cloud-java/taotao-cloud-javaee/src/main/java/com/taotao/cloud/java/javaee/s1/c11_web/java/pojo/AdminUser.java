package com.taotao.cloud.java.javaee.s1.c11_web.java.pojo;

import lombok.Data;

@Data
public class AdminUser {
    private Integer id;
    private String password;
    private String email;
    private String realName;
    private Integer status;

}
