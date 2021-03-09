package com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {

    private Integer id;

    private String name;

    private Integer age;

}
